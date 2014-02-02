package jump61;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.FileReader;
import java.io.IOException;

/** The GUI for jump61 game.
 * @author Derrick Mar
 */

public class GameGui {

    /** holds game. */
    private Game game;
    /** entire Window. */
    private JFrame frame;
    /** totalPanel holds all panels. */
    private JPanel totalPanel;
    /** panel for board of game. */
    private JPanel gamePanel;
    /** panel for options of game. */
    private JPanel optionPanel;
    /** help button. */
    private JButton help;
    /** size button that changes the size of the board. */
    private JButton size;
    /** start button. */
    private JButton start;
    /** set button. */
    private JButton set;
    /** move button. */
    private JButton move;
    /** restart button. */
    private JButton restart;
    /** manual button. */
    private JButton manual;
    /** auto button. */
    private JButton auto;
    /** constraints to design totalPanel layout. */
    private GridBagConstraints c;
    /** pixels for width of window. */
    public static final int WINDOW_W = 600;
    /** pixels for height of window. */
    public static final int WINDOW_H = 800;
    /** pixels of size of game board. */
    public static final int GAMEPANEL_PIXEL = 450;
    /** pixels for width of option panel. */
    public static final int OPTION_W = 200;
    /** pixels for height of option panel. */
    public static final int OPTION_H = 500;
    /** pixels for width of help text area. */
    public static final int TA_W = 20;
    /** pixels for height of help text area. */
    public static final int TA_H = 60;


