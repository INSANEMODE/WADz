package sx.reece.wads.cli;

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
    private static void build(String fileName, String dir, Game game) throws Exception {
        ModernOutputStreamWriter streamWriter;
        WadFile wad;
        File file;

        file         = ModernFile.getFileOrCreateNew(fileName);
        streamWriter = new ModernOutputStreamWriter(new FileOutputStream(file));
        wad          = new WadFile(game);

        setEntriesFromDir(new File(dir), wad.getEntries());
        wad.getHeader().setFfotdVersion(0);
        wad.getHeader().setMagic(game.getMagic().getBuf());

        wad.write(streamWriter);
        streamWriter.flush();
    }

    private static void unpack(String fileName, String dir) throws Exception {
        ModernBuffer wadFile;
        byte[] header;
        WadFile wad;
        Magic magic;
        Game game;

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
        Game game = null;
        String dir;
        String fileName;

        Logger.log("Are we building?");
        isBuilding = Console.readLine().contains("y");

        if (isBuilding) {
            Logger.log("Are we using %s or %s (y = first - t6/mw3)?", Game.PCT6.getName(), Game.PCT5.getName());
            game = Console.readLine().contains("y") ? Game.PCT6 : Game.PCT5;
        }

        Logger.log("The %s directory:", !isBuilding ? "export" : "input");
        dir = Console.readLine();
        if (!(dir.endsWith("/") || dir.endsWith("\\")))
            dir += "/";

        Logger.log("Wad file:");
        fileName = Console.readLine();

        if (isBuilding)
            build(fileName, dir, game);
        else
            unpack(fileName, dir);
    }

    private static void setEntriesFromDir(File dir, WadEntryArray array) {
        Arrays.stream(dir.listFiles()).forEach(entryFile ->{
            WadEntryArray.WadEntry entry;
            ModernFile file;
            byte[] buffer;

            file   = ModernFile.get(entryFile);
            buffer = file.readAllBytes();
            
            entry = new WadEntryArray.WadEntry();
            entry.setName(file.getFileName());
            entry.setPayload(buffer);

            array.getEntries().add(entry);
        });
    }
}
