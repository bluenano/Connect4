/**
 * Connect4NetController.java
 * threaded client side controller
*/

import javafx.scene.paint.Color;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import javafx.application.Platform;
import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;


public class Connect4NetController extends Connect4Controller implements Runnable {
    
    private static int PORT = 8902;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private char mark;
    private String name;  // use this as a display name in UI, might not need to store it

    // have access to GUI named view

    public Connect4NetController(String serverAddress, int port, String name) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.name = name;
            out.println("DISPLAY " + name);
        } catch (UnknownHostException e) {
            // close the program or maybe try to load the menu again
        } catch (IOException e) {

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // consider reloading main menu
            exitApplication();
        }

    }


    public void exitApplication() {
        Platform.runLater(new Runnable() {
            public void run() {
                view.close();                 
            }
        });
        System.exit(0);
    }


    public void handleMessage(String message) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.displayMessage(message);
            }
        });
    }


    public void handleMove(int column, int row, String player, Color c) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.addDisc(column, row, c);
                view.displayMove(player, column, row, c);
            }
        });
    }

    public void handleNewGame() {
        Platform.runLater(new Runnable() {
            public void run() {
				view.setUIScene();
            }
        });
    }
	
    public void handleStateChange(String result) {
        Platform.runLater(new Runnable() {
            public void run() {
                disableUserMoves();
                view.displayMessage(result);
            }
        });
    }

    public void handleUserMove(int column) {
        out.println("MOVE " + column);
    }
	
    public void updateMoveIndicator(Color c) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.setMoveIndicatorFill(c);
            }
        });
    }

    public void resetGame() {
		out.println("REMATCH_PLS");
    }
	
    public Color getPlayerColor() {
        return (mark == RED) ? Color.RED : Color.YELLOW;
    }

    public Color getOpponentColor() {
        return (mark == RED) ? Color.YELLOW : Color.RED;
    }


    public String getPlayer() {
        return (mark == RED) ? "Player Red" : "Player Yellow";
    }


    public String getOpponent() {
        return (mark == RED) ? "Player Yellow" : "Player Red";
    }


    public Color getColorFromServer(char fromServer) {
        return (fromServer == RED) ? Color.RED : Color.YELLOW;
    }


    private void disableUserMoves() {
        view.disableColumns();
        view.enablePlayAgain();
        view.disableMoveIndicator();
    }


    @Override
    public void run() {
        // first message will be WELCOME <char>
        String serverInput;
        try {
            // you need to use Platform.runLater whenever you need to update the
            // GUI from a non-gui thread 

            serverInput = in.readLine();
            String[] tokens = serverInput.split("\\s+");

            if (tokens[0].equals("WELCOME")) {
                mark = tokens[1].charAt(0);
                handleMessage("Welcome, you are " + getPlayer());
            }

            // process messages from server
            while (true) {
                serverInput = in.readLine();
                tokens = serverInput.split("\\s+");

                if (tokens[0].equals("VALID_MOVE")) {
                    
                    handleMove(Integer.parseInt(tokens[1]),
                               Integer.parseInt(tokens[2]),
                               getPlayer(),
                               getPlayerColor());

                } else if (tokens[0].equals("OPPONENT_MOVED")) {

                    handleMove(Integer.parseInt(tokens[1]),
                               Integer.parseInt(tokens[2]),
                               getOpponent(),
                               getOpponentColor());

                } else if (tokens[0].equals("VICTORY")) {
                    handleStateChange("You won");
                } else if (tokens[0].equals("DEFEAT")) {
                    handleStateChange("You lost");
                } else if (tokens[0].equals("DRAW")) {
                    handleStateChange("Draw");
                } else if (tokens[0].equals("MESSAGE")) {

                    String message = "";
                    for (int i = 1; i < tokens.length; i++){
                        message += tokens[i] + " ";
                    }
                    message.trim();
                    handleMessage(message);
   
                } else if (tokens[0].equals("NEW_GAME")) {
                    // reset GUI
					handleNewGame();
                }				
				else if (tokens[0].equals("SET")) {
                    char mark = tokens[1].charAt(0);
                    updateMoveIndicator(getColorFromServer(mark));
                } else if (tokens[0].equals("NAME")) {
                    String opponent = tokens[1];
                    // send to view for display
                    System.out.println("Name received: " + opponent);
                } else if (tokens[0].equals("DISCONNECT")) {
                    handleStateChange("Opponent disconnected");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in run method of Connect4NetController");
            System.out.println("Connection error");
            System.out.println("Closing the application...");
            exitApplication();            
        }
    }
}