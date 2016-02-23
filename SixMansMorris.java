import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SixMansMorris extends JFrame implements ActionListener 
{

	private static boolean isButtonCheck;
	private static boolean isButtonNewGame;
	public static boolean isButtonTake;
	public static boolean isButtonRecieve;


	public SixMansMorris(String title){
		super(title);
		// Creates Panels for separate purposes
		JPanel buttonPanel = new JPanel();
		PiecePanel piecePanel = new PiecePanel();

		// Add swing components to content pane
		Container c = getContentPane();
		c.add(buttonPanel, BorderLayout.SOUTH);
		Dimension size1 = buttonPanel.getPreferredSize();
		size1.height = 75;
		buttonPanel.setPreferredSize(size1);
		buttonPanel.setBackground(Color.WHITE);
		
		c.add(piecePanel, BorderLayout.NORTH);
		Dimension size2 = piecePanel.getPreferredSize();
		size2.height = 300;
		piecePanel.setPreferredSize(size2);
		piecePanel.setBackground(Color.WHITE);
		

		//// Implements Panel with New Game and Check buttons////
		
		//Creating Buttons//
		JButton buttonCheck = new JButton("Check!");
		buttonCheck.setActionCommand("Check");
		JButton buttonNewGame = new JButton("New Game?");
		buttonNewGame.setActionCommand("New Game");

		//makes buttons usable
		buttonCheck.addActionListener(this);
		buttonNewGame.addActionListener(this);

		//Displays Buttons
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

	public SixMansMorris(){
		JFrame frame = new SixMansMorris("Hello World String");
		frame.setLocation(250,250);
		frame.setSize(800, 660);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.WHITE);
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// Creates Frame for Game to Run in //
				new SixMansMorris();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Check".equals(e.getActionCommand())){
			isButtonCheck = true;
			System.out.println("Is it correct?");
			isButtonTake = false;
		}
		else if ("New Game".equals(e.getActionCommand())){
			isButtonNewGame = true;
			System.out.println("New Game!");
			isButtonTake = false;
		}
		else if ("take".equals(e.getActionCommand())){
			isButtonTake = true;
			System.out.println("Add a piece");
		}
		else{
			if (isButtonTake == true){
				System.out.println("Piece added");
				isButtonTake = false;
			}
			isButtonRecieve = true;
		}
	}
}

class PiecePanel extends JPanel {
	
	private Ellipse2D.Double redCircle = new Ellipse2D.Double(100, 100, 100, 100);
	private Ellipse2D.Double blueCircle = new Ellipse2D.Double(575, 100, 100, 100);
	private Ellipse2D.Double testShop = new Ellipse2D.Double(350, 100, 100, 100);
	
	private boolean blueTake;
	private boolean redTake;
	public static boolean isButtonAdded;
	public static boolean redAddedLast;
	public static boolean blueAddedLast;
	
	public PiecePanel() {
		
		blueTake = false;
		redTake = false;
		isButtonAdded = false;
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				if (redCircle.contains(e.getPoint())){
					blueTake = false;
					System.out.println("add red?");
					redTake = true;
				}
				else if (blueCircle.contains(e.getPoint())){
					redTake = false;
					System.out.println("add blue?");
					blueTake = true;
				}
				else if (testShop.contains(e.getPoint())){
					//Piece p = new Piece(testShop.getX(), testShop.getY(), true);
					//add(p);
					
					repaint();
					isButtonAdded = true;
				}
				else if (testShop.contains(e.getPoint())){
					repaint();
					System.out.println("BLUE ADDED");
					
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
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.draw(redCircle);
		g2d.fill(redCircle);
		
		g2d.setColor(Color.BLUE);
		g2d.draw(blueCircle);
		g2d.fill(blueCircle);
		
		
		
		if (isButtonAdded == false){
			g2d.setColor(Color.BLACK);
			g2d.draw(testShop);
		}
		
		if (redTake){
			g.setColor(Color.RED);
			g.fillOval(350, 100, 100, 100);
			redTake = false;
			redAddedLast = true;
			blueAddedLast = false;
		}
		if (blueTake){
			g.setColor(Color.BLUE);
			g.fillOval(350, 100, 100, 100);
			blueTake = false;
			blueAddedLast = true;
			redAddedLast = false;
			//isButtonAdded = true;
		}
		if (redAddedLast){
			g.setColor(Color.RED);
			g.fillOval(350, 100, 100, 100);
		}
		if (blueAddedLast){
			g.setColor(Color.BLUE);
			g.fillOval(350, 100, 100, 100);
		}
	}
}

