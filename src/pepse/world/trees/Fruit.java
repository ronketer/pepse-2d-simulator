package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.Consumer;

/**
 * Represents a fruit in the game world.
 * Handles the behavior when the fruit is consumed by the avatar.
 */
public class Fruit extends GameObject {
    private static final String AVATAR_TAG = "avatar";
    private static final String FRUIT_TAG = "fruit";
    private final Consumer<Fruit> eatFruitConsumer;

    /**
     * Constructs a new Fruit object.
     *
     * @param topLeftCorner    The top-left position of the fruit
     * @param dimensions       The size dimensions of the fruit
     * @param renderable       The visual representation of the fruit
     * @param eatFruitConsumer Callback function executed when the fruit is eaten
     */
    public Fruit(Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            Consumer<Fruit> eatFruitConsumer) {
        super(topLeftCorner, dimensions, renderable);
        this.eatFruitConsumer = eatFruitConsumer;
        this.setTag(FRUIT_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(AVATAR_TAG)) {
            eatFruitConsumer.accept(this);
        }
    }
}
