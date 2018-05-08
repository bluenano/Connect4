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
    private String playerName; 
    private String opponentName;


    /**
     * Constructor for the controller used in a networked game
     * @param serverAddress the address of the server to connect to
     * @param port the port to connect on
     * @param playerName the display name of the player
     */
    public Connect4NetController(String serverAddress, int port, String playerName) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.playerName = playerName;
            out.println("DISPLAY " + playerName);
        } catch (UnknownHostException e) {
            // close the program or maybe try to load the menu again
        } catch (IOException e) {

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // consider reloading main menu
            exitApplication();
        }

    }


    /**
     * Exit the application
     */
    public void exitApplication() {
        Platform.runLater(new Runnable() {
            public void run() {
                view.close();                 
            }
        });
        System.exit(0);
    }


    /**
     * Display a message in the GUI
     * @param message the message to display
     */
    public void handleMessage(String message) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.displayMessage(message);
            }
        });
    }


    /**
     * Handle a move 
     * @param player the display name of the player that moved
     * @param col the column position that the move occurred at
     * @param row the row position that the move occurred at 
     * @param c the color of the player that moved
     */
    public void handleMove(String player, int col, int row, Color c) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.addDisc(col, row, c);
                String move = generateMoveString(player, col, row);
                view.displayMessage(move);
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

    /** 
     * Handle change in the game state
     * involves the game ended by draw or win/loss
     * @param result the final state of the game
     */
    public void handleStateChange(String result) {
        Platform.runLater(new Runnable() {
            public void run() {
                disableUserMoves();
                view.displayMessage(result);
            }
        });
    }


    /**
     * Send a message to the server to request a move
     * @param col the column position to move to
     */
    public void handleUserMove(int col) {
        out.println("MOVE " + col);
    }


    /**
     * Reset the GUI after a game ended
     */
    public void resetGame() {
        out.println("REMATCH_PLS");
    }


    /**
     * Update a move indicator in the GUI
     * @param c the color that determines which move indicator to set
     */
    public void updateMoveIndicator(Color c) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.setMoveIndicatorFill(c);
            }
        });
    }


    /**
     * Set a label in the GUI depending on the mark
     * @param mark the mark used to determine which label to set
     * @param display the display name to set the label to
     */
    public void setLabel(char mark, String display) {
        Platform.runLater(new Runnable() {
            public void run() {
                if (mark == RED) {
                    view.setRedLabel(display);
                } else {
                    view.setYellowLabel(display);
                }
            }
        });
    }
  

    /**
     * Get the color of the player
     * @return the color of the player
     */
    public Color getPlayerColor() {
        return (mark == RED) ? Color.RED : Color.YELLOW;
    }


    /**
     * Get the color of the opponent
     * @return the color of the opponent
     */
    public Color getOpponentColor() {
        return (mark == RED) ? Color.YELLOW : Color.RED;
    }


    /** 
     * Get the display name of the player
     * @return the display name
     */
    public String getPlayer() {
        return playerName;
    }


    /**
     * Get the display name of the opponent
     * @return the display name of the opponent
     */
    public String getOpponent() {
        return opponentName;
    }


    public char getOpponentMark() {
        return (mark == RED) ? YELLOW : RED;
    }


    /**
     * Get the color of the mark sent by the server
     * @param fromServer the mark sent by the server
     * @return the color of the mark sent by the server
     */
    public Color getColorFromServer(char fromServer) {
        return (fromServer == RED) ? Color.RED : Color.YELLOW;
    }


    /**
     * The run method of the Runnable interface
     */
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
                setLabel(mark, playerName);
                handleMessage("Welcome, you are " + getPlayer());
            }

            // process messages from server
            while (true) {
                serverInput = in.readLine();
                tokens = serverInput.split("\\s+");

                if (tokens[0].equals("VALID_MOVE")) {
                    

                    handleMove(getPlayer(),
                               Integer.parseInt(tokens[1]),
                               Integer.parseInt(tokens[2]),
                               getPlayerColor());

                } else if (tokens[0].equals("OPPONENT_MOVED")) {

                    handleMove(getOpponent(),
                               Integer.parseInt(tokens[1]),
                               Integer.parseInt(tokens[2]),c      
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
                
                } else if (tokens[0].equals("SET")) {
                    char mark = tokens[1].charAt(0);
                    updateMoveIndicator(getColorFromServer(mark));
                } else if (tokens[0].equals("NAME")) {
                    opponentName = tokens[1];
                    setLabel(getOpponentMark(), getOpponent());
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