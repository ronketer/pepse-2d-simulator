package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

/**
 * The Tree class provides functionality for creating tree objects.
 * Each tree consists of a trunk and foliage (leaves) created procedurally based on a location and a seed.
 */
public class Tree {
    private static final int TRUNK_WIDTH = Block.SIZE;
    private static final int MIN_TRUNK_HEIGHT = 100;
    private static final int MAX_TRUNK_HEIGHT = 200;

    /**
     * Creates a tree at the specified bottom location with the given seed for randomization.
     *
     * @param baseLocation The {@link Vector2} position of the tree's base.
     * @param seed           A seed value for consistent randomization.
     * @return A list of {@link GameObject} instances representing the tree's components (trunk and leaves).
     */
    public static List<GameObject> create(Vector2 baseLocation, long seed, Consumer<Fruit> eatFruitConsumer){
        Random rand = new Random(Objects.hash(baseLocation.x(), seed));
        List<GameObject> treeObjects = new ArrayList<>();
//        create trunk
        float trunkHeight = rand.nextInt(MAX_TRUNK_HEIGHT - MIN_TRUNK_HEIGHT) + MIN_TRUNK_HEIGHT;
        treeObjects.add(Trunk.create(baseLocation, new Vector2(TRUNK_WIDTH, trunkHeight)));
        treeObjects.addAll(Leafs.createInRange(baseLocation.x() - trunkHeight/2,
                baseLocation.x() + trunkHeight /2,
                baseLocation.y()- trunkHeight*1.5f,
                baseLocation.y()- trunkHeight/2, rand));
        treeObjects.addAll(FruitPlacer.createInRange(baseLocation.x() - trunkHeight/2,
                baseLocation.x() + trunkHeight /2,
                baseLocation.y()- trunkHeight*1.5f,
                baseLocation.y()- trunkHeight/2, rand, eatFruitConsumer));
        return treeObjects;
    }

}
