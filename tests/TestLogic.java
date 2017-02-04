/**
 * Sean Schlaefli
 * TestLogic.java
 * Test the connect four logic. 
 * Accepts several command line parameters
 * that control the testing.
 * -t option will require a second parameter.
 * This parameter must be the name of a file 
 * that will specify the set of test files to
 * run.
 * compiles
 * unfinished
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Character;
import java.lang.Exception;
import java.lang.InterruptedException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class TestLogic {

    public static void main(String[] args) {
	String option = "";
	String file = "";
	if(args.length == 1) {
	    option = args[0];
	    if(option.equals("-t")) {	    
		Scanner cin = new Scanner(System.in);
		file = cin.nextLine();
		initiateTest(file);
	    }	    
	} else if(args.length == 2) {
	    option = args[0];
	    file = args[1];
	    if(option.equals("-t")) { 
		initiateTest(file);
	    }
	} else {
	    System.out.println("Use command line arguments.");
	}
    }


    /**
     * Test a set of game files.
     * Game file format:
     * row
     * col
     * winSize
     * the remaining integers will be the moves
     * made during the duration of the game.
     * @param String file
     */
    public static void initiateTest(String file) {
		
	try {
	    File read = new File(file);
	    Scanner readFiles = new Scanner(read);
	    ArrayList<String> games = new ArrayList<String>();
	    HashMap<String, Character> winners = new HashMap<String, Character>();
	    
	    while(readFiles.hasNextLine()) {
		String game = readFiles.nextLine();
		games.add(game);		
	    }

	    processGames(games, winners);
	    // return from processing and print results
	    printResults(games, winners);
	} catch (FileNotFoundException e) {
	    System.out.println("File not found exception.");
	}

    }


    /**
     * Process a list of game files using worker threads.
     * @param ArrayList<String> games
     * @param HashMap<String, Character> winners
     */
    public static void processGames(ArrayList<String> games, HashMap<String, Character> winners) {
	CountDownLatch control = new CountDownLatch(games.size());
	
	for(int i = 0; i < games.size(); i++) {
	    ConnectFourWorker test = new ConnectFourWorker(games.get(i), winners, control);
	    test.start();	    
	}
	try {
	    control.await();
	} catch(InterruptedException ignored) {}
	
    }


    /**
     * Print the results of processing the games.
     * @param ArrayList<String> games
     * @param HashMap<String, Character> winners
     */
    public static void printResults(ArrayList<String> games,
				    HashMap<String, Character> winners) {

	for(int i = 0; i < games.size(); i++) {
	    System.out.println(winners.get(games.get(i)));
	}
    }
    // Inner thread class to process individual game files.
    public static class ConnectFourWorker extends Thread {

	private String game;       
	private HashMap<String, Character> winners;
	private CountDownLatch control;
	
	public ConnectFourWorker(String game,
				 HashMap<String, Character> winners,
				 CountDownLatch control) {
	    this.game = game;
	    this.winners = winners;
	    this.control = control;
	}

	public void run() {
	    System.out.println(game);
	    processGame(game);
	    System.out.println("Counting down!");
	    control.countDown();
	    System.out.println("latches available: " + control.getCount());
	}


	/**
	 * File format:
	 * number of rows
	 * number of columns
	 * number to win
	 * moves to make
	 */
	public void processGame(String game) {
	    try {
		File connectFour = new File(game);
		Scanner readGame = new Scanner(connectFour);
		
		int rows = readGame.nextInt();
		System.out.println("input rows: " + rows);		
		int columns = readGame.nextInt();
		System.out.println("input columns: " + columns);		
		int winSize = readGame.nextInt();
		System.out.println("input win: " + winSize);
		ConnectFourLogic g = new ConnectFourLogic(rows, columns, winSize);

		while(readGame.hasNextInt()) {
		    int move = readGame.nextInt();
		    g.move(move);

		    if(g.gameOver()) {
			System.out.println("Game over, " + g.getCurrentMove() + " won the game.");
			Character winner = new Character(g.getCurrentMove());
			winners.put(game, winner);
			return;
		    }
		    
		    if(g.isDraw()) {
			System.out.println("Game ended in a draw.");
			return;
		    }
		    
		    
		}
	    } catch(FileNotFoundException e) {
		// handle file not found
		System.out.println(game + ", file not found...");
	    }
	}
    }
    
}
