package jump61;

import java.util.Formatter;
import static jump61.Color.*;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered 0 - size()-1, in
 *  row 2 numbered size() - 2*size() - 1, etc.
 *  @author Derrick Mar
 */
abstract class Board {

    /** get method that returns moves. */
    int getMove() {
        unsupported("getMove");
        return 0;
    }

    /** add NUM to moves. */
    void addMove(int num) {
        unsupported("addMove");
    }

    /** change moves to NUM. */
    void changeMove(int num) {
        unsupported("changeMove");
    }

    /** checks for Win and returns boolean. */
    boolean checkForWin() {
        unsupported("checkForWin");
        return true;
    }

    /** announces winner by returning color. */
    Color announceWinner() {
        unsupported("checkForWin");
        return Color.WHITE;
    }

    /** returns the 2D array of board. */
    Square[][] getcurrBoard() {
        unsupported("getcurrBoard");
        return new Square[2][2];
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        unsupported("clear");
    }

    /** Return the number of rows and of columns of THIS. */
    abstract int size();

    /** Returns the number of spots in the square at row R, column C,
     *  1 <= R, C <= size (). */
    abstract int spots(int r, int c);

    /** Returns the color of the square at row R, column C,
     *  1 <= R, C <= size(). */
    abstract Color color(int r, int c);

    /** Returns the total number of moves made (red makes the odd moves,
     *  blue the even ones). */
    abstract int numMoves();

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 0 <= r && r <= size() - 1 && 0 <= c && c <= size() - 1;
    }

    /** Returns true iff it would currently be legal f
     *  or PLAYER to add a spot to square at row R, column C. */
    boolean isLegal(Color player, int r, int c) {
        return player == color(r, c) || color(r, c) == Color.WHITE;
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Color player, int r, int c) {
        unsupported("addSpot");
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Color player, int n) {
        unsupported("addSpot");
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Clear the undo
     *  history. */
    void set(int r, int c, int num, Color player) {
        unsupported("set");
    }

    /** constructs the dump representation for Board
     *  returns SAMPLE but this method will always be overriden. */
    StringBuilder constructDump() {
        StringBuilder sample = new StringBuilder("just a quick fix");
        return sample;
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();

        return out.toString();
    }

    /** Returns an external rendition of me, suitable for
     *  human-readable textual display.  This is distinct from the dumped
     *  representation (returned by toString). */
    public String toDisplayString() {
        StringBuilder out = new StringBuilder(toString());
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int num = 0;
        if (exists(r + 1, c)) {
            num += 1;
        }
        if (exists(r - 1, c)) {
            num += 1;
        }
        if (exists(r, c + 1)) {
            num += 1;
        }
        if (exists(r, c - 1)) {
            num += 1;
        }
        return num;
    }

    /** Indicate fatal error: OP is unsupported operation. */
    private void unsupported(String op) {
        String msg = String.format("'%s' operation not supported", op);
        throw new UnsupportedOperationException(msg);
    }

    /** The length of an end of line on this system. */
    private static final int NL_LENGTH =
        System.getProperty("line.separator").length();

}
