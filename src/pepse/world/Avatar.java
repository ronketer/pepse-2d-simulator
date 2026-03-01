package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player character in the game. The avatar can move left/right,
 * jump, and manages its energy level. It uses an observer pattern to notify
 * other game elements when it jumps.
 */
public class Avatar extends GameObject {

    private static final String GROUND_TAG = "ground";

    /**
     * The size of the avatar in pixels. Determines both width and height of the
     * avatar sprite.
     */
    public static final float AVATAR_SIZE = 50;

    private static final String AVATAR_TAG = "avatar";
    private static final String INITIAL_IMG_PATH = "assets/idle_0.png";

    // energy logic constants
    private static final double MAX_ENERGY = 100;
    private static final double MIN_ENERGY = 0d;
    private static final double RUN_ENERGY_COST = 0.5d;
    private static final double JUMP_ENERGY_COST = 10d;
    private static final double ENERGY_GAIN = 1.0d;

    // Physics and movement constants
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;

    MovmentState movmentState = MovmentState.IDLE;
    float currentXVel = 0f;
    private final UserInputListener inputListener;
    private final AnimationHandler animationHandler;
    private double energy;
    private final List<AvatarObserver> observers = new ArrayList<>();

    /**
     * Creates a new Avatar instance with initial position, input controls, and
     * animation capabilities.
     *
     * @param pos           Initial position vector of the avatar
     * @param inputListener System for handling keyboard input
     * @param imageReader   Utility for loading sprite images
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(AVATAR_SIZE), imageReader.readImage(INITIAL_IMG_PATH, true));
        this.animationHandler = new AnimationHandler(imageReader, this::renderer);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.setTag(AVATAR_TAG);
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
    }


    /**
     * Updates the avatar's state each frame, including:
     * - Movement based on input
     * - Energy management
     * - Animation states
     * - Observer notifications
     *
     * @param deltaTime Time elapsed since last update in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentXVel = 0;
        boolean isRightPressed = inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean isLeftPressed = inputListener.isKeyPressed(KeyEvent.VK_LEFT);
        boolean isSpacePressed = inputListener.isKeyPressed(KeyEvent.VK_SPACE);
        boolean isOnGround = getVelocity().y() == 0;
        boolean shouldFlipImage = false;

        // 1. Check for idle states (both keys pressed OR neither key pressed)
        if ((isLeftPressed && isRightPressed) || (!isLeftPressed && !isRightPressed)) {
            dontMove();
        } 
        // 2. Check for moving left
        else if (isLeftPressed && energy >= RUN_ENERGY_COST) {
            runLeft();
            shouldFlipImage = true;
        } 
        // 3. Check for moving right
        else if (isRightPressed && energy >= RUN_ENERGY_COST) {
            runRight();
        } 
        // 4. Failsafe (e.g., key is pressed but energy is too low)
        else {
            dontMove();
        }

        // 5. Check for jump
        if (isSpacePressed && isOnGround && (this.energy >= JUMP_ENERGY_COST)) {
            jump();
        }

        transform().setVelocityX(currentXVel);
        animationHandler.update(movmentState, shouldFlipImage);
        }

    private void dontMove() {
        movmentState = MovmentState.IDLE;
        this.energy = Math.min(this.energy + ENERGY_GAIN, MAX_ENERGY);
    }

    private void runLeft() {
        currentXVel -= VELOCITY_X;
        movmentState = MovmentState.RUN;
        this.energy = Math.max(this.energy - RUN_ENERGY_COST, MIN_ENERGY);
    }

    private void runRight() {
        currentXVel += VELOCITY_X;
        movmentState = MovmentState.RUN;
        this.energy = Math.max(this.energy - RUN_ENERGY_COST, MIN_ENERGY);
    }

    private void jump() {
        transform().setVelocityY(VELOCITY_Y);
        movmentState = MovmentState.JUMP;
        this.energy = Math.max(this.energy - JUMP_ENERGY_COST, MIN_ENERGY);
        this.notifyObservers();
    }

    /**
     * Handles collision with other game objects, specifically setting velocity to
     * zero
     * when colliding with the ground.
     *
     * @param other     The other game object involved in the collision
     * @param collision The details of the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(GROUND_TAG)) {
            this.transform().setVelocityY(0);
        }
    }

    /**
     * Gets the current energy level of the avatar.
     *
     * @return Current energy value between MIN_ENERGY and MAX_ENERGY
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Adds given value to energy while keeping it in boundaries.
     *
     * @param energyToAdd amount of energy to add.
     *                    works with both positive and negative vals.
     */
    public void addToEnergy(double energyToAdd) {
        this.energy = Math.max(this.energy + energyToAdd, MIN_ENERGY);
        this.energy = Math.min(this.energy, MAX_ENERGY);
    }

    /**
     * Adds an observer to be notified of avatar jump events.
     *
     * @param observer The observer to add
     */
    public void addObserver(AvatarObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     *
     * @param observer The observer to remove
     */
    public void removeObserver(AvatarObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers when the avatar jumps.
     */
    private void notifyObservers() {
        for (AvatarObserver observer : observers) {
            observer.execute();
        }
    }
}
