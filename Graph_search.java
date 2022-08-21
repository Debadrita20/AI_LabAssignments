import java.io.IOException;
import java.util.*;
public class Graph_search
{
    int n=0, start=0;
    int[][] a;
    int[] goal;
    int k=0;
    boolean isWeighted;
    public Graph_search(int v)
    {
        start=0; //by default, 0 is the start node
        n=v;
        a=new int[n][n];
        for(int i=0;i<n;i++)  //by default, no vertex has an edge, so cost of travel is infinite
        {
            for(int j=0;j<n;j++)
            {
                a[i][j]=Integer.MAX_VALUE;
            }
        }
        goal=new int[n]; //number of goal nodes can atmost be the total number of nodes
        k=0;
        isWeighted=false;
    }    
    void setStartNode(int s)
    {
        start=s;
    }
    void acceptGraph()
    {
        Scanner sc=new Scanner(System.in);
        char c='Y';
        System.out.println("Enter the pairs of vertices having edges\nE.g. if there is an edge from vertex 2 to 3 type 2 3");
        while(c=='Y'||c=='y')
        {
            int v1=sc.nextInt();
            int v2=sc.nextInt();
            a[v1][v2]=1;  
            System.out.println("Want to add more edges? Press Y otherwise press any other key");
            c=sc.next().charAt(0);
        }
        System.out.println("Adjacency matrix");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            System.out.print(a[i][j]+" ");
            System.out.println();
        }
    }
    void acceptWGraph()
    {
        isWeighted=true;
        Scanner sc=new Scanner(System.in);
        char c='Y';
        System.out.println("Enter the pairs of vertices having edges along with the cost"); 
        System.out.println("E.g. if there is an edge from vertex 2 to 3 having cost 6 type 2 3 6");
        while(c=='Y'||c=='y')
        {
            int v1=sc.nextInt();
            int v2=sc.nextInt();
            int cost=sc.nextInt();
            a[v1][v2]=cost;  
            System.out.println("Want to add more edges? Press Y otherwise press any other key");
            c=sc.next().charAt(0);
        }
        /*System.out.println("Adjacency matrix");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(a[i][j]==Integer.MAX_VALUE) System.out.print("inf ");
                else System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }*/
    }
    void acceptGoalNodes()
    {
        Scanner sc=new Scanner(System.in);
        char c='Y';
        k=0;
        System.out.println("Enter a goal node");
        while(c=='Y'||c=='y')
        {
            if(k==n) 
            { System.out.println("Number of goal nodes exceeds the total number of nodes in the graph...please recheck"); return;}
            goal[k++]=sc.nextInt();
            if(goal[k-1]>=n) { System.out.println("Given node not in graph..please recheck"); k--;}
            System.out.println("Want to add more goal node(s)? Press Y otherwise press any other key");
            c=sc.next().charAt(0);
        }
    }
    void changeCost(int u,int v,int c)
    {
        a[u][v]=c;
    }
    boolean isGoal(int v)
    {
        for(int i=0;i<k;i++)
        {
            if(goal[i]==v) return true;
        }
        return false;
    }
    String getPath(ArrayList<Integer> or,int st_index,int dest,String path)
    {
        path=path+" "+or.get(st_index);
        if(st_index==or.size()-1) return path;
        for(int i=st_index+1;i<or.size();i++)
        {
            if(a[or.get(st_index)][or.get(i)]<Integer.MAX_VALUE)
            {
                String s=getPath(or,i,dest,path);
                if(s.endsWith(Integer.toString(dest))) return s;
            }
        }
        return path;
    }
    void bfs()
    {
        Queue<Integer> q=new LinkedList<Integer>();
        ArrayList<Integer> order=new ArrayList<>();
        boolean[] visited=new boolean[n];
        boolean found=false;
        q.add(start);
        visited[start]=true;
        while(!q.isEmpty())
        {
            int v=q.remove();
            order.add(v);
            if(isGoal(v)) { found=true; break; }
            for(int i=0;i<n;i++)
            {
                if(a[v][i]<Integer.MAX_VALUE&&!visited[i])
                {
                    visited[i]=true;
                    q.add(i);
                }
            }
        }
        System.out.println("Using BFS:");
        if(found)
        {
            System.out.println("Goal Node "+order.get(order.size()-1)+" found");
            System.out.println("Order:");
            for(int vv:order)
            System.out.print(vv+" ");
            System.out.println();
            System.out.println("Path:");
            System.out.println(getPath(order,0,order.get(order.size()-1),"").trim());
        }
        else
        System.out.println("No goal node found");
    }
    private boolean dfs_util(int v,boolean[] visited,ArrayList<Integer> order,ArrayList<Integer> path)
    {
        visited[v]=true;
        order.add(v); path.add(v);
        if(isGoal(v)) return true;
        for(int i=0;i<n;i++)
        {
            if(a[v][i]<Integer.MAX_VALUE&&!visited[i])
            if(dfs_util(i,visited,order,path)) return true;
        }
        path.remove(path.lastIndexOf(v));
        return false;
    }
    void dfs()
    {
        ArrayList<Integer> order=new ArrayList<>();
        ArrayList<Integer> path=new ArrayList<>();
        boolean[] visited=new boolean[n];
        boolean b=dfs_util(start,visited,order,path);
        System.out.println("Using DFS:");
        if(b)
        {
            System.out.println("Goal Node "+order.get(order.size()-1)+" found");
            System.out.println("Order:");
            for(int vv:order)
            System.out.print(vv+" ");
            System.out.println();
            System.out.println("Path:");
            for(int vv:path)
            System.out.print(vv+" ");
            System.out.println();
        }
        else
        System.out.println("No goal node found");
    }
    private boolean dls_util(int v,boolean[] visited,ArrayList<Integer> order,int limit,ArrayList<Integer> path)
    {
        visited[v]=true;
        order.add(v); path.add(v);
        if(isGoal(v)) return true;
        if(limit<=0) { path.remove(path.lastIndexOf(v)); return false; }
        for(int i=0;i<n;i++)
        {
            if(a[v][i]<Integer.MAX_VALUE&&!visited[i])
            if(dls_util(i,visited,order,limit-1,path)) return true;
        
        }
        path.remove(path.lastIndexOf(v));
        return false;
    }
    void dls(int limit)
    {
        ArrayList<Integer> order=new ArrayList<>();
        ArrayList<Integer> path=new ArrayList<>();
        boolean[] visited=new boolean[n];
        dls_util(start,visited,order,limit,path);
        System.out.println("Using DLS:");
        if(isGoal(order.get(order.size()-1)))
        {
            System.out.println("Goal Node "+order.get(order.size()-1)+" found");
            System.out.println("Order:");
            for(int vv:order)
            System.out.print(vv+" ");
            System.out.println();
            System.out.println("Path:");
            for(int vv:path)
            System.out.print(vv+" ");
            System.out.println();
        }
        else
        System.out.println("No goal node found");
    }
    void ids()
    {
        ArrayList<Integer> order=new ArrayList<>();
        ArrayList<Integer> path=new ArrayList<>();
        boolean[] visited=new boolean[n];
        boolean flag=false;
        int limit;
        for(limit=0;limit<100;limit++)
        {
            path.clear();
            for(int i=0;i<n;i++)
            visited[i]=false;
            dls_util(start,visited,order,limit,path);
            if(isGoal(order.get(order.size()-1)))
            {
                flag=true; break;
            }
        }
        System.out.println("Using IDS:");
        if(flag)
        {
            System.out.println("Goal Node "+order.get(order.size()-1)+" found");
            System.out.println("Order:");
            for(int vv:order)
            System.out.print(vv+" ");
            System.out.println();
            System.out.println("Path:");
            for(int vv:path)
            System.out.print(vv+" ");
            System.out.println();
        }
        else
        System.out.println("No goal node found");
    }
    private boolean ibs_util(int v,boolean[] visited,ArrayList<Integer> order,int limit,ArrayList<Integer> path)
    {
        visited[v]=true; int b=0;
        order.add(v); path.add(v);
        if(isGoal(v)) return true;
        for(int i=0;i<n;i++)
        {
            if(a[v][i]<Integer.MAX_VALUE&&!visited[i])
            {
            if(ibs_util(i,visited,order,limit,path)) return true;
            b++;
            }
            if(b==limit) break;
        }
        path.remove(path.lastIndexOf(v));
        return false;

    }
    void ibs()
    {
        ArrayList<Integer> order=new ArrayList<>();
        ArrayList<Integer> path=new ArrayList<>();
        boolean[] visited=new boolean[n];
        boolean flag=false;
        int limit;
        for(limit=1;limit<100;limit++)
        {
            path.clear();
            for(int i=0;i<n;i++)
            visited[i]=false;
            ibs_util(start,visited,order,limit,path);
            if(isGoal(order.get(order.size()-1)))
            {
                flag=true; break;
            }
        }
        System.out.println("Using IBS:");
        if(flag)
        {
            System.out.println("Goal Node "+order.get(order.size()-1)+" found");
            System.out.println("Order:");
            for(int vv:order)
            System.out.print(vv+" ");
            System.out.println();
            System.out.println("Path:");
            for(int vv:path)
            System.out.print(vv+" ");
            System.out.println();
        }
        else
        System.out.println("No goal node found");
    }   
    
    public static void main(String args[])throws IOException
    {
        Scanner scm=new Scanner(System.in);
        int n,ch=0,st;
        char c='Y';
        boolean isWeighted;
        System.out.println("Enter the number of vertices in the graph");
        n=scm.nextInt();
        System.out.println("The given graph has vertices numbered from 0 to "+(n-1));
        System.out.println("Enter the start vertex in the graph");
        st=scm.nextInt();
        System.out.println("Enter whether you want to enter a weighted or unweighted graph");
        System.out.println("Press 1 for weighted and any other number for unweighted graph");
        int x=scm.nextInt();
        isWeighted=(x==1);
        //System.out.println(isWeighted);
        Graph_search obj=new Graph_search(n);
        obj.setStartNode(st);
        if(isWeighted) obj.acceptWGraph();
        else obj.acceptGraph();
        obj.acceptGoalNodes();
        while(c=='Y'||c=='y')
        {
            System.out.println("Enter the search technique you wish to use");
            System.out.println("Uninformed search techniques");
            System.out.println("1. Breadth-First Search");
            System.out.println("2. Depth-First Search");
            System.out.println("3. Depth-Limited Search");
            System.out.println("4. Iterative Deepening Search");
            System.out.println("5. Iterative Broadening Search");
            System.out.println("Enter the choice(1-5)");
            ch=scm.nextInt();
            switch(ch)
            {
                case 1: obj.bfs();
                        break;
                case 2: obj.dfs();
                        break;
                case 3: System.out.println("Enter the depth limit");
                        int l=scm.nextInt();
                        obj.dls(l);
                        break;
                case 4: obj.ids();
                        break;
                case 5: obj.ibs();
                        break;
                default:System.out.println("Invalid choice...try again!!!");
            }
            System.out.println("Want to continue? Press Y otherwise press any other key");
            c=scm.next().charAt(0);
            if(c!='Y'&&c!='y') break;
            System.out.println("If you wish to continue with the same graph, then press Y, else press any other key");
            char c1=scm.next().charAt(0);
            if(c1=='Y'||c1=='y')
            {
                System.out.println("If you wish to continue with the same goal nodes, press Y, else press any other key");
                char c2=scm.next().charAt(0);
                if(c2=='Y'||c2=='y') ;
                else obj.acceptGoalNodes();
                System.out.println("If you wish to continue with the same start node, press Y, else press any other key");
                char c3=scm.next().charAt(0);
                if(c3=='Y'||c3=='y') continue;
                System.out.println("Enter the start vertex in the graph");
                st=scm.nextInt();
                obj.setStartNode(st);
            }
            else 
            {
                System.out.println("Enter the number of vertices in the graph");
                n=scm.nextInt();
                System.out.println("Enter the start vertex in the graph");
                st=scm.nextInt();
                System.out.println("The given graph has vertices numbered from 0 to "+(n-1));
                obj=new Graph_search(n);
                obj.setStartNode(st);
                System.out.println("Enter whether you want to enter a weighted or unweighted graph");
                System.out.println("Press 1 for weighted and any other number for unweighted graph");
                int xx=scm.nextInt();
                isWeighted=(xx==1);
                if(isWeighted) obj.acceptWGraph();
                else obj.acceptGraph();
                obj.acceptGoalNodes();
            }
        }      
        
        
    }
}
