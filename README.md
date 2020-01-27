## DACS: 8 Puzzle's README.md

*Written by Kevin Gu, Alex Yuk, and Elven Shum. Assignment submitted Jan 26. 2020*

**The problem:** Write a program to solve the 8 puzzle problem using the A* search algorithm.

The goal is to arrange the blocks so that they are in order, using as few moves as possible. Our approach uses the priority function for a search node in a priority queue.

```
 0  1  3        1  0  3        1  2  3        1  2  3        1  2  3
 4  2  5   =>   4  2  5   =>   4  0  5   =>   4  5  0   =>   4  5  6
 7  8  6        7  8  6        7  8  6        7  8  6        7  8  0
```

## Our project files

**Project Specific Files:** `Board.java`, `Solver.java`, `Client.java`, `DataClient.java`, `DataClient2.java`, `View.java`, `Board.txt`

**Relevant Data Structures:** `MinPQ.java`, `LinkedList.java`, `Stack.java`

**Writeup:** `Puzzle_Writeup.pdf`

We followed the API on Canvas to create the project specific files. In addition to these, our extensions include analyzing data for the various distance metrics, calculating efficiency to find NxN solutions, loading the board from a source file, optimizing the priority queue size, caching the manhattan/hamming distances to reduce calculation costs, and implementing a minimalistic GUI.

## Board.java

This class holds a 2D array of a NxN board. The datatype Board is organized using the following API: 

```
public class Board {
    public Board(int[][] tiles)            // create a board from an n-by-n array of tiles,
                                           // where tiles[row][col] = tile at (row, col)
    public String toString()               // string representation of this board
    public int tileAt(int row, int col)    // tile at (row, col) or 0 if blank
    public int size()                      // board size n
    public int hamming()                   // number of tiles out of place
    public int manhattan()                 // sum of Manhattan distances between tiles and goal
    public boolean isGoal()                // is this board the goal board?
    public boolean equals(Object y)        // does this board equal y?
    public Iterable<Board> neighbors()     // all neighboring boards
    public boolean isSolvable()            // is this board solvable?

```

**Important notes:**

For our `Iterable<Board> neighbors()`, we used a direction as a instance variable in the class to track the current direction. Here, direction 1 is up, 2 is right, 3 is down, 4 is left. Our `hasNext()` uses a for-loop to go from the current direction, iterating through the directions in order (1-2-3-4), and checks whether a swap is possible. If it exists, `next()` then returns the neighbor board and updates the direction to the next one in order.

Also, we cache our hamming/manhattan distances in the constructor so that we don't need to recalculate it again in `Solver.java`, thus saving calculation costs and time.

We have the following private utility functions:

```
int hammingUtility()										// finds hamming distance to cache in instance variable to be used in hamming()
int manhattanUtility()										// finds manhattan distance to cache in instance variable to be used in manhattan()
Board copyBoard(int[][] board)								// returns copy of board
Location findNonEmptySpace(Location current)				// finds the next non-empty space, used in twin() for swapping
Board moveUtility(Board board, int direction)				// moves board in specified direction, returns null if invalid
Board boardSwap(Board b, int y1, int x1, int y2, int x2)	// swaps the elements in the positions in the board
Location findEmptySpace(Board b)							// finds location of empty space
boolean inBounds(int x, int y)								// checks whether location is in bounds
Location solvedLocation(int n)								// returns correct location for given number in solved position
```

Lastly, we have a `Location` class to make the code more readable, with x and y values to store the relevant array indices.

## Solver.java

This class is used to solve/reach the final state of the board, depending on whether it is possible. It implements this by using two boards: one of the inputted board and one "twin" board. It runs until a solution is found in either twin or normal. `Solver.java` implements a priority queue to find the most optimal path to the solution.

The datatype Solver is organized with the following API:

```
public class Solver {
    public Solver(Board initial)             // find a solution to the initial board (using the A* algorithm)
    public int moves()                       // min number of moves to solve initial board
    public Iterable<Board> solution()        // sequence of boards in a shortest solution
    public static void main(String[] args)   // test client (see below) 
}
```

**Important notes:**

We optimize the number of items in the priority queue by by considering the neighbors of a search node: we don't enqueue a neighbor of its board if its board is the same as the board of the previous search node.

For `solution`, we implement a stack to pop off the solution from the path nodes. This is because the end state of the board is solved, and we want an in-order solution. We do this by pushing each node's parent into a stack, and pop it off in the `next()` function.

Next, our `BoardState` is a node that implements comparable to compare the priority for `MinPQ`.

## Client.java

This class is the test client you should run to test the code. It outputs in the console the steps to the solved state of the board, and "Loaded board is unsolvable." for unsolvable boards. It loads the board from `src/Board.txt`.

## DataClient.java

This class contains the client to obtain run data for analysis of the A* algorithm. It randomizes the board for 3x3 and 4x4 dimensions, and records the number of branches needed to solve a NxN puzzle. See writeup for more details on the properties we incorporate for the `isSolvable()` method.

## DataClient2.java

This class contains another client to obtain run data for analysis of the A* algorithm. It creates a board by randomly shuffling the board backwards, and records the number of branches needed to solve a NxN puzzle.

Note that `DataClient.java` and `DataClient2.java` differ in that the former creates a board that we don't know how many steps will take to solve the puzzle, while in the latter one we can clearly indicate the number of shuffles (and thereby test how the steps will affect how many branches are needed).

## View.java

This runnable class creates a GUI using JFrame. It loads the board, and if there is a solution, a window will appear like the one below:

<img src="https://i.imgur.com/mMzaAGV.png" alt="View.java"
	title="View.java" width="312" height="334" />
	
Here, clicking `<` moves the step backwards, `>` forwards, and entering a number in the text field will take you to the corresponding step. The board state will appear on the text pane.

## Board.txt

This text file is used in `Client.java` and `View.java` to load a board state. The file should be formatted in the following way:

```
3
1 2 3
4 5 6
7 8 0
```

Note two properties: the first line contains the board length, and the following lines (with spaces in between) contain the board state.

## Other files
As an extension, we coded `MinPQ.java` as the relevant data structure in this project. `MinPQ` is our minimum priority queue implemented through a heap. The other files, `LinkedList.java` and `Stack.java` were implemented in previous projects.