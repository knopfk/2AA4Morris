import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.io.PrintWriter;
import java.util.Random;

/**
 * GameDisplay
 * Master class for the various parts of the display
 * organizes the panels
 * will allow the panels to interact in the future
 **/
public class GameDisplay extends JFrame implements ActionListener {

	//private static boolean isButtonCheck; //boolean to check a button
	private static boolean isButtonNewGame; // boolean to see if there is a new game
	private static boolean isButtonTake; // boolean to see if a button is selected
	private static boolean isButtonRecieve; //boolean to see if a button is received

	//private Shape[][] shapeArray = new Shape[2][8]; //array of the game pieces
	//private int[][] visibleTeams = new int[2][8]; //array of the current state of each piece
	
	//private Random rnd = new Random(); //random number generator
	
	private boolean redTake; //is red the active player
	private boolean blueTake; //is blue the active player
	
	private PiecePanel piecePanel;
	private static GameBoard gamePanel;
	
	private int play1count = 0; //holds number of active pieces for player1
	private int play2count = 0; //holds number of active pieces for player2
	//private int allowable = 6; //holds number of pieces each player is allowed to have
	
	private int levels = 2; //holds number of levels on board 
	private int places = 8; //holds number of places in each level 
	private int[][] current; //array which will hold number of pieces in each position
	
	private int currentState; //an integer representing the current state of the game
	private int previousState; //an integer representing the previous state of the game
	private int [] prevDisk = new int[2]; //a 2D array representing the currently selected disk
	
	
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
		JButton buttonSaveGame = new JButton("Save Game");
		buttonSaveGame.setActionCommand("Save Game");
		JButton buttonLoadGame = new JButton("Load Game");
		buttonLoadGame.setActionCommand("Load Game");

