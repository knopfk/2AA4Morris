package controller;

import java.util.Random;

public class Board { //holds game logic
	
	int levels = 2; //holds number of levels on board 
	int places = 8; //holds number of places in each level 
	
	int[][] visibleteams; //array which holds top piece in each position 
	int play1count = 0; //holds number of active pieces for player1
	int play2count = 0; //holds number of active pieces for player2
	int allowable = 6; //holds number of pieces each player is allowed to have
	boolean turn; //holds value determining which players turn it is
	boolean first; //holds which player goes first
	
	
		public boolean startboard() {	// startup board structure, determine which player goes first
			visibleteams = new int[levels][places]; //initialize visibleteams array
			for (int i = 0; i < levels; i++) { //for each position....
				for (int j = 0; j < places; j++) {
					visibleteams[i][j] = 0;// initially, set 0 - each place empty
					
				}
			}
			Random random = new Random(); //initialize random
			first = random.nextBoolean(); //generate random boolean
			return first; //return first player
			}
			
		
		
		
		public Boardreturn newpiece(int l, int p, boolean teamnew) {//new piece moved to board - l and p are new position, t - team
			
			Boardreturn npreturn = new Boardreturn();
		    int newcheck;
			if (visibleteams[l][p] >= 1) { //check if peice already in position
				newcheck = 0; //if yes, move illegal, newcheck - 0
			}
			else {
				newcheck = 1; //else, move is legal
			}
			
			npreturn.setmovestat(newcheck);
			
			
			if (newcheck == 0) { //only add piece to board if no pieces already in position 
				
			}
			
			else {
				if (teamnew == true) {
					visibleteams[l][p] = 1; //player 1 is a 1
					play1count = play1count + 1; //keep track of how many pieces player1 has
				}
				else {
					visibleteams[l][p] = 2; //player 2 is a 2
					play2count = play2count + 1; //keep track of how many pieces player2 has
				}
			}
			
			npreturn.setokayboard(visibleteams);
			
		return npreturn;	
		}
		
		
		
		public int[][] getvisibleteam() {
			return visibleteams;
		}
		
		
		public Boardreturn checkmove(int ol,int op) {//checks for the available moves of current piece
			 
			Boardreturn chreturn = new Boardreturn(); //set up boardreturn object
			int mcheck = 0; //holds whether move is legal 
			int[][] okaymove = new int[levels][places]; //array for holding legal moves
			
			for (int t = 0; t < levels; t++) { //for each position, set default to 0..
				for (int p = 0; p < places; p++) {
					okaymove[t][p] = 0;
				}
			
			
			for (int m = 0; m < levels; m++) { //for each place/level....
				for (int o = 0; o < places; o++) {
					if ((((o == (op-1)) || (o == (op + 1))) && (m == ol)) && (visibleteams[m][o] == 0)) { //the places next on the same level are okay as long as they are unoccupied
						okaymove[m][o] = 1; // can move to this spot
						mcheck = 1; //there is an available move
						if (op == 0) { //also if its in place 0, place 7 is okay
							//System.out.println();
							//System.out.println(op);
							okaymove[m][7] = 1; 
							mcheck = 1;
						}
						else if (op == 7) { //if in place 7, place 0 is okay
							okaymove[m][0] = 1;
							mcheck = 1;
						}
						
					}
					else if ((op % 2 != 0) && (o == op) && (o != 0))  { //if the place number is not divisible by 2..
						for (int a = 0; a < levels; a++) { //the same position on the next level is okay
							if (((a != ol) && (m != ol)) && (visibleteams[m][op] == 0)){ //as long as it is unoccupied
								okaymove[m][op] = 1;
								mcheck = 1;
							}
						}
					}
					
				
					}
					
					}
					
				}
		
		chreturn.setokayboard(okaymove); //return array of legal moves
		chreturn.setmovestat(mcheck); //set whether move is legal - 1 yes, 0 no	
		return chreturn; //return boardreturn object
		}



		public void movepiece(int oldl,int oldp,int newl,int newp) { //checks where piece can be moved given current location
			Boardreturn checkob = checkmove(oldl,oldp); //initialize boardreturn object from checkmove function
			int[][] checkboard = checkob.getokayboard(); //get array of okay moves
		
			
			if (checkboard[newl][newp] == 1){ //if move is okay....
				visibleteams[newl][newp] = visibleteams[oldl][oldp]; //add count to new location
				visibleteams[oldl][oldp] =  0; //remove count from previous location
			}
			
		}

		public int okaymovecheck(int team) { //check whether team has any available moves (win condition)
			int okaychecker = 0;
			
			for (int m = 0; m < levels; m++) { //for each place/level....
				for (int o = 0; o < places; o++) {
					if (visibleteams[m][o] == team) { //if the selected team has a piece here
						Boardreturn okays = checkmove(m,o); //check if that space has any moves
						int moves = okays.getmovestat(); //1 - yes, it can move, 0 - no moves
						okaychecker = okaychecker + moves; //update variable holding whether team has any moves
				}
				}
				}
			if (okaychecker > 0){ //there is at least one piece with a legal move..
				okaychecker = 1; 
			}
			
			
			return okaychecker; //1 - team can move, 0 - no moves, team looses
		}
	
	
}
	
	
	
	
	
	
	
	
	
	


