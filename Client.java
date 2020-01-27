import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
 File: Client.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: Test client for Solver.java
 */

public class Client {

    // Location of puzzle text file
    private static final File TEXT_LOCATION = new File("src/Board.txt");

    // Main method
    public static void main(String[] args) throws IOException {
        System.out.println("Kevin, Alex, and Elven's Client.java");
        System.out.println("- Test client for solving the N-puzzle problem");
        System.out.println("- Note: step 0 is initial board state");

        // Set up board and solver
        Board currentBoard = loadBoard();
        Solver solve = new Solver(currentBoard);

        // For displaying number of steps
        int stepTracker = 0;

        // Check if puzzle is solvable; if so, solve it and output solution
        if (solve.isSolvable()) {
            for (Board board : solve.solution()) {
                System.out.println("Step " + stepTracker++ + ":");
                System.out.println(board);
            }
            System.out.println("Minimum number of moves: " + solve.moves() + ".");
        }
        // if not, report unsolvable
        else {
            System.out.println("Loaded board is unsolvable.");
        }

    }

    // Loads board
    private static Board loadBoard() throws IOException {
        // Create scanner
        Scanner sc = new Scanner(new FileReader(TEXT_LOCATION));
        // Get size of board
        int size = Integer.parseInt(sc.next());
        // Create board array
        int[][] boardArray = new int[size][size];
        // Track rows and cols
        int rowCounter = 0;
        int colCounter = 0;

        // Keep going until all integers are loaded
        while (sc.hasNext()) {
            // Adds into array
            boardArray[rowCounter][colCounter++] = sc.nextInt();
            // Updates row and col
            if (colCounter % size == 0) {
                colCounter = 0;
                rowCounter++;
            }
        }
        sc.close();
        return new Board(boardArray);
    }
}