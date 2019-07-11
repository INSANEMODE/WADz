package sx.reece.wads;

import sx.reece.csharp.BitConverter;

import java.util.Arrays;

/**
 * Created by RSX on 06/10/2016.
 */
public enum Magic {
    T5(new byte[]{0x54, 0x33, 0x77, (byte) 0xAB}), T6(new byte[]{(byte) 0xAB, 0x77, 0x33, 0x54});

    private byte[] buf;
    Magic(byte[] bytes) {
        buf = bytes;
    }

    public byte[] getBuf() {
        return buf;
    }

    public String toGame() {
        return BitConverter.toString(buf); //eh... all games have their magic as an alias
    }

    Magic getMagicByMagic(byte[] m) {
        for (Magic magic : Magic.values()) {
            if (Arrays.equals(magic.getBuf(), m))
                return magic;
        }
        return null;
    }
}
