package sx.reece.wads.api;

import sx.reece.games.Game;
import sx.reece.modern.ModernBuffer;
import sx.reece.wads.Header;
import sx.reece.wads.WadEntryArray;

/**
 * Created by RSX on 06/10/2016.
 */
public class WadApi {
    private static WadFile read(Game game) {
        Header header = game.readHeader();
        WadEntryArray array = game.readEntryArray(header);
        game.readFiles(array);
        WadFile wad = new WadFile(game);
        wad.setEntries(array);
        wad.setHeader(header);
        return wad;
    }

    public static WadFile readWad(String game, ModernBuffer buffer) {
        Game gwad = Game.getFromName(game, buffer);
        return read(gwad);
    }

    public static WadFile getNewWadFromGame(String game, ModernBuffer buffer) {
        return new WadFile(Game.getFromName(game, buffer));
    }

    public static class WadFile {
        private Game game;
        private Header header = new Header();
        private WadEntryArray entries = new WadEntryArray();

        public WadFile(Game game) {
            this.game = game;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Game getGame() {
            return game;
        }

        public WadEntryArray getEntries() {
            return entries;
        }

        public void setEntries(WadEntryArray entries) {
            this.entries = entries;
        }

        public void write() {
            game.writeHeader(header, entries);
            game.writeEntries(entries);
        }
    }
}
