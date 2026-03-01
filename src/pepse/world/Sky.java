package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A utility class for creating and managing the sky in the game world.
 * The sky is represented as a rectangular GameObject that acts as the background.
 */
public class Sky {
    private static final String SKY_TAG = "sky";
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Creates a sky GameObject to serve as the background of the game.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return A GameObject representing the sky.
     *
     */
    public static GameObject create(Vector2 windowDimensions){
        Renderable skyRenderable = new RectangleRenderable(BASIC_SKY_COLOR);
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions, skyRenderable);
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
