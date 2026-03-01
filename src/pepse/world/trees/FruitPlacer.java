package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * The `Fruit` class represents a collection of fruit objects
 * that can be created within a specified range on the game map.
 * Each fruit object is created at random locations
 * within the defined range with a certain probability and is represented as a small round object.
 */
public class FruitPlacer {
    private static final String FRUIT_TAG = "fruit";
    private static final Color BASIC_FRUIT_COLOR = Color.RED;
    private static final float FRUIT_CREATION_PROBABILITY = 0.11f;
    private static final Vector2 FRUIT_SIZE = new Vector2(25, 25);

    /**
     * Creates a list of fruit objects within a given range of coordinates.
     * Each potential fruit location is checked for fruit creation based on the `FRUIT_CREATION_PROBABILITY`.
     *
     * @param minX The minimum x-coordinate of the range (inclusive).
     * @param maxX The maximum x-coordinate of the range (inclusive).
     * @param minY The minimum y-coordinate of the range (inclusive).
     * @param maxY The maximum y-coordinate of the range (inclusive).
     * @param random An instance of `Random` used to determine the random placement of fruits.
     * @return A list of `GameObject` representing the fruit objects created within the given range.
     */
    public static java.util.List<GameObject> createInRange(float minX, float maxX,
                                                           float minY, float maxY,
                                                           Random random, Consumer<Fruit> eatFruitConsumer) {
        List<GameObject> fruitList = new ArrayList<>();
        for (int x = (int)minX; x <= maxX; x += Block.SIZE){
            for (int y = (int)minY; y <= maxY; y += Block.SIZE){
                if (random.nextFloat() < FRUIT_CREATION_PROBABILITY) {
                    Renderable renderable = new OvalRenderable(
                            ColorSupplier.approximateColor(BASIC_FRUIT_COLOR));
                    Fruit fruit = new Fruit(new Vector2(x,y), FRUIT_SIZE, renderable, eatFruitConsumer);
                    fruit.setTag(FRUIT_TAG);
                    fruitList.add(fruit);
                }
            }
        }
        return fruitList;
    }


}
