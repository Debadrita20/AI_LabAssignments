
public class HC_Board {
    private int N;              
    byte[][] tiles = null; 
    int empx=0,empy=0; 

    public HC_Board(int[][] blocks) {
        if (blocks == null)
            throw new IllegalArgumentException();
        N = blocks.length;
        if (N < 2 || N >= 128) 
            throw new IndexOutOfBoundsException("dimensions not supported : " + N);
        this.tiles = new byte[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = (byte) blocks[i][j];
                if(this.tiles[i][j]==0)
                {
                    empx=i;
                    empy=j;
                }
            }
        }
    }

    public HC_Board(byte[][] blocks) {
        this.tiles = copySquareArray(blocks);
        N = tiles.length;
    }

    public int dimension() {
        return N;
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
}
