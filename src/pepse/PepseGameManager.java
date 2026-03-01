package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.Avatar;
import pepse.world.trees.Flora;
import pepse.world.LifeDisplay;
import pepse.world.cloud.Cloud;
import pepse.world.trees.Fruit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class manages the Pepse game, handling the infinite world generation,
 * day-night cycle, avatar movement, and object lifecycle.
 * It extends GameManager to provide core game functionality.
 * Uses a window-based approach to generate and remove terrain/flora as the
 * avatar moves.
 */
public class PepseGameManager extends GameManager {
    private static final int BOUND = 32;
    private static final String FRUIT = "fruit";
    private static final String LEAF = "leaf";
    private static final String TRUNK = "trunk";
    private static final int CYCLE_LENGTH = 30;
    private static final int ENERGY_ADDED_FROM_FRUIT = 10;
    Vector2 windowDimensions;
    ImageReader imageReader;
    SoundReader soundReader;
    UserInputListener inputListener;
    WindowController windowController;
    Terrain terrain;
    Cloud cloud;
    Avatar avatar;
    private Flora flora;
    private Vector2 lastAvatarCenter;
    private final Map<Integer, List<GameObject>> floraToWindowIndex = new HashMap<>();
    private final Map<Integer, List<Block>> terrainToWindoIndex = new HashMap<>();
    private static final int SEED = new Random().nextInt(BOUND);

    /**
     * Entry point for the Pepse game.
     * Initializes and runs a new game instance.
     *
     * @param args Command line arguments (not arguments used)
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game world with all required components.
     * Sets up the terrain, flora, avatar, and visual elements like sky and sun.
     * Creates the initial three windows of terrain and flora around avatar
     * position.
     *
     * @param imageReader      Used for reading image assets
     * @param soundReader      Used for reading sound assets
     * @param inputListener    Handles user input
     * @param windowController Controls the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader,
            SoundReader soundReader, UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.addSky();
        this.terrain = new Terrain(windowDimensions, SEED);
        initializeFlora();
        this.addNight();
        this.addSun();
        this.addCloud();
        this.addAvatar();
        this.flora = new Flora(terrain::groundHeightAt, this::eatFruitConsumer, SEED);
        initializeGround();
        this.createLifeDisplay();
        configureLayers();

    }

    private void initializeGround() {
        addFlora(-1);
        addFlora(0);
        addFlora(1);
    }

    private void initializeFlora() {
        addGround(-1);
        addGround(0);
        addGround(1);
    }

    private void configureLayers() {
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.DEFAULT, false);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, false);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND, Layer.STATIC_OBJECTS, true);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND, Layer.FOREGROUND, true);
    }

    private void addSky() {
        gameObjects().addGameObject(
                Sky.create(windowDimensions),
                Layer.BACKGROUND);
    }

    private void addGround(int windowIndex) {
        List<Block> blocks = terrain.createInRange((int) (windowIndex * windowDimensions.x()),
                (int) ((windowIndex + 1) * windowDimensions.x()));
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        this.terrainToWindoIndex.put(windowIndex, blocks);
    }

    private void addNight() {
        gameObjects().addGameObject(
                Night.create(windowDimensions, CYCLE_LENGTH),
                Layer.UI);
    }

    private void addSun() {
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
    }

    private void addCloud() {
        cloud = new Cloud(windowDimensions,
                (gameObj) -> gameObjects().addGameObject(gameObj, Layer.BACKGROUND),
                (gameObj) -> gameObjects().removeGameObject(gameObj, Layer.BACKGROUND));
        List<Block> cloudBlocks = cloud.getBlocks();
        for (Block cloudBlock : cloudBlocks) {
            gameObjects().addGameObject(cloudBlock, Layer.BACKGROUND);
        }
    }

    private void addAvatar() {
        float curAvatarX = windowDimensions.x() / 2;
        float groundHeightAtCenter = terrain.groundHeightAt(curAvatarX);
        Vector2 avatarInitialLocation = Vector2.of(curAvatarX,
                groundHeightAtCenter - Avatar.AVATAR_SIZE * 2);
        avatar = new Avatar(avatarInitialLocation, inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.FOREGROUND);
        avatar.addObserver(cloud);
        Vector2 deltaRelativeToAvatar = this.windowDimensions.mult(0.5f)
                .subtract(avatarInitialLocation);
        setCamera(new Camera(avatar, deltaRelativeToAvatar, this.windowDimensions,
                this.windowDimensions));
        this.lastAvatarCenter = avatarInitialLocation;
    }

    private void addFlora(int windowIndex) {
        List<GameObject> treeParts = flora.createInRange((int) (windowIndex * windowDimensions.x()),
                (int) ((windowIndex + 1) * windowDimensions.x()));
        for (GameObject treePart : treeParts) {
            int partLayer = getFloraLayer(treePart.getTag());
            gameObjects().addGameObject(treePart, partLayer);

        }
        this.floraToWindowIndex.put(windowIndex, treeParts);

    }

    private void createLifeDisplay() {
        LifeDisplay lifeDisplay = new LifeDisplay(Vector2.ZERO, avatar::getEnergy);
        gameObjects().addGameObject(lifeDisplay, Layer.UI);
    }

    private void removeFruitForCycle(Fruit fruit) {
        Vector2 outOfTheScreen = new Vector2(0, windowDimensions.y() + 100);
        Vector2 location = new Vector2(fruit.getTopLeftCorner());
        fruit.setTopLeftCorner(outOfTheScreen);
        new ScheduledTask(fruit, CYCLE_LENGTH, false,
                () -> fruit.setTopLeftCorner(location));
    }

    private void eatFruitConsumer(Fruit fruit) {
        removeFruitForCycle(fruit);
        avatar.addToEnergy(ENERGY_ADDED_FROM_FRUIT);
    }

    private void updateAvatarCenter() {
        this.lastAvatarCenter = avatar.getCenter();
    }

    /**
     * Updates the game state each frame.
     * Handles infinite world generation by creating and removing terrain/flora
     * based on avatar movement between windows.
     *
     * @param deltaTime Time elapsed since last update in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int curWindowIndex = (int) Math.floor(avatar.getCenter().x() / windowDimensions.x());
        int lastWindowIndex = (int) Math.floor(lastAvatarCenter.x() / windowDimensions.x());
        if (curWindowIndex > lastWindowIndex) {
            removeGroundAndFlora(lastWindowIndex - 1);
            addGround(curWindowIndex + 1);
            addFlora(curWindowIndex + 1);
        } else if (curWindowIndex < lastWindowIndex) {
            removeGroundAndFlora(lastWindowIndex + 1);
            addGround(curWindowIndex - 1);
            addFlora(curWindowIndex - 1);
        }
        updateAvatarCenter();
    }

    private void removeGroundAndFlora(int windowIndex) {
        List<Block> blocks = terrainToWindoIndex.remove(windowIndex);
        if (blocks != null) {
            for (Block block : blocks) {
                gameObjects().removeGameObject(block, Layer.STATIC_OBJECTS);
            }
        }
        List<GameObject> floraObjects = floraToWindowIndex.remove(windowIndex);
        if (floraObjects != null) {
            for (GameObject treePart : floraObjects) {
                int partLayer = getFloraLayer(treePart.getTag());
                gameObjects().removeGameObject(treePart, partLayer);
            }
        }
    }

    private int getFloraLayer(String objTag) {
        return switch (objTag) {
            case TRUNK -> Layer.STATIC_OBJECTS;
            case LEAF -> Layer.DEFAULT;
            case FRUIT -> Layer.FOREGROUND;
            default -> Layer.DEFAULT;
        };
    }

}
