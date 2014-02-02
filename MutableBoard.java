package jump61;
import static jump61.Color.*;

/** A Jump61 board state.
 *  @author Derrick Mar
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _N = N;
        _moves = 1;
        currBoard = new Square[_N][_N];
        for (int i = 0; i < _N; i += 1) {
            for (int j = 0; j < _N; j += 1) {
                currBoard[i][j] = new Square(Color.WHITE, 0);
            }
        }
    }

    /** A board constructor that takes in ORIGBOARD, its SIZE,
     *  and its MOVES to create a deep copy of the board. */
    MutableBoard(Board origBoard, int size, int moves) {
        _N = size;
        _moves = moves;
        currBoard = new Square[_N][_N];
        Square [][] origcurrBoard = origBoard.getcurrBoard();
        for (int i = 0; i < _N; i += 1) {
            for (int j = 0; j < _N; j += 1) {
                currBoard[i][j] =
                    new Square(origcurrBoard[i][j].getColor(),
                               origcurrBoard[i][j].getSpots());
            }
        }
    }

    @Override
    Square[][] getcurrBoard() {
        return currBoard;
    }

    @Override
    StringBuilder constructDump() {
        StringBuilder dumpRep = new StringBuilder("===\n   ");
        for (int i = 0; i < _N; i += 1) {
            for (int j = 0; j < _N; j += 1) {
                dumpRep.append(" " + currBoard[i][j].getStringRep());
            }
            if (i != _N - 1) {
                dumpRep.append("\n   ");
            } else {
                dumpRep.append("\n");
            }
        }
        dumpRep.append("===");
        return dumpRep;
    }

    @Override
    int getMove() {
        return _moves;
    }

    @Override
    void addMove(int num) {
        _moves += num;
    }

    @Override
    void changeMove(int num) {
        _moves = num;
    }

    @Override
    void clear(int N) {
        currBoard = new Square[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                currBoard[i][j] = new Square(Color.WHITE, 0);
            }
        }
        _N = N;
        _moves = 1;
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        return currBoard[r][c].getSpots();
    }

    @Override
    Color color(int r, int c) {
        return currBoard[r][c].getColor();
    }

    @Override
    int numMoves() {
        return _moves;
    }

    @Override
    boolean checkForWin() {
        int checker = 0;
        for (int i = 0; i < _N; i += 1) {
            for (int j = 0; j < _N; j += 1) {
                if (currBoard[i][j].getColor() == Color.WHITE) {
                    return false;
                } else if (currBoard[i][j].getColor()
                           == Color.RED) {
                    checker += 1;
                }
            }
        }
        if (checker == 0 || checker == _N * _N) {
            return true;
        }
        return false;
    }

    @Override
    Color announceWinner() {
        return currBoard[0][0].getColor();
    }

    @Override
    void addSpot(Color player, int r, int c) {
        if (isLegal(player, r, c)) {
            currBoard[r][c].add(1);
            currBoard[r][c].changeColor(player);
            currBoard[r][c].updateStringRep();
            if (checkForWin()) {
                return;
            }
            jump(r, c);
        } else {
            _moves -= 1;
            System.err.println("Cannot add spot to opponent's square");

        }
    }

    @Override
    void set(int r, int c, int num, Color player) {
        if (num > neighbors(r, c)) {
            System.err.println("Cannot set spots greater than neighbors");
        } else {
            if (exists(r, c)) {
                if (num != 0) {
                    currBoard[r][c].changeSpots(num);
                    currBoard[r][c].changeColor(player);
                    currBoard[r][c].updateStringRep();
                } else {
                    currBoard[r][c].changeSpots(0);
                    currBoard[r][c].changeColor(Color.WHITE);
                    currBoard[r][c].updateStringRep();
                }
                if (checkForWin()) {
                    System.out.println("Winner is " + announceWinner());
                    return;
                }
            } else {
                System.err.println("Error: Spot does not exist");
            }
        }
    }

    /** Do all jumping on this board, assuming that initially,
     *  R and C  is the only square that might be over-full. */
    private void jump(int r, int c) {
        Color currColor = currBoard[r][c].getColor();
        if (spots(r, c) > neighbors(r, c)) {
            set(r, c, 1, currColor);
            if (exists(r + 1, c)) {
                currBoard[r + 1][c].changeColor(currColor);
                addSpot(currBoard[r + 1][c].getColor(), r + 1, c);
            }
            if (exists(r - 1, c)) {
                currBoard[r - 1][c].changeColor(currColor);
                addSpot(currBoard[r - 1][c].getColor(), r - 1, c);
            }
            if (exists(r, c + 1)) {
                currBoard[r][c + 1].changeColor(currColor);
                addSpot(currBoard[r][c + 1].getColor(), r, c + 1);
            }
            if (exists(r, c - 1)) {
                currBoard[r][c - 1].changeColor(currColor);
                addSpot(currBoard[r][c - 1].getColor(), r, c - 1);
            }
        }
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;

    /** 2D array representation of the Board. */
    private Square[][] currBoard;

    /** Convenience variable: size of board (squares along one edge). */
    private int _N;

}
