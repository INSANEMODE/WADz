package sx.reece.bootstrap;

import sx.reece.csharp.BitConverter;
import sx.reece.csharp.Console;
import sx.reece.logger.Logger;
import sx.reece.modern.ModernBuffer;
import sx.reece.modern.ModernFile;
import sx.reece.stream.ModernOutputStreamWriter;
import sx.reece.wads.Magic;
import sx.reece.wads.WadEntryArray;
import sx.reece.wads.api.WadApi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

//extremely ugly function just to test and create wads
public class Main {
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
        if (!(dir.endsWith("/") || dir.endsWith("\\"))) dir += "/";
        Logger.log("Wad file:");
        fileName = Console.readLine();
        if (isBuilding) {
            File file = ModernFile.getFileOrCreateNew(fileName);
            ModernOutputStreamWriter buffer = new ModernOutputStreamWriter(new FileOutputStream(file));
            WadApi.WadFile wad = WadApi.getNewWadFromGame(magic.toGame(), buffer);
            setEntriesFromDir(new File(dir), wad.getEntries());
            wad.getHeader().setFfotdVersion(0);
            wad.getHeader().setMagic(magic.getBuf());
            wad.write();
            buffer.flush();
            return;
        }
        String fuckjavaeight = dir;
        ModernBuffer bb = ModernFile.get(fileName).getModernBuffer();
        String game = BitConverter.toString(bb.readBytes(4));
        bb.setIndex(0);
        WadApi.readWad(game, bb).getEntries().getEntries().forEach(item -> {
            ModernFile.get(ModernFile.getFileOrCreateNew(fuckjavaeight + item.getName())).writeAllBytes(item.getFile());
        });
    }

    public static void setEntriesFromDir(File dir, WadEntryArray array) {
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
