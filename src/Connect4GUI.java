/**
 * Sean Schlaefli
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

public class Connect4GUI extends Application {

    private static final int TILE_SIZE = 80;
    private static final int FONT_SIZE = 20;
    private static final int BOX_WIDTH = 100;
    private static final int INDICATOR_RADIUS = BOX_WIDTH / 5;
    private static final int BUTTON_WIDTH = 120;
    
    private Stage stage;
    private int columnSize;
    private int rowSize;
    private Connect4Controller controller;
    private boolean redMove;
    private BorderPane layout;
    private StackPane moveLog;
    private HBox options;
    private Group gridRoot;
    private ArrayList<Rectangle> columns;
    private Circle redIndicator;
    private Circle yellowIndicator;

        
    public Connect4GUI(Connect4Controller controller){
	this.controller = controller;
	controller.attachView(this);
	columnSize = controller.getColumns();
	rowSize = controller.getRows();
	redMove = controller.getCurrentMove();
    }    
    
    
    public Scene createGameUI() {
	initializeGridElements();
	setupMoveIndicator();
	setupGrid();
	setupLayout();
	return new Scene(layout, 2 * BOX_WIDTH + TILE_SIZE * columnSize, 650);
    }

    
    private void initializeGridElements() {
	layout = new BorderPane();
	gridRoot = new Group();
	columns = new ArrayList<Rectangle>();
    }


    private void setupMoveIndicator() {
	redIndicator = createIndicator();       
	yellowIndicator = createIndicator();
	setIndicatorFill();
    }


    private Circle createIndicator() {
	return new Circle(INDICATOR_RADIUS);
    }

    
    private void setIndicatorFill() {
	if (redMove) {
	    redIndicator.setFill(Color.RED);
	    yellowIndicator.setFill(Color.BLACK);
	} else {
	    redIndicator.setFill(Color.BLACK);
	    yellowIndicator.setFill(Color.YELLOW);
	}
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
	Pane red = createPlayerBox("Player 1", Color.RED);
	Pane yellow = createPlayerBox("Player 2", Color.YELLOW);
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
	disableButtons(hbox);
	return hbox;
    }


    private void setupButtons(HBox box) {
	Button play = createButton("Play again");
	Button quit = createButton("Quit");
	play.setOnMouseClicked(e -> {
		controller.resetGame();
		stage.setScene(createGameUI());
	    });
	quit.setOnMouseClicked(e -> System.exit(0));		
	box.getChildren().addAll(play, quit);
    }
    
    private Button createButton(String display) {
	Button button = new Button(display);
	button.setPrefWidth(BUTTON_WIDTH);
	return button;
    }

    
    private void enableButtons(Pane pane) {
	for (Node node : pane.getChildren()) {
	    node.setDisable(false);
	}
    }

    
    private void disableButtons(Pane pane) {
	for (Node node : pane.getChildren()) {
	    node.setDisable(true);
	}
    }

    
    private void setupPane(Pane pane, int height, int width) {
	pane.setPrefHeight(height);
	pane.setPrefWidth(width);
	BackgroundFill color = new BackgroundFill(Color.BLACK, null, null);
	pane.setBackground(new Background(color));
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
	rect.setOnMouseClicked(e -> handleUserMove(colPosition));
    }


    private void disableColumns() {
	for(int i = 0; i < columns.size(); i++) {
	    columns.get(i).setOnMouseClicked(null);
	}
    }

    
    private void handleUserMove(int colPosition) {
	if (controller.verifyMove(colPosition)) {
	    int rowPosition = controller.makeMove(colPosition);
	    addDisc(colPosition, rowPosition);
	    updateAfterMove(colPosition, rowPosition);
	}
    }


    private void updateAfterMove(int colPosition, int rowPosition) {
	Text display;
	if (controller.isWin()) {
	    disableColumns();
	    enableButtons(options);
	    display = createWinDisplay();
	} else if (controller.isDraw()) {
	    disableColumns();
	    enableButtons(options);
	    display = createDrawDisplay();
	} else {
	    display = createMoveDisplay(colPosition, rowPosition);
	    updateCurrentMove();
	}
	displayMove(display);
    }


    private void updateCurrentMove() {
	switchTurns();
	controller.switchTurns();
	setIndicatorFill();	
    }

    
    private void displayMove(Text display) {
	removeDisplay();
	addDisplay(display);
    }


    private void addDisplay(Text display) {
	moveLog.getChildren().add(display);
    }

    
    private void removeDisplay() {
	moveLog.getChildren().clear();
    }

    
    private Text createWinDisplay() {
	if (redMove) {
	    return generateDisplay("Player 1 won the game.", Color.RED);
	} else {
	    return generateDisplay("Player 2 won the game.", Color.YELLOW);
	}
    }


    private Text createDrawDisplay() {
	return generateDisplay("The game ended in a draw.", Color.WHITE);
    }

    
    private Text createMoveDisplay(int colPosition, int rowPosition) {
	String move = generateMoveString(colPosition, rowPosition);
	return generateDisplay(move, getColor());
    }


    private String generateMoveString(int colPosition, int rowPosition) {
	StringBuilder buildMove = new StringBuilder(getPlayer() + " moved to column ");
	buildMove.append(Integer.toString(colPosition+1));
	buildMove.append(", row ");
	buildMove.append(Integer.toString(rowPosition+1));
	buildMove.append(".");
	return buildMove.toString();
    }

    
    private void addDisc(int colPosition, int rowPosition) {
	Circle insert = new Circle(TILE_SIZE / 3, getColor());
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

    
    private Pane createPlayerBox(String label, Color color){
	VBox display = new VBox();
	display.setPrefWidth(BOX_WIDTH);
	display.setPrefHeight(TILE_SIZE * rowSize);
	display.setAlignment(Pos.BASELINE_CENTER);
	display.setSpacing(BOX_WIDTH / 4);
	Text player = new Text(label);
	player.setFont(new Font(FONT_SIZE));
	player.setFill(color);
	BackgroundFill fill = new BackgroundFill(Color.BLACK, null, null);
	display.setBackground(new Background(fill));
	display.getChildren().add(player);
	return display;	    
    }

    
    private Text generateDisplay(String outcome, Color color){
	Text result = new Text();
	result.setText(outcome);
	result.setFill(color);
	result.setFont(new Font(FONT_SIZE));
	return result;	    
    }
    

    public void switchTurns() {
	redMove = (redMove) ? false : true;
    }


    public Color getColor() {
	return (redMove) ? Color.RED : Color.YELLOW;
    }


    public String getPlayer() {
	return (redMove) ? "Player 1" : "Player 2";
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
	stage = primaryStage;
	stage.setScene(createGameUI());
      	stage.setTitle("Connect4");
	stage.show();
    }
    
}