		//makes buttons usable
		buttonCheck.addActionListener(this);
		buttonNewGame.addActionListener(this);
		buttonSaveGame.addActionListener(this);
		buttonLoadGame.addActionListener(this);

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
		gc1.gridx = 1;
		gc1.gridy = 0;
		buttonPanel.add(buttonSaveGame, gc1);
		gc1.gridx = 1;
		gc1.gridy = 1;
		buttonPanel.add(buttonLoadGame, gc1);
		//add mouse listener to panels	
		state0();
		gamePanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) { //if there was a mouse click
				super.mouseClicked(e); //uses the super classes command
				//System.out.println("I'm here");
				// if we are in state 1, the placing of the pieces
				if(currentState == 1){
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && blueTake == true){ //if the piece was clicked and it is blue's turn
							//System.out.println("Picked Blue");
							gamePanel.setVisibleTeams(i,j,2); //change the value in the color array
							current[i][j] ++;
							blueTake = false; //it is no longer blue's turn 
							redTake = true; //it is now red's turn
							play2count ++;
							piecePanel.setBlueCount(Integer.toString(6 - play2count));
							if(checkMill(i,j,2)){ ///check for a mill
								//state3(2);
								currentState = 3;
								previousState = 1;
								state3();
							}else{
							state1();
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && redTake == true){//if the piece was clicked and it is red's turn
							gamePanel.setVisibleTeams(i,j,1); //change the value in the color array
							current[i][j] ++;
							blueTake = true; // it is no longer red's turn
							redTake = false; // it is blue's turn
							play1count ++;
							piecePanel.setRedCount(Integer.toString(6 - play1count));
							if(checkMill(i,j,1)){ //check for a mill
								//state3(2);
								currentState = 3;
								previousState = 1;
								state3();
							}else{
							state1();
							}
						}
					}
				}
				gamePanel.repaint(); //repaint the board
			}
				//if we are in state 2, where disks now move around the board instead of being placed.
			if(currentState ==2){
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && blueTake == true){ //if the piece was clicked and it is blue's turn	
							if(gamePanel.getVisibleTeams(i,j) == 1 && prevDisk[0] == -1){
								piecePanel.setLabel("Moving Phase: Red Piece Selected");
								prevDisk[0] = i; prevDisk[1] = j;
							}
							else if(gamePanel.getVisibleTeams(i,j) == 0 && prevDisk[0] != -1 &&prevDisk[1] != -1){
								if(GameControl.movepiece(prevDisk[0],prevDisk[1],i,j,gamePanel.getVisibleTeams()).getmovestat() == 1){
								gamePanel.setVisibleTeams(i, j, 1);
								gamePanel.setVisibleTeams(prevDisk[0], prevDisk[1], 0);
								prevDisk[0] = -1;
								prevDisk[1] = -1;
								if(checkMill(i,j,1)){ //check for a mill
									//state3(2);
									currentState = 3;
									previousState = 2;
									state3();
								}
								else{
									blueTake = false; // it is no longer blues's turn
									redTake = true; // it is red's turn
									piecePanel.setLabel("Moving Phase: Blue's Turn");
								}
								}
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && redTake == true){//if the piece was clicked and it is red's turn
							if(gamePanel.getVisibleTeams(i,j) == 2 && prevDisk[0] == -1){
								prevDisk[0] = i; prevDisk[1] = j;
								piecePanel.setLabel("Moving Phase: Blue Piece Selected");
							}
							else if(gamePanel.getVisibleTeams(i,j) == 0 && prevDisk[0] != -1){
								if(GameControl.movepiece(prevDisk[0],prevDisk[1],i,j,gamePanel.getVisibleTeams()).getmovestat() == 1){
								gamePanel.setVisibleTeams(i, j, 2);
								gamePanel.setVisibleTeams(prevDisk[0], prevDisk[1], 0);
								prevDisk[0] = -1;
								prevDisk[1] = -1;
								if(checkMill(i,j,2)){ //check for a mill
									currentState = 3;
									previousState = 2;
									state3();
								}
								else{
									piecePanel.setLabel("Moving Phase: Red's Turn");
									blueTake = true; // it is no longer red's turn
									redTake = false; // it is blue's turn
								}
							}
							}
						}
					}
				}
				gamePanel.repaint(); //repaint the board
				state2();
			}
			//if we are in state 3, milling of another players pieces
			if(currentState ==3){
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && blueTake == true){ //if the piece was clicked and it is blue's turn	
							if(gamePanel.getVisibleTeams(i,j) == 2 && prevDisk[0] == -1){
								gamePanel.setVisibleTeams(i,j,0);
								play1count--;
								blueTake = false; // it is no longer blues's turn
								redTake = true; // it is red's turn
								currentState = previousState;
								previousState = 3;
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && redTake == true){//if the piece was clicked and it is red's turn
							if(gamePanel.getVisibleTeams(i,j) == 1 && prevDisk[0] == -1){
								gamePanel.setVisibleTeams(i,j,0);
								play2count--;
								blueTake = true; // it is no longer blues's turn
								redTake = false; // it is red's turn
								currentState = previousState;
								previousState = 3;
							}
						}
					}
				}
				gamePanel.repaint(); //repaint the board
			}
				
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

	//state 0: initial set up
	private void state0(){
		//set initial piece amounts
		this.play1count =0;
		this.play2count = 0;
		//randomly determine first player
		Random random = new Random(); //initialize random
		if(random.nextBoolean() == true){
			redTake = true;
			blueTake = false;
			piecePanel.setLabel("Game Start: Red Goes First");
			
		}
		else{
			blueTake = true;
			redTake = false;
			piecePanel.setLabel("Game Start: Blue Goes First");
		}
		piecePanel.setRedCount(Integer.toString(6 - play1count));
		piecePanel.setBlueCount(Integer.toString(6 - play2count));
		// set board to blank initially
		this.clearBoard();
		
		//move to next state
		this.currentState = 1;
		prevDisk[0] = -1;
		prevDisk[1] = -1;
		//System.out.println("I got to state 1");
		
	}
	//state 1: placing pieces
	private void state1(){
		if(redTake){
			piecePanel.setLabel("Red Turn");
		}
		if(blueTake){
			piecePanel.setLabel("Blue Turn");
		}
		//if we have finished placing pieces
		if(play1count == 6 && play2count == 6){
			currentState  = 2;
			prevDisk[0] = -1;
			prevDisk[1] = -1;
			blueTake = !blueTake;
			redTake = !redTake;
			piecePanel.setLabel("All Pieces Placed, Moving Phase");
		}
	}
	//state 2: moving pieces
	private void state2(){
		//if player piece count drops below necessary, or another condition is met, move to state 3
		if(play1count < 3){
			piecePanel.setLabel("Red Wins");
			currentState = 4;
		}
		else if(play2count < 3){
			piecePanel.setLabel("Blue Wins");
			currentState = 4;
		}
	}
	//state 3: Milling
	private void state3(){
		piecePanel.setLabel("Player Has a Mill");

	}
	//state 4: State that we have a winner/ Game over
	//private void state4(){
		
	//}
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
		//	isButtonCheck = true;
			//System.out.println("Is it correct?");
			//isButtonTake = false;
		//	Error temp = this.checkboard();
			//How to output Error?
		//	if(temp != null){
		///		System.out.println(temp.geterrortype());
		//	}
			
		}
		// checks if new game was pressed
		else if ("New Game".equals(e.getActionCommand())){
			this.clearBoard();
			state0();
		}
		else if ("Save Game".equals(e.getActionCommand())){
			this.saveGame();
			piecePanel.setLabel("Game Saved");
		}
		else if ("Load Game".equals(e.getActionCommand())){
			this.loadGame();
			piecePanel.setLabel("Game Loaded");
			this.repaint();
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
	/*
	private void setPlayer(int player){
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
	}*/
	
	//method to clear board
	private void clearBoard(){
		isButtonNewGame = true;
		//System.out.println("New Game!");
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
	//method to see if you have a mill
	private boolean checkMill(int i,int j,int colour){ //make return a boolean, if true move to milling state
		if(j == 0 || j == 1 || j == 2){
			if(gamePanel.getVisibleTeams(i,0) == colour && gamePanel.getVisibleTeams(i,1) == colour && gamePanel.getVisibleTeams(i,2) == colour){
				return true;
			}
		}
		if(j == 4 || j == 5 || j == 6){
			if(gamePanel.getVisibleTeams(i,4) == colour && gamePanel.getVisibleTeams(i,5) == colour && gamePanel.getVisibleTeams(i,6) == colour){
				return true;
			}
		}
		if(j == 0 || j == 7 || j == 6){
			if(gamePanel.getVisibleTeams(i,0) == colour && gamePanel.getVisibleTeams(i,7) == colour && gamePanel.getVisibleTeams(i,6) == colour){
				return true;
			}
		}
		if(j == 2 || j == 3 || j == 4){
			if(gamePanel.getVisibleTeams(i,2) == colour && gamePanel.getVisibleTeams(i,3) == colour && gamePanel.getVisibleTeams(i,4) == colour){
				return true;
			}
		}
		return false;
	}

	
//should be a seperate IO class probably
	
	//method to save the game state to a text file
	private void saveGame(){
		PrintWriter writer;
		try {
			writer = new PrintWriter("savedGame.txt", "UTF-8");
			for(int i = 0; i < 2; i ++){ //for each disk on the board
				for(int j = 0; j < 8; j++){
					writer.print(gamePanel.getVisibleTeams(i,j));
				}
			}
			writer.println("");
			writer.println(redTake);
			writer.println(currentState);
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//method to load a game state from a txt file.
	private void loadGame(){
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File("savedGame.txt")));
		char[] inputArray = br.readLine().toCharArray();
		play1count = 0; //holds number of active pieces for player1
		play2count = 0; //holds number of active pieces for player2
		for(int i = 0; i < 2; i ++){ //for each disk on the board
			for(int j = 0; j < 8; j++){
				gamePanel.setVisibleTeams(i, j, Character.getNumericValue(inputArray[j + i*8]));
				if(Character.getNumericValue(inputArray[j + i*8]) == 1){
					play1count ++;
				}
				if(Character.getNumericValue(inputArray[j + i*8]) == 2){
					play2count ++;
				}
			}
		}
		redTake = Boolean.parseBoolean(br.readLine());
		blueTake = !redTake;
		currentState = Integer.parseInt(br.readLine());
		br.close();
		}
		catch(Exception e){
			
		}
	}
	
}
