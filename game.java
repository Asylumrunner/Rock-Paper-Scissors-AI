import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class rpsAI {
	float[][] markovChain; // used to store markov chain probabilities
	int[] timesPlayed; // contains the number of times the player has played each of the three moves. For calculation purposes.
	
	int lastMove; //last move of the human player
	int moveBeforeLast; //move before last of the human player
	
	rpsAI(){
		
	}
	
	String makeMove(){
		return "rock";
	}
	
	void update(String newMove){
		
	}
	
	void saveData(Scanner keyboard){
		System.out.print("Enter the name of the file: ");
		String filename = keyboard.nextLine();
		
		Path currentRelativePath = Paths.get("");
		String workingDirectory = currentRelativePath.toAbsolutePath().toString();
		String newFilePath = workingDirectory + "/" + filename;
		File outfile = new File(newFilePath);
		
		PrintWriter output;
		try {
			output = new PrintWriter(outfile);
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					output.print(markovChain[i][j]);
					output.print(" ");
				}
			}
			output.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("That file was not found.");
		}
		
		
	}
	
	void setChain(int x, int y, float probability){
		markovChain[x][y] = probability;
	}
}

public class game {
	public static void main(String[] args){
		boolean wannaQuit = false;
		rpsAI opponent = new rpsAI();
		Scanner keyboard = new Scanner(System.in);
		
		do {	
			System.out.println("Welcome to the Rock, Paper, Scissors AI Challenge!");
			System.out.println("Please select a numeric option below:");
			System.out.println("1. Play RPS against a new AI");
			System.out.println("2. Load the probabilistic data from an old session");
			System.out.println("3. Quit");
			System.out.print("Please enter a choice: ");
			
			int choice = Integer.parseInt(keyboard.nextLine());
			while(choice > 3 || choice < 1){
				System.out.println("Invalid entry.");
				System.out.print("Please enter a valid choice: ");
				keyboard.nextLine();
				choice = Integer.parseInt(keyboard.nextLine());
			}
			switch(choice) {
				case 1: play(keyboard, opponent);
					break;
				case 2: loadData(keyboard, opponent);
					break;
				case 3: wannaQuit = true;
					break;
				default: break;
			}

		}while(!wannaQuit);
		
		
	}
	
	static void loadData(Scanner keyboard, rpsAI opponent){
		System.out.print("Please enter the name of the file, including file extension:");
		String file = keyboard.nextLine();
		try {
			Scanner input = new Scanner(new File(file));
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					opponent.setChain(i, j, input.nextFloat());
				}
			}
			
			input.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("That file was not found.");
		}
	}
	
	static void play(Scanner keyboard, rpsAI opponent){
		System.out.println("Let's play Rock, Paper, Scissors!");
		boolean playing = true;
		
		do{
			System.out.print("Enter 'rock', 'paper' or 'scissors':");
			String playermove = keyboard.nextLine().trim().toLowerCase();

			while(playermove.equals("rock") && playermove.equals("paper") && playermove.equals("scissors")){
				System.out.println("That is not a valid move!");
				System.out.print("Please enter 'rock', 'paper', or 'scissors'");
				playermove = keyboard.nextLine().trim().toLowerCase();
			}
			
			String AIMove = opponent.makeMove();
			int result = results(playermove, AIMove);
			
			if(result == 0){
				System.out.println("It was a tie! Both of you played " + playermove);
			}
			else if(result == 1){
				System.out.println("You won! " + playermove + " beat " + AIMove);
			}
			else{
				System.out.println("You lost! " + AIMove + " beat " + playermove);
			}
			
			opponent.update(playermove);
			
			System.out.println("What would you like to do?");
			System.out.println("1. Keep playing");
			System.out.println("2. Save AI data to file, and keep playing");
			System.out.println("3. Return to menu");
			System.out.print("Please select an option: ");
			
			int choice = Integer.parseInt(keyboard.nextLine());
			while(choice > 3 || choice < 1){
				System.out.println("Invalid entry.");
				System.out.print("Please enter a valid choice: ");
				keyboard.nextLine();
				choice = Integer.parseInt(keyboard.nextLine());
			}
			switch(choice) {
				case 1:
					break;
				case 2: opponent.saveData(keyboard);
					break;
				case 3: playing = false;
					break;
				default: break;
			}
			
		}while(playing);
		
	}
	
	static int results(String playerMove, String AIMove){
		if(playerMove.equals("rock")){
			if(AIMove.equals("rock")){
				return 0;
			}
			else if(AIMove.equals("paper")){
				return -1;
			}
			else{
				return 1;
			}
		}
		else if(playerMove.equals("paper")){
			if(AIMove.equals("rock")){
				return 1;
			}
			else if(AIMove.equals("paper")){
				return 0;
			}
			else{
				return -1;
			}
		}
		else{
			if(AIMove.equals("rock")){
				return -1;
			}
			else if(AIMove.equals("paper")){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
}
