import javax.swing.SwingUtilities;
import java.util.Random;


//class should inheriant from gamedisplay, to control its methods
// or game display should use its methods ? create a new super class that uses both?
public class GameControl {
	
	
	int levels = 2; //holds number of levels on board 
	int places = 8; //holds number of places in each level 
	int[][] current; //array which will hold number of pieces in each position
	String[][] teams; //array which will hold stack of players in each position
	int[][] visibleteams; //array which holds top piece in each position 
	int play1count = 0; //holds number of active pieces for player1
	int play2count = 0; //holds number of active pieces for player2
	int allowable = 6; //holds number of pieces each player is allowed to have
	boolean turn; //holds value determining which players turn it is
	private boolean first; //holds which player goes first
	
	/* Game Control
	 * - Contains main function and data for the game
	 * - should interact / direct the other modules
	 * - contains the logic for the game aswell
	 * 
	 */
	public boolean startboard() {	// startup board structure, determine which player goes first
		current = new int[levels][places]; // initialize current array
		teams = new String[levels][places]; //initialize teams array
		visibleteams = new int[levels][places]; //initialize visibleteams array
		for (int i = 0; i < levels; i++) { //for each position....
			for (int j = 0; j < places; j++) {
				current[i][j] = 0;// initially, set 0 - each place empty
				teams[i][j] = ""; //initially set null - no team
			}
		}
		Random random = new Random(); //initialize random
		first = random.nextBoolean(); //generate random boolean
		return first; //return first player
		}
		
	public void newpiece(int l, int p, boolean teamnew) {//new piece moved to board - l and p are new position, t - team
		current[l][p] = current[l][p] + 1; //add count to current board
		
		String s = teams[l][p]; //track what colour pieces are on board, stack;
		if (teamnew == true) {
			teams[l][p] = s.concat("a"); //player 1 is an "a"
			play1count = play1count + 1; //keep track of how many pieces player1 has
		}
		else {
			teams[l][p] = s.concat("b"); //player 2 is a "b"
			play2count = play2count + 1; //keep track of how many pieces player2 has
		}
		
	}

	public Error checkboard() { //check if board is legal
		Error error = new Error(); //set up instance of error class
		int[][] errorarray = new int[levels][places]; //initialize array to hold error problems
		int errorcase = 0; //initialize int variable to hold what kind of error occuring
		int whichteam = 0;
		
		for (int i = 0; i < levels; i++) {//test each place on board
			for (int j = 0; j < places; j++) {
				if (current[i][j] <= 1) {
					continue; //if count less than or equal to 1, checkout remains true
				}
				else {
					errorarray[i][j] = 1;//set 1, represents error in that location
					errorcase = 1;//set 1, represents stack error
				}
			}
		}
		
		if ((play1count > allowable) || (play2count > allowable)) { //determine if either player has too many pieces active
			if (play1count > allowable) {
				whichteam = 1; //player 1 has too many pieces
			}
			else {
				whichteam = 2; //player 2 has 2 many pieces
			}
			errorcase = errorcase + 2; //one of the players has too many pieces
		}
	
		//if errorcase is not 0, there is error present
		error.seterrortype(errorcase); // errorcase = 0 - no error, errorcase = 1 - stacked, errorcase = 2 - too many pieces on board, 3 = both kinds of error
		error.seterrorarray(errorarray); //1 represents error location on board
		error.setteamerror(whichteam); //0 - neither player, 1 - player 1, 2 - player 2
		return error; //return error instance
		}
	
	
	public int[][] visibleteam() { //returns int array of top piece of stack for each place on board
		for (int i = 0; i < levels; i++) {
			for (int j = 0; j < places; j++) {
				String square = teams[i][j]; // for each position...
				
				if (square.length() == 0) { //if nul, 0 represents no piece
					visibleteams[i][j] = 0;
				}
				else { //if not nul....
					String topteam = square.substring(square.length()-1);						
					if (topteam.equals("a")) { //if top piece "a", 1 represents player 1
						visibleteams[i][j] = 1;
					}
					else if (topteam.equals("b")) { //if top piece "b", 2 represents player 2 
						visibleteams[i][j] = 2;
					}
				}
			}	
		}
		return visibleteams; //return list of top pieces
	}
		
	public static void main(String args[]){
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					// Creates Frame for Game to Run in //
					GameDisplay gameDisplay = new GameDisplay();
					//Set first player
					Random random = new Random(); //initialize random
					gameDisplay.setPlayer(random.nextInt()%2);
					//gameDisplay.
				}
			});
		}

}
