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


import java.util.Scanner;


public class TestLogic {

    public static void main(String[] args) {
	if(args.length == 2) {
	    String option = args[0];
	    String file = args[1];
	    if(option.equals("-t")) { 
		initiateTest(file);
	    }
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
	// test logic here
    }
    
}
