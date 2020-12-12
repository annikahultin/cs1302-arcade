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
    Scene titleScene;
    Scene gameScene;
    Scene winScene;
    Stage stage;
    int whiteScore = 2;
    int blackScore = 2;
    TilePane gameBoard;
    TileSquare[][] tiles;
    Image green = new Image("file:resources/green.jpg", 75, 75, true, false);
    Image black = new Image("file:resources/black.jpg", 75, 75, true, false);
    Image white = new Image("file:resources/white.jpg", 75, 75, true, false);
    Image whiteWin = new Image("file:resources/whitewin.jpg", 500, 500, true, false);
    Image blackWin = new Image("file:resources/blackwin.jpg", 500, 500, true, false);
    VBox gameVBox = new VBox();
    String[][] board;
    boolean whiteTurn = false;
    int x;
    int y;
    int xDirection[] = {-1,1,0,0,1,-1,1,-1};
    int yDirection[] = {0,0,1,-1,1,-1,-1,1};
    List<Integer> validFlipDir;
    Label score;
    String currentTurn = "Black";

    /**
     * Determines if there is a valid flip in the specified direction.
     * @param xVal  the x location of the tile placed in the board.
     * @param yVal  the y location of the tile placed on the board
     * @param dir   the direction to check if there is a valid flip
     * @return true if the flip is valid; false otherwise
     */
    private boolean validFlip(int xVal, int yVal, int d) {
        String oppColor;
        String currentColor;
        int potentialFlips = 0;
        if (whiteTurn) {
            currentColor = "W";
            oppColor = "B";
        } else {
            currentColor = "B";
            oppColor = "W";
        } //if
        for (int i = 0; i < 8; i++) {
            xVal += xDirection[d];
            yVal += yDirection[d];
            if (xVal < 8 && xVal > -1 && yVal < 8 && yVal > -1) {
                if (board[xVal][yVal].equals(oppColor)) {
                    potentialFlips ++;
                } else if (board[xVal][yVal].equals(currentColor)) {
                    if (potentialFlips > 0) {
                        return true;
                    } else {
                        return false;
                    } //if
                } else {
                    return false;
                } //if
            } //if
        } //for
        return false;
    } //validFlip

    /**
     * Determines wether a move is valid.
     * @return true if the move is valid; false otherwise
     */
    private boolean validMove(int xVal, int yVal) {
        validFlipDir = new LinkedList<Integer>();
        boolean valid = false;
        if (board[xVal][yVal].equals("")) {
            for (int i = 0; i < 8; i++) {
                if (validFlip(xVal, yVal, i)) {
                    validFlipDir.add(i);
                    valid = true;
                } //if
            } //for
        } //if
        return valid;
    } //validMove

    /**
     * Handles flipping the tiles on each player's turn.
     * @param dir  the direction in which to flip the tiles.
     */
    private void flipTiles(int d) {
        String oppColor;
        if (whiteTurn) {
            oppColor = "B";
        } else {
            oppColor = "W";
        } //if
        int tileX = x + xDirection[d];
        int tileY = y + yDirection[d];
        while (board[tileX][tileY].equals(oppColor)) {
            if (oppColor.equals("W")) {
                tiles[tileX][tileY].updateImage(black);
                board[tileX][tileY] = "B";
                blackScore ++;
                whiteScore --;
                tileX += xDirection[d];
                tileY += yDirection[d];
            } else {
                tiles[tileX][tileY].updateImage(white);
                board[tileX][tileY] = "W";
                whiteScore ++;
                blackScore --;
                tileX += xDirection[d];
                tileY += yDirection[d];
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
        error.showAndWait();
    } //if

    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            TileSquare tileClicked = (TileSquare) event.getSource();
            x = tileClicked.xValue();
            y = tileClicked.yValue();
            if (validMove(x, y)) {
                if (whiteTurn) {
                    tileClicked.updateImage(white);
                    board[x][y] = "W";
                    whiteScore ++;
                    for (int i = 0; i < validFlipDir.size(); i++) {
                        flipTiles(validFlipDir.get(i));
                    } //for
                    whiteTurn = false;
                } else {
                    tileClicked.updateImage(black);
                    board[x][y] = "B";
                    blackScore ++;
                    for (int i = 0; i < validFlipDir.size(); i++) {
                        flipTiles(validFlipDir.get(i));
                    } //for
                    whiteTurn = true;
                } //if
            } else {
                displayError();
            } //if
            if (whiteTurn) {
                currentTurn = "White";
            } else {
                currentTurn = "Black";
            } //if
            String updatedScore = "Score:   White = " + whiteScore + "   Black = " + blackScore
                + "   " + currentTurn + "'s Turn";
            score.setText(updatedScore);
            if (anyValidMoves() == false) {
                setUpWinScene();
                stage.setScene(winScene);
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
                if (validMove(i, j)) {
                    validMoves = true;
                } //if
            } //for
        } //for
        return validMoves;
    } //anyValidMoves

    /**
     * Return a key event handler that moves to the rectangle to the left
     * or the right depending on what key event is generated by the associated
     * node.
     * @return the key event handler
     */
    private EventHandler<? super KeyEvent> createKeyHandler() {
        return event -> {
            System.out.println(event);
            switch (event.getCode()) {
            case LEFT:  // KeyCode.LEFT
                r.setX(r.getX() - 10.0);
                break;
            case RIGHT: // KeyCode.RIGHT
                r.setX(r.getX() + 10.0);
                break;
            default:
                // do nothing
            } // switch
            // TODO bounds checking
        };
    } // createKeyHandler

    /**
     * Sets up the game board.
     */
    public void setUpGameBoard() {
        board = new String[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "";
            } //for
        } //for
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
                tiles[i][j].updateImage(green);
            } //for
        } //for
        tiles[3][3].updateImage(white);
        tiles[3][4].updateImage(black);
        tiles[4][3].updateImage(black);
        tiles[4][4].updateImage(white);
        whiteScore = 2;
        blackScore = 2;
        String updatedScore = "Score:   White = " + whiteScore + "    Black = " + blackScore
            + "   Black's Turn";
        score.setText(updatedScore);
        setUpGameBoard();
    } //resetGameBoard

    /**
     * Sets up the alert to tell the user the directions for the game.
     */
    private void setUpInstructions() {
        String directions = "Click the square where you would like to place\na tile. You must be "
            + "about to flip at least one tile of\nthe opposite color by surrounding the tile with"
            + "\nyour color tile on either side. The game is\nover if either player no longer has"
            + "any valid\nmoves. Black goes first.";
        Alert instructions = new Alert(AlertType.NONE, directions, ButtonType.OK);
        instructions.setResizable(true);
        instructions.showAndWait();
    } //setUpInstructions

    /**
     * Sets up the game window.
     */
    private void setUpGameScene() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Pause");
        MenuItem resume = new MenuItem("Resume");
        MenuItem endGame = new MenuItem("Leave Game");
        EventHandler<ActionEvent> endGameHandler = event -> {
            stage.setScene(titleScene);
            stage.sizeToScene();
        };
        endGame.setOnAction(endGameHandler);
        menu.getItems().addAll(resume, endGame);
        menuBar.getMenus().add(menu);
        HBox header = new HBox();
        score = new Label("Score:   White = 2   Black = 2   Black's Turn");
        header.getChildren().add(score);
        gameBoard = new TilePane();
        gameBoard.setPrefColumns(8);
        tiles = new TileSquare[8][8];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new TileSquare(green, i, j);
                gameBoard.getChildren().addAll(tiles[i][j]);
            } //for
        } //for
        tiles[3][3].updateImage(white);
        tiles[3][4].updateImage(black);
        tiles[4][3].updateImage(black);
        tiles[4][4].updateImage(white);
        gameVBox.getChildren().addAll(menuBar, header, gameBoard);
        gameScene = new Scene(gameVBox, 600, 640);
        stage.sizeToScene();
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
        EventHandler<ActionEvent> playEvent = event -> {
            resetGameBoard();
            stage.setScene(gameScene);
            setUpInstructions();
        };
        playButton.setOnAction(playEvent);
        hbox.getChildren().add(playButton);
        hbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(hbox, iv);
        titleScene = new Scene(vbox);
    } //setUpTitleScene

    /**
     * Sets up the win scene.
     */
    private void setUpWinScene() {
        VBox vbox = new VBox();
        ImageView winView = new ImageView();
        if (whiteScore > blackScore) {
            winView.setImage(whiteWin);
        } else {
            winView.setImage(blackWin);
        } //if
        Button playAgain = new Button("PLAY AGAIN");
        EventHandler<ActionEvent> playAgainHandler = event -> {
            stage.setScene(titleScene);
            stage.sizeToScene();
        };
        playAgain.setOnAction(playAgainHandler);
        vbox.getChildren().addAll(playAgain, winView);
        winScene = new Scene(vbox);
    } //if

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        setUpGameScene();
        setUpTitleScene();
        setUpGameBoard();
//        r.setX(50);                                // 50px in the x direction (right)
//        r.setY(50);                                // 50ps in the y direction (down)
//        group.getChildren().add(r);                // add to main container
//        group.setOnKeyPressed(createKeyHandler()); // left-right key presses move the rectangle

//        Scene scene = new Scene(group, 640, 480);
        stage.setTitle("cs1302-arcade!");
        stage.setScene(titleScene);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        group.requestFocus();

    } // start

} // ArcadeApp
