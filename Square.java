package jump61;
import static jump61.Color.*;

/** An object for squares within the board.
 *  @author Derrick Mar
 */

class Square {

    /** number of spots on square. */
    private int _spots;
    /** color of square. */
    private Color _color;
    /** String representation of square for dump method. */
    private String stringRep;

    /** Square constructor that creates a square with COLOR
     * and SPOTS. */
    Square(Color color, int spots) {
        _color = color;
        _spots = spots;
        updateStringRep();
    }

    /** returns stringRep of Square. */
    String getStringRep() {
        return stringRep;
    }

    /** updates stringRep when called. */
    void updateStringRep() {
        if (this._spots == 0) {
            stringRep = "--";
        } else if (this._color == Color.RED) {
            stringRep = String.format("%d" + "r", _spots);
        } else if (this._color == Color.BLUE) {
            stringRep = String.format("%d" + "b", _spots);
        } else {
            System.err.println("invalid color");
        }
    }

    /** Returns color of Square. */
    Color getColor() {
        return _color;
    }

    /** Returns spots of Square. */
    int getSpots() {
        return _spots;
    }

    /** Add SPOTS to _spots. */
    void add(int spots) {
        _spots += spots;
    }

    /** Change SPOTS of _spots. */
    void changeSpots(int spots) {
        _spots = spots;
    }

    /** changes color of square to COLOR. */
    void changeColor(Color color) {
        _color = color;
    }

}
