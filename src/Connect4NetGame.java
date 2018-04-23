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
    //private char currentMark; 
    // use game.getCurrentMove() to get the currentMark

    public Connect4NetGame(Connect4Logic game) {
        this.game = game;
    }


    public synchronized boolean isValidMove(char mark, int column) {
        return mark == game.getCurrentMove() 
               &&
               game.verifyMove(column);
    }


    public class ClientHandler extends Thread {

        private char mark;
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;
        private ClientHandler opponent;

        public ClientHandler(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("MESSAGE Waiting for other player to connect");
                out.println("WELCOME " + mark);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        public void setOpponent(ClientHandler opponent) {
            this.opponent = opponent;
        }


        // handle the other player move message
        public void opponentMoved(int column, int row) {
            out.println("OPPONENT_MOVED" + column + " " + row);
            System.out.println("OPPONENT_MOVED");
            // check if opponent wins or if there is a draw
            String result = game.isWin() ? "DEFEAT" : game.isDraw() ? "DRAW" : "";
            out.println(result);
        }


        public void run() {
            try {
                out.println("MESSAGE Players have connected, the game will begin now");

                if (mark == game.getCurrentMove()) {
                    out.println("MESSAGE It it your turn");
                }

                // handle client requests
                while (true) {
                    String clientMessage = in.readLine();

                    if (clientMessage.startsWith("MOVE")) {
                        // HANDLE MOVE
                        int column = Integer.parseInt(clientMessage.substring(5));
                        synchronized(this) {
                            if (isValidMove(mark, column)) {
                                // this code may contain a race condition
                                // and lead to inconsistencies in game state
                                int row = game.makeMove(column);
                                game.switchTurns();
                                out.println("VALID_MOVE " + column + " " + row);
                                opponent.opponentMoved(column, row);
                            }
                        }
                    } else if (clientMessage.startsWith("QUIT")) {
                        // thread is no longer needed
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client lost connection");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

    }

}