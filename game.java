import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

class rpsAI {
	private float[][] markovChain; // used to store markov chain probabilities
	private int[] timesPlayed; // contains the number of times the player has played each of the three moves. For calculation purposes.
	
	private int lastMove; //last move of the human player
	private int moveBeforeLast; //move before last of the human player
	
	rpsAI(){//constructor for the AI class
		markovChain = new float[][] {{0.33f, 0.33f, 0.33f}, {0.33f, 0.33f, 0.33f}, {0.33f, 0.33f, 0.33f}};//Didn't really see a need to do this via a loop when a simple assign statement would do
		timesPlayed = new int[] {0, 0, 0};
	}
	
	public String makeMove(){//main function for the class, probabilistically guesses the player's next move, then counters it
		Random rand = new Random();//establish a random number generator
		float ranFloat = rand.nextFloat();//generate a random float
		if(ranFloat <= markovChain[lastMove][1]){//use that random float, along with our Markov Chain values of potential moves, to generate a probabilisically determined move
			return "paper";//Note that the returned moves are, in fact, the counters to the move the AI predicts the user to perform
		}
		else if (ranFloat <= markovChain[lastMove][2] + markovChain[lastMove][1]){//We add these two values together here to properly model the possibility space
			return "scissors";
		}
		else{
			return "rock";
		}
	}
	
	public void update(String newMove){//Takes in the results of the last game played, and uses them to update the Markov chain using relevant data
		moveBeforeLast = lastMove;
		if(newMove.equals("rock")){
			lastMove = 0;
		}
		else if(newMove.equals("paper")){
			lastMove = 1;
		}
		else{
			lastMove = 2;
		}
		
		//Here comes the hard part: updating the Markov Chain
		/*
		 * 1. Multiply everything in the appropriate column of the Markov Chain by timesPlayed[moveBeforeLast]
		 * 2. Increment the row value we want (that is, markovChain[moveBeforeLast][lastMove] by one
		 * 3. Increment timesPlayed[moveBeforeLast] by one
		 * 4. Divide all values in markovChain[moveBeforeLast][x] by timesPlayed[moveBeforeLast]
		 */
		
		for(int i = 0; i < 3; i++){ //1. Multiply everything in the appropriate column of the Markov Chain by timesPlayed[moveBeforeLast]
			markovChain[moveBeforeLast][i] *= timesPlayed[moveBeforeLast];
		}
		
		//2. Increment the row value we want (that is, markovChain[moveBeforeLast][lastMove] by one 
		markovChain[moveBeforeLast][lastMove] += 1;
		
		//3. Increment timesPlayed[moveBeforeLast] by one
		timesPlayed[moveBeforeLast]++;
		
		//4. Divide all values in markovChain[moveBeforeLast][x] by timesPlayed[moveBeforeLast]
		for(int j = 0; j < 3; j++){
			markovChain[moveBeforeLast][j] /= timesPlayed[moveBeforeLast];
		}
		
		//For debug purposes, let's go ahead and print the contents of this Markov Chain:
		System.out.println("New Markov Chain");
		System.out.println("Rock to Rock: " + markovChain[0][0] + " Rock to Paper: " + markovChain[0][1] + " Rock to Scissors: " + markovChain[0][2]);
		System.out.println("Paper to Rock: " + markovChain[1][0] + " Paper to Paper: " + markovChain[1][1] + " Paper to Scissors: " + markovChain[1][2]);
		System.out.println("Scissors to Rock: " + markovChain[2][0] + " Scissors to Paper: " + markovChain[2][1] + " Scissors to Scissors: " + markovChain[2][2]);
		
		
	}
	
	public void saveData(Scanner keyboard){//Function which saves the Markov chain developed by the AI to a text file for later use
		System.out.print("Enter the name of the file: ");
		String filename = keyboard.nextLine();//User inputs their desired name for the file
		
		Path currentRelativePath = Paths.get("");//Current working directory for the program is found
		String workingDirectory = currentRelativePath.toAbsolutePath().toString();//This directory is then converted into an absolute path, then into a string
		String newFilePath = workingDirectory + "/" + filename;//this string then has the new file appended to the end (TODO: I think I fucked this up? Is this pointing to a file or directory?)
		File outfile = new File(newFilePath);//then creates a new file at the address
		
		PrintWriter output;//PrintWriter is then opened
		try {
			output = new PrintWriter(outfile);//Printwriter is connected to the outfile
			for(int i = 0; i < 3; i++){//and writes the contents of the Markov Chain to it
				for(int j = 0; j < 3; j++){
					output.print(markovChain[i][j]);
					output.print(" ");
				}
			}
			
			for(int k = 0; k < 3; k++){//and we also print the contents of timesPlayed to it as well, so our future loaded AI can properly update its inherited Markov Chain
				output.print(timesPlayed[k]);
				output.print(" ");
			}
			output.close();
		} 
		catch (FileNotFoundException e) {//I don't think this will ever trigger, but in case it does, this exception is caught
			System.out.println("That file was not found.");
		}
		
		
	}
	
	public void loadData(Scanner keyboard){//A function which reads in a file and reconstructs the described Markov Chain 
		System.out.print("Please enter the name of the file, including file extension:");
		String file = keyboard.nextLine();//User enters in the name of the data file
		try {
			Scanner input = new Scanner(new File(file));//A scanner is then opened to read in the data
			for(int i = 0; i < 3; i++){//Use the setter for the RPSAI to construct the old Markov Chain
				for(int j = 0; j < 3; j++){
					this.setChain(i, j, input.nextFloat());
				}
			}
			
			for(int k = 0; k < 3; k++){//and also reconstruct our old timesPlayed array
				this.setTimesPlayed(k, input.nextInt());
			}
			
			input.close();
		} 
		catch (FileNotFoundException e) {//if we can't find the file the user's talking about, let 'em know, and exit the function
			System.out.println("That file was not found.");
		}
	}
	
