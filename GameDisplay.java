import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * GameDisplay
 * Master class for the various parts of the display
 * organizes the panels
 * will allow the panels to interact in the future
 **/
public class GameDisplay extends JFrame implements ActionListener {

	private static boolean isButtonCheck; //boolean to check a button
	private static boolean isButtonNewGame; // boolean to see if there is a new game
	public static boolean isButtonTake; // boolean to see if a button is selected
	public static boolean isButtonRecieve; //boolean to see if a button is recieved

	//private Shape[][] shapeArray = new Shape[2][8]; //array of the game pieces
	private int[][] visibleTeams = new int[2][8]; //array of the current state of each piece
	
	Random rnd = new Random(); //random number generator
	
	private boolean redTake; //is red the active player
	private boolean blueTake; //is blue the active player
	
	private PiecePanel piecePanel;
	private static GameBoard gamePanel;
	
	int play1count = 0; //holds number of active pieces for player1
	int play2count = 0; //holds number of active pieces for player2
	int allowable = 6; //holds number of pieces each player is allowed to have
	
	int levels = 2; //holds number of levels on board 
	int places = 8; //holds number of places in each level 
	int[][] current; //array which will hold number of pieces in each position
	
	/**
	 * GameDisplay - the main class for controlling the other display classes
	 * @param title - Title of the game
	 */
	public GameDisplay(String title){
		super(title);
		// Creates Panels for separate purposes
		JPanel buttonPanel = new JPanel();
		piecePanel = new PiecePanel();
		gamePanel = new GameBoard();
		
		current = new int[levels][places];

		// Add swing components to content pane
		Container c = getContentPane();
		//add button panel
		c.add(buttonPanel, BorderLayout.SOUTH);
		Dimension size1 = buttonPanel.getPreferredSize();
		size1.height = 75;
		size1.width = 500;
		buttonPanel.setPreferredSize(size1);
		buttonPanel.setBackground(Color.WHITE);
		
		//add piecePanel
		c.add(piecePanel, BorderLayout.CENTER);
		Dimension size2 = piecePanel.getPreferredSize();
		size2.height = 100;
		size2.width = 500;
		piecePanel.setPreferredSize(size2);
		piecePanel.setBackground(Color.WHITE);
		
		//Add gameboard panel
		c.add(gamePanel, BorderLayout.NORTH);
		Dimension size3 = gamePanel.getPreferredSize();
		size3.height = 500;
		size3.width = 500;
		gamePanel.setPreferredSize(size3);
		gamePanel.setBorder(new LineBorder(Color.BLACK));
		gamePanel.setBackground(Color.WHITE);

		//// Implements Panel with New Game and Check buttons////
		
		//Creating Buttons//
		JButton buttonCheck = new JButton("Check!");
		buttonCheck.setActionCommand("Check");
		JButton buttonNewGame = new JButton("New Game?");
		buttonNewGame.setActionCommand("New Game");

		//makes buttons usable
		buttonCheck.addActionListener(this);
		buttonNewGame.addActionListener(this);

		//Displays Buttons and information about buttons
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.weightx = 0.5;
		gc1.weighty = 0.5;
		gc1.gridx = 0;
		gc1.gridy = 0;
		buttonPanel.add(buttonCheck, gc1);
		gc1.gridx = 0;
		gc1.gridy = 1;
		buttonPanel.add(buttonNewGame, gc1);
		//add mouse listener to panels
		redTake = true; //for testing
		blueTake = false; //for testing
		gamePanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) { //if there was a mouse click
				super.mouseClicked(e); //uses the super classes command
				System.out.println("I'm here");
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && blueTake == true){ //if the piece was clicked and it is blue's turn
							System.out.println("Picked Blue");
							gamePanel.setVisibleTeams(i,j,2); //change the value in the color array
							current[i][j] ++;
							//blueTake = false; //it is no longer blue's turn 
							//redTake = true; //it is now red's turn
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && redTake == true){//if the piece was clicked and it is red's turn
							System.out.println("Picked Red");
							gamePanel.setVisibleTeams(i,j,1); //change the value in the color array
							current[i][j] ++;
							//blueTake = true; // it is no longer red's turn
							//redTake = false; // it is blue's turn
						}
					}
				}
				gamePanel.repaint(); //repaint the board
			}	
		});
		
		piecePanel.addMouseListener(new MouseAdapter(){ //add a mouse listener
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			
			//if red is selected, make red active
			if (piecePanel.getRedCircle().contains(e.getPoint())){ 
				blueTake = false;
				System.out.println("add red?");
				redTake = true;
			}
			//if blue is selected, make blue active
			else if (piecePanel.getBlueCircle().contains(e.getPoint())){
				redTake = false;
				System.out.println("add blue?");
				blueTake = true;
			}
			// if test space is selected, color it
		}
	});
	}

	/**
	 * Default constructor for GameDisplay
	 */
	public GameDisplay(){
		JFrame frame = new GameDisplay("Six Mens Morris");
		
		frame.setLocation(0,0);
		frame.setSize(520, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.WHITE);
	}

	@Override
	/**
	 * actionPerformed
	 * takes actions if a button was presed
	 **/
	public void actionPerformed(ActionEvent e) {
		// check if check button was pressed
		if ("Check".equals(e.getActionCommand())){
			isButtonCheck = true;
			System.out.println("Is it correct?");
			isButtonTake = false;
			Error temp = this.checkboard();
			//How to output Error?
			if(temp != null){
				System.out.println(temp.geterrortype());
			}
			
		}
		// checks if new game was pressed
		else if ("New Game".equals(e.getActionCommand())){
			this.clearBoard();
		}
		// adds a piece to a space
		else if ("take".equals(e.getActionCommand())){
			isButtonTake = true;
			System.out.println("Add a piece");
		}
		//otherwise a piece was added
		else{
			if (isButtonTake == true){
				System.out.println("Piece added");
				isButtonTake = false;
			}
			isButtonRecieve = true;
		}
	}
	
	public void setPlayer(int player){
		if(player == 0){
			System.out.println("Red is current player");
			redTake = true;
			blueTake = false;
		}
		else if(player == 1){
			System.out.println("Blue is current player");
			redTake = false;
			blueTake = true;
		}
	}
	//need to move error checker
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
	
	//method to clear board
	public void clearBoard(){
		isButtonNewGame = true;
		System.out.println("New Game!");
		isButtonTake = false;
		//empty the board
		for(int i = 0; i < 2; i ++){ //for each disk on the board
			for(int j = 0; j < 8; j++){
				gamePanel.setVisibleTeams(i,j,0);
				current[i][j] = 0;
			}
		}
		gamePanel.repaint();
	}
}
