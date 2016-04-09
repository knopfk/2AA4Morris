import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;
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
	
	private boolean blueTake;
	private boolean redTake;
	private static boolean isButtonAdded;
	private static boolean redAddedLast;
	private static boolean blueAddedLast;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;

	public Shape getRedCircle(){
		return redCircle;
	}
	
	public Shape getBlueCircle(){
		return blueCircle;
	}
	
	public void setLabel(String newText){
		label1.setText(newText);
	}
	
	public void setRedCount(String newText){
		label2.setText(newText);
	}
	
	public void setBlueCount(String newText){
		label3.setText(newText);
	}
	
	/**
	 * Constructor for PiecePanel class
	 * 
	 */
	public PiecePanel() {
				
		blueTake = false; //sets initial value of blueTake to false
		redTake = false; //sets initial value of redTake to false
		isButtonAdded = false; //sets initial value of isButtonAdded to false
		this.setLayout(null);
		 label1 = new JLabel();
		 label1.setText("Default");
		 label1.setSize(200,20);
		 label1.setHorizontalAlignment(0);
		 label1.setLocation(150,40);
		 add(label1);
		 
		 label2 = new JLabel();
		 label2.setText("6");
		 label2.setSize(50,20);
		 label2.setLocation(70, 40);
		 add(label2);
		 
		 label3 = new JLabel();
		 label3.setText("6");
		 label3.setSize(50,20);
		 label3.setLocation(420, 40);
		 add(label3);

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

		
		//g2d.setColor(Color.BLACK);
	//	g2d.draw(testShop);
			
		// if it is has not been added, set it back to black
		if (isButtonAdded == false){
		//	g2d.setColor(Color.BLACK);
		//	g2d.draw(testShop);
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
