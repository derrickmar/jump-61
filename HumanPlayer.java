package jump61;

/** A Player that gets its moves from manual input.
 *  @author Derrick Mar
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        System.out.println("this is not used in Human Player");
        return;
    }

    @Override
    void makeMove(int row, int col) {
        Game game = getGame();
        Board mutBoard = getMutableBoard();
        mutBoard.addSpot(getColor(), row, col);
    }

}
