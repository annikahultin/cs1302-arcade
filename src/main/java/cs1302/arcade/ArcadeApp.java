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
    int whiteScore = 2;
    int blackScore = 2;
    TilePane gameBoard;
    TileSquare[][] tiles;
    Image green = new Image("https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/129502363_69217092505763"
        + "6_8837454463363984394_n.jpg?_nc_cat=102&ccb=2&_nc_sid=730e14&_nc_ohc=6EaJcW1b_8kAX9whb9o"
        + "&_nc_ht=scontent-atl3-1.xx&oh=9488f88524913240fe134fa8b5e3e123&oe=5FEFB16F",
         75, 75, true, false);
    Image black = new Image("https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/128921566_69217850172354"
        + "5_1376646196384949752_n.jpg?_nc_cat=102&ccb=2&_nc_sid=730e14&_nc_ohc=Qo2_rnCXcm8AX-kHjWr"
        + "&_nc_ht=scontent-atl3-1.xx&oh=2bb70bbb10e4aedfe6d61412c50c7dbf&oe=5FF06FBD"
        , 75, 75, true, false);
    Image white = new Image("https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/128557674_69217621839044"
        + "0_8937463809284160316_n.jpg?_nc_cat=107&ccb=2&_nc_sid=730e14&_nc_ohc=381d6OVWfUcAX8sPm9D"
        + "&_nc_ht=scontent-atl3-1.xx&oh=403463ef5412958b0d09826828483727&oe=5FED4344"
        , 75, 75, true, false);
    VBox gameVBox = new VBox();
    String[][] board;
    boolean whiteTurn = false;
    int x;
    int y;
    int dirX[] = {-1,1,0,0,1,-1,1,-1};
    int dirY[] = {0,0,1,-1,1,-1,-1,1};
    int left = 0;
    int right = 1;
    int up = 2;
    int down = 3;
    int upRightDiag = 4;
    int downLeftDiag = 5;
    int downRightDiag = 6;
    int upLeftDiag = 7;
    List<Integer> validFlipDir;
    Label score;

    /**
     * Determines if there is a valid flip in the specified direction.
     * @param xVal  the x location of the tile placed in the board.
     * @param yVal  the y location of the tile placed on the board
     * @param dir   the direction to check if there is a valid flip
     */
    private boolean validFlip(int xVal, int yVal, int dir) {
        String oppColor;
        String currentColor;
        boolean potentialFlip = false;
        if (whiteTurn) {
            currentColor = "W";
            oppColor = "B";
        } else {
            currentColor = "B";
            oppColor = "W";
        } //if
        for (int i = 0; i < 8; i++) {
            xVal += dirX[dir];
            yVal += dirY[dir];
            if (xVal < 8 && xVal > -1 && yVal < 8 && yVal > -1) {
                if (board[xVal][yVal].equals(oppColor)) {
                    potentialFlip = true;
                } else if (board[xVal][yVal].equals(currentColor)) {
                    if (potentialFlip) {
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
     */
    private boolean validMove(int xVal, int yVal) {
        validFlipDir = new LinkedList<Integer>();
        boolean valid = false;
        if (board[xVal][yVal].equals("")) {
            for (int dir = 0; dir < 8; dir++) {
                if (validFlip(xVal, yVal, dir)) {
                    validFlipDir.add(dir);
                    valid = true;
                } //if
            } //for
        } else {
            return false;
        } //if
        return valid;
    } //validMove

    /**
     * Handles flipping the tiles on each player's turn.
     * @param dir  the direction in which to flip the tiles.
     */
    private void flipTiles(int dir) {
        String oppColor;
        if (whiteTurn) {
            oppColor = "B";
        } else {
            oppColor = "W";
        } //if
        int tileX = x + dirX[dir];
        int tileY = y + dirY[dir];
        while (board[tileX][tileY].equals(oppColor)) {
            if (oppColor.equals("W")) {
                tiles[tileX][tileY].updateImage(black);
                board[tileX][tileY] = "B";
                blackScore ++;
                whiteScore --;
                tileX += dirX[dir];
                tileY += dirY[dir];
            } else {
                tiles[tileX][tileY].updateImage(white);
                board[tileX][tileY] = "W";
                whiteScore ++;
                blackScore --;
                tileX += dirX[dir];
                tileY += dirY[dir];
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
            String updatedScore = "Score:   White = " + whiteScore + "    Black = " + blackScore;
            score.setText(updatedScore);
        };
    } // createMouseHandler

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
        setUpGameBoard();
    } //resetGameBoard

    /**
     * Sets up the game window.
     * @param stage  the stage object for the game
     */
    private void setUpGameScene(Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Pause");
        MenuItem resume = new MenuItem("Resume");
        MenuItem endGame = new MenuItem("Leave Game");
        EventHandler<ActionEvent> endGameHandler = event -> stage.setScene(titleScene);
        endGame.setOnAction(endGameHandler);
        menu.getItems().addAll(resume, endGame);
        menuBar.getMenus().add(menu);
        HBox header = new HBox();
        score = new Label("Score:   White = 2    Black = 2");
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
     * @param stage  the stage object for the app.
     */
    private void setUpTitleScene(Stage stage) {
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Image image = new Image("https://store-images.s-microsoft.com/image/apps."
            + "40439.13902272735533786.62dcd87f-a7f1-4a5f-a2b7-ff69f15a9bcc.fee5bed0-c445-44c4-"
            + "b6cb-09ae31b4c69b?mode=scale&q=90&h=1080&w=1920", 677, 378, true, false);
        ImageView iv = new ImageView(image);
        Button playButton = new Button("PLAY");
        EventHandler<ActionEvent> playEvent = event -> {
            resetGameBoard();
            stage.setScene(gameScene);
        };
        playButton.setOnAction(playEvent);
        hbox.getChildren().add(playButton);
        hbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(hbox, iv);
        titleScene = new Scene(vbox, 672, 490);
        stage.sizeToScene();
    } //setUpTitleScene

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        setUpGameScene(stage);
        setUpTitleScene(stage);
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
