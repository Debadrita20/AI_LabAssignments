

import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private int N;              
    private byte[][] tiles = null;  

    public Board(int[][] blocks) {
        if (blocks == null)
            throw new IllegalArgumentException();
        N = blocks.length;
        if (N < 2 || N >= 128) 
            throw new IndexOutOfBoundsException("dimensions not supported : " + N);
        this.tiles = new byte[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (byte) blocks[i][j];
            }
        }
    }
    private Board(byte[][] blocks) {
        this.tiles = copySquareArray(blocks);
        N = tiles.length;
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int c = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != i * N + j + 1)
                    c++;
            }
        }
        return c;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) continue;
                int row = (tiles[i][j] - 1) / N;
                int col = (tiles[i][j] - 1) % N;
                manhattan += (Math.abs(i - row) + Math.abs(j - col));
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        return (hamming() == 0);
    }


    public Board twin() {
        byte[][] copy = copySquareArray(tiles);
        if (N <= 1) return new Board(copy);
        int row = 0,col = 0;
        byte value = 0;
        byte lastValue = tiles[0][0];

        boolean flag=false;

        for (row = 0; row < N; row++) {
            for (col = 0; col < N; col++) {
                value = tiles[row][col];
                if (value != 0 && lastValue != 0 && col > 0) { flag=true; break; }
                lastValue = value;
            }
            if(flag) break;
        }
        copy[row][col] = lastValue;
        copy[row][col - 1] = value;
        return new Board(copy);
    }

    private byte[][] copySquareArray(byte[][] original) {
        int len = original.length;
        byte[][] copy = new byte[len][len];
        for (int row = 0; row < len; row++) {
            assert original[row].length == len;
            for (int col = 0; col < len; col++)
                copy[row][col] = original[row][col];
        }
        return copy;
    }

    public boolean equals(Object y) {

        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    private byte[][] swap(byte[][] array,int fromRow,int fromCol,int toRow,int toCol) {
        byte[][] copy = copySquareArray(array);
        byte temp = copy[toRow][toCol];
        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = temp;
        return copy;
    }

    public Iterable<Board> neighbors() {

        Queue<Board> q = new LinkedList<Board>();
        int row = 0,col = 0;
        boolean flag=false;

        for (row = 0; row < N; row++) {
            for (col = 0; col < N; col++) {
                if (tiles[row][col] == 0) { flag=true; break; }
            }
            if(flag) break;
        }

        if (row > 0) q.add(new Board(swap(tiles, row, col, row - 1, col))); //top       
        if (row < N - 1) q.add(new Board(swap(tiles, row, col, row + 1, col))); //bottom
        if (col > 0) q.add(new Board(swap(tiles, row, col, row, col - 1)));  //left
        if (col < N - 1) q.add(new Board(swap(tiles, row, col, row, col + 1)));  //right
        return q;
    }

    public String toString() {

        String s=N+"X"+N+" Board\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s=s+String.format("%2d ", tiles[i][j]);
            }
            s=s+"\n";
        }
        return s;
    }

    public static void main(String[] args) {
        int a[][] = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board ob = new Board(a);
        System.out.println(ob.toString());
        System.out.println("Hamming=" + ob.hamming());
        System.out.println("Manhattan=" + ob.manhattan());
        System.out.println(ob.twin().toString());
        System.out.println(ob.isGoal());
        System.out.println(ob.equals(ob.twin()));
    }
}

