package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The Flora class is responsible for creating trees in a specified range.
 * Trees are placed at random positions based on the given seed and the ground height at each x-coordinate.
 */
public class Flora {
    private static final float TREE_PLANT_PROBABILITY = 0.1f;
    private final Function<Float, Float>  groundHeightCallback;
    private final Consumer<Fruit> eatFruitConsumer;
    private final int seed;

    /**
     * Constructs a Flora instance.
     *
     * @param groundHeightCallback A callback to get the ground height at a given x-coordinate.
     * @param seed                 A seed for controlling the randomness of tree placement.
     */
    public Flora(Function<Float, Float> groundHeightCallback,
                 Consumer<Fruit> eatFruitConsumer,
                 int seed) {
        this.groundHeightCallback = groundHeightCallback;
        this.eatFruitConsumer = eatFruitConsumer;
        this.seed = seed;

    }

    /**
     * Creates trees in the specified x-coordinate range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of {@link GameObject} instances representing tree parts.
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        int startX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        int endX = (int) (Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
        List<GameObject> treeParts = new ArrayList<>();
        for (int x = startX; x <= endX; x += Block.SIZE){
            Random rand = new Random(Objects.hash(x, seed));
            float nextFloat = rand.nextFloat(0,1);
            if (nextFloat < TREE_PLANT_PROBABILITY){
//                plant tree
                Vector2 baseLocation = new Vector2(x, groundHeightCallback.apply((float)x));
                treeParts.addAll(Tree.create(baseLocation, seed, eatFruitConsumer));
            }
        }
        return treeParts;
    }


}
