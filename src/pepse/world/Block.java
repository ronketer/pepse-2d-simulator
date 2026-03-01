package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a basic block in the game world.
 * Each block is a square with fixed size and immovable properties.
 */
public class Block extends GameObject {
    /**
     * The fixed size of each block in pixels. All blocks are square with this
     * dimension.
     */
    public static final int SIZE = 30;

    /**
     * Constructs a new Block instance.
     *
     * @param topLeftCorner The top-left corner position of the block in the game
     *                      world.
     * @param renderable    The renderable object that defines the block's
     *                      appearance.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Sets the width of the block.
     *
     * @param width the new width to set for this block
     */
    public void setWidth(float width) {
        this.setDimensions(new Vector2(width, this.getDimensions().y()));
    }
}
