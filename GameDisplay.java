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

	private PiecePanel piecePanel;
	private static GameBoard gamePanel;
	private GameData gameData;
	
	private int levels = 2; //holds number of levels on board 
	private int places = 8; //holds number of places in each level 
	private int[][] current; //array which will hold number of pieces in each position
	
	private int [] prevDisk = new int[2]; //a 2D array representing the currently selected disk
	
	private boolean aiOn;
	private int aiPlayer;
	private int[] aiTarget;
	
	/**
	 * GameDisplay - the main class for controlling the other display classes
	 * @param title - Title of the game
	 */
	public GameDisplay(String title){
		super(title);
		// Creates Panels for separate purposes
		gameData = new GameData();
		JPanel buttonPanel = new JPanel();
		piecePanel = new PiecePanel();
		gamePanel = new GameBoard(gameData);
		
		current = new int[levels][places];
		aiOn = false;

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
		JButton buttonNewAIGame = new JButton("New AI Game?");
		buttonNewAIGame.setActionCommand("New AI Game");

		//makes buttons usable
		buttonCheck.addActionListener(this);
		buttonNewGame.addActionListener(this);
		buttonSaveGame.addActionListener(this);
		buttonLoadGame.addActionListener(this);
		buttonNewAIGame.addActionListener(this);

		//Displays Buttons and information about buttons
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.weightx = 0.5;
		gc1.weighty = 0.5;
		gc1.gridx = 1;
		gc1.gridy = 1;
		buttonPanel.add(buttonCheck, gc1);
		gc1.gridx = 2;
		gc1.gridy = 0;
		buttonPanel.add(buttonNewGame, gc1);
		gc1.gridx = 0;
		gc1.gridy = 0;
		buttonPanel.add(buttonSaveGame, gc1);
		gc1.gridx = 0;
		gc1.gridy = 1;
		buttonPanel.add(buttonLoadGame, gc1);
		gc1.gridx = 2;
		gc1.gridy = 1;
		buttonPanel.add(buttonNewAIGame, gc1);
		
		//add mouse listener to panels	
		state0();
		gamePanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) { //if there was a mouse click
				super.mouseClicked(e); //uses the super classes command
				// if we are in state 1, the placing of the pieces
				if(gameData.getCurrentState() == 1){
					if(aiOn == true && aiPlayer == 0 && gameData.getRedTake() == true){
					aiTarget = robut.place(gameData.getVisibleTeams(), 1, gameData.getPlay1count()); //what is returned, what are variables?
					gameData.setVisibleTeams(aiTarget[0],aiTarget[1],1);
					gameData.incrementPlay1count();	
					piecePanel.setRedCount(Integer.toString(6 - gameData.getPlay1count()));
					if(checkMill(aiTarget[0],aiTarget[1],1)){ ///check for a mill
						gameData.setState(3);
						state3();
					}else{
						gameData.incrementTake();
						gameData.setState(1);
						state1();
					}
					}
					else if(aiOn == true && aiPlayer == 1 && gameData.getBlueTake() == true){
						aiTarget = robut.place(gameData.getVisibleTeams(), 2, gameData.getPlay2count()); //what is returned, what are variables?
						gameData.setVisibleTeams(aiTarget[0],aiTarget[1],2);
						gameData.incrementPlay2count();
						piecePanel.setBlueCount(Integer.toString(6 - gameData.getPlay2count()));
						if(checkMill(aiTarget[0],aiTarget[1],2)){ ///check for a mill
							gameData.setState(3);
							state3();
						}else{
							gameData.incrementTake();			
							gameData.setState(1);
							state1();
						}
					}
					else{
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getBlueTake() == true){ //if the piece was clicked and it is blue's turn
							gameData.setVisibleTeams(Board.newpiece(i, j, 2, gameData.getVisibleTeams(), gameData.getPlay1count(), gameData.getPlay2count()).getokayboard());
							current[i][j] ++;
							gameData.incrementTake();
							gameData.incrementPlay2count();
							piecePanel.setBlueCount(Integer.toString(6 - gameData.getPlay2count()));
							if(checkMill(i,j,2)){ ///check for a mill
								gameData.setState(3);
								state3();
							}else{
								gameData.setState(1);
							state1();
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getRedTake() == true){//if the piece was clicked and it is red's turn
							gameData.setVisibleTeams(Board.newpiece(i, j, 1, gameData.getVisibleTeams(), gameData.getPlay1count(), gameData.getPlay2count()).getokayboard());
							current[i][j] ++;
							gameData.incrementTake();
							gameData.incrementPlay1count();
							piecePanel.setRedCount(Integer.toString(6 - gameData.getPlay1count()));
							if(checkMill(i,j,1)){ //check for a mill
								gameData.setState(3);
								state3();
							}else{
							gameData.setState(1);
							state1();
							}
						}
					}
				}
					}
				gamePanel.repaint(); //repaint the board
			}
				//if we are in state 2, where disks now move around the board instead of being placed.
			if(gameData.getCurrentState() ==2){
				if(aiOn == true && aiPlayer == 0 && gameData.getRedTake() == true){
					aiTarget =	robut.move(gameData.getVisibleTeams(), 1); 
				prevDisk[0] = aiTarget[0]; prevDisk[1] = aiTarget[1];
				gameData.setVisibleTeams(aiTarget[2], aiTarget[3], 1);
				gameData.setVisibleTeams(prevDisk[0], prevDisk[1], 0);
				prevDisk[0] = -1;
				prevDisk[1] = -1;
				gameData.incrementPreviousMoves();
				if(checkMill(aiTarget[2],aiTarget[3],1)){ //check for a mill
						gameData.setState(3);
						state3();
					}
					else{
						gameData.incrementTake();
						piecePanel.setLabel("Moving Phase: Blue's Turn");
					}
				}
				else if(aiOn == true && aiPlayer == 1 && gameData.getBlueTake() == true){
					aiTarget =	robut.move(gameData.getVisibleTeams(), 2); 
					prevDisk[0] = aiTarget[0]; prevDisk[1] = aiTarget[1];
					gameData.setVisibleTeams(aiTarget[2], aiTarget[3], 2);
					gameData.setVisibleTeams(prevDisk[0], prevDisk[1], 0);
					prevDisk[0] = -1;
					prevDisk[1] = -1;
					gameData.incrementPreviousMoves();
					if(checkMill(aiTarget[2],aiTarget[3],2)){ //check for a mill
						gameData.setState(3);
						state3();
					}
					else{
						gameData.incrementTake();
						piecePanel.setLabel("Moving Phase: Red's Turn");
					}
				}
				else{
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getRedTake() == true){ //if the piece was clicked and it is blue's turn	
							if(gameData.getVisibleTeams()[i][j] == 1 && prevDisk[0] == -1){
								piecePanel.setLabel("Moving Phase: Red Piece Selected");
								prevDisk[0] = i; prevDisk[1] = j;
							}
							else if(gameData.getVisibleTeams()[i][j] == 0 && prevDisk[0] != -1 &&prevDisk[1] != -1){
								if(Board.checkpiece(prevDisk[0],prevDisk[1],i,j,gameData.getVisibleTeams()) == 1){
								gameData.setVisibleTeams(Board.movepiece(prevDisk[0],prevDisk[1],i,j,gameData.getVisibleTeams()));
									
								prevDisk[0] = -1;
								prevDisk[1] = -1;
								gameData.incrementPreviousMoves();
								if(checkMill(i,j,1)){ //check for a mill
									gameData.setState(3);
									state3();
								}
								else{
									gameData.incrementTake();
									if(aiOn == true){
										piecePanel.setLabel("Moving Phase: AI Blue's Turn");
									}
									else{
										piecePanel.setLabel("Moving Phase: Blue's Turn");

									}
								}
								}
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getBlueTake() == true){//if the piece was clicked and it is red's turn
							if(gameData.getVisibleTeams()[i][j] == 2 && prevDisk[0] == -1){
								prevDisk[0] = i; prevDisk[1] = j;
								piecePanel.setLabel("Moving Phase: Blue Piece Selected");
							}
							else if(gameData.getVisibleTeams()[i][j] == 0 && prevDisk[0] != -1){
								if(Board.checkpiece(prevDisk[0],prevDisk[1],i,j,gameData.getVisibleTeams()) == 1){
								gameData.setVisibleTeams(Board.movepiece(prevDisk[0],prevDisk[1],i,j,gameData.getVisibleTeams()));
								prevDisk[0] = -1;
								prevDisk[1] = -1;
								gameData.incrementPreviousMoves();
								if(checkMill(i,j,2)){ //check for a mill
									gameData.setState(3);
									state3();
								}
								else{
									gameData.incrementTake();
									if(aiOn == true){
										piecePanel.setLabel("Moving Phase: AI Red's Turn");
									}
									else{
										piecePanel.setLabel("Moving Phase: Red's Turn");
									}
								}
							}
							}
						}
					}
				}
				}
				gamePanel.repaint(); //repaint the board
				gameData.setState(2);
				state2();
			}
			//if we are in state 3, milling of another players pieces
			if(gameData.getCurrentState() ==3){
				if(aiOn == true && aiPlayer == 0 && gameData.getRedTake() == true){
					aiTarget = robut.mill(gameData.getVisibleTeams(), 1); //what is returned, what are variables?
					gameData.setVisibleTeams(aiTarget[0],aiTarget[1],0);
					gameData.incrementTake();
					gameData.decrementPlay2count();
					gameData.setState(gameData.getPreviousState());
				}
				else if(aiOn == true && aiPlayer == 1 && gameData.getBlueTake() == true){
					aiTarget = robut.mill(gameData.getVisibleTeams(), 2); //what is returned, what are variables?
					gameData.setVisibleTeams(aiTarget[0],aiTarget[1],0);
					gameData.incrementTake();
					gameData.decrementPlay1count();
					gameData.setState(gameData.getPreviousState());
				
				}
				else{
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getBlueTake() == true){ //if the piece was clicked and it is blue's turn	
							if(gameData.getVisibleTeams()[i][j] == 1 && prevDisk[0] == -1){
								gameData.setVisibleTeams(i,j,0);
								gameData.decrementPlay1count();
								gameData.setState(gameData.getPreviousState());
								gameData.incrementTake();
								if(aiOn == true){
									piecePanel.setLabel("Moving Phase: AI Red's Turn");
								}
								else{
									piecePanel.setLabel("Moving Phase: Red's Turn");
								}
							}
						}
						else if(gamePanel.getShapeArray()[i][j].contains(e.getPoint()) && gameData.getRedTake() == true){//if the piece was clicked and it is red's turn
							if(gameData.getVisibleTeams()[i][j] == 2 && prevDisk[0] == -1){
								gameData.setVisibleTeams(i,j,0);
								gameData.decrementPlay2count();
								gameData.setState(gameData.getPreviousState());
								gameData.incrementTake();
								if(aiOn == true){
									piecePanel.setLabel("Moving Phase: AI Blue's Turn");
								}
								else{
									piecePanel.setLabel("Moving Phase: Blue's Turn");
								}
								
							}
						}
					}
				}
				}
				gameData.resetPreviousMoves();
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
				gameData.incrementTake();
				System.out.println("add red?");
			}
			//if blue is selected, make blue active
			else if (piecePanel.getBlueCircle().contains(e.getPoint())){
				gameData.incrementTake();
				System.out.println("add blue?");
			}
			// if test space is selected, color it
		}
	});
	}
	
	//state 0: initial set up
	private void state0(){
		//set initial piece amounts
		boolean temp = Board.startboard(gameData.getVisibleTeams());
		if(aiOn == true){
			//determine AI player 
			Random rand = new Random();
			aiPlayer = rand.nextInt(1);
		}
		else{
			aiPlayer = -1;
		}
		gameData.setPlay1count(0);
		gameData.setPlay2count(0);
		//Random random = new Random(); //initialize random
		if(temp == true){
			gameData.setRedTake(true);
			gameData.setBlueTake(false);
			if(aiPlayer == 0){
				piecePanel.setLabel("Game Start: AI Red Goes First");
			}
			else{
				piecePanel.setLabel("Game Start: Red Goes First");
			}
			
		}
		else{
			gameData.setRedTake(false);
			gameData.setBlueTake(true);
			if(aiPlayer == 1){
				piecePanel.setLabel("Game Start: AI Blue Goes First");
			}
			else{
				piecePanel.setLabel("Game Start: Blue Goes First");
			}
		}
		piecePanel.setRedCount(Integer.toString(6 - gameData.getPlay1count()));
		piecePanel.setBlueCount(Integer.toString(6 - gameData.getPlay2count()));
		// set board to blank initially
		gamePanel.repaint();
		gameData.setState(1);
		prevDisk[0] = -1;
		prevDisk[1] = -1;
		
	}
	//state 1: placing pieces
	private void state1(){
		if(gameData.getRedTake()){
			piecePanel.setLabel("Red Turn");
		}
		if(gameData.getBlueTake()){
			piecePanel.setLabel("Blue Turn");
		}
		//if we have finished placing pieces
		if(gameData.getPlay1count() == 6 && gameData.getPlay2count() == 6){
			gameData.setState(2);
			prevDisk[0] = -1;
			prevDisk[1] = -1;
			piecePanel.setLabel("All Pieces Placed, Moving Phase");
		}
	}
	//state 2: moving pieces
	private void state2(){
		//if player piece count drops below necessary, or another condition is met, move to state 3
		if(gameData.getPlay1removed() == 0){
			piecePanel.setLabel("Blue Wins");
			gameData.setState(4);
		}
		else if(gameData.getPlay2removed() == 0){
			piecePanel.setLabel("Red Wins");
			gameData.setState(4);
		}
		//check for draw conditions
		else if(gameData.getPreviousMoves() >= 16){
			piecePanel.setLabel("Draw by inaction");
			gameData.setState(4);
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
	 * takes actions if a button was pressed
	 **/
	public void actionPerformed(ActionEvent e) {
		// check if check button was pressed
		if ("Check".equals(e.getActionCommand())){		
		}
		// checks if new game was pressed
		else if ("New Game".equals(e.getActionCommand())){
			aiOn = false;
			this.clearBoard();
			state0();
		}
		else if ("New AI Game".equals(e.getActionCommand())){
			aiOn = true;
			this.clearBoard();
			state0();
		}
		else if ("Save Game".equals(e.getActionCommand())){
			FileIO.saveGame(gameData);
			piecePanel.setLabel("Game Saved");
		}
		else if ("Load Game".equals(e.getActionCommand())){
			gameData.loadGame();
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
	
	//method to clear board
	private void clearBoard(){
		isButtonNewGame = true;
		//System.out.println("New Game!");
		isButtonTake = false;
		//empty the board
		for(int i = 0; i < 2; i ++){ //for each disk on the board
			for(int j = 0; j < 8; j++){
				gameData.setVisibleTeams(i,j,0);
				current[i][j] = 0;
			}
		}
		gamePanel.repaint();
	}
	//method to see if you have a mill
	private boolean checkMill(int i,int j,int colour){ //make return a boolean, if true move to milling state
		if(j == 0 || j == 1 || j == 2){
			if(gameData.getVisibleTeams()[i][0] == colour && gameData.getVisibleTeams()[i][1] == colour && gameData.getVisibleTeams()[i][2] == colour){
				return true;
			}
		}
		if(j == 4 || j == 5 || j == 6){
			if(gameData.getVisibleTeams()[i][4] == colour && gameData.getVisibleTeams()[i][5] == colour && gameData.getVisibleTeams()[i][6] == colour){
				return true;
			}
		}
		if(j == 0 || j == 7 || j == 6){
			if(gameData.getVisibleTeams()[i][0] == colour && gameData.getVisibleTeams()[i][7] == colour && gameData.getVisibleTeams()[i][6] == colour){
				return true;
			}
		}
		if(j == 2 || j == 3 || j == 4){
			if(gameData.getVisibleTeams()[i][2] == colour && gameData.getVisibleTeams()[i][3] == colour && gameData.getVisibleTeams()[i][4] == colour){
				return true;
			}
		}
		return false;
	}	
}
