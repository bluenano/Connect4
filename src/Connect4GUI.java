/**
 * Sean Schlaefli
 * Connect4GUI.java
 * GUI implementation to represent the Connect4 game
 * compiles
 * working/tested
 */


import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.geometry.Pos;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private static final int BOX_WIDTH = 100;
    private Stage stage;
    private int columnSize;
    private int rowSize;
    private Connect4Controller controller;
    private boolean redMove;
    private BorderPane layout;
    private Group gridRoot;
    private ArrayList<Rectangle> columns;
    
    
    public Connect4GUI(Connect4Controller controller){
	this.controller = controller;
	controller.attachView(this);
	columnSize = controller.getColumns();
	rowSize = controller.getRows();
	redMove = controller.getCurrentMove();
    }    
    
    
    public Scene createGameUI() {
	initializeElements();
	setupGrid();
	setupLayout();
	return new Scene(layout, 2 * BOX_WIDTH + TILE_SIZE * columnSize, 650);
    }

    
    private void initializeElements() {
	layout = new BorderPane();
	gridRoot = new Group();
	columns = new ArrayList<Rectangle>();
    }

    private void setupGrid() {
	gridRoot.prefWidth(TILE_SIZE * columnSize);
	gridRoot.prefHeight(TILE_SIZE * rowSize);
	gridRoot.getChildren().add(createGrid());
	createColumns();
	gridRoot.getChildren().addAll(columns);
    }

    
    private void setupLayout() {
	layout.setCenter(gridRoot);
	layout.setLeft(createPlayerBox("Player 1", Color.RED));
	layout.setRight(createPlayerBox("Player 2", Color.YELLOW));
    }
    
    
    private Shape createGrid() {
	Shape grid = new Rectangle(columnSize * TILE_SIZE, rowSize * TILE_SIZE);
	for (int x = 0; x < columnSize; x++) {
	    for (int y = 0; y < rowSize; y++) {
		grid = Shape.subtract(grid, createCircle(x,y, Color.WHITE));
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

    
    private Circle createCircle(int x, int y, Color color) {
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


    private void handleUserMove(int colPosition) {
	if (controller.verifyMove(colPosition)) {
	    int rowPosition = controller.makeMove(colPosition);
	    addDisc(colPosition, rowPosition);
	    if (controller.isGameOver()) {
		controller.resetGame();
		stage.setScene(createGameUI());
	    } else {
		switchTurns();
		controller.switchTurns();
	    }
	}
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
	Text player = new Text(label);
	player.setFont(new Font(20));
	player.setFill(color);
	BackgroundFill fill = new BackgroundFill(Color.BLACK, null, null);
	display.setBackground(new Background(fill));
	display.getChildren().add(player);
	return display;	    
    }

    
    private Text generateResult(String outcome, Color color){
	Text result = new Text();
	result.setText(outcome);
	result.setFill(color);
	result.setFont(new Font(20));
	return result;	    
    }
    

    /*
    private void gameOver(boolean result) {
	final Stage gameOver = new Stage();
	// blocks all events to other windows
	gameOver.initModality(Modality.APPLICATION_MODAL);
	Text display = new Text();

	if(result) {
	    if(redMove) {
		display = generateResult("Red won the game.", Color.RED);
	    } else {
		display = generateResult("Yellow won the game.", Color.YELLOW);
	    }
	} else {
	    display = generateResult("The game ended in a draw.", Color.WHITE);
	}
	
	VBox dialog = new VBox();
	dialog.setAlignment(Pos.CENTER);
	dialog.getChildren().add(display);
	BackgroundFill fill = new BackgroundFill(Color.BLACK, null, null);
	dialog.setBackground(new Background(fill));
	Scene dialogScene = new Scene(dialog, 300, 200);
	gameOver.setScene(dialogScene);
	gameOver.setOnCloseRequest(e -> System.exit(0));
	gameOver.show();	
    }
    */
    

    public void switchTurns() {
	redMove = (redMove) ? false : true;
    }


    private Color getColor() {
	return (redMove) ? Color.RED : Color.YELLOW;
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
	stage = primaryStage;
	stage.setScene(createGameUI());
      	stage.setTitle("Connect4");
	stage.show();
    }
    
}
