import java.util.Iterator;

/*
 File: Board.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: Board class with utility functions for solving problem
 */

public class Board {

    // Board Variables
    private int[][] board;
    private int size;

    // Constructor
    public Board(int[][] board) {
        this.board = board;
        this.size = board.length;
    }

    // Returns size of board
    public int dimension() {
        return size;
    }

    // Calculates hamming distance
    public int hamming() {
        // Tracks hamming score and the correct in-order number for comparison/
        int hammingScore = 0;
        int correctNumber = 1;

        // Goes through board
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++, correctNumber++) {
                // Does not count 0
                if (board[i][j] == 0) continue;
                // Increase hamming score if number is out of order
                if (board[i][j] != correctNumber) hammingScore++;
            }
        return hammingScore;
    }

    // Calculates manhattan score
    public int manhattan() {
        // Tracks manhattan score
        int manhattanScore = 0;

        // Goes through board
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                // Does not count 0
                if (board[i][j] == 0) continue;
                // Get the correct location
                Location correctLocation = solvedLocation(board[i][j]);
                // Adds the absolute value/comparison between the correct location and the actual location
                manhattanScore += Math.abs(i - correctLocation.y) + Math.abs(j - correctLocation.x);
            }
        return manhattanScore;
    }

    // Checks whether board is solved
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                // Does not count 0
                if (board[i][j] == 0) continue;
                // If is not correctly on solved location return false
                Location solvedLocation = solvedLocation(board[i][j]);
                if (i != solvedLocation.y || j != solvedLocation.x)
                    return false;
            }
        return true;
    }

    // Returns twin board
    public Board twin() {
        Board twinBoard = copyBoard(board);

        // First and second indices of clear board space
        Location firstSpace = findSpace(new Location(0, 0));
        Location secondSpace = findSpace(firstSpace);

        // Swaps
        elementExchange(twinBoard.board, firstSpace.y, firstSpace.x, secondSpace.y, secondSpace.x);

        return twinBoard;
    }

    // Returns if boards are equal
    public boolean equals(Board b) {
        for (int i = 0; i < b.dimension(); i++)
            for (int j = 0; j < b.dimension(); j++)
                if (b.board[i][j] != board[i][j])
                    return false;
        return true;
    }

    // toString method
    public String toString() {
        String s = "";
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (j < dimension() - 1)
                    s += board[i][j] + " ";
                else
                    s += board[i][j];
            }
            s += "\n";
        }
        return s;
    }

    // Board iterator
    public Iterable<Board> neighbors() {
        class boardIterator implements Iterator<Board> {

            // Location tracker
            private int direction = 1;

            // Returns whether iterator has next
            @Override
            public boolean hasNext() {
                // Goes through directions in order, starting from current direction
                for (int currentDirection = direction; currentDirection <= 4; currentDirection++)
                    // If direction move exists, return true and update direction
                    if (moveUtility(copyBoard(board), currentDirection) != null) {
                        direction = currentDirection;
                        return true;
                    }
                return false;
            }

            // Returns next board neighbor
            @Override
            public Board next() {
                // Returns move in direction and updates direction
                return moveUtility(copyBoard(board), direction++);
            }
        }

        // Iterable
        class boardIterable implements Iterable<Board> {
            public Iterator<Board> iterator() {
                return new boardIterator();
            }
        }
        return new boardIterable();
    }

    // Copies board
    private Board copyBoard(int[][] board) {
        int[][] copy = new int[dimension()][dimension()];
        for (int r = 0; r < dimension(); r++)
            for (int c = 0; c < dimension(); c++)
                copy[r][c] = board[r][c];
        return new Board(copy);
    }

    // Finds free space after specified location
    private Location findSpace(Location current) {
        // Iterates through board
        for (int i = current.y; i < dimension(); i++)
            for (int j = current.x; j < dimension(); j++) {
                // Does not count 0
                if (board[i][j] == 0 || i == current.y || j == current.x) continue;
                // Returns non-blank space
                return new Location(j, i);
            }
        return new Location(0, 0);
    }

    // Moves board in specified direction
    private Board moveUtility(Board b, int direction) {
        // Finds empty space
        Location emptySpace = findEmptySpace(b);
        int x = emptySpace.x;
        int y = emptySpace.y;

        // Moves in the specified direction
        switch (direction) {
            // Up
            case 1:
                if (inBounds(x, y - 1))
                    return boardSwap(b, x, y, x, y - 1);
                return null;
            // Right
            case 2:
                if (inBounds(x + 1, y))
                    return boardSwap(b, x, y, x + 1, y);
                return null;
            // Down
            case 3:
                if (inBounds(x, y + 1))
                    return boardSwap(b, x, y, x, y + 1);
                return null;
            // left
            case 4:
                if (inBounds(x - 1, y))
                    return boardSwap(b, x, y, x - 1, y);
                return null;
            // Invalid direction, return null
            default:
                return null;
        }
    }

    // Swaps two positions for board
    private Board boardSwap(Board b, int y1, int x1, int y2, int x2) {
        int temp = b.board[x1][y1];
        b.board[x1][y1] = b.board[x2][y2];
        b.board[x2][y2] = temp;
        return b;
    }

    // Exchanges two elements at specified indices
    public void elementExchange(int[][] array, int i, int j, int i1, int j1) {
        int temp = array[i][j];
        array[i][j] = array[i1][j1];
        array[i1][j1] = temp;
    }

    // Finds empty space
    private Location findEmptySpace(Board b) {
        for (int i = 0; i < b.dimension(); i++)
            for (int j = 0; j < b.dimension(); j++)
                if (b.board[i][j] == 0)
                    return new Location(j, i);
        return new Location(0, 0);
    }

    // Checks whether space is in bounds
    private boolean inBounds(int x, int y) {
        return x >= 0 && x < dimension() && y >= 0 && y < dimension();
    }

    // Returns correct location for number if solved
    private Location solvedLocation(int number) {
        return new Location((number - 1) % dimension(), (number - 1) / dimension());
    }

    // Location class
    private class Location {
        // x and y locations
        int x;
        int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
