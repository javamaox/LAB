
public class CopyOfCombination {
	private static int n=5;
	private static int m=3;
	
	public static void main(String[] args) {
		int n=5;
		int m=3;
		int[]list=new int [m];
		int k=1;
		int l=m;
		combine(list,k,l,m,n);
	}

	public static void combine(int[] list, int k,int l,int m,int n){
		if(k+l>n+1)return;
		if(l==0){
			for(int i=0;i<m;i++) System.out.print(list[i]+"");
			System.out.println();
			return;
		}
		list[m-l]=k;
		combine(list,k+1,l-1,m,n);
		if(k+l<=n) combine(list,k+1,l,m,n);
	}
}
