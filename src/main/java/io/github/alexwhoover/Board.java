package io.github.alexwhoover;

import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(" ").append(tiles[i][j]).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int goal = n * i + j + 1;
                if (tiles[i][j] != goal && tiles[i][j] != 0) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    manhattan += manhattan(tiles[i][j], i, j, n);
                }
            }
        }

        return manhattan;
    }

    private static int manhattan(int value, int row, int col, int n) {
        int goalRow = (value - 1) / n;
        int goalCol = (value - 1) % n;

        return Math.abs(goalRow - row) + Math.abs(goalCol - col);
    }

    public boolean isGoal() {
        return manhattan() == 0;
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (!(y instanceof Board)) {
            return false;
        }

        Board that = (Board) y;

        if (this.n != that.n) {
            return false;
        }

        return sameTiles(this.tiles, that.tiles);
    }

    private boolean sameTiles(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbours() {
        ArrayList<Board> adjBoards = new ArrayList<>(4);
        int[] blankCoords = getEmptyTileCoords();
        int row = blankCoords[0];
        int col = blankCoords[1];

        // Above
        if (row != 0) {
            int[][] copy = copyTiles(tiles);
            swap(copy, row, col, row - 1, col);
            adjBoards.add(new Board(copy));
        }

        // Below
        if (row != n - 1) {
            int[][] copy = copyTiles(tiles);
            swap(copy, row, col, row + 1, col);
            adjBoards.add(new Board(copy));
        }

        // Left
        if (col != 0) {
            int[][] copy = copyTiles(tiles);
            swap(copy, row, col, row, col - 1);
            adjBoards.add(new Board(copy));
        }

        // Right
        if (col != n - 1) {
            int[][] copy = copyTiles(tiles);
            swap(copy, row, col, row, col + 1);
            adjBoards.add(new Board(copy));
        }

        return adjBoards;
    }

    private void swap(int[][] array, int r1, int c1, int r2, int c2) {
        int temp = array[r1][c1];
        array[r1][c1] = array[r2][c2];
        array[r2][c2] = temp;
    }

    private int[][] copyTiles(int[][] original) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    private int[] getEmptyTileCoords() {
        int[] coords = new int[2];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    coords[0] = i;
                    coords[1] = j;
                    return coords;
                }
            }
        }

        throw new IllegalStateException("Board does not contain an empty tile (0)");
    }

    public Board twin() {
        int[][] copy = copyTiles(tiles);

        // swap first two non-blank tiles in the first row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (copy[i][j] != 0 && copy[i][j+1] != 0) {
                    swap(copy, i, j, i, j+1);
                    return new Board(copy);
                }
            }
        }

        throw new IllegalStateException("No twin could be created");
    }

    public static void main(String[] args) {
        int[][] tiles = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };

        int[][] tiles2 = {
            {2, 1, 3},
            {4, 5, 0},
            {6, 8, 7}
        };

        Board board1 = new Board(tiles);
        Board board2 = new Board(tiles2);

        System.out.println("Testing Print");
        System.out.println("Board 1: ");
        System.out.println(board1);
        System.out.println("Board 2: ");
        System.out.println(board2);

        System.out.println("Testing Equals");
        System.out.println(board1.equals(board2));

        System.out.println("Testing isGoal");
        System.out.println(board1.isGoal());

        System.out.println("Testing Dimension");
        System.out.println(board1.dimension());

        System.out.println("Testing Hamming");
        System.out.println(board1.hamming());

        System.out.println("Testing Manhattan");
        System.out.println(board1.manhattan());

        System.out.println("Testing Neighbours");
        Iterable<Board> adjBoards = board1.neighbours();
        for (Board b : adjBoards) {
            System.out.println(b);
        }

        System.out.println("Testing Twin");
        System.out.println(board1.twin());
    }
}