package sx.reece.wads.games;

import sx.reece.javakit.modern.ModernBuffer;
import sx.reece.wads.Header;
import sx.reece.wads.Magic;
import sx.reece.wads.WadEntryArray;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Created by RSX on 06/10/2016.
 */
public abstract class Game {
    public static final Game PCT6 = new PCT6();
    public static final Game PCT5 = new PCT5();

    private static Game[] GAMES = {PCT6, PCT5};

    Game() { }

    public static Optional<Game> getFromName(String search) {
        return Arrays.stream(GAMES).filter((game) -> {
            boolean aliasMatch = Stream.of(game.aliases()).filter(name -> name.equals(search)).count() > 0;
            boolean nameMatch = search.equals(game.getName());
            return aliasMatch || nameMatch;
        }).findAny();
    }

    public static Optional<Game> getFromMagic(Magic magic) {
        return Arrays.stream(GAMES).filter((game) -> game.getMagic() == magic).findAny();
    }

    public abstract String getName();
    public abstract Header readHeader(ModernBuffer buffer);
    public abstract WadEntryArray readEntryArray(ModernBuffer buffer, Header header);
    public abstract WadEntryArray readFiles(ModernBuffer buffer, WadEntryArray array);
    public abstract void writeEntries(ModernBuffer buffer, WadEntryArray array);
    public abstract void writeHeader(ModernBuffer buffer, Header header, WadEntryArray entries);

    public abstract Magic getMagic();
    public abstract String[] aliases();

}
