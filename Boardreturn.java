
public class Boardreturn {
		
	private int updatedboard[][];
	private int movestat;
	
	
	public void setokayboard(int[][] boardchange) {
		updatedboard = boardchange;
	}
	
	public void setmovestat(int status) {
		movestat = status;
	}
	
	public int[][] getokayboard(){
		return updatedboard;
	}
	
	public int getmovestat() {
		return movestat;
	}
}