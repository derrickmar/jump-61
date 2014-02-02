package jump61;
import java.util.List;
import java.util.ArrayList;

/** An automated Player.
 *  @author Derrick Mar
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board mutBoard = getMutableBoard();
        int[] result = minmax(getColor(), mutBoard,
                              4, Integer.MIN_VALUE,
                              Integer.MAX_VALUE);
        mutBoard.addSpot(getColor(), result[1], result[2]);
        System.out.println(Color.capFirst(getColor()) + " moves "
                           + (result[1] + 1) + " "
                           + (result[2] + 1) + ".");
    }

    @Override
    void makeMove(int row, int col) {
        System.out.println("this is not used in AI");
        return;
    }

    /** minmax algorithm for AI. The alrorithm takes in the
     *  AI Player P and the Board B. Also considers Depth D and
     *  has alpha-beta pruning with ALPHA and BETA. Returns
     *  the moves in an integer array. YEAH BUDDY! */
    private int[] minmax(Color p, Board b, int d, int alpha, int beta) {
        List<int[]> nextMoves = generateMoves(p, b);
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;
        if (d == 0 || b.checkForWin()) {
            int heuristic =  staticEval(b);
            return new int[] {heuristic, bestRow, bestCol};
        } else {
            for (int[] move: nextMoves) {
                Board nextcopyBoard =
                    new MutableBoard(b, b.size(), b.getMove());
                if (p == getColor()) {
                    nextcopyBoard.addSpot(p, move[0], move[1]);
                    currentScore =
                        minmax(p.opposite(), nextcopyBoard,
                               d - 1, alpha, beta)[0];
                    if (currentScore >= alpha) {
                        alpha = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    nextcopyBoard.addSpot(p, move[0], move[1]);
                    currentScore =
                        minmax(p.opposite(), nextcopyBoard,
                               d - 1, alpha, beta)[0];
                    if (currentScore <= beta) {
                        beta = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return new int[] {(p == getColor()) ? alpha : beta, bestRow, bestCol};
    }

    /** generates a list of possible moves for player P
     * with board B and returns this list. */
    private List<int[]> generateMoves(Color p, Board b) {
        List<int[]> possibleMoves = new ArrayList<int[]>(0);
        Square[][] currBoard = b.getcurrBoard();
        if (b.checkForWin()) {
            return possibleMoves;
        }
        for (int i = 0; i < b.size(); i += 1) {
            for (int j = 0; j < b.size(); j += 1) {
                int[] currMove = new int[2];
                if (currBoard[i][j].getColor() == p
                    || currBoard[i][j].getColor() == Color.WHITE) {
                    currMove[0] = i;
                    currMove[1] = j;
                    possibleMoves.add(currMove);
                }
            }
        }
        return possibleMoves;
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Board b) {
        int value = 0;
        Square[][] currBoard = b.getcurrBoard();
        if (b.checkForWin()) {
            if (b.announceWinner() != getColor()) {
                value = Integer.MIN_VALUE;
            } else {
                value = Integer.MAX_VALUE;
            }
        } else {
            for (int i = 0; i < currBoard.length; i += 1) {
                for (int j = 0; j < currBoard[i].length; j += 1) {
                    if (currBoard[i][j].getColor() == getColor()) {
                        value += currBoard[i][j].getSpots() * 10;
                        value += 10;
                    }
                    if (currBoard[i][j].getColor()
                        == getColor().opposite()) {
                        value -= currBoard[i][j].getSpots() * 10;
                        value -= 10;
                    }

                }
            }
        }
        return value;
    }
}
