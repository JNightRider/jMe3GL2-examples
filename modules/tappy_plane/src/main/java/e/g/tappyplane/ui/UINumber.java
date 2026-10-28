/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.ui;

import com.jme3.math.Vector3f;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.QuadBackgroundComponent;

import e.g.jme3hudl.ControlLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * A container for numbers.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class UINumber extends Container {

    /** Texture map of numbers {@code 0 – 9}. */
    private static final Map<Integer, QuadBackgroundComponent> NUMBERS = new HashMap<>();

    /** Texture resolution. */
    private static final int
            IMG_WIDTH = 124,
            IMG_HIGHT = 177;

    static {
        for (int i = 0; i < 10; i++) {
            NUMBERS.put(i, new QuadBackgroundComponent(
                    GuiGlobals.getInstance()
                            .loadTexture("Textures/Number/%d.png".formatted(i), true, false)
            ));
        }
    }

    /** Font size. */
    private float fontSize;
    /** The value. */
    private int value;

    /**
     * Constructor.
     *
     * @param rootPane RootPane
     */
    public UINumber(ControlLayout.RootPane rootPane) {
        super(new ControlLayout(rootPane));
        setFontSize(0.5f);
    }

    /**
     * Sets a new font size, ranging from 0% to 100% of the resolution of the
     * textures currently in use.
     *
     * @param fontSize float {@code 0f - 1f}
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        this.updateText();
    }

    /**
     * Sets the contained value.
     *
     * @param value int
     */
    public void setValue(int value) {        
        this.value = value;
        this.updateText();
    }

    /**
     * Return the value.
     *
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the font size.
     *
     * @return float
     */
    public float getFontSize() {
        return fontSize;
    }

    /**
     * Update the numbers to be displayed on the screen.
     */
    private void updateText() {
        detachAllChildren();
        ControlLayout layout = (ControlLayout) getLayout();

        int number = value,
            count  = 0;
        while (number > 0) {
            int digit = number % 10;

            Label string = createNewLabel(digit);
            float offset = string.getPreferredSize().x * count;
            layout.setAttribute(ControlLayout.POSITION, string, new Vector3f(offset, 0, 0));

            number /= 10;
            count++;
        }
        setPreferredSize(new Vector3f(IMG_WIDTH * fontSize * count, IMG_HIGHT * fontSize, 0));
    }

    /**
     * Create a new label with the given number.
     *
     * @param value int
     * @return Label
     */
    private Label createNewLabel(int value) {
        ControlLayout layout = (ControlLayout) getLayout();

        Label label = new Label(" ");
        label.setBackground(NUMBERS.get(value).clone());
        label.setPreferredSize(new Vector3f(IMG_WIDTH * fontSize, IMG_HIGHT * fontSize, 0f));
        layout.addChild(label, ControlLayout.Alignment.RightCenter);
        return label;
    }
}