	private void setChain(int x, int y, float probability){//simple setter routine, used to generate a Markov Chain from a file
		markovChain[x][y] = probability;
	}
	
	private void setTimesPlayed(int x, int y){
		timesPlayed[x] = y;
	}
}

public class game {
	public static void main(String[] args){
		boolean wannaQuit = false;//wannaQuit serves as the boolean operator for the main do-while loop
		rpsAI opponent = new rpsAI();//create a new AI
		Scanner keyboard = new Scanner(System.in);//then open up a Scanner to read user input. This'll get used a /lot/
		
		do {	
			System.out.println("Welcome to the Rock, Paper, Scissors AI Challenge!");
			System.out.println("Please select a numeric option below:");
			System.out.println("1. Play RPS against a new AI");
			System.out.println("2. Load the probabilistic data from an old session");
			System.out.println("3. Quit");
			System.out.print("Please enter a choice: "); //Print out options. Standard affair.
			
			int choice = Integer.parseInt(keyboard.nextLine());//read the user's input, then try to parse an int from it, representing the three menu options
			while(choice > 3 || choice < 1){//If the int you get isn't a valid input given the menu
				System.out.println("Invalid entry.");//Waggle your finger at the user
				System.out.print("Please enter a valid choice: ");//Then make them enter a new option
				keyboard.nextLine();
				choice = Integer.parseInt(keyboard.nextLine());
			}
			switch(choice) {//Now that we've verified input, this switch sends the user to the proper function given their request.
				case 1: play(keyboard, opponent);
					break;
				case 2: opponent.loadData(keyboard);
					break;
				case 3: wannaQuit = true;//Except case 3, of course, which simply breaks the do-while loop
					break;
				default: break;
			}

		}while(!wannaQuit);
		
		
	}
	

	
	static void play(Scanner keyboard, rpsAI opponent){//play() forms the core gameplay loop of the game
		System.out.println("Let's play Rock, Paper, Scissors!");
		boolean playing = true;
		
		do{//This do-while loop is, literally, the gameplay loop
			System.out.print("Enter 'rock', 'paper' or 'scissors':");
			String playermove = keyboard.nextLine().trim().toLowerCase();//prompt the user for a move, then correct it to proper style

			while(playermove.equals("rock") && playermove.equals("paper") && playermove.equals("scissors")){//if the user's input isn't rock, paper, or scissors
				System.out.println("That is not a valid move!");//Scold 'em
				System.out.print("Please enter 'rock', 'paper', or 'scissors'");//Then make them re-enter
				playermove = keyboard.nextLine().trim().toLowerCase();
			}
			
			String AIMove = opponent.makeMove();//Now that we have the user's move, have the AI generate it's move
			int result = results(playermove, AIMove);//Then send both moves to results() to generate the results
			
			if(result == 0){//then, given the result of results, print out a message describing the outcome of the game
				System.out.println("It was a tie! Both of you played " + playermove);
			}
			else if(result == 1){
				System.out.println("You won! " + playermove + " beat " + AIMove);
			}
			else{
				System.out.println("You lost! " + AIMove + " beat " + playermove);
			}
			
			opponent.update(playermove);//then update the AI with the outcome of that game
			
			System.out.println("What would you like to do?");//Now, offer the user options as to what they want to do next
			System.out.println("1. Keep playing");
			System.out.println("2. Save AI data to file, and keep playing");
			System.out.println("3. Return to menu");
			System.out.print("Please select an option: ");
			
			int choice = Integer.parseInt(keyboard.nextLine());//Have them enter a numeric choice from the menu, then correct it as needed
			while(choice > 3 || choice < 1){
				System.out.println("Invalid entry.");
				System.out.print("Please enter a valid choice: ");
				keyboard.nextLine();
				choice = Integer.parseInt(keyboard.nextLine());
			}
			switch(choice) {
				case 1://Case 1 simply has them traverse this do-while loop again
					break;
				case 2: opponent.saveData(keyboard); //Case 2 tells the opponent to begin the process of saving it's knowledge to a file
					
					System.out.println("Do you wanna keep playing?:");
					System.out.println("1. Yes");
					System.out.println("2. No");
					
					choice = Integer.parseInt(keyboard.nextLine())
					while(choice != 1 && choice != 2){
						System.out.println("Invalid entry.");
						System.out.print("Please enter a valid choice: ");
						keyboard.nextLine();
						choice = Integer.parseInt(keyboard.nextLine());
					}
					if(choice == 2){
						playing = false;
					}
					break;
				case 3: playing = false; //And Case 3 breaks this do-while loop, allowing the user to return to the main menu
					break;
				default: break;
			}
			
		}while(playing);
		
	}
	
	static int results(String playerMove, String AIMove){//Results is an extremely simple function which returns the results of a game of rock, paper, scissors
		if(playerMove.equals("rock")){
			if(AIMove.equals("rock")){
				return 0;//a return of 0 indicates a tie
			}
			else if(AIMove.equals("paper")){
				return -1;//a return of -1 indicates a player 2 win (which in this case is the AI)
			}
			else{
				return 1;//and a return of 1 indicates a player 1 win (which in this case is the player)
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
