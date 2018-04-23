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


public class Connect4NetController extends Connect4Controller implements Runnable {
    
    private static int PORT = 8902;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private char mark;

    // have access to GUI named view

    public Connect4NetController(String serverAddress) {
        try {
            socket = new Socket(serverAddress, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            // close the program or maybe try to load the menu again
        } catch (IOException e) {

        }
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


    public void handleWin() {
        Platform.runLater(new Runnable() {
            public void run() {

            }
        });
    }

    public void handleDefeat() {
        Platform.runLater(new Runnable() {
            public void run() {

            }
        });
    }

    public void handleDraw() {
        Platform.runLater(new Runnable() {
            public void run() {

            }
        });
    }


    public void handleUserMove(int column) {
        out.println("MOVE " + column);
        System.out.println("MOVE " + column);
    }


    public void resetGame() {

    }

    public void updateMoveIndicator() {

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


    @Override
    public void run() {
        // first message will be WELCOME <char>
        String fromServer;
        try {
            // you need to use Platform.runLater whenever you need to update the
            // GUI from a non-gui thread 

            fromServer = in.readLine();
            if (fromServer.startsWith("WELCOME")) {
                mark = fromServer.charAt(8);
                handleMessage("Welcome, you are " + getPlayer());
            }

            // process messages from server
            while (true) {
                fromServer = in.readLine();
                if (fromServer.startsWith("VALID_MOVE")) {
                    // do something
                    int column = Integer.parseInt(fromServer.substring(11, 12));
                    int row = Integer.parseInt(fromServer.substring(13));
                    handleMove(column, row, getPlayer(), getPlayerColor());
                } else if (fromServer.startsWith("OPPONENT_MOVED")) {
                    int column = Integer.parseInt(fromServer.substring(15, 16));
                    int row = Integer.parseInt(fromServer.substring(17));
                    String opponent = getOpponent();
                    Color c = getOpponentColor();
                    System.out.println("Opponent moved to" + column + " " + row);
                    handleMove(column, row, opponent, c);
                } else if (fromServer.startsWith("VICTORY")) {
                    // display victory and lock controls
                    handleWin();
                } else if (fromServer.startsWith("DEFEAT")) {
                    // display defeat and lock controls
                    handleDefeat();
                } else if (fromServer.startsWith("DRAW")) {
                    // display a tie and lock controls
                    handleDraw();
                } else if (fromServer.startsWith("MESSAGE")) {
                    // display message in GUI
                    String message = fromServer.substring(8);
                    handleMessage(message);
                } else if (fromServer.startsWith("NEW_GAME")) {
                    // clear display

                }
            }
        } catch (Exception e) {

        }
    }
}