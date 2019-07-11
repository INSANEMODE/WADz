package sx.reece.wads;

import sx.reece.wads.games.Game;
import sx.reece.javakit.utils.DataUtils;

import java.util.Arrays;
import java.util.Optional;

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

    public String toString() {
        return DataUtils.getHex(buf);
    }

    public Game toGame() {
        return Game.getFromMagic(this).orElseThrow(() -> {return new RuntimeException("Game not implemented");});
    }

    public static Optional<Magic> getByMagic(byte[] m) {
        return Arrays.stream(Magic.values()).filter((magic) -> Arrays.equals(magic.getBuf(), m)).findAny();
    }
}
