package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

/**
 * The Trunk class is responsible for creating the trunk of a tree.
 * Each trunk is a rectangular block rendered with a wooden color.
 */
public class Trunk {
    private static final String TRUNK_TAG = "trunk";
    private static final Color BASIC_TRUNK_COLOR = new Color(100, 50, 20);

    /**
     * Creates a trunk GameObject with the specified base location and dimensions.
     *
     * @param baseLocation The {@link Vector2} position of the trunk's base
     *                     (bottom-left corner).
     * @param dimensions   A {@link Vector2} representing the width and height of
     *                     the trunk.
     * @return A {@link GameObject} instance representing the tree trunk.
     */
    public static GameObject create(Vector2 baseLocation, Vector2 dimensions) {
        Renderable trunkRenderable = new RectangleRenderable(
                ColorSupplier.approximateColor(BASIC_TRUNK_COLOR));
        Vector2 topLeftCorner = baseLocation.add(
                Vector2.UP.mult(dimensions.y()));
        GameObject trunk = new GameObject(topLeftCorner, dimensions, trunkRenderable);
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        trunk.setTag(TRUNK_TAG);
        return trunk;
    }
}
