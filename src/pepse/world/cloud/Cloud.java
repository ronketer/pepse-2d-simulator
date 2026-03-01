
package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.AvatarObserver;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * The Cloud class is responsible for creating cloud objects in the game.
 * Each cloud consists of blocks arranged in a predefined shape with smooth
 * opacity transitions.
 */
public class Cloud implements AvatarObserver {
    private static final Random random = new Random();
    // cloud constants
    private static final float DROP_CREATION_PROBABILITY = 0.5f;
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final float TRANSITION_TIME = 12f;
    private static final float CLOUD_HEIGHT_FACTOR = (float) 1 /6;
    private static final int[][] DEFAULT_SHAPE = {
            { 0, 1, 1, 0, 0 },
            { 1, 1, 1, 0, 1 },
            { 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 0 },
            { 0, 0, 0, 0, 0 }
    };


    private final List<Block> blocks = new ArrayList<>();
    private final Vector2 windowDimensions;
    private final Consumer<GameObject> raindropAdder;
    private final Consumer<GameObject> raindropRemover;
    private final int cloudWidth;
    private final Vector2 transitionLength;

    /**
         * Constructor.
         *
         * @param windowDimensions The dimensions of the game window
         * @param raindropAdder Callback to add raindrops to the game
         * @param raindropRemover Callback to remove raindrops from the game
         */
        public Cloud(Vector2 windowDimensions, Consumer<GameObject> raindropAdder,
                Consumer<GameObject> raindropRemover) {
            this.windowDimensions = windowDimensions;
            this.raindropAdder = raindropAdder;
            this.raindropRemover = raindropRemover;
            this.cloudWidth = DEFAULT_SHAPE[0].length * Block.SIZE;
            this.transitionLength = new Vector2(windowDimensions.x() + cloudWidth, 0);

            this.createCloudBlocks();

        }

    /**
     * Returns Block array.
     *
     * @return block list
     *
     */
        public List<Block> getBlocks() {
            return blocks;
        }

        /**
         * Creates and initializes the cloud blocks at the specified location.
         * The cloud starts from outside the left screen and moves continuously
         * across the screen.
         *
         */
        private void createCloudBlocks() {
            // Start position outside left screen
            Vector2 basePosition = new Vector2(-cloudWidth, windowDimensions.y() * CLOUD_HEIGHT_FACTOR);
            for (int i = 0; i < DEFAULT_SHAPE.length; i++) {
                for (int j = 0; j < DEFAULT_SHAPE[i].length; j++) {
                    if (DEFAULT_SHAPE[i][j] == 1) {
                        Vector2 blockPosition = basePosition.add(
                                Vector2.of(j * Block.SIZE, i * Block.SIZE));
                        blocks.add(createSingleBlock(blockPosition));
                    }
                }
            }
    }

    /**
     * Creates a single cloud block with the specified location and animations.
     *
     * @param location The top-left corner position for the block.
     * @return The initialized cloud block.
     */
    private Block createSingleBlock(Vector2 location) {
        RectangleRenderable blockRenderable = new RectangleRenderable(
                ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
        Block cloudBlock = new Block(location, blockRenderable);
        cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        addTransition(cloudBlock, location, location.add(transitionLength));
        cloudBlock.setTag("cloud");
        return cloudBlock;
    }

    /**
     * Adds a looping transition to move a cloud block horizontally.
     *
     * @param block The cloud block to animate
     * @param startPos Starting position of the transition
     * @param endPos Ending position of the transition
     */
    private static void addTransition(Block block, Vector2 startPos, Vector2 endPos) {
        new Transition<>(
                block,
                block::setTopLeftCorner,
                startPos,
                endPos,
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * Generates raindrops beneath the cloud based on CREATION_PROBABILITY.
     * Each raindrop has gravity applied and fades out over time.
     */
    private void makeCloudRain() {
        int startCloudX = (int)blocks.getFirst().getTopLeftCorner().x();
        int endCloudX = startCloudX + cloudWidth;
        int cloudY = (int)blocks.getFirst().getTopLeftCorner().y()+ cloudWidth;
        for (int x = startCloudX; x < endCloudX; x += Block.SIZE) {
            if (random.nextFloat() < DROP_CREATION_PROBABILITY) {
                GameObject raindrop = Raindrop.create(new Vector2(x, cloudY), raindropRemover);
                raindropAdder.accept(raindrop);
            }
        }
    }

    /**
     * Implementation of AvatarObserver interface.
     * Triggers the cloud's rain effect.
     */
    @Override
    public void execute() {
        makeCloudRain();
    }
}