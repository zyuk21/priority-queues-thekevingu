import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;

/*
 File name: Solver.java
 Names: Kevin Gu, Alex Yuk, Elven Shum
 Date: Jan 27, 2020
 Description: Solver class with to solve problem
 */

public class Solver {

    // The final state of the board, aka the solution that keeps track of all of its previous moves
    private BoardState endState;

    // Used for analysis
    public int counter;

    // Constructor
    public Solver(Board startingBoard) {
        // Priority Queue of the original board
        MinPQ<BoardState> states = new MinPQ<BoardState>();
        states.insert(new BoardState(startingBoard));

        // Priority Queue of the twinned board
        MinPQ<BoardState> twinStates = new MinPQ<BoardState>();
        twinStates.insert(new BoardState(startingBoard.twin()));

        // Runs until found solution in either twin or normal
        do {
            // Used fo analysis
            counter++;
            // Runs with states
            endState = testAll(states);
            // Runs with twinStates
        } while (endState == null && testAll(twinStates) == null);
    }

    // Searches through all paths to solve board, returns null if no solution
    private BoardState testAll(MinPQ<BoardState> states) {
        // Return null is priority queue is empty
        if (states.isEmpty())
            return null;

        // The best states in original priority queue
        BoardState best = states.delMin();

        // Return best state if its board matches goal
        if (best.board.isGoal())
            return best;

        // Iterates through all paths possible for each state of the board
        for (Board board : best.board.neighbors())
            if (best.parent == null || !board.equals(best.parent.board))
                states.insert(new BoardState(board, best));

        return null;
    }

    // Returns true if endState is not null
    public boolean isSolvable() {
        return endState != null;
    }

    // Returns number of moves that takes to solve
    public int moves() {
        if (isSolvable())
            return endState.numberOfMovesTaken;
        return -1;
    }

    // For graphics
    public LinkedList<Board> solutionList() {
        BoardState tempState = endState;
        Stack path = new Stack<Board>();
        while (tempState != null) {
            path.push(tempState.board);
            tempState = tempState.parent;
        }

        LinkedList<Board> pathList = new LinkedList<>();
        while (!path.isEmpty())
            pathList.add((Board) path.pop());
        return pathList;
    }

    // Returns a iterable stack with the correct path to solution
    public Iterable<Board> solution() {
        class boardIterator implements Iterator<Board> {

            /* track path */
            private Stack path;

            /* constructor, pushing all moves into stack for popping later  */
            public boardIterator() {
                BoardState tempState = endState;
                path = new Stack<Board>();
                while (tempState != null) {
                    path.push(tempState.board);
                    tempState = tempState.parent;
                }
            }

            /* returns whether stack has more moves */
            @Override
            public boolean hasNext() {
                return !path.isEmpty();
            }

            /* returns next step */
            @Override
            public Board next() {
                return (Board) path.pop();
            }
        }

        /* iterable */
        class boardIterable implements Iterable<Board> {
            public Iterator<Board> iterator() {
                return new boardIterator();
            }
        }
        return new boardIterable();
    }

    /* Node for board state, implements comparable for comparing priority */
    private class BoardState implements Comparable<BoardState> {
        // The previous board state
        private BoardState parent;
        // Current board
        private Board board;
        // The number of moves taken to get to this board
        private int numberOfMovesTaken = 0;

        // Constructor
        public BoardState(Board board) {
            this.board = board;
        }

        // Constructor
        public BoardState(Board board, BoardState parent) {
            this.board = board;
            this.parent = parent;
            this.numberOfMovesTaken = parent.numberOfMovesTaken + 1;
        }

        // This is how the priority of each state is compared and calculated
        public int compareTo(BoardState state) {
            return this.numberOfMovesTaken - state.numberOfMovesTaken + this.board.manhattan() - state.board.manhattan() + this.board.hamming() - board.hamming();
        }
    }
}
