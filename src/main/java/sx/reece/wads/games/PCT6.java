package sx.reece.wads.games;

import sx.reece.javakit.modern.ModernBuffer;
import sx.reece.wads.Header;
import sx.reece.wads.Magic;
import sx.reece.wads.WadEntryArray;

/**
 * Created by RSX on 06/10/2016.
 */
public class PCT6 extends PCT5 {
    public PCT6() {

    }

    @Override
    public String getName() {
        return "Black Ops 2";
    }

    @Override
    public Header readHeader(ModernBuffer mbuffer) {
        Header header = new Header();
        header.setMagic(mbuffer.readBytes(4));
        header.setTimestamp(mbuffer.readUInt32LE().getValue());
        header.setEntries((int)mbuffer.readUInt32LE().getValue());
        header.setFfotdVersion((int)mbuffer.readUInt32LE().getValue());
        return header;
    }

    @Override
    public WadEntryArray readFiles(ModernBuffer mbuffer, WadEntryArray array) {
        array.getEntries().forEach(item -> item.setCompressedPayload(mbuffer.readBytes((int) item.getCompressedLen())));
        return array;
    }

    @Override
    public void writeHeader(ModernBuffer mbuffer, Header header, WadEntryArray entries) {
        mbuffer.writeBytes(header.getMagic());
        mbuffer.writeUInt32LE(header.getTimestamp());
        mbuffer.writeUInt32LE(entries.getEntries().size());
        mbuffer.writeUInt32LE(header.getFfotdVersion());
    }

    @Override
    public String[] aliases() {
        return new String[]{"steam_t6", "steam_iw5", "iw4", "t6", "pciw5", "pct6", "AB-77-33-54"};
    }

    @Override
    public Magic getMagic() {
        return Magic.T6;
    }
}
