
/*
 * Error class
 * - container for error types
 * - used by Game Control
 * 
 */
public class Error { //error class
	int errortype; //holds type of error - if any
	int[][] errorarray; //holds location of stack errors
	int teamerror; //holds which team has too many pieces - if either

	void seterrortype(int type) { //sets type of error
		errortype = type; //0 = no error, 1 = stack error, 2 = too many pieces, 3 = both
	}
	
	void seterrorarray(int[][] array) { //sets array of stack error locations
		errorarray = array;
	}
	
	void setteamerror(int team) { //sets which team has too many pieces
		teamerror = team; //0 = no error, 1 = player 1, 2 = player 2
	}
	
	int geterrortype() { //returns error type
		return errortype;
	}
	int[][] geterrorarray() { //returns array of stack error locations
		return errorarray;
	}
	int getteamerror() { //returns which team has too many pieces on board
		return teamerror;
	}
	
}