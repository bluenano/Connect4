/**
 * Sean Schlaefli
 * Connect4.java
 * Launches the Connect4 application.
 * compiles
 * working/tested
 */


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.lang.Thread;

public class Connect4 extends Application {

    private static final int SIZE = 100;

    public static void main(String [] args) {
        launch(args);
    }


    public Scene createLaunchScene(Button local, Button online, Button quit) {
        VBox box = new VBox();
        box.setPrefWidth(SIZE);
        local.setMinWidth(SIZE);
        online.setMinWidth(SIZE);
        quit.setMinWidth(SIZE);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(30);
        box.getChildren().addAll(local, online, quit);
        return new Scene(box, SIZE*2, SIZE*2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Button local = new Button("Play Local");
        local.setOnMouseClicked(e -> {
            Connect4Logic game = new Connect4Logic();
            Connect4Controller controller = new Connect4LocalController(game);
            Connect4GUI app = new Connect4GUI(controller);
            try {
                app.start(primaryStage);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        });

        Button online = new Button("Play Online");
        online.setOnMouseClicked(e -> {
            String server = "localhost";
            Connect4NetController controller = new Connect4NetController(server);
            Connect4GUI app = new Connect4GUI(controller);
            try {
                app.start(primaryStage);
                Thread thread = new Thread(controller);
                thread.start();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        });

        Button quit = new Button("Quit");
        quit.setOnMouseClicked(e -> System.exit(0));

        primaryStage.setScene(createLaunchScene(local, online, quit));
        primaryStage.setTitle("Connect4");
        primaryStage.show();
    }


}
