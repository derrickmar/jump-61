package jump61;

/** An unchecked exception that represents internally any kind of user
 *  input error.
 *  @author Derrick Mar
 */
class GameSyntaxException extends GameException {

    /** A GameException with no message. */
    GameSyntaxException() {
    }

    /** A GameException for which .getMessage() is MSG. */
    GameSyntaxException(String msg) {
        super(msg);
    }

}
