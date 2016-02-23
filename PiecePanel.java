import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

/**
 * PiecePanel
 * class that manages the piece/color select buttons
 * extends jpanel to create a panel for color select
 * 
 */
class PiecePanel extends JPanel {
	
	//initialize member variables used in the class
	private Shape redCircle = new Ellipse2D.Double(50, 25, 50, 50);
	private Shape blueCircle = new Ellipse2D.Double(400, 25, 50, 50);
	private Shape testShop = new Ellipse2D.Double(225, 25, 50, 50);
	
	private boolean blueTake;
	private boolean redTake;
	public static boolean isButtonAdded;
	public static boolean redAddedLast;
	public static boolean blueAddedLast;

	/**
	 * Constructor for PiecePanel class
	 * 
	 */
	public PiecePanel() {
				
		blueTake = false; //sets initial value of blueTake to false
		redTake = false; //sets initial value of redTake to false
		isButtonAdded = false; //sets initial value of isButtonAdded to false
		
		addMouseListener(new MouseAdapter(){ //add a mouse listener
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				//if red is selected, make red active
				if (redCircle.contains(e.getPoint())){ 
					blueTake = false;
					System.out.println("add red?");
					redTake = true;
				}
				//if blue is selected, make blue active
				else if (blueCircle.contains(e.getPoint())){
					redTake = false;
					System.out.println("add blue?");
					blueTake = true;
				}
				// if test space is selected, color it
				else if (testShop.contains(e.getPoint())){
					//Piece p = new Piece(testShop.getX(), testShop.getY(), true);
					//add(p);
					
					repaint();
					isButtonAdded = true;
				}
				else if (testShop.contains(e.getPoint())){
					repaint();
					
				}
			}
		});
	}
//	@Override
//	public void paint(Graphics g){
//		super.paintComponent(g);
//		
//		
//	}
	@Override
	/**
	 * Override the painting component of the draw
	 * 
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		//draw the initial circles
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.draw(redCircle);
		g2d.fill(redCircle);
		
		g2d.setColor(Color.BLUE);
		g2d.draw(blueCircle);
		g2d.fill(blueCircle);

		
		g2d.setColor(Color.BLACK);
		g2d.draw(testShop);
			
		// if it is has not been added, set it back to black
		if (isButtonAdded == false){
			g2d.setColor(Color.BLACK);
			g2d.draw(testShop);
		}
		
		//if a color has been selected, change the middle circle to that color
		if (redTake){
			g.setColor(Color.RED);
			g.fillOval(225, 25, 50, 50);
			redTake = false;
			redAddedLast = true;
			blueAddedLast = false;
		}
		if (blueTake){
			g.setColor(Color.BLUE);
			g.fillOval(225, 25, 50, 50);
			blueTake = false;
			blueAddedLast = true;
			redAddedLast = false;
			//isButtonAdded = true;
		}
		if (redAddedLast){
			g.setColor(Color.RED);
			g.fillOval(225, 25, 50, 50);
		}
		if (blueAddedLast){
			g.setColor(Color.BLUE);
			g.fillOval(225, 25, 50, 50);
		}
	}
	
}
