
public class GameData {
	
	private int[][] visibleTeams = new int[2][8]; //array of the current state of each piece	
	private boolean redTake; //is red the active player
	private boolean blueTake; //is blue the active player
	private boolean first; // represents which player goes first 0 is____ 1 is____
	private int currentState;
	private int previousState;
		
	private int play1count; //holds number of active pieces for player1
	private int play2count; //holds number of active pieces for player2
	
	private int play1removed;
	private int play2removed;
	
	private int previousMoves;
	
	private final int allowable = 6; //holds number of pieces each player is allowed to have
	
	//create an intial game data class
	public GameData(){
		for(int i = 0; i < 8; i ++){
			setVisibleTeams(0,i,0);
			setVisibleTeams(1,i,0);

		}
		play1count = 0;
		play2count = 0;
		play1removed = allowable - 2;
		play2removed = allowable - 2;
		previousMoves = 0;
	}
	
	//a getter method for visibleTeams
	public int[][] getVisibleTeams(){
		return visibleTeams;
	}
	
	//a setter method for an individual value of visibleTeams
	public void setVisibleTeams(int x, int y, int value){
		visibleTeams[x][y] = value;
	}
	
	//a setter method for the whole of visible teams
	public void setVisibleTeams(int[][] newTeams){
		this.visibleTeams = newTeams;
	}
	
	//increment the played piece count of player 1
	public void incrementPlay1count(){
		if(play1count < allowable){
			play1count++;
		}
	}
	
	//decrement the played piece count of player 1
	public void decrementPlay1count(){
		if(play1removed > 0){
			//play1count--;
			play1removed --;
		}
	}
	
	//set the played piece count of player 1
	public void setPlay1count(int count){
		play1count = count;
	}
	
	//get the played piece count for player 1
	public int getPlay1count(){
		return play1count;
	}
	
	//get the amount of pieces removed by player 1
	public int getPlay1removed(){
		return play1removed;
	}
	
	//increment the played pieces by player 2 
	public void incrementPlay2count(){
		if(play2count < allowable){
			play2count++;
		}
	}
	
	//decrement the played piece count of player 2
	public void decrementPlay2count(){
		if(play2removed > 0){
			//play2count--;
			play2removed--;
		}
	}
	
	//set the played piece count of player 2
	public void setPlay2count(int count){
		play2count = count;
	}
	
	//get the played piece count of player 2
	public int getPlay2count(){
		return play2count;
	}
	
	//get the number of removed pieces for player 2
	public int getPlay2removed(){
		return play2removed;
	}
	
	//set if it is red player's turn
	public void setRedTake(boolean value){
		this.redTake = value;
	}
	
	//get if it is red player's turn
	public boolean getRedTake(){
		return this.redTake;
	}
	
	//set if it is blue player's turn
	public void setBlueTake(boolean value){
		this.blueTake = value;
	}
	
	//get if it is blue player's turn
	public boolean getBlueTake(){
		return this.blueTake;
	}
	
	//switch the active player
	public void incrementTake(){
		this.redTake = ! this.redTake;
		this.blueTake = ! this.blueTake;
	}
	
	//set which player is going first
	public void setFirst(boolean bool){
		this.first = bool;
	}
	
	//get which player is going first
	public boolean getFirst(){
		return first;
	}
	
	//set the current state of the game
	public void setState(int state){
		this.previousState = this.currentState;
		this.currentState = state;
	}
	
	//get the current state of the game
	public int getCurrentState(){
		return this.currentState;
	}
	
	//get the previous state of the game
	public int getPreviousState(){
		return this.previousState;
	}
	
	//get the previous number of moves
	public int getPreviousMoves(){
		return this.previousMoves;
	}
	// reset the previous number of moves
	public void resetPreviousMoves(){
		this.previousMoves =0;
	}
	//increment previous moves
	public void incrementPreviousMoves(){
		this.previousMoves++;
	}
	

	//method to load a game state from a txt file.
	//Uses FileIO class
	public void loadGame(){
		int[][] loadedGame = FileIO.loadGame();
		for(int i = 0; i < 2; i ++){ //for each disk on the board
			for(int j = 0; j < 8; j++){
				this.setVisibleTeams(i,j,loadedGame[i][j]);
				visibleTeams[i][j] = loadedGame[i][j];
			}
		}
		this.setPlay1count(loadedGame[2][0]);
		this.setPlay2count(loadedGame[2][1]);
		redTake = loadedGame[2][2] != 0;
		blueTake = loadedGame[2][3] != 0;
		currentState = loadedGame[2][4];
	}
	
	
}
