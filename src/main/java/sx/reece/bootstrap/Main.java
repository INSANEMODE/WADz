package sx.reece.bootstrap;

import sx.reece.wads.games.Game;
import sx.reece.javakit.csharp.Console;
import sx.reece.javakit.logger.Logger;
import sx.reece.javakit.modern.ModernBuffer;
import sx.reece.javakit.modern.ModernFile;
import sx.reece.javakit.stream.ModernOutputStreamWriter;
import sx.reece.wads.Magic;
import sx.reece.wads.WadEntryArray;
import sx.reece.wads.WadFile;
import sx.reece.wads.Wadz;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class Main {
    private static void build(String fileName, String dir, Magic magic) throws Exception {
        WadFile wad;
        ModernOutputStreamWriter streamWriter;
        File file;

        file = ModernFile.getFileOrCreateNew(fileName);
        streamWriter = new ModernOutputStreamWriter(new FileOutputStream(file));
        wad = new WadFile(magic.toGame());

        setEntriesFromDir(new File(dir), wad.getEntries());
        wad.getHeader().setFfotdVersion(0);
        wad.getHeader().setMagic(magic.getBuf());

        wad.write(streamWriter);
        streamWriter.flush();
    }

    private static void unpack(String fileName, String dir) throws Exception {
        ModernBuffer wadFile;
        WadFile wad;
        Game game;
        Magic magic;
        byte[] header;

        wadFile = ModernFile.get(fileName).getModernBuffer();

        header  = wadFile.readBytes(4);
        wadFile.setIndex(0);

        magic   = Magic.getByMagic(header).orElseThrow(() -> {return new RuntimeException("MAGIC not found");});
        game    = magic.toGame();

        wad = Wadz.readWad(game, wadFile);

        wad.print();

        wad.getEntries().getEntries().forEach(item -> {
            ModernFile.get(ModernFile.getFileOrCreateNew(dir + item.getName())).writeAllBytes(item.getFile());
        });
    }

    public static void main(String[] args) throws Exception {
        boolean isBuilding;
        Magic magic = null;
        String dir;
        String fileName;

        Logger.log("Are we building?");
        isBuilding = Console.readLine().contains("y");

        if (isBuilding) {
            Logger.log("Are we using %s or %s (y = first - t6/mw3)?", Magic.T6.toGame(), Magic.T5.toGame());
            magic = Console.readLine().contains("y") ? Magic.T6 : Magic.T5;
        }

        Logger.log("The %s directory:", !isBuilding ? "export" : "input");
        dir = Console.readLine();
        if (!(dir.endsWith("/") || dir.endsWith("\\")))
            dir += "/";

        Logger.log("Wad file:");
        fileName = Console.readLine();

        if (isBuilding)
            build(fileName, dir, magic);
        else
            unpack(fileName, dir);
    }

    private static void setEntriesFromDir(File dir, WadEntryArray array) {
        Arrays.stream(dir.listFiles()).forEach(entryFile ->{
            ModernFile file = ModernFile.get(entryFile);
            byte[] buffer = file.readAllBytes();
            WadEntryArray.WadEntry entry = new WadEntryArray.WadEntry();
            entry.setName(file.getFileName());
            entry.setPayload(buffer);
            array.getEntries().add(entry);
        });
    }
}
