package pepse.world.daynight;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the night overlay effect that cycles between light and dark in the game.
 */
public class Night {

    private static final String NIGHT_TAG = "night";
    private static final float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates a night overlay effect that cycles between light and dark.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength      The duration of a complete day/night cycle in seconds
     * @return A GameObject representing the night overlay
     */
    public static GameObject create(Vector2 windowDimensions,
            float cycleLength) {
        Renderable rectangle = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, rectangle);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        createTransition(night, windowDimensions, cycleLength);
        return night;
    }

    /**
     * Creates the transition animation for the night overlay.
     *
     * @param night            The night GameObject to animate
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength      The duration of a complete day/night cycle
     */
    private static void createTransition(GameObject night, Vector2 windowDimensions,
            float cycleLength) {
        new Transition<Float>(night, night.renderer()::setOpaqueness,
                0f, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

}
