import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
public class Solver_blind {
    // Current search node
    Node cur;
    private boolean solvable;
    private class Node {
        private Board board;
        private Node parent; //link to parent
        private int move;

        public Node(Board b, Node p, int m) {
            board = b;
            parent = p;
            move = m;
        }
    }
    public Solver_blind(Board initial) {
        cur = new Node(initial, null, 0);
    }
    public void solve(int ch)
    {
        try{
            Scanner scm=new Scanner(System.in);
            switch(ch)
            {
                case 1: bfs();
                        break;
                case 2: dfs();
                        break;
                case 3: System.out.println("Enter the depth limit");
                        int l=scm.nextInt();
                        dls(l);
                        break;
                case 4: ids();
                        break;
                case 5: ibs();
                        break;
                default:System.out.println("Invalid choice...try again!!!");
            }
    
        }catch(Exception e) { System.out.println(e.getMessage());}
        if (cur.board.isGoal())
            solvable = true;
        else
            solvable = false;
    }
    void bfs()
    {
        Queue<Node> q=new LinkedList<Node>();
        ArrayList<Board> v=new ArrayList<>();
        int itr=0;
        q.add(cur);
        v.add(cur.board);
        while(!q.isEmpty())
        { 
            itr++;
            Node nb=q.remove();            
            if(nb.board.isGoal()) { 
                solvable=true; cur=nb; 
                System.out.println("No. of comparisons needed to find the solution: "+itr);
                break; 
            }
            for(Board i : nb.board.neighbors())
            {
                if(!v.contains(i))
                {
                    v.add(i);
                    q.add(new Node(i,nb,nb.move+1));
                }
            }
            if(itr==2000000) { solvable=false; break; }
        }
    }
    private boolean dfs_util(Node nb,ArrayList<Board> v,int itr)
    {
        if(itr==2000000) return false;
        v.add(nb.board);
        if(nb.board.isGoal()) { cur=nb; System.out.println("Depth checked: "+itr); return true; }
        for (Board i : nb.board.neighbors())
        {
            if(!v.contains(i))
            {
                Node node=new Node(i,nb,nb.move+1);
                if(dfs_util(node,v,itr+1)) return true;
            }
        }
        return false;
    }
    void dfs()
    {
        ArrayList<Board> v=new ArrayList<>();
        solvable=dfs_util(cur,v,0);
    }
    private boolean dls_util(Node nb,ArrayList<Board> v,int limit)
    {
        if(limit<=0) return false;
        v.add(nb.board);
        if(nb.board.isGoal()) { cur=nb; return true; }
        for (Board i : nb.board.neighbors())
        {
            if(!v.contains(i))
            {
                Node node=new Node(i,nb,nb.move+1);
                if(dls_util(node,v,limit-1)) return true;
            }
        }
        return false;
    }
    void dls(int limit)
    {
        ArrayList<Board> v=new ArrayList<>();
        solvable=dls_util(cur,v,limit);
    }
    void ids()
    {
        ArrayList<Board> v=new ArrayList<>();
        int limit;
        solvable=false;
        for(limit=0;limit<100;limit++)
        {
            v.clear();
            dls_util(cur,v,limit);
            if(cur.board.isGoal())
            {
                solvable=true; break;
            }
        }
    }
    private boolean ibs_util(Node nb,ArrayList<Board> v,int limit)
    {
        v.add(nb.board); int b=0;
        if(nb.board.isGoal()) { cur=nb; return true; }
        for (Board i : nb.board.neighbors())
        {
            if(!v.contains(i))
            {
                Node node=new Node(i,nb,nb.move+1);
                //System.out.println("here");
                if(ibs_util(node,v,limit)) return true;
                b++;
            }
            if(b==limit) break;
        }
        return false;

    }
    void ibs()
    {
        ArrayList<Board> v=new ArrayList<>();
        int limit;
        solvable=false;
        for(limit=1;limit<100;limit++)
        {
            v.clear();
            ibs_util(cur,v,limit);
            if(cur.board.isGoal())
            {
                solvable=true; break;
            }
        }        
    }  

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (solvable) return cur.move;
        return -1;
    }

    public Stack<Board> solution() {
        if (!solvable) return null;
        Stack<Board> q = new Stack<Board>();
        Node i = cur;
        while (i != null) {
            q.push(i.board);
            i = i.parent;
        }
        return q;
    }


    public static void main(String[] args) {
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

        Board initial = new Board(blocks);
        char c='y';
        // solve the puzzle
        while(c=='Y'||c=='y'){
        Solver_blind solver = new Solver_blind(initial);
        System.out.println("Enter the search technique you wish to use");
        System.out.println("Uninformed search techniques");
        System.out.println("1. Breadth-First Search");
        System.out.println("2. Depth-First Search");
        System.out.println("3. Depth-Limited Search");
        System.out.println("4. Iterative Deepening Search");
        System.out.println("5. Iterative Broadening Search");
        System.out.println("Enter the choice(1-5)");
        System.out.println("If you want to stop, press any other number");
        int ch=sc.nextInt();
        if(ch<1||ch>5) break;
        solver.solve(ch);
        if (!solver.isSolvable()) 
            System.out.println("No solution found in 2000000 comparisons");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            Stack<Board> soln=solver.solution();
            while(!soln.empty())
            {
                System.out.println(soln.pop());
            }
        }
        System.out.println("Want to stop? Press 0 else press any other number");
        int cont=sc.nextInt();
        if(cont==0) break;
        System.out.println("Do you want to work with the same initial board? Press (Y/N)");
        char dec=sc.next().charAt(0);
        if(dec=='N'||dec=='n')
        {
            System.out.println("Enter the dimension (N if Puzzle is NXN)");
            N = sc.nextInt();
            if (N <= 0) throw new IndexOutOfBoundsException("Board dimension must be greater than 0!!");
            System.out.println("Enter the initial board with 0 for empty space");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) 
                    blocks[i][j] = sc.nextInt();                      
        }
    }
    }
    }
}

