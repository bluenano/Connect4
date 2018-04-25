// package statement

/**
 * Connect4Server.java 
 * A server for a networked Connect4
 * game. The server communicates with
 * clients via strings and adheres to the
 * Connect4 protocol.
 * 
 * Connect4 protocol
 *
 * Client -> Server       Server -> Client
 * ----------------       ----------------
 * MOVE <col>             WELCOME <char> (char {R, Y})
 * QUIT                   VALID_MOVE <col> <row>
 * REMATCH                OTHER_PLAYER_MOVED <col> <row> 
 *                        VICTORY 
 *                        DEFEAT
 *                        DRAW
 *                        MESSAGE <text>
 *                        NEW_GAME
 *                        SET <char> 
*/                        

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Connect4Server {

    private static int PORT = 8902;
    private static char RED = 'r';
    private static char YELLOW = 'y';


    public static void main(String[] args) {
        try {
            ServerSocket listener = new ServerSocket(PORT); 
            while (true) {
                Connect4NetGame game = new Connect4NetGame(new Connect4Logic());
                Connect4NetGame.ClientHandler p1 = game.new ClientHandler(listener.accept(), RED, "Thread1");
                Connect4NetGame.ClientHandler p2 = game.new ClientHandler(listener.accept(), YELLOW, "Thread2");
                p1.setOpponent(p2);
                p2.setOpponent(p1);
                p1.start();
                p2.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }
}