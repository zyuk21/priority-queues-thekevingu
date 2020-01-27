import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
 File: View.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: GUI for Solver.java
 */

public class View extends JPanel implements Runnable {

    // File location
    private static final String TEXT_LOCATION = "src/Board.txt";

    // Graphics
    private static JFrame f;
    private static final int APPLICATION_WIDTH = 200;
    private static final int APPLICATION_HEIGHT = 200;
    private static final String TITLE = "View.java";

    // Board solution step list
    private static LinkedList<Board> solutionList;

    // Current step index
    private static int stepNumber;

    private static int boardSize;

    // Main
    public static void main(String[] args) throws IOException {
        init();
    }

    // Initialize
    private static void init() throws IOException {
        // Instructions
        System.out.println("Kevin, Alex, and Elven's View.java");
        System.out.println("- GUI for Solver.java");
        System.out.println("- Note: step 0 is initial board state");

        // Set up board and solver
        Board board = loadBoard();
        Solver solve = new Solver(board);

        if (solve.isSolvable()) {
            // Get solution list for graphics output
            solutionList = solve.solutionList();
            stepNumber = 0;
            // Swing application
            SwingUtilities.invokeLater(View::start);
        } else {
            System.out.println("Loaded board is not solvable.");
        }
    }

    // Graphics settings
    private View() {
        setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
        setFocusable(true);
        initializeJObjects();
    }

    // JFrame settings
    private static void start() {
        f = new JFrame();
        f.setTitle(TITLE);
        f.setResizable(false);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        f.add(new View(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    // Initializes and creates all JObjects
    private void initializeJObjects() {
        // Displaying board
        JTextPane displayBoard = new JTextPane();
        displayBoard.setBounds(20, 20, 160, 130);
        SimpleAttributeSet attribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribute, StyleConstants.ALIGN_CENTER);
        displayBoard.setParagraphAttributes(attribute, true);
        displayBoard.setText(spacing(solutionList.get(stepNumber).toString()));
        f.getContentPane().add(displayBoard);

        // Text field
        JTextField stepInput = new JTextField();
        stepInput.setBounds(60, 160, 40, 33);
        stepInput.setText(String.valueOf(stepNumber));
        stepInput.addActionListener(e -> {
            int input = Integer.valueOf(stepInput.getText());
            if (inBounds(input))
                displayBoard.setText(spacing(solutionList.get(input).toString()));
            else
                System.out.println("Not within bounds.");
        });
        f.getContentPane().add(stepInput);

        // Label
        JLabel totalSteps = new JLabel("/ " + (solutionList.size() - 1));
        totalSteps.setBounds(103, 160, 40, 33);
        f.getContentPane().add(totalSteps);

        // Left button
        JButton left = new JButton("<");
        left.setBounds(20, 160, 35, 33);
        left.addActionListener(e -> {
            if (inBounds(stepNumber - 1))
                stepNumber--;
            displayBoard.setText(spacing(solutionList.get(stepNumber).toString()));
            stepInput.setText(String.valueOf(stepNumber));
        });
        f.getContentPane().add(left);

        // Right button
        JButton right = new JButton(">");
        right.setBounds(145, 160, 35, 33);
        right.addActionListener(e -> {
            if (inBounds(stepNumber + 1))
                stepNumber++;
            displayBoard.setText(spacing(solutionList.get(stepNumber).toString()));
            stepInput.setText(String.valueOf(stepNumber));
        });
        f.getContentPane().add(right);

    }

    // Spaces text
    private static String spacing(String text) {
        int times = (9 - boardSize) / 2;
        String s = "";
        for (int i = 0; i < times; i++)
            s += "\n";
        s += text;
        return s;
    }

    // Checks whether index is in bounds
    private static boolean inBounds(int index) {
        return index >= 0 && index < solutionList.size();
    }

    // Loads board
    private static Board loadBoard() throws IOException {
        // Create scanner
        Scanner sc = new Scanner(new FileReader(TEXT_LOCATION));
        // Get size of board
        boardSize = Integer.parseInt(sc.next());
        // Create board array
        int[][] boardArray = new int[boardSize][boardSize];
        // Track rows and cols
        int rowCounter = 0;
        int colCounter = 0;

        // Keep going until all integers are loaded
        while (sc.hasNext()) {
            // Adds into array
            boardArray[rowCounter][colCounter++] = sc.nextInt();
            // Updates row and col
            if (colCounter % boardSize == 0) {
                colCounter = 0;
                rowCounter++;
            }
        }
        sc.close();
        return new Board(boardArray);
    }

    @Override
    public void run() {
    }
}