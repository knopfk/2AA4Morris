import java.util.concurrent.ThreadLocalRandom;

public class robut {

	/**
	 * 
	 * @param visibleTeams
	 *            : the current board state
	 * @param colour
	 *            : the colour of the ai
	 * @param notColour
	 *            : the colour of the opponent
	 * @param added
	 *            : the number of pieces added by the ai
	 * @return the location on the board, as an integer array [x,y] where to
	 *         place the next piece
	 */
	public static int[] place(int[][] visibleTeams, int colour, int added) {
		int notColour; 		// determines the other team's colour based on the ai's colour
		if (colour == 1) notColour = 2;
		else notColour = 1;
		int[] target = { 0 };
		if (added == 0)		// if no pieces have been added, adds a piece to a random location
			target = random(visibleTeams,0);
		else {		// if pieces have been added
			int[][] checkNearMill = nearMill(visibleTeams, colour, 0);
			int[][] checkMill = nearMill(visibleTeams, notColour, 0);
			int[] checkAdjacent = randomAdjacent(visibleTeams, colour);
			if (checkNearMill[0] != null)		// if there is a near mill for the AI, the mill will be completed
				target = checkNearMill[0];
			else if (checkMill[0] != null)	// if there is no near mill for the AI but the opponent has a near mill, blocks the
				target = checkMill[0];		// 		opponent's mill
			else if (checkAdjacent != null)
				target = checkAdjacent;
			else						// otherwise a piece is added to a random location
				target = random(visibleTeams,0);

		}
		return target;
	}

	/**
	 * 
	 * @param visibleTeams
	 *            : the current board state
	 * @param colour
	 *            : the colour of the ai
	 * @return the location on the board, as an integer array [x1,y1] where to
	 *         place the next piece and the location on the board where it is
	 *         moving to [x2,y2] : returned as [x1,y1,x2,y2]
	 */
	public static int[] move(int[][] visibleTeams, int colour) {
		int notColour;		// determines the other team's colour based on the ai's colour
		if (colour == 1) notColour = 2;
		else notColour = 1;
		int[] target = { 0 };
		int[][] checkNearMillA = nearMill(visibleTeams, colour, 0);
		int[][] checkNearMillB = nearMill(visibleTeams, notColour, 0);;
		if (checkNearMillA[0] != null)	// if there is a near mill the ai will attempt to move a nearby piece to complete it
			target = nearbyPiece(visibleTeams, checkNearMillA, colour);	// only if there is a nearby piece
		if (checkNearMillB[0] != null && (target.length < 2))	// if there is a near mill for the other team and a target has not
			target = nearbyPiece(visibleTeams, checkNearMillB, colour);		// been found the ai will attempt to stop it
		if (target.length < 2){	// otherwise a random piece is moved to an adjacent location
			boolean check = true;
			while (check == true){ // has a piece been found?
				int[] random = random(visibleTeams, 0);	// a random empty spot is chosen
				int[][] adjacent = checkAdjacent(visibleTeams, colour, random[0], random[1]);	// the adjacent pieces of that
				for (int i = 0 ; i < adjacent.length; i++){													// spot are found
					if (adjacent[i][0] != -1){	// if the adjacent piece is of the correct colour that piece will be moved to the 
						target = new int[] {adjacent[i][0], adjacent[i][1], random[0], random[1]};	// empty spot
						check = false;	// a piece has been found!
					}
				}
			}
		}
		return target;
	}

	/**
	 * 
	 * @param visibleTeams
	 *            : the current board state
	 * @param colour
	 *            : the colour of the ai
	 * @return the location on the board, as an integer array [x,y] where to
	 *         remove a piece from the opponent
	 */
	public static int[] mill(int[][] visibleTeams, int colour) {
		int notColour;	// determines the other team's colour based on the ai's colour
		if (colour == 1) notColour = 2;
		else notColour = 1;
		int[] target = { 0 };
		int[][] checkMill = findMill(visibleTeams, notColour);
		int[][] checkNearMill = nearMill(visibleTeams, notColour, 1);
		if (checkMill[0] != null) // if there is a mill for the opponent, a piece will be removed from it
			target = checkMill[0];
		else if (checkNearMill[0] != null) // if there is a near mill for the opponent, a piece will be removed from it
			target = checkNearMill[0];
		else
			target = random(visibleTeams, notColour); // otherwise a random piece of the opponent's will be removed
		return target;
	}

	// returns a random piece of a certain colour or an empty space
	private static int[] random(int[][] visibleTeams, int search) {
		boolean check = true; // collector variables
		int i = 0;
		int j = 0;
		while (check) { // has a piece been found?
			i = ThreadLocalRandom.current().nextInt(0, 2); // chooses a random level
			j = ThreadLocalRandom.current().nextInt(0, 8); // choses a random spot on level
			if (visibleTeams[i][j] == search)	// if a piece (or empty spot) exists at that location that spot will be returned
				check = false; // a piece has been found
		}
		return new int[] { i, j };
	}

