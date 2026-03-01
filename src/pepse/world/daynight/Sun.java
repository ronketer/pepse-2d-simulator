package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The Sun class creates and manages the sun object in the game world. The sun
 * moves in a circular path over the window dimensions to simulate a day-night
 * cycle.
 *
 * The sun is represented by a yellow oval, and its movement follows a
 * transition between 0° and 360°, creating a full circular path within the
 * camera's coordinate space. The speed of this movement is controlled by the
 * cycle length, which defines how long it takes for the sun to complete one
 * full rotation.
 *
 */
public class Sun {
        private static final int INITIAL_HEIGHT = 150;
        private static final int SUN_RADIUS = 100;

        /**
         * Creates a sun GameObject that follows a circular path to simulate a day-night
         * cycle.
         *
         * @param windowDimensions The dimensions of the window where the sun will move.
         *                         The sun will be placed at the center of the window
         *                         and move in a circular path.
         * @param cycleLength      The length of one cycle of the sun's movement, in
         *                         seconds. This controls how fast the sun completes one
         *                         full 360° rotation.
         * @return A GameObject representing the sun, which can be added to the game
         *         world.
         */
        public static GameObject create(Vector2 windowDimensions, float cycleLength) {
                Renderable sunRenderable = new OvalRenderable(Color.YELLOW);
                GameObject sun = new GameObject(windowDimensions.mult(0.5F),
                                new Vector2(SUN_RADIUS, SUN_RADIUS), sunRenderable);
                sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                sun.setTag("sun");
                createTransition(sun, windowDimensions, cycleLength);
                return sun;
        }

        private static void createTransition(GameObject sun, Vector2 windowDimensions, float cycleLength) {
                Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, INITIAL_HEIGHT);
                Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() * 2 / 3);
                new Transition<Float>(sun,
                                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                                                .rotated(angle).add(cycleCenter)),
                                (float) 0, 360F, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                                Transition.TransitionType.TRANSITION_LOOP, null);
        }
}
