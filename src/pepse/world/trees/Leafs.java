package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A utility class for creating and animating leaves in the game world.
 * Leaves are represented as instances of the {@link Block} class and are placed in a given range.
 * Each leaf undergoes periodic changes in angle and width for a dynamic appearance.
 */
public class Leafs {
    private static final String LEAF_TAG = "leaf";
    private static final Color BASIC_LEAF_COLOR = new Color(50, 200, 30);
    private static final float MIN_ANGLE = -10f;
    private static final float MAX_ANGLE = 10f;
    private static final float MIN_WIDTH = 25f;
    private static final float MAX_WIDTH = 35f;
    private static final float LEAF_CREATION_PROBABILITY = 0.8f;

    /**
     * Creates a list of leaf objects in a given range.
     *
     * @param minX   The minimum x-coordinate for leaf placement.
     * @param maxX   The maximum x-coordinate for leaf placement.
     * @param minY   The minimum y-coordinate for leaf placement.
     * @param maxY   The maximum y-coordinate for leaf placement.
     * @param random A {@link Random} instance for controlling randomness.
     * @return A list of {@link GameObject} instances representing leaves.
     */
    public static List<GameObject> createInRange(float minX, float maxX,
                                                 float minY, float maxY,
                                                 Random random) {
        List<GameObject> leafs = new ArrayList<>();
        for (int x = (int)minX; x <= maxX; x += Block.SIZE){
            for (int y = (int)minY; y <= maxY; y += Block.SIZE){
                if (random.nextFloat() < LEAF_CREATION_PROBABILITY) {
                    Renderable renderable = new RectangleRenderable(
                            ColorSupplier.approximateColor(BASIC_LEAF_COLOR));
                    Block leaf = new Block(new Vector2(x,y), renderable);
                    addScheduledTask(leaf, random.nextFloat(0, 1));
                    leaf.setTag(LEAF_TAG);
                    leafs.add(leaf);
                }
            }
        }
        return leafs;
    }

    /**
     * Schedules a task to create transitions for a leaf after a given delay.
     *
     * @param leaf     The leaf {@link Block} for which transitions will be created.
     * @param waitTime The delay in seconds before starting the transitions.
     */
    private static void addScheduledTask(Block leaf, float waitTime){
        new ScheduledTask(leaf, waitTime, false, () -> createTransitions(leaf));

    }

    /**
     * Creates transitions for a given leaf, animating its angle and width over time.
     *
     * @param leaf The leaf {@link Block} to animate.
     */
    private static void createTransitions(Block leaf){
//        transition on angle
        new Transition<>(leaf, leaf.renderer()::setRenderableAngle,
                MIN_ANGLE, MAX_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        new Transition<>(leaf, leaf::setWidth,
                MIN_WIDTH, MAX_WIDTH,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);


    }


}
