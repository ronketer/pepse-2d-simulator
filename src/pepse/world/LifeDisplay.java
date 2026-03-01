package pepse.world;

import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.util.function.Supplier;
import danogl.GameObject;
import danogl.components.CoordinateSpace;

/**
 * Represents a display for showing the life percentage in the game world.
 * Extends the GameObject class and updates the life display based on the
 * provided supplier.
 */
public class LifeDisplay extends GameObject {

    private static final float LIFE_DISPLAY_WIDTH = 100;
    private static final float LIFE_DISPLAY_HEIGHT = 40;
    private static final String INITIAL_DISPLAY_TEXT = "100%";
    private static final String DISPLAY_FORMAT = "%.0f%%";


    private final Supplier<Double> lifeSupplier;
    private final TextRenderable textRenderable;

    /**
     * Constructs a LifeDisplay instance.
     *
     * @param topLeftCorner the top-left corner position of the display
     * @param lifeSupplier  a supplier that provides the current life value
     */
    public LifeDisplay(Vector2 topLeftCorner, Supplier<Double> lifeSupplier) {
        super(topLeftCorner, new Vector2(LIFE_DISPLAY_WIDTH, LIFE_DISPLAY_HEIGHT),
                new TextRenderable(INITIAL_DISPLAY_TEXT));
        this.textRenderable = (TextRenderable) this.renderer().getRenderable();
        this.lifeSupplier = lifeSupplier;
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the life display with the current life value.
     *
     * @param deltaTime the time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double currentValue = lifeSupplier.get();
        textRenderable.setString(String.format(DISPLAY_FORMAT, currentValue));
    }
}
