//First draft of morris game board using j-buttons as spaces
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class Gameboard extends JFrame{
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JPanel board;
    private JButton[][] boardSquares = new JButton[7][7];
    private JLabel statusbar;

    public Gameboard() {
        
        initUI();
   }
    
   private void initUI() {

        statusbar = new JLabel("Other stuff here");
        add(statusbar, BorderLayout.SOUTH);
        
        board = new JPanel(new GridLayout(0, 7));
        board.setBorder(new LineBorder(Color.BLACK));
        gui.add(board);
        
        Insets buttonMargin = new Insets(0,0,0,0);
 
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                if ((j % 3 == 0 && i % 6 == 0)) {
                    b.setBackground(Color.BLACK);
                } else if((i == 2 || i == 4) && ( j > 1 && j < 5 )){
                	b.setBackground(Color.BLACK);
                } else if( i == 3 && j % 2 == 0){
                	b.setBackground(Color.BLACK);
                } else {
                	if((i%3 == 0 || j%3 == 0 )&& !(i ==3 && j ==3)){
                		b.setBackground(Color.GRAY);
                	}
                	b.setEnabled(false);
                    b.setBorderPainted(false);
                }
                boardSquares[j][i] = b;
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
               board.add(boardSquares[j][i]);
            }
        }
        setSize(400, 400);
        setTitle("6-Man Morris");
        add(gui);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);       
   }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                Gameboard game = new Gameboard();
                game.setVisible(true);
            }
        });                
    }                 
} 