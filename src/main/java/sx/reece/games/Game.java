package sx.reece.games;

import sx.reece.modern.ModernBuffer;
import sx.reece.modern.ModernFile;
import sx.reece.wads.Header;
import sx.reece.wads.WadEntryArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Created by RSX on 06/10/2016.
 */
public abstract class Game {
    public static List<Class<? extends Game>> games = new ArrayList<>();

    protected Game() {
    }

    public static Game getFromName(String game, String path) {
        try {
            return getFromName(game).getDeclaredConstructor(String.class).newInstance(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static Game getFromName(String game, ModernBuffer buffer) {
        try {
            return getFromName(game).getDeclaredConstructor(ModernBuffer.class).newInstance(buffer);
        } catch (Exception e) {
            return null;
        }
    }

    private static Class<? extends Game> getFromName(String search) {
        Optional<Class<? extends Game>> ret = games.stream().filter(clazz -> {
            Game item = null;
            try {
                item = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Stream.of(item.aliases()).filter(name -> name.equals(search)).count() > 0 || search.equals(item.getName());
        }).findAny();
        return  ret.get();
    }

    static {
        games.add(((Game) new PCT6()).getClass());
        games.add(((Game) new PCT5()).getClass());
    }

    protected ModernBuffer mbuffer;

    public Game(String path) {
        mbuffer = new ModernFile(path).getModernBuffer();
    }

    public Game(byte[] buffer) {
        mbuffer = ModernBuffer.fromBytes(buffer);
    }

    public Game(ModernBuffer buffer) {
        mbuffer = buffer;
    }

    public abstract String getName();
    public abstract Header readHeader();
    public abstract WadEntryArray readEntryArray(Header header);
    public abstract WadEntryArray readFiles(WadEntryArray array);
    public abstract void writeEntries(WadEntryArray array);
    public abstract void writeHeader(Header header, WadEntryArray entries);
    public abstract String[] aliases();

}
