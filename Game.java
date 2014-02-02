package jump61;

import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

import static jump61.Color.*;
import static jump61.GameException.error;

/** Main logic for playing (a) game(s) of Jump61.
 *  @author Derrick Mar
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "jump61/Help.txt";

    /** A new Game that takes command/move input from INPUT, prints
     *  normal output on OUTPUT, prints prompts for input on PROMPTS,
     *  and prints error messages on ERROROUTPUT. The Game now "owns"
     *  INPUT, PROMPTS, OUTPUT, and ERROROUTPUT, and is responsible for
     *  closing them when its play method returns. */
    Game(Reader input, Writer prompts, Writer output, Writer errorOutput) {
        _board = new MutableBoard(Defaults.BOARD_SIZE);
        _readonlyBoard = new ConstantBoard(_board);
        _prompter = new PrintWriter(prompts, true);
        _inp = new Scanner(input);
        _inp.useDelimiter("(?m)$|^|\\p{Blank}+");
        _out = new PrintWriter(output, true);
        _err = new PrintWriter(errorOutput, true);

        playerRed = new HumanPlayer(this, Color.RED);
        playerBlue = new AI(this, Color.BLUE);
        _playing = false;
        size = Defaults.BOARD_SIZE;
    }

    /** Returns a readonly view of the game board.  This board remains valid
     *  throughout the session. */
    Board getBoard() {
        return _readonlyBoard;
    }

    /** Returns the mutable game board. */
    Board getMutableBoard() {
        return _board;
    }

    /** Play a session of Jump61.  This may include multiple games,
     *  and proceeds until the user exits.  Returns an exit code: 0 is
     *  normal; any positive quantity indicates an error.  */
    int play() {
        _out.println("Welcome to " + Defaults.VERSION);
        _out.flush();
        while (promptForNext()) {
            readExecuteCommand();
        }
        return 0;
    }

    /** Returns true if NUM is odd. */
    boolean isOdd(int num) {
        return num % 2 == 1;
    }

    /** method that enables the AI to move. Called in
     *  promptforNext() every time to check if AI should move */
    void aiMove() {
        while (_playing) {
            if (isOdd(_board.getMove())
                && playerRed instanceof AI) {
                playerRed.makeMove();
                _board.addMove(1);
                checkForWin();
            } else if (!isOdd(_board.getMove())
                       && playerBlue instanceof AI) {
                playerBlue.makeMove();
                _board.addMove(1);
                checkForWin();
            } else {
                return;
            }
        }
    }

    /** Add a spot to R C, if legal to do so. */
    void makeMove(int r, int c) {
        if (_playing) {
            if (_board.exists(r - 1, c - 1)) {
                if (isOdd(_board.getMove())) {
                    playerRed.makeMove(r - 1, c - 1);
                    _board.addMove(1);
                    checkForWin();
                } else {
                    playerBlue.makeMove(r - 1, c - 1);
                    _board.addMove(1);
                    checkForWin();
                }
            } else {
                reportError("move %d %d out of bounds", r, c);
            }
        } else {
            reportError("no game in progress");
        }
    }

    /** Send a message to the user as determined by FORMAT and ARGS, which
     *  are interpreted as for String.format or PrintWriter.printf. */
    void message(String format, Object... args) {
        _out.printf(format, args);
    }

    /** Check whether we are playing and there is an unannounced
     *  winner.
     *  If so, announce and stop play. */
    private void checkForWin() {
        if (_board.checkForWin()) {
            announceWinner();
        }
        return;
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        if (_board.announceWinner() == Color.RED) {
            _out.println("Red wins.");
        } else {
            _out.println("Blue wins.");
        }
        restartGame();
    }

    /** Make PLAYER an AI for subsequent moves. */
    private void setAuto(Color player) {
        if (player == Color.RED) {
            playerRed = new AI(this, Color.RED);
        } else {
            playerBlue = new AI(this, Color.BLUE);
        }
    }

    /** Make PLAYER take manual input from the user for subsequent moves. */
    private void setManual(Color player) {
        if (player == Color.RED) {
            playerRed = new HumanPlayer(this, Color.RED);
        } else {
            playerBlue = new HumanPlayer(this, Color.BLUE);
        }
    }

    /** Stop any current game and clear the board to its initial
     *  state. */
    private void clear() {
        _playing = false;
        _board.clear(size);
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _out.println(_board.constructDump());
        _out.flush();
    }

    /** Print a help message. */
    private void help() {
        Main.printHelpResource(HELP, _out);
    }

    /** Stop any current game and set the move number to N. */
    private void setMoveNumber(int n) {
        _board.changeMove(n);
        System.out.println("changed move");
        _playing = false;
    }

    /** Seed the random-number generator with SEED. */
    private void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Place SPOTS spots on square R:C and color the square red or
     *  blue depending on whether COLOR is "r" or "b".  If SPOTS is
     *  0, clears the square, ignoring COLOR.  SPOTS must be less than
     *  the number of neighbors of square R, C. */
    private void setSpots(int r, int c, int spots, String color) {
        if (!_board.exists(r, c)) {
            throw error("move %d %d out of bounds", r, c);
        } else if (color.equals("r")) {
            _board.set(r, c, spots, Color.RED);
        } else if (color.equals("b")) {
            _board.set(r, c, spots, Color.BLUE);
        } else if (spots == 0) {
            _board.set(r, c, spots, Color.WHITE);
        } else {
            throw error("Error: syntax error in 'set' command");
        }
    }

    /** Stop any current game and set the board to an empty N x N board
     *  with numMoves() == 0.  */
    private void setSize(int n) {
        _playing = false;
        size = n;
        _board.clear(n);
    }

    /** Begin accepting moves for game.  If the game is won,
     *  immediately print a win message and end the game. */
    void restartGame() {
        clear();
    }

    /** Save move R C in _move.  Error if R and C do not indicate an
     *  existing square on the current board. */
    private void saveMove(int r, int c) {
        if (!_board.exists(r, c)) {
            throw error("move %d %d out of bounds", r, c);
        }
        _move[0] = r;
        _move[1] = c;
    }

    /** Returns a color (player) name from _inp: either RED or BLUE.
     *  Throws an exception if not present. */
    private Color readColor() {
        return Color.parseColor(_inp.next("[rR][eE][dD]|[Bb][Ll][Uu][Ee]"));
    }

    /** readColor to see if STR appropriate command for commands
     *  that contain a color. */
    private void readColor(String str) {
        boolean matches = str.matches("[rR][eE][dD]|[Bb][Ll][Uu][Ee]");
        if (matches) {
            return;
        } else {
            throw new GameSyntaxException();
        }
    }

    /** readColor to see if STR is appropriate command specifically
     *  for set. */
    private void readColorForSet(String str) {
        boolean matches = str.matches("[b]|[r]");
        if (matches) {
            return;
        } else {
            throw new GameSyntaxException();
        }
    }

    /** Read and execute one command.  Leave the input at
     *  the start of a line, if there is more input. */
    private void readExecuteCommand() {
        String cmnd = _inp.nextLine();
        executeCommand(cmnd);
    }

    /** checks if command for move is legal with row FIRST
     *  and column SECOND. This helper was created due to
     *  make style's requirement of having
     *  methods less than 60 lines. */
    private void activateMakeMove(String first, String second)
        throws GameSyntaxException {
        if (isInteger(second)) {
            makeMove(Integer.parseInt(first),
                     Integer.parseInt(second));
        } else {
            throw new GameSyntaxException();
        }
    }

    /** activates command with FC as first command the rest
     * of the arguments in LINE. This helper was created due to
     *  make style's requirement of having
     *  methods less than 60 lines. */
    private void activateCommand(String fc, String[] line)
        throws IndexOutOfBoundsException, NumberFormatException,
               GameException {
        if (isInteger(fc)) {
            activateMakeMove(fc, line[1]);
        } else {
            switch (fc) {
            case "":
                break;
            case "clear":
                clear(); break;
            case "start":
                start(); break;
            case "quit":
                System.exit(0); break;
            case "auto":
                readColor(line[1]);
                setAuto(Color.parseColor(line[1])); break;
            case "manual":
                readColor(line[1]);
                setManual(Color.parseColor(line[1])); break;
            case "size":
                setSize(Integer.parseInt(line[1])); break;
            case "move":
                setMoveNumber(Integer.parseInt(line[1])); break;
            case "set":
                if (Integer.parseInt(line[3]) != 0) {
                    readColorForSet(line[4]);
                } else {
                    line[4] = "WHITE";
                }
                _board.set(Integer.parseInt(line[1]) - 1,
                           Integer.parseInt(line[2]) - 1,
                           Integer.parseInt(line[3]),
                           Color.parseColor(line[4])
                           );
                _playing = false; break;
            case "seed":
                break;
            case "dump":
                dump(); break;
            case "help":
                help(); _out.flush(); break;
            case "\n": case "\r\n":
                return;
            case "#":
                break;
            default:
                throw new GameException();
            }
        }
    }

    /** Gather arguments and execute command CMND.
     *  Throws GameException on errors. */
    void executeCommand(String cmnd) {
        String[] line = cmnd.split("\\p{Blank}+");
        if (line[0].equals("")) {
            line = Arrays.copyOfRange(line, 1, line.length);
            if (line.length == 0) {
                return;
            }
        }
        String fc = line[0];
        try {
            activateCommand(fc, line);
        } catch (IndexOutOfBoundsException e) {
            reportError("not enough arguments");
        } catch (NumberFormatException e) {
            reportError("syntax error in '%s' command", fc);
        } catch (GameSyntaxException e) {
            if (isInteger(fc)) {
                reportError("syntax error in 'move' command");
            } else {
                reportError("syntax error in '%s' command", fc);
            }
        } catch (GameException e) {
            reportError("invalid first command");
        }
    }

    /** Start the game. */
    public void start() {
        _out.println("Starting game");
        _playing = true;
    }

    /** Checks if STR is an integer. Returns boolean. */
    public static boolean isInteger(String str) {
        return str.matches("^-?\\d+$");
    }

    /** Print a prompt and wait for input. Returns true iff there is another
     *  token. */
    private boolean promptForNext() {
        aiMove();
        if (_playing) {
            if (isOdd(_board.getMove())) {
                _out.print("red> ");
            } else {
                _out.print("blue> ");
            }
        } else {
            _out.print("> ");
        }
        _out.flush();
        if (_inp.hasNextLine()) {
            return true;
        }
        return false;
    }

    /** Send an error message to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf. */
    void reportError(String format, Object... args) {
        _err.print("Error: ");
        _err.printf(format, args);
        _err.println();
    }

    /** returns boolean of playing for JUnit testing purposes. */
    boolean getPlaying() {
        return _playing;
    }

    /** Writer on which to print prompts for input. */
    private final PrintWriter _prompter;
    /** Scanner from current game input.  Initialized to return
     *  newlines as tokens. */
    private final Scanner _inp;
    /** Outlet for responses to the user. */
    private final PrintWriter _out;
    /** Outlet for error responses to the user. */
    private final PrintWriter _err;

    /** The board on which I record all moves. */
    private final Board _board;
    /** A readonly view of _board. */
    private final Board _readonlyBoard;

    /** A pseudo-random number generator used by players as needed. */
    private final Random _random = new Random();

    /** True iff a game is currently in progress. */
    private boolean _playing;

    /** Size of the board. */
    private int size;
    /** Red Player for Game. */
    private Player playerRed;
    /** Blue Player for Game. */
    private Player playerBlue;

    /** Used to return a move entered from the console.  Allocated
     *  here to avoid allocations. */
    private final int[] _move = new int[2];
}
