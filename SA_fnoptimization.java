import java.util.Scanner;

public class SA_fnoptimization {
    int fn(int a[])
    {
        int s=0;
        for(int i=0;i<a.length;i++)
        {
            if(i!=0)
            s+=(i+1)*a[i];
            else s-=a[i];
        }
        return s;
    }
    void sa(int[][] bounds,int n,int maxiter,double temp)
    {
        int[] initial=new int[n];
        int[] cur=new int[n];
        int[] cand=new int[n];
        for(int i=0;i<n;i++)
        {
            initial[i]=(int)(bounds[i][0]+bounds[i][1])/2;
            cur[i]=(int)(bounds[i][0]+bounds[i][1])/2;
        }
        int val=fn(initial),curval=val;
        for(int i=1;i<=maxiter;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(Math.random()<0.5)
                cand[j]=cur[j]+(int)((Math.random()*(bounds[j][1]-cur[j])));
                else
                cand[j]=cur[j]-(int)((Math.random()*(cur[j]-bounds[j][0])));
            }
            int candval=fn(cand);
            if(candval>val)
            {
                //System.out.println("hello1");
                val=candval;
                for(int j=0;j<n;j++)
                initial[j]=cand[j];
            }
            int d=candval-curval;
            double curtemp=temp*1.0/(i+1);
            double acc=Math.exp(-d/curtemp);
            if(d>0||(acc<(2*acc*Math.random())))
            {
                //System.out.println("hello2");
                curval=candval;
                for(int j=0;j<n;j++)
                cur[j]=cand[j];
            }
        }
        System.out.println("Optimized value in "+maxiter+" iterations is: "+val);
        for(int i=0;i<n;i++)
        {
            System.out.print(initial[i]+" ");
        }
    }
    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the number of variables in the function");
        int n=sc.nextInt();
        System.out.println("Enter lower and upper bounds (space separated) for each");
        int bo[][]=new int[n][2];
        for(int i=0;i<n;i++)
        {
            System.out.print((i+1)+". ");
            bo[i][0]=sc.nextInt();
            bo[i][1]=sc.nextInt();
        }
        SA_fnoptimization obj=new SA_fnoptimization();
        obj.sa(bo, n, 1000, 10);
    }
}
