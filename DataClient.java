import java.io.IOException;
import java.util.Random;

/*
 File: DataClient.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: Client to obtain data for analysis
 */

public class DataClient {

    /* parameters */
    private static final int CYCLES = 5;

    public static void main(String[] args) {
        System.out.println("Kevin, Alex, and Elven's DataClient.java");
        System.out.println("- Generates randomized n by n boards to test algorithm efficiency");
        System.out.println("- Note: step 0 is initial board state");
        System.out.println("- Note: 4 by 4 takes a VERY long time to run");


        // Tests board dimension 3 and 4
        for (int boardDimension = 3; boardDimension <= 4; boardDimension++) {
            System.out.println("Board Dimension: " + boardDimension);
            // arr is randomly generated N by N array
            int[] arr = generateNArray(boardDimension);
            // Counter
            double average = 0;
            // Run 1000 iterations
            for (int i = 0; i < CYCLES; i++) {
                // Creates solver for new randomised board
                Solver solver = new Solver(new Board(shuffleInto2D(arr, boardDimension)));
                average += solver.counter;
            }
            System.out.println("Average: " + average / CYCLES + " queues");
        }
    }

    // Checks to see if board is solvable
    // This is a mathematical function to detect whether it is solvable
    private static boolean isSolvable(int[] board) {
        int parity = 0;
        int gridDimension = (int) Math.sqrt(board.length);
        // The current row we are on
        int row = 0;
        // The row with the blank tile
        int blankRow = 0;

        for (int i = 0; i < board.length; i++) {
            // Advance to next row
            if (i % gridDimension == 0)
                row++;
            // The blank tile
            if (board[i] == 0) {
                // Save the row on which encountered
                blankRow = row;
                continue;
            }
            for (int j = i + 1; j < board.length; j++)
                if (board[i] > board[j] && board[j] != 0)
                    parity++;
        }

        // Even grid
        if (gridDimension % 2 == 0)
            // Blank on odd row; counting from bottom
            if (blankRow % 2 == 0)
                return parity % 2 == 0;
                // Blank on even row; counting from bottom
            else
                return parity % 2 != 0;
            // Odd grid
        else
            return parity % 2 == 0;
    }

    // Generates N * N sized array
    private static int[] generateNArray(int n) {
        int[] arr = new int[n * n];
        for (int i = 0; i < n * n; i++)
            arr[i] = i;
        return arr;
    }

    // Returns a randomised 2D array created from arr
    private static int[][] shuffleInto2D(int[] arr, int n) {
        int[][] arr2D = new int[n][n];
        Random rand = new Random();

        // Randomise array until solvable
        do {
            for (int i = 0; i < arr.length; i++) {
                int randIndex = rand.nextInt(arr.length);
                // Swap
                int temp = arr[randIndex];
                arr[randIndex] = arr[i];
                arr[i] = temp;
            }
        } while (!isSolvable(arr));

        // Transfer array into 2D array
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                arr2D[r][c] = arr[r * n + c];

        return arr2D;
    }

    private static boolean inBounds(int x, int y, int dimension) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }

}