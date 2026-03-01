package pepse.world;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Terrain class is responsible for generating ground blocks and providing
 * the ground height at a given X coordinate. It is not a GameObject itself,
 * but it creates GameObjects representing the ground blocks.
 */
public class Terrain {
    private static final int START_POINT_NOISE = 256;
    private static final String GROUND = "ground";
    private static final Color BASE_GROUND_COLOR =
            new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final double GROUND_HEIGHT_FACTOR =  2.0 / 3;
    private static final float NOISE_FACTOR = Block.SIZE * 7; // creates hills ~7 blocks tall

    private final int groundHeightAtX0;
    private final RectangleRenderable blockRenderable;
    private final NoiseGenerator noiseGenerator;

    /**
     * Constructs a Terrain object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed A seed for the random noise generator.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.blockRenderable = new RectangleRenderable(ColorSupplier.approximateColor(
                BASE_GROUND_COLOR));
        this.groundHeightAtX0 = (int) (windowDimensions.y() * GROUND_HEIGHT_FACTOR);
        this.noiseGenerator = new NoiseGenerator(seed, START_POINT_NOISE);
    }


    /**
     * Calculates the ground height at the given X coordinate.
     *
     * @param x The X coordinate.
     * @return The Y coordinate representing the ground height.
     */
    public float groundHeightAt(float x) {
        return (float) (groundHeightAtX0 + noiseGenerator.noise(x, NOISE_FACTOR));
    }

    /**
     * Creates a list of ground blocks within the specified range of X coordinates.
     * For each X position in the given range,
     * it generates a column of blocks starting from the ground height at that X.
     * The blocks are stacked from the calculated ground height
     * down to the depth specified by `TERRAIN_DEPTH`.
     *
     * @param minX The minimum X coordinate of the range . The ground generation will start at this X value.
     * @param maxX The maximum X coordinate of the range . The ground generation will stop at this X value.
     * @return A list of `Block` objects representing the generated terrain in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        int startX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        int endX = (int) (Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
        List<Block> blocks = new ArrayList<>();
        
        for (int x = startX; x <= endX; x += Block.SIZE) {
            int topY = (int)Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (int y = topY; y < topY + TERRAIN_DEPTH * Block.SIZE; y += Block.SIZE){
                
                // GENERATE A NEW RENDERABLE FOR EVERY SINGLE BLOCK HERE
                RectangleRenderable renderable = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR)
                );
                
                Block block = new Block(new Vector2(x,y), renderable);
                block.setTag(GROUND);
                blocks.add(block);
            }
        }
        return blocks;
    }
    // public List<Block> createInRange(int minX, int maxX) {
    //     int startX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
    //     int endX = (int) (Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
    //     List<Block> blocks = new ArrayList<>();
    //     for (int x = startX; x <= endX; x += Block.SIZE) {
    //         int topY = (int)Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
    //         for (int y = topY; y < topY + TERRAIN_DEPTH * Block.SIZE; y+= Block.SIZE){
    //             Block block = new Block(new Vector2(x,y), blockRenderable);
    //             block.setTag(GROUND);
    //             blocks.add(block);
    //         }
    //     }
    //     return blocks;
    // }

}
