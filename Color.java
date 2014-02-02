
package jump61;

/** Player and square colors for jump61.
 *  @author P. N. Hilfinger
 */
enum Color {

    /** Possible square or player colors. */
    WHITE, RED, BLUE;

    /** Return the reverse of this Color: BLUE for RED, RED for BLUE, WHITE for
     *  WHITE. */
    Color opposite() {
        switch (this) {
        case BLUE:
            return RED;
        case RED:
            return BLUE;
        default:
            return WHITE;
        }
    }

    /** Return true iff it is legal for the player of THIS Color to play on
     *  a square of Color COLOR. */
    boolean playableSquare(Color color) {
        return color == WHITE || color == this;
    }

    /** Return the color named COLORNAME, ignoring case differences (convenience
     *  method). */
    static Color parseColor(String colorName) {
        if (colorName.equals("r")) {
            return valueOf("RED");
        } else if (colorName.equals("b")) {
            return valueOf("BLUE");
        } else if (colorName.equals("w")) {
            return valueOf("WHITE");
        } else {
            return valueOf(colorName.toUpperCase());
        }
    }

    /** takes in COLOR and returns a String version of it with
     *  the first character capitalized. */
    static String capFirst(Color color) {
        if (color == RED) {
            return "Red";
        } else if (color == BLUE) {
            return "Blue";
        } else {
            return "White";
        }
    }

    /** Return my lower-case name. */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /** Return my capitalized name. */
    public String toCapitalizedString() {
        return super.toString().charAt(0) + toString().substring(1);
    }
}
