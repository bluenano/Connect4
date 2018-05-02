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


    public void handleWin() {
        Platform.runLater(new Runnable() {
            public void run() {
                handleGameOver("Victory");
            }
        });
    }


    public void handleDefeat() {
        Platform.runLater(new Runnable() {
            public void run() {
                handleGameOver("Defeat");
            }
        });
    }


    public void handleDraw() {
        Platform.runLater(new Runnable() {
            public void run() {
                handleGameOver("Draw");
            }
        });
    }


    public void handleGameOver(String result) {
        disableUserMoves();
        view.displayMessage(result);
    }


    public void handleUserMove(int column) {
        out.println("MOVE " + column);
    }


    public void resetGame() {

    }

    public void updateMoveIndicator(Color c) {
        Platform.runLater(new Runnable() {
            public void run() {
                view.setMoveIndicatorFill(c);
            }
        });
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
        //view.enablePlayAgain();
        view.disableMoveIndicator();
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
                    
                    handleMove(Integer.parseInt(fromServer.substring(11, 12)),
                               Integer.parseInt(fromServer.substring(13)),
                               getPlayer(),
                               getPlayerColor());
                } else if (fromServer.startsWith("OPPONENT_MOVED")) {

                    handleMove(Integer.parseInt(fromServer.substring(15,16)),
                               Integer.parseInt(fromServer.substring(17)),
                               getOpponent(),
                               getOpponentColor());

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
                    // reset GUI

                } else if (fromServer.startsWith("SET")) {
                    // I'm not sure why this works without using runLater
                    char mark = fromServer.charAt(4);
                    updateMoveIndicator(getColorFromServer(mark));
                } else if (fromServer.startsWith("NAME")) {
                    String opponent = fromServer.substring(5);
                    // send to view for display
                    System.out.println("Name received: " + opponent);
                } else if (fromServer.startsWith("DISCONNECT")) {
                    displayMessage("Opponent disconnected");
                    disableUserMoves();
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