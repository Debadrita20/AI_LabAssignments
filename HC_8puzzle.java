import java.util.Scanner;

public class HC_8puzzle {
    private HC_Board cur;
    private HC_Board goal;
    private String[] rules;
    public HC_8puzzle(HC_Board start,HC_Board target)
    {
        cur=start;
        goal=target;
        rules=new String[10000];
    }
    public int hillclimbingfn(HC_Board config) {
        int fn = 0;
        int N=config.dimension();
        int pos[][]=new int[N*N][2];
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
                pos[goal.tiles[i][j]][0]=i;
                pos[goal.tiles[i][j]][1]=j;
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fn += (Math.abs(pos[config.tiles[i][j]][0] - i) + Math.abs(pos[config.tiles[i][j]][1] - j));
            }
        }
        fn=-fn;
        return fn;
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
    private byte[][] swap(byte[][] array,int fromRow,int fromCol,int toRow,int toCol) {
        byte[][] copy = copySquareArray(array);
        byte temp = copy[toRow][toCol];
        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = temp;
        return copy;
    }
    public boolean solve()
    {
        int x=hillclimbingfn(cur);
        int maxfn=hillclimbingfn(goal);
        int t=0;
        if(x==maxfn) return true;
        //HC_Board succ=cur;
        for(int i=0;i<10;i++)
        {
        int row = 0,col = 0;
        boolean flag=false;
        byte[][] tiles=new byte[cur.dimension()][cur.dimension()];
        for(int j=0;j<cur.dimension();j++)
        {
            for(int k=0;k<cur.dimension();k++)
            tiles[j][k]=cur.tiles[j][k];

        }
        //finding empty space
        for (row = 0; row < cur.dimension(); row++) {
            for (col = 0; col < cur.dimension(); col++) {
                if (cur.tiles[row][col] == 0) { flag=true; break; }
            }
            if(flag) break;
        }
        HC_Board hb;
        int vall=1,valr=1,valt=1,valb=1;
        int minm=Integer.MIN_VALUE;
        char move='L';
        if (col > 0) { hb=new HC_Board(swap(tiles, row, col, row, col - 1));  //left
            vall=hillclimbingfn(hb);
            if(vall==maxfn) { rules[t++]="Move LEFT"; return true;}    
            if(vall>minm) { 
                minm=vall; move='L';
            }
        }
        if (col < cur.dimension() - 1) {
            hb=new HC_Board(swap(tiles, row, col, row, col + 1));  //right
            valr=hillclimbingfn(hb);
            if(valr==maxfn) { rules[t++]="Move RIGHT"; return true;}    
            if(valr>minm) { 
                minm=valr; move='R';
            }
        }
        if (row > 0) { hb=new HC_Board(swap(tiles, row, col, row - 1, col)); //top
            valt=hillclimbingfn(hb);
            if(valt==maxfn) { rules[t++]="Move UP"; return true;}    
            if(valt>minm) { 
                minm=valt; move='U';
            } 
        }
        if (row < cur.dimension() - 1) {hb=new HC_Board(swap(tiles, row, col, row + 1, col)); //bottom
            valb=hillclimbingfn(hb);
            if(valb==maxfn) { rules[t++]="Move DOWN"; return true;}    
            if(valb>minm) { 
                minm=valb; move='D';
            }
        } 
        switch(move)
        {
            case 'L': //System.out.println("Move LEFT "+vall);
                      rules[t]="Move LEFT"; 
                      cur=new HC_Board(swap(tiles, row, col, row, col - 1));
                      break;
            case 'R': //System.out.println("Move RIGHT "+valr);
                      rules[t]="Move RIGHT"; 
                      cur=new HC_Board(swap(tiles, row, col, row, col + 1));
                      break;
            case 'U': //System.out.println("Move UP "+valt);
                      rules[t]="Move UP"; 
                      cur=new HC_Board(swap(tiles, row, col, row - 1, col));
                      break;
            case 'D': //System.out.println("Move DOWN "+valb);
                      rules[t]="Move DOWN"; 
                      cur=new HC_Board(swap(tiles, row, col, row + 1, col));
                      break;
        }
        //System.out.println("L"+vall+"R"+valr+"T"+valt+"B"+valb);      
        t++;
    }
    return false;
    }
    public void getResult()
    {
        boolean b=solve();
        if(b)
        {
            System.out.println("Solution Found");
            if(rules[0]==null)
            System.out.println("The starting board was the target");
            else
            {
                System.out.println("Rules:");
                for(int i=0;i<10000;i++)
                {
                    if(rules[i]==null) break;
                    System.out.println((i+1)+". "+rules[i]);
                }
            }
        }
        else{
        System.out.println("Solution Not Found in given number of moves\nFirst 10 moves:");
        for(int i=0;i<10;i++)
                {
                    //if(rules[i]==null) break;
                    System.out.println((i+1)+". "+rules[i]);
                }
        }
    }
    public static void main(String args[])throws Exception
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the dimension (N if Puzzle is NXN)");
        int N = sc.nextInt();
        if (N <= 0) throw new IndexOutOfBoundsException("Board dimension must be greater than 0!!");
        int[][] blocks = new int[N][N];
        System.out.println("Enter the initial board with 0 for empty space");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                blocks[i][j] = sc.nextInt();                      
        }
        HC_Board st=new HC_Board(blocks);
        System.out.println("Enter the target board with 0 for empty space");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                blocks[i][j] = sc.nextInt();                      
        }
        HC_Board ta=new HC_Board(blocks);
        HC_8puzzle puzzle=new HC_8puzzle(st, ta);
        puzzle.getResult();
    }
}