	// Given a team, finds any near mills for that team and returns an array of spots that can finish the mill or remove a piece
	private static int[][] nearMill(int[][] visibleTeams, int colour, int action) {
		// if action == 0, add
		// if action == 1, remove
		int[][] nearMills = new int[8][]; // collector array for near mills
		int check = 0; // adds near mills to the next open array spot
		for (int i = 0; i < 2; i++) {	// iterates through each near mill location on each row to check for near mills
			if (visibleTeams[i][0] == colour && visibleTeams[i][1] == colour && visibleTeams[i][2] == 0)
				nearMills[check++] = new int[] {i, nearMillRandom(2,0,1,action)};
			else if (visibleTeams[i][0] == colour && visibleTeams[i][1] == 0 && visibleTeams[i][2] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(1,0,2,action)};
			else if (visibleTeams[i][0] == 0 && visibleTeams[i][1] == colour && visibleTeams[i][2] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(0,2,1,action)};

			if (visibleTeams[i][4] == colour && visibleTeams[i][5] == colour && visibleTeams[i][6] == 0)
				nearMills[check++] = new int[] {i, nearMillRandom(6,5,4,action)};
			else if (visibleTeams[i][4] == colour && visibleTeams[i][5] == 0 && visibleTeams[i][6] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(5,6,4,action)};
			else if (visibleTeams[i][4] == 0 && visibleTeams[i][5] == colour && visibleTeams[i][6] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(4,6,5,action)};

			if (visibleTeams[i][0] == colour && visibleTeams[i][7] == colour && visibleTeams[i][6] == 0)
				nearMills[check++] = new int[] {i, nearMillRandom(6,0,7,action)};
			else if (visibleTeams[i][0] == colour && visibleTeams[i][7] == 0 && visibleTeams[i][6] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(7,0,6,action)};
			else if (visibleTeams[i][0] == 0 && visibleTeams[i][7] == colour && visibleTeams[i][6] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(0,7,6,action)};

			if (visibleTeams[i][2] == colour && visibleTeams[i][3] == colour && visibleTeams[i][4] == 0)
				nearMills[check++] = new int[] {i, nearMillRandom(4,3,2,action)};
			else if (visibleTeams[i][3] == colour && visibleTeams[i][3] == 0 && visibleTeams[i][4] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(3,4,2,action)};
			else if (visibleTeams[i][2] == 0 && visibleTeams[i][3] == colour && visibleTeams[i][4] == colour)
				nearMills[check++] = new int[] {i, nearMillRandom(2,4,3,action)};
		}
		return nearMills;
	}

	// given a near mill, returns either the empty spot to be filled or a piece to be removed
	private static int nearMillRandom(int empty, int pieceA, int pieceB, int action){
		if (action == 0)	// returns the empty spot in the near mill
			return empty;		// used to complete mills 
		else{		// otherwise randomly picks between the two pieces in the near mill and returns it
			int rand = ThreadLocalRandom.current().nextInt(0, 2);	// used to remove parts of a near mill
			if (rand == 0)
				return pieceA;
			else
				return pieceB;
		}
	}

	// Given a location and a type for adjacent search, returns an array of adjacent spots of that type
	private static int[][] checkAdjacent(int[][] visibleTeams, int adj, int i, int j){
		int otherLevel;		// determines the row that was not given in the location
		if (i == 0) otherLevel = 1;
		else otherLevel = 0;
		if (j == 0){	// 0 requires a special condition as if it searches for visibleTeam[7] by subtracting 1 it will cause an out of bounds error
			int[][] adjacent = new int[2][];	 // collects adjacent pieces of the desired type and saves their location in an array
			if (visibleTeams[i][7] == adj)
				adjacent[0] = new int[] {i,7};
			else adjacent[0] = new int[] {-1};	// if there is no piece of the desired type in an adjacent spot, -1 is saved in the array
			if (visibleTeams[i][1] == adj)
				adjacent[1] = new int[] {i,1};
			else adjacent[1] = new int[] {-1};
			return adjacent;					// returns the array
		}
		else if (j == 2 || j == 4 || j == 6){// 2, 4, and 6 have only two adjacent spots
			int[][] adjacent = new int[2][]; 	 // collects adjacent pieces of the desired type and saves their location in an array
			if (visibleTeams[i][j-1] == adj)
				adjacent[0] = new int[] {i,j-1};
			else adjacent[0] = new int[] {-1};	// if there is no piece of the desired type in an adjacent spot, -1 is saved in the array
			if (visibleTeams[i][j+1] == adj)
				adjacent[1] = new int[] {i,j+1};
			else adjacent[1] = new int[] {-1};
			return adjacent;					// returns the array
		}
		else if (j == 1 || j == 3 || j == 5){	// 1,3, and 5 have three adjacent spots
			int[][] adjacent = new int[3][];	 // collects adjacent pieces of the desired type and saves their location in an array
			if (visibleTeams[i][j-1] == adj)
				adjacent[0] = new int[] {i,j-1};
			else adjacent[0] = new int[] {-1};	// if there is no piece of the desired type in an adjacent spot, -1 is saved in the array
			if (visibleTeams[i][j+1] == adj)
				adjacent[1] = new int[] {i,j+1};
			else adjacent[1] = new int[] {-1};
			if (visibleTeams[otherLevel][j] == 0){	// checks for adjacent pieces on the other level of the board
				adjacent[2] = new int[] {otherLevel,j};}
			else adjacent[2] = new int[] {-1};
			return adjacent;					// returns the array
		}
		else if (j == 7){	// 7 requires a special condition as if it searches for visibleTeam[0] by adding 1 it will cause an out of bounds error
			int[][] adjacent = new int[3][];	 // collects adjacent pieces of the desired type and saves their location in an array
			if (visibleTeams[i][6] == adj)
				adjacent[0] = new int[] {i,6};
			else adjacent[0] = new int[] {-1};	// if there is no piece of the desired type in an adjacent spot, -1 is saved in the array
			if (visibleTeams[i][0] == adj)
				adjacent[1] = new int[] {i,0};
			else adjacent[1] = new int[] {-1};
			if (visibleTeams[otherLevel][7] == adj)	// checks for adjacent pieces on the other level of the board
				adjacent[2] = new int[] {otherLevel,7};
			else adjacent[2] = new int[] {-1};
			return adjacent;					// returns the array
		}
		return null;
	}