    /** GameGui constructor. Take in the current game CURRGAME. */
    public GameGui(Game currgame) {
        game = currgame;
        frame = new JFrame("Jump-61 by Derrick Mar");
        frame.setSize(WINDOW_H, WINDOW_W);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.
                                       EXIT_ON_CLOSE);
        totalPanel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(totalPanel, BorderLayout.NORTH);
        c = new GridBagConstraints();
        gamePanel = new JPanel();
        gamePanel = setBoard(gamePanel);
        gamePanel.setPreferredSize(new Dimension(GAMEPANEL_PIXEL,
                                                 GAMEPANEL_PIXEL));
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0; c.gridy = 0;
        totalPanel.add(gamePanel, c);
        optionPanel = new JPanel(new GridLayout(8, 1));
        optionPanel.setPreferredSize(new Dimension(OPTION_W, OPTION_H));
        optionPanel = initializeComp(optionPanel);
        c.gridx = 1; c.gridy = 0;
        totalPanel.add(optionPanel, c);
        frame.setVisible(true);
    }

    /** takes in a PANEL and initializes its components and finally
     *  returns the new Jpanel. */
    public JPanel initializeComp(JPanel panel) {
        start = new JButton("start");
        panel.add(start);
        start.addActionListener(new StartAction());
        restart = new JButton("restart");
        panel.add(restart);
        restart.addActionListener(new RestartAction());
        size = new JButton("size");
        panel.add(size);
        size.addActionListener(new SizeAction());
        manual = new JButton("manual");
        panel.add(manual);
        manual.addActionListener(new ManualAction());
        auto = new JButton("auto");
        panel.add(auto);
        auto.addActionListener(new AutoAction());
        set = new JButton("set");
        panel.add(set);
        set.addActionListener(new SetAction());
        move = new JButton("move");
        panel.add(move);
        move.addActionListener(new MoveAction());
        help = new JButton("help");
        panel.add(help);
        help.addActionListener(new HelpAction());
        return panel;
    }

    /** Inner class that controls size button. */
    class HelpAction implements ActionListener {
        /** HelpAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            try {
                JTextArea ta = new JTextArea(TA_W, TA_H);
                ta.read(new FileReader("jump61/Help.txt"), null);
                ta.setEditable(false);
                JOptionPane.showMessageDialog(help, new JScrollPane(ta));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /** Inner class that controls size button. */
    class AutoAction implements ActionListener {
        /** AutoAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            String setmanualInput = JOptionPane.
                showInputDialog(null,
                                "What player would you like to change to a"
                                + "computer player?\n(Type either red for Red  "
                                + "player blue for Blue player).");
            game.executeCommand("auto " + setmanualInput);
        }
    }

    /** Inner class that controls size button. */
    class ManualAction implements ActionListener {
        /** ManualAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            String setmanualInput = JOptionPane.
                showInputDialog(null,
                                "What player would you like to change to "
                                + "manual?\n (Type either red for Red player "
                                + "or blue for Blue player).");
            game.executeCommand("manual " + setmanualInput);
        }
    }

    /** Inner class that controls size button. */
    class RestartAction implements ActionListener {
        /** RestartAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            game.restartGame();
            gamePanel = setBoard(gamePanel);
            gamePanel.revalidate();
        }
    }

    /** Inner class that controls move button. */
    class MoveAction implements ActionListener {
        /** MoveAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            String moveInput = JOptionPane.
                showInputDialog(null,
                                "What would you like the move number to be?\n"
                                + "(Odd number means red will move next and "
                                + "even number means blue will move next).");
            game.executeCommand("move " + moveInput);
        }
    }

    /** Inner class that controls size button. */
    class SetAction implements ActionListener {
        /** SetAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            String setInput = JOptionPane.
                showInputDialog(null,
                                "Set the move of the player by using this "
                                + "format below: \nR C N P where R is row,  "
                                + "C is column, N is number of spots,  "
                                + "and P is player (either ‘b’ "
                                + "or ‘r’ for blue or red).");
            game.executeCommand("set " + setInput);
            gamePanel = setBoard(gamePanel);
            gamePanel.revalidate();
        }
    }

    /** Inner class that controls size button. */
    class StartAction implements ActionListener {
        /** StartAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            game.start();
            game.aiMove();
            gamePanel = setBoard(gamePanel);
            gamePanel.revalidate();
        }
    }

    /** Inner class that controls size button. */
    class SizeAction implements ActionListener {
        /** ButtonAction constructor that takes in R and C for row
         *  and column for Makemove. */

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            String sizeInput = JOptionPane.
                showInputDialog(null,
                                "Input the new size of the board");
            game.executeCommand("size " + sizeInput);
            gamePanel = setBoard(gamePanel);
            gamePanel.revalidate();
        }
    }

    /** set the Board after ever move. Transforms PANEL, by returning
     *  edited JPanel */
    public JPanel setBoard(JPanel panel) {
        panel.removeAll();
        Square[][] currBoard = game.getMutableBoard().getcurrBoard();
        int N = currBoard.length;
        panel.setLayout(new GridLayout(N, N));
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                Square square = currBoard[i][j];
                JButton btn = new JButton(square.getStringRep());
                if (square.getColor() == Color.WHITE) {
                    btn.setBackground(java.awt.Color.white);
                } else if (square.getColor() == Color.RED) {
                    btn.setBackground(java.awt.Color.red);
                } else {
                    btn.setBackground(java.awt.Color.blue);
                }
                panel.add(btn);
                btn.addActionListener(new ButtonAction(i + 1, j + 1));
            }
        }
        return panel;
    }

    /** Inner class that controls actions to button. */
    class ButtonAction implements ActionListener {
        /** ButtonAction constructor that takes in R and CL for row
         *  and column for Makemove. */
        ButtonAction(int r, int cl) {
            row = r;
            col = cl;
        }

        /** action performed with mouseclick event E. */
        public void actionPerformed(ActionEvent e) {
            game.start();
            game.aiMove();
            game.makeMove(row, col);
            GameGui.this.gamePanel = setBoard(GameGui.this.gamePanel);
            GameGui.this.gamePanel.revalidate();
            game.aiMove();
            GameGui.this.gamePanel = setBoard(GameGui.this.gamePanel);
            GameGui.this.gamePanel.revalidate();
        }

        /** Instance variable Row. */
        private int row;
        /** Instance variable Col. */
        private int col;
    }
}
