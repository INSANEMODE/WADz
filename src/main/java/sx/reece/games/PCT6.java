package sx.reece.games;

import sx.reece.logger.Logger;
import sx.reece.modern.ModernBuffer;
import sx.reece.wads.Header;
import sx.reece.wads.WadEntryArray;

/**
 * Created by RSX on 06/10/2016.
 */
public class PCT6 extends PCT5 {
    public PCT6() {

    }
    public PCT6(String path) {
        super(path);
    }

    public PCT6(byte[] buffer) {
        super(buffer);
    }

    public PCT6(ModernBuffer buffer) {
        super(buffer);
    }

    @Override
    public String getName() {
        return "Black Ops 2";
    }

    @Override
    public Header readHeader() {
        Header header = new Header();
        header.setMagic(mbuffer.readBytes(4));
        header.setTimestamp(mbuffer.readUInt32BE().getValue());
        header.setEntries((int)mbuffer.readUInt32LE().getValue());
        header.setFfotdVersion((int)mbuffer.readUInt32BE().getValue());
        header.print();
        return header;
    }

    @Override
    public WadEntryArray readFiles(WadEntryArray array) {
        array.getEntries().forEach(item -> item.setCompressedPayload(mbuffer.readBytes((int) item.getCompressedLen())));
        return array;
    }

    @Override
    public void writeHeader(Header header, WadEntryArray entries) {
        mbuffer.writeBytes(header.getMagic());
        mbuffer.writeUInt32BE(header.getTimestamp());
        mbuffer.writeUInt32LE(entries.getEntries().size());
        mbuffer.writeUInt32BE(header.getFfotdVersion());
    }

    @Override
    public String[] aliases() {
        return new String[]{"steam_t6", "steam_iw5", "iw4", "t6", "pciw5", "pct6", "AB-77-33-54"};
    }
}
