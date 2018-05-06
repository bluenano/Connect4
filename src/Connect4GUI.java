/**
 * Connect4GUI.java
 * GUI object to represent the Connect 4 game
 * compiles
 * working/tested
 */


import java.util.ArrayList;
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
    private Label redLabel;
    private Label yellowLabel;
    


    /**
     * Constructor for Connect4GUI
     * @param controller the controller object that handles communication
     * between the GUI and the game logic
     */
    public Connect4GUI(Connect4Controller controller) {
        this.controller = controller;
        controller.attachView(this);
        columnSize = controller.getColumns();
        rowSize = controller.getRows();
        scene = null;
    }  


    /**
     * Creates the game user interface
     */
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


    /**
     * Create the game user interface and set the scene
     */
    public void setUIScene() {
        createGameUI();
        stage.setScene(scene);
        stage.setMaximized(true);
    }


    /**
     * Close the application
     */
    public void close() {
        Platform.exit();
    }


    /**
     * Initialize the objects used to create the game board
     */
    private void initializeGridElements() {
        layout = new BorderPane();
        gridRoot = new Group();
        columns = new ArrayList<Rectangle>();
    }


    /**
     * Create the move indicators used to show whose turn it is
     */
    private void setupMoveIndicator() {
        redIndicator = new Circle(INDICATOR_RADIUS);
        yellowIndicator = new Circle(INDICATOR_RADIUS);
        setMoveIndicatorFill(controller.getPlayerColor());
    }


    /**
     * Setup the grid object used to create the game board
     */
    private void setupGrid() {
        gridRoot.prefWidth(TILE_SIZE * columnSize);
        gridRoot.prefHeight(TILE_SIZE * rowSize);
        gridRoot.getChildren().add(createGrid());
        createColumns();
        gridRoot.getChildren().addAll(columns);
    }


    /**
     * Setup the layout object of the game user interface
     */
    private void setupLayout() {
        moveLog = createBackground();
        options = createOptions();
        redLabel = new Label("Player Red");
        yellowLabel = new Label("Player Yellow");
        addPlayerBoxes();
        layout.setCenter(gridRoot);
        layout.setTop(moveLog);
        layout.setBottom(options);
    }


    /**
     * Set the red yellow
     * @param text the new label text
     */
    public void setRedLabel(String text) {
        redLabel.setText(text);
    }


    /**
     * Set the yellow label
     * @param text the new label text
     */
    public void setYellowLabel(String text) {
        yellowLabel.setText(text);
    }


    /**
     * Add player boxes to the layout
     * @param red the player who is red
     * @param yellow the player who is yellow
     */
    private void addPlayerBoxes() {
        Pane redPane = createPlayerBox(redLabel, RED);
        Pane yellowPane = createPlayerBox(yellowLabel, YELLOW);
        redPane.getChildren().add(redIndicator);
        yellowPane.getChildren().add(yellowIndicator);
        layout.setLeft(redPane);
        layout.setRight(yellowPane);
    }


    /**
     * Create the background of the game board
     * @return the background object
     */
    private StackPane createBackground() {
        StackPane background = new StackPane();
        setupPane(background, BOX_WIDTH, TILE_SIZE * columnSize);
        background.setAlignment(Pos.CENTER);
        return background;
    }


    /**
     * Create the pane that holds the option buttons
     * @return the options object
     */
    private HBox createOptions() {
        HBox hbox = new HBox();
        setupPane(hbox, BOX_WIDTH, TILE_SIZE * columnSize);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(FONT_SIZE);
        setupButtons(hbox);
        //disablePlayAgain();
        togglePlayAgain(true);
        return hbox;
    }


    /**
     * Setup the buttons in the options pane
     * @param box the pane object used to hold the options
     */
    private void setupButtons(HBox box) {
        play = createButton("Play again");
        quit = createButton("Quit");
        play.setOnMouseClicked(e -> {
            controller.resetGame();
        });
        quit.setOnMouseClicked(e -> System.exit(0));
        box.getChildren().addAll(play, quit);
    }


    /**
     * Create a button used in the GUI
     * @param display the text of the button
     * @return a button used in the GUI
     */
    private Button createButton(String display) {
        Button button = new Button(display);
        button.setPrefWidth(BUTTON_WIDTH);
        return button;
    }


    /**
     * Toggle the play again button
     * @param disable the boolean value that determines
     * whether to show the button or not
     */
    public void togglePlayAgain(boolean disable) {
        play.setDisable(disable);
    }


    /**
     * Setup a pane object used in the GUI
     * @param pane the pane to setup
     * @param height the preferred height of the pane
     * @param width the preferred width of the pane
     */
    private void setupPane(Pane pane, int height, int width) {
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
    }


    /**
     * Create the grid object used to represent the game board
     * @return the grid object that represents the game board
     */
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


    /**
     * Create the columns used as elements of the game board
     */
    private void createColumns() {
        for (int x = 0; x < columnSize; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, rowSize * TILE_SIZE);
            rect.setX(x * TILE_SIZE);
            rect.setFill(Color.TRANSPARENT);
            initializeColumnAction(rect, x);
            columns.add(rect);
        }
    }


    /**
     * Create a grid circle used as a piece in the game board
     * @param x the x position in the game board
     * @param y the y position in the game board
     * @param color the color of the piece
     * @return a piece used in the game board
     */
    private Circle createGridCircle(int x, int y, Color color) {
        Circle circle = new Circle(TILE_SIZE / 3);
        circle.setCenterX(x * TILE_SIZE + TILE_SIZE / 2);
        circle.setCenterY(y * TILE_SIZE + TILE_SIZE / 2);
        circle.setFill(color);
        return circle;
    }


    /**
     * Create the light used to light the game board
     * @return Light.Distant the light object 
     */
    private Light.Distant createLight() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);
        return light;
    }


    /**
     * Create the lighting used to show the user what column they are selecting
     * @param light the light object
     * @return the lighting object
     */
    private Lighting createLighting(Light.Distant light) {
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        return lighting;
    }


    /**
     * Initialize the actions of a column in the game board
     * @param rect the column
     * @param col the position in the columns 
     */
    private void initializeColumnAction(Rectangle rect, int col) {
        rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
        rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
        rect.setOnMouseClicked(e -> controller.handleUserMove(col));
    }


    /**
     * Disable the actions of the columns
     */
    public void disableColumns() {
        for(int i = 0; i < columns.size(); i++) {
            columns.get(i).setOnMouseClicked(null);
        }
    }


    /**
     * Display a Text object in the GUI
     * @param text the text to display
     */
    private void display(Text text) {
        clearDisplay();
        addDisplay(text);
    }


    /**
     * Add a Text object to the display pane
     * @param display the Text object to display
     */
    private void addDisplay(Text display) {
        moveLog.getChildren().add(display);
    }


    /**
     * Clear the display pane
     */
    private void clearDisplay() {
        moveLog.getChildren().clear();
    }


    /**
     * Display a message in the GUI
     * @param message the string to display
     */
    public void displayMessage(String message) {
        display(generateDisplay(message));
    }


    /**
     * Add a piece to the game board
     * @param col the position in the columns to add a piece to
     * @param row the position in the rows to add a piece to
     * @param color the color of the piece to add to the game board
     */
    public void addDisc(int col, int row, Color color) {
        Circle insert = new Circle(TILE_SIZE / 3, color);
        insert.setCenterX(TILE_SIZE / 2);
        insert.setCenterY(TILE_SIZE / 2);
        insert.setTranslateX(col * TILE_SIZE);
        gridRoot.getChildren().add(insert);
        playAnimation(insert, row);
    }


    /**
     * Play the animation that shows a piece being placed on the
     * game board
     * @param circle the piece being placed
     * @param rowPosition the row position to place the piece
     */
    private void playAnimation(Circle circle, int row) {
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), circle);
        animation.setToY(row * TILE_SIZE);
        animation.play();
    }


    /**
     * Create a player box used in the GUI
     * @param player the name of the player
     * @return the pane object used to display a player box
     */
    private Pane createPlayerBox(Label label, Color color) {
        VBox display = new VBox();
        setupDisplayBox(display);
        label.setFont(new Font(FONT_SIZE));
        display.getChildren().add(label);
        return display;
    }


    /**
     * Setup the pane that displays messages in the GUI
     * @param display the pane used to display messages
     */
    private void setupDisplayBox(VBox display) {
        display.setPrefWidth(BOX_WIDTH);
        display.setPrefHeight(TILE_SIZE * rowSize);
        display.setAlignment(Pos.BASELINE_CENTER);
        display.setSpacing(BOX_WIDTH / 4);
    }


    /**
     * Generate a Text object to display in the GUI
     * @param display the string to display
     * @return the Text object to display
     */
    private Text generateDisplay(String display) {
        Text result = new Text();
        result.setText(display);
        result.setFont(new Font(FONT_SIZE));
        return result;
    }


    /**
     * Set the move indicator to indicate whose turn it is
     * @param player the color of the player whose turn it is
     */
    public void switchTurns(Color player) {
        setMoveIndicatorFill(player);
    }


    /**
     * Set the color of a move indicator
     * @param player the color of the current player
     */
    public void setMoveIndicatorFill(Color player) {
        if (player == RED) {
            redIndicator.setFill(RED);
            yellowIndicator.setFill(null);
        } else {
            redIndicator.setFill(null);
            yellowIndicator.setFill(YELLOW);
        }
    }


    /**
     * Disable the red and yellow move indicators
     */
    public void disableMoveIndicators() {
        redIndicator.setFill(null);
        yellowIndicator.setFill(null);
    }


    /**
     * Start the game by initializing the user interface
     * @param primaryStage the Stage object used to show the GUI
     */
    public void startGame(Stage primaryStage) {
        stage = primaryStage;
        setUIScene();
        stage.setTitle("Connect4");
        stage.show();
    }


    /**
     * Launch the GUI application
     * @param primaryStage the Stage object used to display the GUI
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        startGame(primaryStage);
    }


}
