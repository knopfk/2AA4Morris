import javax.swing.SwingUtilities;

public class GameMain {
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// Creates Frame for Game to Run in //
				GameDisplay gameDisplay = new GameDisplay();			
			}
		});
	}
}
