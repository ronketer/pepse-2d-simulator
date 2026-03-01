package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import java.awt.*;
import java.util.function.Consumer;


/**
 * Represents a raindrop in the game world that falls with gravity and fades over time.
 * Each raindrop is rendered as a small blue rectangle that gradually becomes transparent
 * before being removed from the game.
 */
public class Raindrop {
    private static final Color RAINDROP_COLOR = new Color(30, 144, 255);
    private static final float GRAVITY = 300f;
    private static final float FADE_TIME = 2f;
    private static final float RAINDROP_SIZE = 10f;

    /**
     * Creates a new raindrop at a specified location.
     *
     * @param location The initial position of the raindrop in the game world.
     * @param raindropRemover A callback function to remove the raindrop from the game once it has faded out.
     * @return A new {@link GameObject} representing the raindrop.
     */
    public static GameObject create(Vector2 location, Consumer<GameObject> raindropRemover){
        Renderable renderable = new RectangleRenderable(
                ColorSupplier.approximateColor(RAINDROP_COLOR));
        GameObject raindrop = new GameObject(location,Vector2.ONES.mult(RAINDROP_SIZE),renderable);
        raindrop.transform().setAccelerationY(GRAVITY);
        raindrop.setTag("raindrop");
        raindrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        new Transition<>(
                raindrop, raindrop.renderer()::setOpaqueness,
                1f,
                0f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                FADE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> raindropRemover.accept(raindrop));

        return raindrop;
    }
}