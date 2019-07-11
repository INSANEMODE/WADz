package sx.reece.games;

import sx.reece.logger.Logger;
import sx.reece.modern.ModernBuffer;
import sx.reece.wads.Header;
import sx.reece.wads.WadEntryArray;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by RSX on 06/10/2016.
 */
public class PCT5 extends Game {
    public PCT5() {

    }
    public PCT5(String path) {
        super(path);
    }

    public PCT5(byte[] buffer) {
        super(buffer);
    }

    public PCT5(ModernBuffer buffer) {
        super(buffer);
    }

    @Override
    public String getName() {
        return "Black Ops";
    }

    @Override
    public Header readHeader() {
        Header header = new Header();
        header.setMagic(mbuffer.readBytes(4));
        header.setTimestamp((int)mbuffer.readUInt32LE().getValue());
        header.setEntries((int) mbuffer.readUInt32BE().getValue());
        header.setFfotdVersion((int)mbuffer.readUInt32LE().getValue());
        header.print();
        return header;
    }

    @Override
    public WadEntryArray readEntryArray(Header header) {
        WadEntryArray array = new WadEntryArray();
        for (int i = 0; i < header.getEntries(); i++) {
            WadEntryArray.WadEntry entry = new WadEntryArray.WadEntry();

            String c = mbuffer.readCString();           //read name
            mbuffer.readBytes(32 - (c.length() + 1));   //read padding

            entry.setName(c);
            entry.setCompressedLen(mbuffer.readUInt32BE().getValue());
            entry.setSize(mbuffer.readUInt32BE().getValue());
            entry.setOffset(mbuffer.readUInt32BE().getValue());

            array.getEntries().add(entry);
        }
        array.print();
        return array;
    }

    @Override
    public WadEntryArray readFiles(WadEntryArray array) {
        array.getEntries().forEach(item -> item.setCompressedPayload(mbuffer.readBytes((int) item.getCompressedLen())));
        return array;
    }

    @Override
    public void writeEntries(WadEntryArray array) {
        int curOffset = ((4 * 4) + (array.getEntries().size() * 44));
        for (WadEntryArray.WadEntry item : array.getEntries()) {
            byte[] pad = new byte[32 - (item.getName().length() + 1)];
            mbuffer.writeCString(item.getName());
            mbuffer.writeBytes(pad);
            mbuffer.writeUInt32BE(item.getCompressEntry().getSize());
            mbuffer.writeUInt32BE(item.getSize());
            mbuffer.writeUInt32BE(curOffset);
            curOffset += (item.getCompressEntry().getSize());
        }
        array.getEntries().forEach(item -> mbuffer.writeBytes(item.getCompressEntry().getCompressedBuffer()));
    }

    @Override
    public void writeHeader(Header header, WadEntryArray entries) {
        mbuffer.writeBytes(header.getMagic());
        mbuffer.writeUInt32LE(header.getTimestamp());
        mbuffer.writeUInt32BE(entries.getEntries().size());
        mbuffer.writeUInt32LE(header.getFfotdVersion());
    }

    @Override
    public String[] aliases() {
        return new String[]{"steam_t5", "pct5", "54-33-77-AB"};
    }
}
