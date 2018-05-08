/**
 * Connect4Server.java 
 * A server for a networked Connect4
 * game. The server communicates with
 * clients via strings and adheres to the
 * Connect4 protocol.
 * 
 * default port is 8902
 * you can also specify a port on the command line
 * usage: 
 * java Connect4Server
 * java Connect4Server <port>
 * Connect4 protocol
 *
 * Client -> Server       Server -> Client
 * ----------------       ----------------
 * MOVE <col>             WELCOME <char> (char {R, Y})
 * QUIT                   VALID_MOVE <col> <row>
 * REMATCH                OTHER_PLAYER_MOVED <col> <row> 
 * DISPLAY <name>         VICTORY 
 *                        DEFEAT
 *                        DRAW
 *                        MESSAGE <text>
 *                        NEW_GAME
 *                        SET <char> 
 *                        NAME <string>
 *                        DISCONNECT
 */                        

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Connect4Server {

    private static final int PORT = 8902;
    private static char RED = 'r';
    private static char YELLOW = 'y';


    /**
     * Main function of the server application
     * @param args the command line arguments passed to this program
     */
    public static void main(String[] args) {
        try {
            ServerSocket listener;
            if (args.length == 2) {
                int port = Integer.parseInt(args[1]);
                listener = new ServerSocket(port);
                System.out.println("Listening on " + port);
            } else {
                listener = new ServerSocket(PORT); 
                System.out.println("Listening on " + PORT);
            }
            while (true) {
                Connect4NetGame game = new Connect4NetGame(new Connect4Logic());
                Connect4NetGame.ClientHandler p1 = game.new ClientHandler(listener.accept(), RED);
                Connect4NetGame.ClientHandler p2 = game.new ClientHandler(listener.accept(), YELLOW);
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