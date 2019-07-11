package sx.reece.wads;


import sx.reece.javakit.logger.Logger;
import sx.reece.javakit.modern.ModernString;
import sx.reece.javakit.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * Created by RSX on 06/10/2016.
 */
public class WadEntryArray {
    private List<WadEntry> entries = new ArrayList<>();

    public List<WadEntry> getEntries() {
        return entries;
    }

    public void print() {
        Logger.debug("-----------Got Files-----------");
        Logger.debug(ModernString.join(", ", entries.stream().map(WadEntry::getName).toArray()));
        Logger.debug("-----------Got Files-----------");
    }

    public static class WadEntry {
        private long offset = -1;
        private byte[] file = new byte[0];
        private long compressedLen;
        private CompressEntry compressEntry;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOffset() {
            if (offset == -1) Logger.warn("Offset is still -1 but get requested!");
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getSize() {
            return file.length;
        }

        public void setSize(long size) {
            file = new byte[(int) size];
        }

        public void setPayload(byte[] buffer) {
            file = buffer;
            compressEntry = new CompressEntry(buffer, false);
        }

        public void setCompressedPayload(byte[] buffer) {
            compressEntry = new CompressEntry(buffer, true);
            file = compressEntry.decompress(file.length);
        }

        public void setCompressedLen(long compressedLen) {
            this.compressedLen = compressedLen;
        }

        public long getCompressedLen() {
            return compressedLen;
        }

        public byte[] getFile() {
            return file;
        }

        public CompressEntry getCompressEntry() {
            return compressEntry;
        }

        public class CompressEntry {
            private byte[] buffer;

            public CompressEntry(byte[] entry, boolean compressed) {
                if (!compressed) {
                    Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
                    deflater.setInput(entry);
                    deflater.finish();
                    byte[] tempBuffer = new byte[Short.MAX_VALUE];
                    int len = deflater.deflate(tempBuffer);
                    buffer = new byte[len];
                    System.arraycopy(tempBuffer, 0, buffer, 0, len);
                } else {
                    buffer = entry;
                }
            }

            public byte[] decompress(long decompressedSize) {
                byte[] ret = new byte[(int) decompressedSize];
                Inflater decompresser = new Inflater();
                decompresser.setInput(buffer, 0, buffer.length);
               //Logger.debug(BitConverter.toString(buffer));
                int resultLength = -1;
                try {
                    resultLength = decompresser.inflate(ret);
                } catch (DataFormatException e) {
                    Logger.debug(DataUtils.getHex(buffer));
                    e.printStackTrace();
                    return null;
                }
                if (resultLength == -1) {
                    Logger.warn("Bad compressed size!");
                }
                decompresser.end();
                return ret;
            }

            public byte[] getCompressedBuffer() {
                return buffer;
            }

            public long getSize() {
                return buffer.length;
            }
        }
    }
}