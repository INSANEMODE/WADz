package sx.reece.wads;


import sx.reece.javakit.csharp.BitConverter;
import sx.reece.javakit.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RSX on 06/10/2016.
 */
public class Header {
    private byte[] magic;
    private long timestamp = System.currentTimeMillis();
    private int ffotdVersion = 0;
    private int entries = 0;

    public int getFfotdVersion() {
        return ffotdVersion;
    }

    public void setFfotdVersion(int ffotdVersion) {
        this.ffotdVersion = ffotdVersion;
    }

    public byte[] getMagic() {
        return magic;
    }

    public void setMagic(byte[] magic) {
        this.magic = magic;
    }

    public int getTimestamp() {
        return (int) timestamp / 1000;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp * 1000;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

    public void setTime(Date date) {
        this.timestamp = date.getTime();
    }

    public void print() {
        Logger.debug("----------- Header -----------");
        Logger.debug("Version  : " + ffotdVersion);
        Logger.debug("Time     : " + new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(new Date(timestamp)));
        Logger.debug("Entities : " + entries);
        Logger.debug("Magic    : " + BitConverter.toString(magic));
        Logger.debug("----------- Header -----------");
    }
}
