package pepse.world;

/**
 * Represents the movement state of a character in the game.
 */
public enum MovmentState {
    /**
     * State when the character is not moving and standing still
     */
    IDLE,
    /**
     * State when the character is performing a vertical jump
     */
    JUMP,
    /**
     * State when the character is moving horizontally
     */
    RUN
}
