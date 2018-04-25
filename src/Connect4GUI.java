/**
 * Connect4GUI.java
 * GUI implementation to represent the Connect4 game
 * compiles
 * working/tested
 */


import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Integer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.application.Platform;

public class Connect4GUI extends Application {

    private static final int TILE_SIZE = 80;
    private static final int FONT_SIZE = 20;
    private static final int BOX_WIDTH = 150;
    private static final int INDICATOR_RADIUS = TILE_SIZE / 3;
    private static final int BUTTON_WIDTH = 120;

    private static final Color RED = Color.RED;
    private static final Color YELLOW = Color.YELLOW;
    private static final Color DRAW = Color.WHITE;
    private static final Color BACKGROUND = Color.WHITE;

    private Stage stage;
    private Scene scene;
    private int columnSize;
    private int rowSize;
    private Connect4Controller controller;
    private BorderPane layout;
    private StackPane moveLog;
    private HBox options;
    private Group gridRoot;
    private ArrayList<Rectangle> columns;
    private Circle redIndicator;
    private Circle yellowIndicator;
    private Button play;
    private Button quit;


    public Connect4GUI(Connect4Controller controller) {
        this.controller = controller;
        controller.attachView(this);
        columnSize = controller.getColumns();
        rowSize = controller.getRows();
        scene = null;
    }  


    public void createGameUI() {
        initializeGridElements();
        setupMoveIndicator();
        setupGrid();
        setupLayout();
        if (scene != null) {
            scene = new Scene(layout, scene.getWidth(), scene.getHeight());
        } else {
            scene = new Scene(layout, 2 * BOX_WIDTH + TILE_SIZE * columnSize, 750);
        }
    }


    public void setUIScene() {
        createGameUI();
        stage.setScene(scene);
        stage.setMaximized(true);
    }


    public void close() {
        Platform.exit();
    }

    private void initializeGridElements() {
        layout = new BorderPane();
        gridRoot = new Group();
        columns = new ArrayList<Rectangle>();
    }


    private void setupMoveIndicator() {
        redIndicator = createIndicator();
        yellowIndicator = createIndicator();
        setMoveIndicatorFill(controller.getPlayerColor());
    }


    private Circle createIndicator() {
        return new Circle(INDICATOR_RADIUS);
    }


    private void setupGrid() {
        gridRoot.prefWidth(TILE_SIZE * columnSize);
        gridRoot.prefHeight(TILE_SIZE * rowSize);
        gridRoot.getChildren().add(createGrid());
        createColumns();
        gridRoot.getChildren().addAll(columns);
    }


    private void setupLayout() {
        moveLog = createBackground();
        options = createOptions();
        addPlayerBoxes();
        layout.setCenter(gridRoot);
        layout.setTop(moveLog);
        layout.setBottom(options);
    }


    private void addPlayerBoxes() {
        Pane red = createPlayerBox("Player Red", RED);
        Pane yellow = createPlayerBox("Player Yellow", YELLOW);
        red.getChildren().add(redIndicator);
        yellow.getChildren().add(yellowIndicator);
        layout.setLeft(red);
        layout.setRight(yellow);
    }


    private StackPane createBackground() {
        StackPane background = new StackPane();
        setupPane(background, BOX_WIDTH, TILE_SIZE * columnSize);
        background.setAlignment(Pos.CENTER);
        return background;
    }


    private HBox createOptions() {
        HBox hbox = new HBox();
        setupPane(hbox, BOX_WIDTH, TILE_SIZE * columnSize);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(FONT_SIZE);
        setupButtons(hbox);
        disablePlayAgain();
        return hbox;
    }


    private void setupButtons(HBox box) {
        play = createButton("Play again");
        quit = createButton("Quit");
        play.setOnMouseClicked(e -> {
                controller.resetGame();
        });
        quit.setOnMouseClicked(e -> System.exit(0));
        box.getChildren().addAll(play, quit);
    }


    private Button createButton(String display) {
        Button button = new Button(display);
        button.setPrefWidth(BUTTON_WIDTH);
        return button;
    }


    public void enablePlayAgain() {
        play.setDisable(false);

    }


    public void disablePlayAgain() {
        play.setDisable(true);
    }


    private void setupPane(Pane pane, int height, int width) {
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
        //BackgroundFill color = new BackgroundFill(BACKGROUND, null, null);
        //pane.setBackground(new Background(color));
    }