	// returns a random open spot adjacent to a piece of a certain colour
	private static int[] randomAdjacent(int[][] visibleTeams, int search){
		boolean check = true;
		int[] target = {0};
		while (check){ // has a spot been found yet?
			int[] temp = random(visibleTeams, search);	//	chooses a random piece of a certain colour
			int[][] adj = checkAdjacent(visibleTeams, 0, temp[0], temp[1]);	// checks for open spots next to the piece
			for (int i = 0; i < adj.length; i++){	// iterates through the array of spots to find on open one
				if (adj[i][0] != -1){
					target = new int[] {adj[i][0], adj[i][1]};
					check = false;	// a spot has been found!
				}
			}
		}
		return target;
	}

	// Checks for mills, then returns a random location of one of the pieces in the mill to be deleted
	private static int[][] findMill(int[][] visibleTeams, int notColour){
		int[][] mills = new int[8][]; // collector array to hold mills
		int check = 0;
		for (int i = 0; i < 2; i++){ // checks through each level and if there are any mills saves them in the array
			if (visibleTeams[i][0] == notColour && visibleTeams[i][1] == notColour && visibleTeams[i][2] == notColour)
				mills[check++] = new int[] {i, findMillRandom(1,2,3)};
			if (visibleTeams[i][4] == notColour && visibleTeams[i][5] == notColour && visibleTeams[i][6] == notColour)
				mills[check++] = new int[] {i, findMillRandom(4,5,6)};
			if (visibleTeams[i][0] == notColour && visibleTeams[i][7] == notColour && visibleTeams[i][6] == notColour)
				mills[check++] = new int[] {i, findMillRandom(0,7,6)};
			if (visibleTeams[i][2] == notColour && visibleTeams[i][3] == notColour && visibleTeams[i][4] == notColour)
				mills[check++] = new int[] {i, findMillRandom(2,3,4)};
		}
		return mills;
	}

	// given a mill, returns a piece to be removed
	private static int findMillRandom(int pieceA, int pieceB, int pieceC){
		int rand = ThreadLocalRandom.current().nextInt(0, 3);
		if (rand == 0)	// picks a random int between 0 and 2 and responds a corresponding piece in the mill to be removed
			return pieceA;
		else if (rand == 1)
			return pieceB;
		else
			return pieceC;
	}

	// given an array of piece locations, array of near mill locations, and a team colour, returns what piece to move and where to
	//		move it to complete or block a mill
	private static int[] nearbyPiece(int[][] visibleTeams, int[][] nearMills, int search){
		int[] target = {0};
		if (nearMills[0] != null){	// if there is a near mill
			// goes through each near mill and checks if there is a piece of the proper colour adjacent to the
			for (int i = 0; i < nearMills.length; i++){		// empty part of the mill
				if (nearMills[i] != null){ 
					int[][] adjacent = checkAdjacent(visibleTeams, search, nearMills[i][0], nearMills[i][1]);
					for (int k = 0; k < adjacent.length; k++){
						if (adjacent[k][0] != -1 && adjacent[k][1] != nearMills[i][2] && adjacent[k][1] != nearMills[i][3]){
							target = new int[] {adjacent[k][0], adjacent[k][1], nearMills[i][0], nearMills[i][1]};
						}
					}
				}
			}
		}
		return target;
	}
}
