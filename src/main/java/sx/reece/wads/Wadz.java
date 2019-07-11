package sx.reece.wads;

import sx.reece.wads.games.Game;
import sx.reece.javakit.modern.ModernBuffer;

public class Wadz {
    public static WadFile readWad(Game game, ModernBuffer buffer) {
        Header header = game.readHeader(buffer);
        WadEntryArray array = game.readEntryArray(buffer, header);
        game.readFiles(buffer, array);
        WadFile wad = new WadFile(game);
        wad.setEntries(array);
        wad.setHeader(header);
        return wad;
    }

    public static WadFile readWad(String game, ModernBuffer buffer) {
        Game gwad = Game.getFromName(game).orElseThrow(() -> {return new RuntimeException("Invalid Game");});
        return readWad(gwad, buffer);
    }

    public static WadFile newWad(String game) {
        Game gwad = Game.getFromName(game).orElseThrow(() -> {return new RuntimeException("Invalid Game");});
        return new WadFile(gwad);
    }

    public static WadFile newWad(Game game) {
        return new WadFile(game);
    }
}
