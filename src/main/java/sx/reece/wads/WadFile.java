package sx.reece.wads;

import sx.reece.wads.games.Game;
import sx.reece.javakit.modern.ModernBuffer;

public class WadFile {
    private Game game;
    private Header header = new Header();
    private WadEntryArray entries = new WadEntryArray();

    protected WadFile(Game game) {
        this.game = game;
    }

    public Header getHeader() {
        return header;
    }

    protected void setHeader(Header header) {
        this.header = header;
    }

    public Game getGame() {
        return game;
    }

    public WadEntryArray getEntries() {
        return entries;
    }

    protected void setEntries(WadEntryArray entries) {
        this.entries = entries;
    }

    public void write(ModernBuffer buffer) {
        game.writeHeader(buffer, header, entries);
        game.writeEntries(buffer, entries);
    }

    public void print() {
        header.print();
        entries.print();
    }
}
