
/**
 * 穷举全部组合
 */
public class Combination {
	private static int n=5;
	private static int m=3;
	private static int[] combs;
	private static int[] datas;
	
	public static void main(String[] args) {
		combs=new int [m];
		datas=new int[]{1,2,3,4,5};
		combine(1,m);
	}

	public static void combine(int now,int step){
		if(now+step>n+1)return;
		if(step==0){
			for(int i=0;i<m;i++) System.out.print(combs[i]+"");
			System.out.println();
			return;
		}
		combs[m-step]=datas[now-1];
		combine(now+1,step-1);
		if(now+step<=n) combine(now+1,step);
	}
}
