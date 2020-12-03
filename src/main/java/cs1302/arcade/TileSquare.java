package cs1302.arcade;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * Component class to create the game tiles of the game board.
 */
public class TileSquare extends ImageView {

    /**
     * Creates a TileSquare object with the provided image.
     * @param image  the image to be used in the image view
     */
    public TileSquare(Image image) {
        super();
        this.setImage(image);
    } //TileSquare

    /**
     * Changes the image in the image view.
     * @param image the image to replace the original image
     */
    public void updateImage(Image image) {
        this.setImage(image);
    } //updateImage

} //TileSquare
