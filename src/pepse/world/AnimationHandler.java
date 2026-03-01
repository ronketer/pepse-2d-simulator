package pepse.world;

import danogl.components.RendererComponent;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;

import java.util.function.Supplier;

/**
 * A class for handling animations of game objects. It manages loading and
 * transitioning between different animation states.
 */
public class AnimationHandler {

    private static final String RUN_STATE = "run";

    private static final String JUMP_STATE = "jump";

    private static final String IDLE_STATE = "idle";

    /**
     * Format string for asset file paths. Used to load animation frame images.
     */
    public static final String ASSETS_FORMAT = "assets/%s_%d.png";

    // Animation constants
    private static final int IDLE_FRAMES = 4;
    private static final int JUMP_FRAMES = 4;
    private static final int RUN_FRAMES = 6;
    private static final float ANIMATION_TIME = 0.1f;

    private final ImageReader imageReader;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;
    private final AnimationRenderable idleAnimation;
    private final Supplier<RendererComponent> rendererSupplier;

    /**
     * Initializes the Avatar's animations by loading idle, jump, and run
     * animations.
     */
    public AnimationHandler(ImageReader imageReader, Supplier<RendererComponent> rendererSupplier) {
        this.imageReader = imageReader;
        this.rendererSupplier = rendererSupplier;
        this.idleAnimation = loadAnimation(IDLE_STATE, IDLE_FRAMES);
        this.jumpAnimation = loadAnimation(JUMP_STATE, JUMP_FRAMES);
        this.runAnimation = loadAnimation(RUN_STATE, RUN_FRAMES);
    }

    /**
     * loads an animation by reading a sequence of image files.
     *
     * @param name      the base name of the animation frames
     * @param numFrames the number of frames in the animation
     * @return an AnimationRenderable composed of the loaded frames
     */
    private AnimationRenderable loadAnimation(String name, int numFrames) {
        Renderable[] clips = new ImageRenderable[numFrames];
        for (int i = 0; i < numFrames; i++) {
            clips[i] = imageReader.readImage(String.format(ASSETS_FORMAT, name, i), true);
        }
        return new AnimationRenderable(clips, ANIMATION_TIME);
    }

    /**
     * Updates the current animation based on the given movement state. It also
     * flips the animation horizontally if needed (e.g., for running left or right).
     *
     * @param movmentState the current movement state of the avatar, determining
     *                     which animation to play (IDLE, JUMP, RUN).
     * @param shouldFlip   a boolean indicating whether the animation should be
     *                     flipped horizontally (used for running in the opposite
     *                     direction).
     */
    public void update(MovmentState movmentState, boolean shouldFlip) {
        RendererComponent renderer = rendererSupplier.get();
        switch (movmentState) {
        case IDLE:
            renderer.setRenderable(idleAnimation);
            break;
        case JUMP:
            renderer.setRenderable(jumpAnimation);
            break;
        case RUN:
            renderer.setRenderable(runAnimation);
            renderer.setIsFlippedHorizontally(shouldFlip);
            break;
        default:
            break;
        }
    }
}
