import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class FileIO {
	//should be a seperate IO class probably
	
		//method to save the game state to a text file
		public static void saveGame(GameData gameData){
			PrintWriter writer;
			try {
				writer = new PrintWriter("savedGame.txt", "UTF-8");
				for(int i = 0; i < 2; i ++){ //for each disk on the board
					for(int j = 0; j < 8; j++){
						writer.print(gameData.getVisibleTeams()[i][j]);
					}
				}
				writer.println("");
				writer.println(gameData.getRedTake());
				writer.println(gameData.getCurrentState());
				writer.println(gameData.getPlay1removed());
				writer.println(gameData.getPlay2removed());
				writer.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//method to load a game state from a txt file.
		public static int[][] loadGame(){
			int[][] returnArray = new int[3][8];
			try{
			BufferedReader br = new BufferedReader(new FileReader(new File("savedGame.txt")));
			char[] inputArray = br.readLine().toCharArray();
			returnArray[2][0] = 0;
			returnArray[2][1] = 0;
			
			for(int i = 0; i < 2; i ++){ //for each disk on the board
				for(int j = 0; j < 8; j++){
					returnArray[i][j] = Character.getNumericValue(inputArray[j + i*8]);
					if(Character.getNumericValue(inputArray[j + i*8]) == 1){
						returnArray[2][0]++;
					}
					if(Character.getNumericValue(inputArray[j + i*8]) == 2){
						returnArray[2][1]++;
					}
				}
			}
			returnArray[2][2] = Integer.parseInt(br.readLine()); //redTake
			if(returnArray[2][2] >0){ //blueTake
				returnArray[2][3] = 0;
			}
			else{
				returnArray[2][3] = 1;
			}
			returnArray[2][4] = Integer.parseInt(br.readLine()); //currentState
			returnArray[2][5] = Integer.parseInt(br.readLine());
			returnArray[2][6] = Integer.parseInt(br.readLine());
			br.close();
			}
			catch(Exception e){
				
			}
			return returnArray;
		}
}
