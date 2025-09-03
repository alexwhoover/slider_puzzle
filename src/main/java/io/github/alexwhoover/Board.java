package io.github.alexwhoover;

public class Board {
    private final int[][] tiles;
    private int n;

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

    public static int manhattan(int value, int row, int col, int n) {
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

    public static void main(String[] args) {
        int[][] tiles = {
            {2, 1, 3},
            {4, 5, 0},
            {6, 7, 8}
        };

        int[][] tiles2 = {
            {2, 1, 3},
            {4, 5, 0},
            {6, 8, 7}
        };

        Board board = new Board(tiles);
        Board board2 = new Board(tiles2);
        System.out.println(board);
        System.out.println(board.equals(board2));
    }
}