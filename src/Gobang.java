import java.util.LinkedList;
import java.util.List;


public class Gobang {
	private static int max=0;
	private static int min=1;
	private static double value;
	private static BoardSituation currentBoardSituation;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		value=Integer.MIN_VALUE;
		List<Step> stepList;
		List<BoardSituation> countList;//当前情况来说比较好的几个盘面
		calSeveralGoodPlace(currentBoardSituation,countList);
		pos=countList.getHeadPosition();
		BoardSituation board;
		double ivalue;
		for(int i=0;(ivalue=search(board,min,value,0))>0;);
		value=select(value,board.value,max);
	}

	private static double search(BoardSituation board,int mode,double oldvalue,int depth){
		List mDeepList;
		if(depth oldvalue==true){
			if(mode==max){
				value=select(value,search(successorBoard,min,value,depth+1),max);
			}else{
				value=select(value,search(successorBoard,max,value,depth+1),min);
			}
			return value;
		}else{
			if(goal(board)!=0){//分出胜负了。
				return goal(board);
			}else{
				return evlation(board);
			}
		}
	}
	
	private static double select(double a,double b,int mode){
		if((a>b&&mode==max)||(a<b&&mode==min)){
			return a;
		}else{
			return b;
		}
	}
	
	class Step{
		int m,n;//坐标值
		char side;//下子方
	}
	class BoardSituation{
		LinkedList<Step> StepList;//每步的队列
		char FiveArea[][];
		Step machineStep;//机器所下的那一步
		double value;//当前盘面的分数
	}
}
