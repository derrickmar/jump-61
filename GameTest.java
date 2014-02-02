package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/** Unit tests of Boards.
 *  @author Derrick Mar
 */
public class GameTest {

    @Test
    public void teststart() {
        Writer output = new OutputStreamWriter(System.out);
        Game game = new Game(new InputStreamReader(System.in),
                             output, output,
                             new OutputStreamWriter(System.err));
        game.start();
        assertEquals("start not working", true, game.getPlaying());
    }

    @Test
    public void testmakeMove() {
        Writer output = new OutputStreamWriter(System.out);
        Game game = new Game(new InputStreamReader(System.in),
                             output, output,
                             new OutputStreamWriter(System.err));
        game.start();
        game.makeMove(1, 1);
        checkBoard("#1", game.getMutableBoard(), 0, 0, 1, RED);
    }



    private void checkBoard(String msg, Board B, Object... contents) {
        for (int k = 0; k < contents.length; k += 4) {
            String M = String.format("%s at %d %d", msg, contents[k],
                                     contents[k + 1]);
            assertEquals(M, (int) contents[k + 2],
                         B.spots((int) contents[k], (int) contents[k + 1]));
            assertEquals(M, contents[k + 3],
                         B.color((int) contents[k], (int) contents[k + 1]));
        }
        int c;
        c = 0;
        for (int i = B.size() - 1; i >= 0; i -= 1) {
            for (int j = B.size() - 1; j >= 0; j -= 1) {
                assertTrue("bad white square #" + i,
                           (B.color(i, j) == WHITE) == (B.spots(i, j) == 0));
                if (B.color(i, j) != WHITE) {
                    c += 1;
                }
            }
        }
        assertEquals("extra squares filled", contents.length / 4, c);
    }


    /** NOTE: Please note that there are not many tests here due
     *  due to the implementation of my game. Almost all methods
     *  in Game call methods in Mutatable Board. This is where
     *  I did most of my testing. Also, many of the the methods
     *  in game are private and are only able to be tested through
     *  reflection. I did however, test all these methods specfically
     *  without JUnit tests. */

}
