package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import java.awt.*;


/**
 * Represents the halo effect around the sun in the game.
 * The halo is rendered as a translucent oval surrounding the sun.
 */
public class SunHalo {
    private static final String HALO_TAG = "halo";
    private static final Color HALO_COLOR = new Color(255, 255, 0, 40);
    private static final float MULTIPLY_FACTOR = 2.7f;

    /**
     * Creates the halo object around the given sun object.
     * The halo is an oval with a specified size relative to the sun's dimensions,
     * and it follows the sun's position as the game progresses.
     *
     * @param sun The sun GameObject around which the halo will be created.
     * @return The GameObject representing the halo.
     */
    public static GameObject create(GameObject sun){
        Renderable haloRenderable = new OvalRenderable(HALO_COLOR);
        GameObject halo = new GameObject(sun.getTopLeftCorner(),
                sun.getDimensions().mult(MULTIPLY_FACTOR),
                haloRenderable);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(HALO_TAG);
        halo.addComponent((float deltaTime) -> halo.setCenter(sun.getCenter()));
        return halo;
    }
}
