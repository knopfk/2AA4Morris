import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JLayeredPane;

public class Piece extends JLayeredPane{

	private final boolean colour;
	private double coordX;
	private double coordY;
	private Ellipse2D.Double discPiece;
	
	
	Piece (double x, double y, boolean c){
		this.coordX = x;
		this.coordY = y;
		this.colour = c;
		this.discPiece = new Ellipse2D.Double(coordX, coordY, 100, 100);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (colour) g2d.setColor(Color.BLUE);
		else g2d.setColor(Color.RED);
		g2d.draw(discPiece);
		g2d.fill(discPiece);
		System.out.println("Piece added");
	}
}
