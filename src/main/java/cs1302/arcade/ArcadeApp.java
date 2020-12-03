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
    TileSquare[] tiles;
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

    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            System.out.println(event);
            r.setX(rng.nextDouble() * (640 - r.getWidth()));
            r.setY(rng.nextDouble() * (480 - r.getHeight()));
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
        Label score = new Label("Score:   White = 2    Black = 2");
        header.getChildren().add(score);
        gameBoard = new TilePane();
        gameBoard.setPrefColumns(8);
        tiles = new TileSquare[64];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new TileSquare(green);
            gameBoard.getChildren().addAll(tiles[i]);
        } //for
        tiles[27].updateImage(white);
        tiles[28].updateImage(black);
        tiles[35].updateImage(black);
        tiles[36].updateImage(white);
        gameVBox.getChildren().addAll(menuBar, header, gameBoard);
        gameScene = new Scene(gameVBox, 600, 640);
        stage.sizeToScene();
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
        EventHandler<ActionEvent> playEvent = event -> stage.setScene(gameScene);
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
//        r.setX(50);                                // 50px in the x direction (right)
//        r.setY(50);                                // 50ps in the y direction (down)
//        group.getChildren().add(r);                // add to main container
//        r.setOnMouseClicked(createMouseHandler()); // clicks on the rectangle move it randomly
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
