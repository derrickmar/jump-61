package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Squares.
 *  @author Derrick Mar
 */
public class SquareTest {

    @Test
    public void testAdd() {
        Square S = new Square(Color.WHITE, 0);
        assertEquals("add not working", 0, S.getSpots());
        S.add(1);
        assertEquals("add not working", 1, S.getSpots());
        S.add(1);
        assertEquals("add not working", 2, S.getSpots());
    }

    @Test
    public void testchangeSpots() {
        Square S = new Square(Color.RED, 5);
        S.changeSpots(3);
        assertEquals("changeSpots not working", 3, S.getSpots());
        S.changeSpots(10);
        assertEquals("changeSpots not working", 10, S.getSpots());
    }

    @Test
    public void testchangeColor() {
        Square S = new Square(Color.RED, 3);
        S.changeColor(Color.BLUE);
        assertEquals("changeColor not working",
                     Color.BLUE, S.getColor());
    }

    @Test
    public void testStringRep() {
        Square S = new Square(Color.WHITE, 0);
        assertEquals("String not working", "--", S.getStringRep());
        S.add(1);
        S.changeColor(Color.BLUE);
        S.updateStringRep();
        assertEquals("String not working", "1b", S.getStringRep());
        S.add(1);
        S.changeColor(Color.RED);
        S.updateStringRep();
        assertEquals("String not working", "2r", S.getStringRep());
    }


}
