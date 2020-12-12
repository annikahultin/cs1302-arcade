package cs1302.arcade;

import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import java.util.List;
import java.util.LinkedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class ArcadeApp extends Application {

    Group group = new Group();           // main container
    Random rng = new Random();           // random number generator
    Rectangle r = new Rectangle(20, 20); // some rectangle
    Scene titleScene; // the scene for the title screen
    Scene gameScene; // the scene for the game board
    Scene winScene; // the scene that displays the win message
    Stage stage;
    int whiteScore = 2; //keeps track of the white score
    int blackScore = 2; //keeps track of the black score
    TilePane gameBoard; // the game board doling all the image views
    TileSquare[][] tiles; // holds all of the image views
    Image green = new Image("file:resources/green.jpg", 75, 75, true, false);
    Image black = new Image("file:resources/black.jpg", 75, 75, true, false);
    Image white = new Image("file:resources/white.jpg", 75, 75, true, false);
    Image whiteWin = new Image("file:resources/whitewin.jpg", 500, 500, true, false);
    Image blackWin = new Image("file:resources/blackwin.jpg", 500, 500, true, false);
    VBox gameVBox = new VBox(); // holds all of the contents of the game board
    String[][] board; // keeps track of the status of each tile in the game board
    boolean whiteTurn = false; //keeps track of whose turn it is
    int x; // x location of the tile clicked
    int y; // y locatin of the tile clicked
    // arrays that hold that hold the values that are added to the x and y values in a direction
    int[] xDirection = {-1,1,0,0,1,1,-1,-1}; //left, right, up, down, right up diag
    int[] yDirection = {0,0,1,-1,1,-1,1,-1}; // right down diag, left up diag, left down diag
    List<Integer> validFlipDir; // contains the indices of the two arrays above that are valid flips
    Label score; //displays the score in the game scene
    String currentTurn = "Black";

    /**
     * Determines if there is a valid flip in the specified direction.
     * @param xVal  the x location of the tile placed in the board.
     * @param yVal  the y location of the tile placed on the board
     * @param d     the direction to check if there is a valid flip
     * @return true if the flip is valid; false otherwise
     */
    private boolean validFlip(int xVal, int yVal, int d) {
        String oppColor; // keeps track of the opposite color
        String currentColor; // keeps track of the current color
        int potentialFlips = 0; // keeps track of how many tiles could potentially flip
        boolean valid = false; // wether or not there is a valid flip
        if (whiteTurn) {
            currentColor = "W";
            oppColor = "B";
        } else {
            currentColor = "B";
            oppColor = "W";
        } //if
        for (int i = 0; i < board.length; i++) {
            xVal += xDirection[d]; // shifts the y value of the tile in the specified direction
            yVal += yDirection[d]; // shifts the y value of the tile in the specified direction
            if (xVal < 8 && xVal > -1 && yVal < 8 && yVal > -1) { // makes sure indices are in bound
                if (board[xVal][yVal].equals(oppColor)) { // checks if next tile is opposite color
                    potentialFlips ++; //number of potential flips increases
                } else if (board[xVal][yVal].equals(currentColor)) { //make sure reach actual color
                    if (potentialFlips > 0) { //makes sure there are tiles of opp color between
                        valid = true;
                        potentialFlips = 0;
                    } //if
                } //if
            } //if
        } //for
        return valid;
    } //validFlip

    /**
     * Determines wether a move is valid.
     * @param xVal  the x value for the square to check
     * @param yVal  the y value for the square to check
     * @return true if the move is valid; false otherwise
     */
    private boolean validMove(int xVal, int yVal) {
        validFlipDir = new LinkedList<Integer>();
        boolean valid = false;
        if (board[xVal][yVal].equals("")) { // makes sure tile is place in an emtpy square
            for (int i = 0; i < xDirection.length; i++) {
                if (validFlip(xVal, yVal, i)) { //checks if there is a valid flip in all directions
                    validFlipDir.add(i); //adds the direction to the list if the flip is valid
                    valid = true;
                } //if
            } //for
        } //if
        return valid;
    } //validMove

    /**
     * Handles flipping the tiles on each player's turn.
     * @param d  the direction in which to flip the tiles.
     */
    private void flipTiles(int d) {
        String oppColor;
        if (whiteTurn) {
            oppColor = "B";
        } else {
            oppColor = "W";
        } //if
        int tileX = x + xDirection[d]; //shift the x value in specified direction
        int tileY = y + yDirection[d]; //shifts the y value in the specified direction
        // changes the image in the imageview while the square is still the opposite color
        while (board[tileX][tileY].equals(oppColor)) {
            if (oppColor.equals("W")) {
                tiles[tileX][tileY].updateImage(black); //changes image to the opp color image
                board[tileX][tileY] = "B"; //updates the game board
                blackScore ++;
                whiteScore --;
                tileX += xDirection[d]; //shifts the x value in the specified direction again
                tileY += yDirection[d]; //shifts the y value in the specified direction again
            } else {
                tiles[tileX][tileY].updateImage(white); //changes image to the opp color image
                board[tileX][tileY] = "W"; //updates the game board
                whiteScore ++;
                blackScore --;
                tileX += xDirection[d]; //shifts the x value in the specified direction again
                tileY += yDirection[d]; //shifts the y value in the specified direction again
            } //if
        } //while
    } //flipTiles

    /**
     * Displays error message when player tries to make an invalid move.
     */
    private void displayError() {
        Alert error = new Alert(AlertType.ERROR);
        error.setResizable(true);
        error.setContentText("Invalid move: Must be able to flip a tile");
        error.showAndWait(); //displays the alert with the error message to the user
    } //if

    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            TileSquare tileClicked = (TileSquare) event.getSource(); //grabs the TileSquare clicked
            x = tileClicked.xValue(); //gets the x value of the square
            y = tileClicked.yValue(); //gets the y value of the square
            if (validMove(x, y)) { //checks if the square clicked was a valid move
                if (whiteTurn) {
                    tileClicked.updateImage(white); //updates the image in the square clicked
                    board[x][y] = "W"; //updates board
                    whiteScore ++;
                    for (int i = 0; i < validFlipDir.size(); i++) {
                        flipTiles(validFlipDir.get(i)); //flips the tiles in the valid directions
                    } //for
                    whiteTurn = false;
                } else {
                    tileClicked.updateImage(black); //updates the image in the square clicked
                    board[x][y] = "B"; //updates board
                    blackScore ++;
                    for (int i = 0; i < validFlipDir.size(); i++) {
                        flipTiles(validFlipDir.get(i)); //flips the tiles in the valid directions
                    } //for
                    whiteTurn = true;
                } //if
            } else {
                displayError(); //displays error to the user
            } //if
            if (whiteTurn) {
                currentTurn = "White";
            } else {
                currentTurn = "Black";
            } //if
            String updatedScore = "Score:   White = " + whiteScore + "   Black = " + blackScore
                + "   " + currentTurn + "'s Turn"; //updates the string for the score label
            score.setText(updatedScore);
            if (anyValidMoves() == false) { //checks if the game is over
                setUpWinScene();
                stage.setScene(winScene); //switches to the win scene
                stage.sizeToScene();
            } //if
        };
    } // createMouseHandler

    /**
     * Determines whether there are any valid moves for the player.
     * @return true if there are valid moves, false otherwise
     */
    private boolean anyValidMoves() {
        boolean validMoves = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //checks if there are any valid moves for the current player at all indices
                if (validMove(i, j)) {
                    validMoves = true;
                } //if
            } //for
        } //for
        return validMoves;
    } //anyValidMoves

    /**
     * Sets up the game board.
     */
    public void setUpGameBoard() {
        board = new String[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ""; //sets all squares to empty
            } //for
        } //for
        // updates the board for the intial middle for squares
        board[3][3] = "W";
        board[3][4] = "B";
        board[4][3] = "B";
        board[4][4] = "W";
    } //setUpGameBoard

    /**
     * Resets the game board.
     */
    private void resetGameBoard() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].updateImage(green); //resets all the squares to the empty image
            } //for
        } //for
        // updates the images for the middle 4 squares for intial set up
        tiles[3][3].updateImage(white);
        tiles[3][4].updateImage(black);
        tiles[4][3].updateImage(black);
        tiles[4][4].updateImage(white);
        whiteScore = 2; //resets the white score
        blackScore = 2; //resets the black score
        String updatedScore = "Score:   White = " + whiteScore + "    Black = " + blackScore
            + "   Black's Turn"; //resets the string for the score label
        score.setText(updatedScore);
        setUpGameBoard(); //resets the game board String[][] array
        whiteTurn = false;
    } //resetGameBoard

    /**
     * Sets up the alert to tell the user the directions for the game.
     */
    private void setUpInstructions() {
        String directions = "Click the square where you would like to place\na tile. You must be "
            + "about to flip at least one tile of\nthe opposite color by surrounding the tile with"
            + "\nyour color tile on either side. The game is\nover if either player no longer has"
            + " any valid\nmoves. Black goes first."; // the string presented to the user
        Alert instructions = new Alert(AlertType.NONE, directions, ButtonType.OK);
        instructions.setResizable(true);
        instructions.showAndWait(); //shows the instructions to the user
    } //setUpInstructions

    /**
     * Sets up the game window.
     */
    private void setUpGameScene() {
        MenuBar menuBar = new MenuBar(); //holds the resume and end game button
        Menu menu = new Menu("Pause");
        MenuItem resume = new MenuItem("Resume");
        MenuItem endGame = new MenuItem("Leave Game");
        EventHandler<ActionEvent> endGameHandler = event -> { //handles the end game button
            stage.setScene(titleScene); //switches back to the title scene
            stage.sizeToScene();
        };
        endGame.setOnAction(endGameHandler); //adds action to the end game button
        menu.getItems().addAll(resume, endGame); //adds all the buttons to the menu bar
        menuBar.getMenus().add(menu);
        HBox header = new HBox(); //holds the score label for the game board
        score = new Label("Score:   White = 2   Black = 2   Black's Turn"); //displays the score
        header.getChildren().add(score);
        gameBoard = new TilePane(); //holds the images for the game board
        gameBoard.setPrefColumns(8);
        tiles = new TileSquare[8][8]; // the image views for the tile pane
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new TileSquare(green, i, j); //creates new TileSquare
                gameBoard.getChildren().addAll(tiles[i][j]); //adds each TileSquare to tilepane
            } //for
        } //for
        //updates images for the middle 4 squares for the beginning of the game
        tiles[3][3].updateImage(white);
        tiles[3][4].updateImage(black);
        tiles[4][3].updateImage(black);
        tiles[4][4].updateImage(white);
        gameVBox.getChildren().addAll(menuBar, header, gameBoard); //adds all items to the game vbox
        gameScene = new Scene(gameVBox, 600, 640); //creates game scene
        stage.sizeToScene();
        //sets the action when the mouse is clicked on each square on the game board
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].setOnMouseClicked(createMouseHandler());
            } //for
        } //for
    } //setUpGameWindow

    /**
     * Sets up the intial title window for the game.
     */
    private void setUpTitleScene() {
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Image image = new Image("file:resources/reversi.jpeg", 677, 378, true, false);
        ImageView iv = new ImageView(image);
        Button playButton = new Button("PLAY");
        EventHandler<ActionEvent> playEvent = event -> { //when button is pressed the game begins
            resetGameBoard(); //resets the game board if there was a previous game played
            stage.setScene(gameScene); //switches to the game scene
            setUpInstructions(); //displays the instructions to the user
        };
        playButton.setOnAction(playEvent); //sets action to the play button
        hbox.getChildren().add(playButton);
        hbox.setAlignment(Pos.CENTER_LEFT); //adjusts position of the play button
        vbox.getChildren().addAll(hbox, iv); //adds all the items to the overall vbox
        titleScene = new Scene(vbox); //creates the title scene
    } //setUpTitleScene

    /**
     * Sets up the win scene.
     */
    private void setUpWinScene() {
        VBox vbox = new VBox();
        ImageView winView = new ImageView();
        if (whiteScore > blackScore) { //sets the win message image based on the winner
            winView.setImage(whiteWin);
        } else {
            winView.setImage(blackWin);
        } //if
        Button playAgain = new Button("PLAY AGAIN");
        EventHandler<ActionEvent> playAgainHandler = event -> { //when it will return to title scene
            stage.setScene(titleScene); //switches back to title scene
            stage.sizeToScene();
        };
        playAgain.setOnAction(playAgainHandler); //sets action on the play again button
        vbox.getChildren().addAll(playAgain, winView); //adds all items to the overall vbox
        winScene = new Scene(vbox); //creates the win scene
    } //if

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage; //initalizes the stage variable
        setUpGameScene();
        setUpTitleScene();
        setUpGameBoard();
        stage.setTitle("cs1302-arcade!");
        stage.setScene(titleScene);
        stage.sizeToScene();
        stage.show();
    } // start

} // ArcadeApp
