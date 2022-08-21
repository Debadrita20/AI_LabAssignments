

import java.util.Scanner;
import java.util.Stack;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Solver {

    private static final Comparator<Node> ord = new Order();
    // priority queue to save all the possible options
    private PriorityQueue<Node> pq = new PriorityQueue<>(ord);
    // priority queue for the twin board
    private PriorityQueue<Node> pq2 = new PriorityQueue<>(ord);
    // Current search node
    private Node cur;
    // Current search node of twin board
    private Node cur2;
    private boolean solvable;

    private class Node implements Comparable<Node> {
        private Board board;
        private Node parent; //link to parent
        private int move;
        private int priority;

        public Node(Board b, Node p, int m,int a) {
            board = b;
            parent = p;
            move = m;
            priority = (a==1)?(b.manhattan() + m):(a==2)?b.manhattan():m;
        }

        public int compareTo(Node other) {
            if (this.priority < other.priority) return -1;
            if (this.priority > other.priority) return 1;
            return 0;
        }
    }

    private static class Order implements Comparator<Node> {
        public int compare(Node v, Node w) {
            if (v.priority < w.priority) return -1;
            else if (v.priority > w.priority) return 1;
            else return 0;
        }
    }

    public Solver(Board initial,int algo) {
        cur = new Node(initial, null, 0,algo);
        cur2 = new Node(initial.twin(), null, 0,algo);
        pq.add(cur);
        pq2.add(cur2);
        int n=0;
        while (!cur.board.isGoal() /*&& !cur2.board.isGoal()*/) {
            cur = pq.poll();
            cur2 = pq2.poll();
            for (Board i : cur.board.neighbors()) {
                if (cur.parent == null || !i.equals(cur.parent.board)) 
                    pq.add(new Node(i, cur, cur.move + 1,algo));
            }
            /*for (Board i : cur2.board.neighbors()) {
                if (cur2.parent == null || !i.equals(cur2.parent.board)) 
                    pq2.add(new Node(i, cur2, cur2.move + 1,algo));
            }*/
            n++;
            if(n==200000) return;
        }
        System.out.println("No. of iterations taken to arrive at the result: "+n);
        if (cur.board.isGoal())
            solvable = true;
        else
            solvable = false;
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
        int cont=1;
        System.out.println("Enter the dimension (N if Puzzle is NXN)");
        int N = sc.nextInt();
        if (N <= 0) throw new IndexOutOfBoundsException("Board dimension must be greater than 0!!");
        int[][] blocks = new int[N][N];
        System.out.println("Enter the initial board with 0 for empty space");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                blocks[i][j] = sc.nextInt();                      
        }

        int a=1;
        while(cont!=0) {
        // solve the puzzle
        while(true) {
        System.out.println("1. A* Search");
        System.out.println("2. Best-First Greedy Search");
        System.out.println("3. Uniform Cost Search");
        System.out.println("Enter your choice(1-3)");
        a=sc.nextInt();
        if(a>=1&&a<=3) break;
        System.out.println("Invalid Choice!!!Try Again...");
        }
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial,a);

        if (!solver.isSolvable()) 
            System.out.println("No solution found in 200000 iterations");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            Stack<Board> soln=solver.solution();
            while(!soln.empty())
            {
                System.out.println(soln.pop());
            }
        }
        System.out.println("Want to stop? Press 0 else press any other number");
        cont=sc.nextInt();
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

