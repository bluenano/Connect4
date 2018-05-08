/**
 * Connect4NetworkGame.java
 * ClientHandler inner class that is threaded
*/

import java.lang.Thread;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Connect4NetGame {

    private Connect4Logic game;

    /**
     * Constructor for the Connect4NetGame object
     * @param game the Connect4Logic object used in this game
     */
    public Connect4NetGame(Connect4Logic game) {
        this.game = game;
    }

    
    /**
     * Check if a move is valid
     * @param mark the mark of the player requesting a move
     * @param col the column position to move to
     * @return true if the move is valid, false otherwise
     */
    //public synchronized boolean isValidMove(char mark, int col) {
    public boolean isValidMove(char mark, int col) {
        return mark == game.getCurrentMove() 
               &&
               game.verifyMove(col);
    }
    


    /**
     * Inner class that is threaded
     * An inner class makes sense here because it needs access to the
     * instance variables of Connect4NetGame
     */
    public class ClientHandler extends Thread {

        private char mark;
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;
        private ClientHandler opponent;
		    private int rematch_counter = 1;


        /** 
         * Constructor for the ClientHandler object
         * @param socket the Socket object to communicate with
         * @param mark the player mark assigned to this client
         */
        public ClientHandler(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("WELCOME " + mark);
                out.println("MESSAGE Waiting for other player to connect");
                updateClientIndicator();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }


        /**
         * Set the thread of the opponent
         * @param opponent the opponent thread
         */
        public void setOpponent(ClientHandler opponent) {
            this.opponent = opponent;
        }


        /**
         * Send a connection loss message to the client
         * Occurs if the opponent of this client loses connection
         */

        public void connectionLoss() {
            out.println("DISCONNECT");
        }


        /**
         * Send a message to the client to indicate what color
         * to set the move indicator
         */
        public void updateClientIndicator() {
            out.println("SET " + game.getCurrentMove());
        }


        /** 
         * Send a message to the client that indicates the opponent
         * made a move
         * @param col the column position that the opponent moved to
         * @param row the row position that the opponent moved to
         */
        public void opponentMoved(int col, int row) {
            out.println("OPPONENT_MOVED" + " " + col + " " + row);
            String result = game.isWin() ? "DEFEAT" : game.isDraw() ? "DRAW" : "";
            out.println(result);
        }

		// sends a rematch message to the opponent
        public void sendRematch() {
            out.println("MESSAGE Your opponent wants a rematch. Press 'play again' to accept");
            // String result = game.isWin() ? "DEFEAT" : game.isDraw() ? "DRAW" : "";
            // out.println(result);
        }

        /**
         * Send a message to the client to indicate the opponents display name
         * @param name the name of the opponent
         */ 
        public void setOpponentName(String name) {
            out.println("NAME " + name);
        }

		public void resetOpponent(){
			out.println("NEW_GAME " + game.getCurrentMove());
		}


        /** 
         * The Thread run method
         */
        public void run() {
            try {
                out.println("MESSAGE Players have connected, the game will begin now");
                if (mark == game.getCurrentMove()) {
                    out.println("MESSAGE It it your turn");
                }

                while (true) {
                    String clientMessage = in.readLine();
                    
                    if (clientMessage == null) {
                        // lost connection to this client
                        opponent.connectionLoss();
                        return;
                    }

                    if (clientMessage.startsWith("MOVE")) {
                        int column = Integer.parseInt(clientMessage.substring(5));
                        synchronized(this) {
                            if (isValidMove(mark, column)) {
                                int row = game.makeMove(column);
                                out.println("VALID_MOVE " + column + " " + row);
                                opponent.opponentMoved(column, row);
                                if (game.isWin()) {
                                    out.println("VICTORY");
                                } else if (game.isDraw()) {
                                    out.println("DRAW");
                                } else {
                                    game.switchTurns();
                                    updateClientIndicator();
                                    opponent.updateClientIndicator();
                                }

                            }
                        }
                    } else if (clientMessage.startsWith("QUIT")) {
                        return;
                    } else if (clientMessage.startsWith("REMATCH_PLS")) {
						synchronized(this) {
							game.incRematch();
							if (game.getRematchCount() == 1){								
								opponent.sendRematch();
							}
							else if(game.getRematchCount() == 2){
								game.resetRematch();
								// if both clients have sent a REMATCH_PLS request to server, then server tells both clients to reset
								game.reset();
								out.println("NEW_GAME");
								opponent.resetOpponent();
							}
                            
                        }
                    } else if (clientMessage.startsWith("DISPLAY")) {
                        String name = clientMessage.substring(8);
                        opponent.setOpponentName(name);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

    }

}