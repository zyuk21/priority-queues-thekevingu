import java.io.IOException;
import java.util.Random;

/*
 File: DataClient2.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: Client to obtain data for analysis
 */

public class DataClient2 {

    /* parameters */
    private static final int CYCLES = 5;
    private static final int BEGINNING_STEP = 5;
    private static final int END_STEP = 45;
    private static final int INCREMENT = 5;

    public static void main(String[] args) {
        System.out.println("Kevin, Alex, and Elven's DataClient2.java");
        System.out.println("- Generates boards by randomly moving blocks to test algorithm efficiency");
        System.out.println("- Note: step 0 is initial board state");

        int[][] solvedBoard3 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] solvedBoard4 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};

        for (int step = BEGINNING_STEP; step <= END_STEP; step += INCREMENT) {
            double average = 0;
            for (int i = 0; i < CYCLES; i++) {
                // This is used for 3x3
//                int[][] randomizedArr3 = reverseStep(solvedBoard3, solvedBoard3.length, step);
//                Board board3 = new Board(randomizedArr3);
//                Solver solve3 = new Solver(board3);
//                average += solve3.counter;

                // This is used by 4x4
                int[][] randomizedArr4 = reverseStep(solvedBoard4, solvedBoard4.length, step);
                Board board4 = new Board(randomizedArr4);
                Solver solve4 = new Solver(board4);
                average += solve4.counter;
            }
            System.out.println("Solve steps: " + step + "\tAverage: " + average / CYCLES + " queues");
        }
    }

    // Created a random 2D array by moving the blank/zero numMoves times
    private static int[][] reverseStep(int[][] arr2D, int dimension, int numMoves) {
        int randDirection;

        // x and y of 0
        int x = 2;
        int y = 2;

        for (int i = 0; i < numMoves; i++) {
            do {
                // Chooses 1 - 4 randomly
                randDirection = (int) (Math.random() * 4 + 1);
                // Up
                if (randDirection == 1) {
                    if (inBounds(x, y - 1, dimension)) {
                        int temp = arr2D[x][y];
                        arr2D[x][y] = arr2D[x][y - 1];
                        arr2D[x][y - 1] = temp;
                        break;
                    }
                }
                // Right
                else if (randDirection == 2) {
                    if (inBounds(x + 1, y, dimension)) {
                        int temp = arr2D[x][y];
                        arr2D[x][y] = arr2D[x + 1][y];
                        arr2D[x + 1][y] = temp;
                        break;
                    }
                }
                // Down
                else if (randDirection == 3) {
                    if (inBounds(x, y + 1, dimension)) {
                        int temp = arr2D[x][y];
                        arr2D[x][y] = arr2D[x][y + 1];
                        arr2D[x][y + 1] = temp;
                        break;
                    }
                }
                // Left
                else if (randDirection == 4) {
                    if (inBounds(x - 1, y, dimension)) {
                        int temp = arr2D[x][y];
                        arr2D[x][y] = arr2D[x - 1][y];
                        arr2D[x - 1][y] = temp;
                        break;
                    }
                }
            } while (true);
        }

        return arr2D;
    }

    private static boolean inBounds(int x, int y, int dimension) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }

}