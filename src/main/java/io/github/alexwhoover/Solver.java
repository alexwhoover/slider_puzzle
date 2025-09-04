package io.github.alexwhoover;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private boolean solvable;
    private SearchNode goalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null");
        }

        Board twin = initial.twin();

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        pq.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(twin, 0, null));

        goalNode = null;

        while (true) {
            goalNode = step(pq);
            if (goalNode != null) {
                solvable = true;
                break;
            }

            SearchNode twinGoal = step(twinPQ);
            if (twinGoal != null) {
                solvable = false;
                break;
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode prev;
        private int priority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    private SearchNode step(MinPQ<SearchNode> pq) {
        if (pq.isEmpty()) return null;

        SearchNode node = pq.delMin();
        if (node.board.isGoal()) return node;

        for (Board neighbour : node.board.neighbours()) {
            // Do not add previous board state
            if (node.prev == null || !neighbour.equals(node.prev.board)) {
                pq.insert(new SearchNode(neighbour, node.moves + 1, node));
            }
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        return goalNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable || goalNode == null) return null;

        List<Board> solutionList = new ArrayList<>(goalNode.moves + 1);

        for (SearchNode currentNode = goalNode; currentNode != null; currentNode = currentNode.prev) {
            solutionList.add(currentNode.board);
        }

        Collections.reverse(solutionList);

        return solutionList;
    }
    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = {
                {1, 2, 3},
                {5, 4, 6},
                {8, 7, 0}
        };

        Board board = new Board(tiles);

        Solver solvedBoard = new Solver(board);
        Iterable<Board> solutionList = solvedBoard.solution();

        // Test Solvable
        System.out.println(solvedBoard.isSolvable());

        // Testing Solution List
        for (Board b : solutionList) {
            System.out.println(b);
        }
    }

}