    private Shape createGrid() {
        Shape grid = new Rectangle(columnSize * TILE_SIZE, rowSize * TILE_SIZE);
        for (int x = 0; x < columnSize; x++) {
            for (int y = 0; y < rowSize; y++) {
                grid = Shape.subtract(grid, createGridCircle(x,y, Color.WHITE));
            }
        }
        grid.setFill(Color.BLUE);
        grid.setEffect(createLighting(createLight()));
        return grid;
    }


    private void createColumns() {
        for (int x = 0; x < columnSize; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, rowSize * TILE_SIZE);
            rect.setX(x * TILE_SIZE);
            rect.setFill(Color.TRANSPARENT);
            initializeColumnAction(rect, x);
            columns.add(rect);
        }
    }


    private Circle createGridCircle(int x, int y, Color color) {
        Circle circle = new Circle(TILE_SIZE / 3);
        circle.setCenterX(x * TILE_SIZE + TILE_SIZE / 2);
        circle.setCenterY(y * TILE_SIZE + TILE_SIZE / 2);
        circle.setFill(color);
        return circle;
    }


    private Light.Distant createLight() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);
        return light;
    }


    private Lighting createLighting(Light.Distant light) {
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        return lighting;
    }


    private void initializeColumnAction(Rectangle rect, int colPosition) {
        rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
        rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
        rect.setOnMouseClicked(e -> controller.handleUserMove(colPosition));
    }


    public void disableColumns() {
        for(int i = 0; i < columns.size(); i++) {
            columns.get(i).setOnMouseClicked(null);
        }
    }


    private void display(Text move) {
        clearDisplay();
        addDisplay(move);
    }


    private void addDisplay(Text display) {
        moveLog.getChildren().add(display);
    }


    private void clearDisplay() {
        moveLog.getChildren().clear();
    }


    public void displayWin(String player, Color color) {
        display(generateDisplay(player + " has won the game.", color));
    }


    public void displayDraw() {
        display(generateDisplay("The game ended in a draw.", DRAW));
    }


    public void displayMessage(String message) {
        display(generateDisplay(message, null));
    }


    public void displayMove(String player, int colPosition, int rowPosition, Color color) {
        String move = generateMoveString(player, colPosition, rowPosition);
        display(generateDisplay(move, color));
    }


    private String generateMoveString(String player, int colPosition, int rowPosition) {
        StringBuilder buildMove = new StringBuilder(player + " moved to column ");
        buildMove.append(Integer.toString(colPosition+1));
        buildMove.append(", row ");
        buildMove.append(Integer.toString(rowPosition+1));
        buildMove.append(".");
        return buildMove.toString();
    }


    public void addDisc(int colPosition, int rowPosition, Color color) {
        Circle insert = new Circle(TILE_SIZE / 3, color);
        insert.setCenterX(TILE_SIZE / 2);
        insert.setCenterY(TILE_SIZE / 2);
        insert.setTranslateX(colPosition * TILE_SIZE);
        gridRoot.getChildren().add(insert);
        playAnimation(insert, rowPosition);
    }


    private void playAnimation(Circle circle, int rowPosition) {
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), circle);
        animation.setToY(rowPosition * TILE_SIZE);
        animation.play();
    }


    private Pane createPlayerBox(String label, Color color) {
        VBox display = new VBox();
        setupDisplayBox(display);
        Label player = new Label(label);
        player.setFont(new Font(FONT_SIZE));
        //player.setFill(color);
        display.getChildren().add(player);
        return display;
    }


    private void setupDisplayBox(VBox display) {
        display.setPrefWidth(BOX_WIDTH);
        display.setPrefHeight(TILE_SIZE * rowSize);
        display.setAlignment(Pos.BASELINE_CENTER);
        display.setSpacing(BOX_WIDTH / 4);
        //BackgroundFill fill = new BackgroundFill(BACKGROUND, null, null);
        //display.setBackground(new Background(fill));
    }


    private Text generateDisplay(String outcome, Color color) {
        Text result = new Text();
        result.setText(outcome);
        //result.setFill(color);
        result.setFont(new Font(FONT_SIZE));
        return result;
    }


    public void switchTurns(Color player) {
        setMoveIndicatorFill(player);
    }


    public void setMoveIndicatorFill(Color player) {
        if (player == RED) {
            redIndicator.setFill(RED);
            yellowIndicator.setFill(null);
        } else {
            redIndicator.setFill(null);
            yellowIndicator.setFill(YELLOW);
        }
    }


    public void disableMoveIndicator() {
        redIndicator.setFill(BACKGROUND);
        yellowIndicator.setFill(BACKGROUND);
    }


    public void startGame(Stage primaryStage) {
        stage = primaryStage;
        setUIScene();
        stage.setTitle("Connect4");
        stage.show();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        startGame(primaryStage);
    }


}
