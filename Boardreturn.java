
public class Boardreturn {
		
	private int updatedboard[][];
	private int movestat;
	
	
	public void setupdatedboard(int[][] boardchange) {
		updatedboard = boardchange;
	}
	
	public void setmovestat(int status) {
		movestat = status;
	}
	
	public int[][] getupdatedboard(){
		return updatedboard;
	}
	
	public int getmovestat() {
		return movestat;
	}
}