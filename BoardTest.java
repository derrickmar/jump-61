package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Boards.
 *  @author Derrick Mar
 */
public class BoardTest {

    private static final String NL = System.getProperty("line.separator");

    @Test
    public void testSize() {
        Board B = new MutableBoard(5);
        assertEquals("bad length", 5, B.size());
        ConstantBoard C = new ConstantBoard(B);
        assertEquals("bad length", 5, C.size());
    }

    @Test
    public void testSet() {
        Board B = new MutableBoard(5);
        B.set(2, 2, 1, RED);
        assertEquals("wrong number of spots", 1, B.spots(2, 2));
        assertEquals("wrong color", RED, B.color(2, 2));
    }

    @Test
    public void testMove() {
        Board B = new MutableBoard(6);
        B.addSpot(RED, 1, 1);
        checkBoard("#1", B, 1, 1, 1, RED);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#2", B, 1, 1, 1, RED, 2, 1, 1, BLUE);
        B.addSpot(RED, 1, 1);
        checkBoard("#3", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#4", B, 1, 1, 2, RED, 2, 1, 2, BLUE);
        B.addSpot(RED, 1, 1);
        checkBoard("#5", B, 1, 1, 3, RED, 2, 1, 2, BLUE);
    }

    @Test
    public void testclear() {
        Board B = new MutableBoard(3);
        B.addSpot(RED, 1, 1);
        B.addSpot(RED, 1, 1);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#1", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.clear(3);
        B.addSpot(RED, 1, 1);
        B.addSpot(RED, 1, 1);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#1", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
    }

    @Test
    public void testcheckForWin() {
        Board B = new MutableBoard(2);
        B.addSpot(RED, 0, 0);
        B.addSpot(RED, 0, 1);
        B.set(1, 0, 1, RED);
        B.set(1, 1, 1, RED);
        assertEquals("checkForWin fails", true, B.checkForWin());
    }

    @Test
    public void testjump() {
        Board B = new MutableBoard(2);
        B.addSpot(RED, 0, 0);
        B.addSpot(RED, 0, 0);
        B.addSpot(RED, 0, 0);
        checkBoard("#1", B, 0, 0, 1, RED, 0, 1, 1, RED,
                   1, 0, 1, RED);
        B.addSpot(RED, 0, 1);
        B.addSpot(RED, 0, 1);
        checkBoard("#1", B, 0, 0, 2, RED, 0, 1, 1, RED,
                   1, 0, 1, RED, 1, 1, 1, RED);
    }

    @Test
    public void testexist() {
        Board B = new MutableBoard(3);
        assertEquals("exist incorrect", true, B.exists(1, 1));
        assertEquals("exist incorrect", true, B.exists(0, 1));
        assertEquals("exist incorrect", true, B.exists(2, 1));
        assertEquals("exist incorrect", false, B.exists(5, 5));
    }

    @Test
    public void testneighbors() {
        Board B = new MutableBoard(3);
        assertEquals("neighbors incorrect", 2, B.neighbors(0, 0));
        assertEquals("neighbors incorrect", 3, B.neighbors(0, 1));
        assertEquals("neighbors incorrect", 4, B.neighbors(1, 1));
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
}
