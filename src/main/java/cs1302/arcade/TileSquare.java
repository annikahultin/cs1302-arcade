package cs1302.arcade;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * Component class to create the game tiles of the game board.
 */
public class TileSquare extends ImageView {

    int x;
    int y;

    /**
     * Creates a TileSquare object with the provided image.
     * @param image  the image to be used in the image view
     * @param x  the x value for the tile square
     * @param y  the y value for the tile square
     */
    public TileSquare(Image image, int x, int y) {
        super();
        this.setImage(image);
        this.x = x;
        this.y = y;
    } //TileSquare

    /**
     * Changes the image in the image view.
     * @param image the image to replace the original image
     */
    public void updateImage(Image image) {
        this.setImage(image);
    } //updateImage

    /**
     * Returns the x value for the tile square.
     * @return  the x value of the square
     */
    public int xValue() {
        return x;
    } //xValue

    /**
     * Returns the y value for the tile square.
     * @return the y value for the square
     */
    public int yValue() {
        return y;
    } //yValue
} //TileSquare
