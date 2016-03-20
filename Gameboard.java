import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JPanel;


/**
 * GameBoard class: represents the gameboard display 
 * @author Group 3 Tutorial 2
 *
 */
class GameBoard extends JPanel {

	private Shape[][] shapeArray = new Shape[2][8]; //array of the game pieces
	
	private int[][] visibleTeams = new int[2][8]; //array of the current state of each piece
	
	Random rnd = new Random(); //random number generator
	
	private boolean redTake; //is red the active player
	private boolean blueTake; //is blue the active player

	
	public int [][] sizingArray = new int[2][3]; //array to keep track of the size of the board in terms of Mans (3, 6, 9, etc), used for scaling
	public double height = 500; //predetermined height, should receive from controller
	public double width = 500; //predetermined width, should receive from controller
	public double size = 100; //predetermined size, should receive from controller
	
	public Shape[][] getShapeArray(){
		return shapeArray;
	}
	
	public void setVisibleTeams(int i, int j, int value){
		visibleTeams[i][j] = value;
	}
	
	public int getVisibleTeams(int i, int j){
		return visibleTeams[i][j];
	}
	
	/* GameBoard constructor
	 * Builds and maintains the gameboard display
	 * 
	 */
	public GameBoard() {

		//intialize the sizeArray for the gameboard to values that reflect 6 Man Morris
		// other values would be able to be easily added if there was expansion to larger game boards
		sizingArray[0][0] = 0;
		sizingArray[0][1] = 8;
		sizingArray[0][2] = 4;
		sizingArray[1][0] = 2;
		sizingArray[1][1] = 6;
		sizingArray[1][2] = 2;
		
		//initialize all disks to empty, should receive this from controller
		for(int i = 0; i < 8; i ++){
			visibleTeams[0][i] = 0;
			visibleTeams[1][i] = 0;
		}
		
		//set the location on the game board for each disk location element
		// using the scaling array to determine the position of each piece on the board
		for(int i = 0; i < 2; i++){
			shapeArray[i][0] = new Ellipse2D.Double(sizingArray[i][0]*(width/9), sizingArray[i][0]*(height/9),height/10, width/10);
			shapeArray[i][1] = new Ellipse2D.Double((sizingArray[i][0] + sizingArray[i][2])*(width/9), sizingArray[i][0]*(height/9),height/10, width/10);
			shapeArray[i][2] = new Ellipse2D.Double(sizingArray[i][1]*(width/9), sizingArray[i][0]*(height/9),height/10, width/10);
			
			shapeArray[i][3] = new Ellipse2D.Double(sizingArray[i][0]*(width/9), (sizingArray[i][0] + sizingArray[i][2])*(height/9),height/10, width/10);
			shapeArray[i][4] = new Ellipse2D.Double(sizingArray[i][1]*(width/9), (sizingArray[i][0] + sizingArray[i][2])*(height/9),height/10, width/10);
			
			shapeArray[i][5] = new Ellipse2D.Double(sizingArray[i][0]*(width/9), sizingArray[i][1]*(height/9),height/10, width/10);
			shapeArray[i][6] = new Ellipse2D.Double((sizingArray[i][0] + sizingArray[i][2])*(width/9), sizingArray[i][1]*(height/9),height/10, width/10);
			shapeArray[i][7] = new Ellipse2D.Double(sizingArray[i][1]*(width/9), sizingArray[i][1]*(height/9),height/10, width/10);
			
		}
	}
	@Override
	protected void paintComponent(Graphics g){ //define the rules for drawing the board to the window
		super.paintComponent(g); //use the parent class's methods
		Graphics2D g2d = (Graphics2D) g; //create a graphics object to use
		
		//draw the background of the gameboard
		g2d.setColor(Color.BLACK); //set the current color to black
		Shape temp; //create a temporary shape to use to draw the lines
		for(int i = 0; i < 2; i++){ //for each level (rectangle) on the board
			temp = new Rectangle2D.Double((sizingArray[i][0])*(width/9) + width/18, (sizingArray[i][0])*(height/9) + height/18,height*2*sizingArray[i][2]/9,width*2*sizingArray[i][2]/9); //draw the rectangles using the scaling array from before
			g2d.draw(temp); //actually draw it to the screen
			
		}
		//draw connector lines
		temp = new Line2D.Double(sizingArray[0][0]*(width/9) + width/18, (sizingArray[0][0] + sizingArray[0][2])*(height/9)+ height/18, sizingArray[1][0]*(width/9) + width/18, (sizingArray[1][0] + sizingArray[1][2])*(height/9) + height/18);
		g2d.draw(temp);
		temp = new Line2D.Double(sizingArray[0][1]*(width/9) + width/18, (sizingArray[0][1] - sizingArray[0][2])*(height/9)+ height/18, sizingArray[1][1]*(width/9) + width/18, (sizingArray[1][1] - sizingArray[1][2])*(height/9) + height/18);
		g2d.draw(temp);
		
		temp = new Line2D.Double((sizingArray[0][0] + sizingArray[0][2])*(width/9) + width/18, (sizingArray[0][0])*(height/9)+ height/18, (sizingArray[1][0] + sizingArray[1][2])*(width/9) + width/18, (sizingArray[1][0])*(height/9) + height/18);
		g2d.draw(temp);
		temp = new Line2D.Double((sizingArray[0][1] - sizingArray[0][2])*(width/9) + width/18, (sizingArray[0][1])*(height/9)+ height/18, (sizingArray[1][1] - sizingArray[1][2])*(width/9) + width/18, (sizingArray[1][1])*(height/9) + height/18);
		g2d.draw(temp);
		
		//draw disk/ pieces on the board
		for(int i = 0; i < 2; i++){ //for each level (rectangle)
			for(int j = 0; j < 8; j++){ //for each piece on that level
				if(visibleTeams[i][j] == 0){ //if it is empty
					g2d.setColor(Color.BLACK); //set the color to black (empty)
				}
				else if(visibleTeams[i][j] == 1){ //if the space is controlled by red
					g2d.setColor(Color.RED); //set the color to red
				}
				else if(visibleTeams[i][j] == 2){ //if the space is controlled by blue
					g2d.setColor(Color.BLUE); //set the color to blue
				}
				//g2d.draw(shapeArray[i][j]);
				g2d.fill(shapeArray[i][j]); // draw the filled in disk to the board
			}
		}
	}
}