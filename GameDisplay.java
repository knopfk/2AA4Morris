import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

	/**
	 * GameDisplay - the main class for controlling the other display classes
	 * @param title - Title of the game
	 */
	public GameDisplay(String title){
		super(title);
		// Creates Panels for separate purposes
		JPanel buttonPanel = new JPanel();
		PiecePanel piecePanel = new PiecePanel();
		GameBoard gamePanel = new GameBoard();

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
		}
		// checks if new game was pressed
		else if ("New Game".equals(e.getActionCommand())){
			isButtonNewGame = true;
			System.out.println("New Game!");
			isButtonTake = false;
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
}
