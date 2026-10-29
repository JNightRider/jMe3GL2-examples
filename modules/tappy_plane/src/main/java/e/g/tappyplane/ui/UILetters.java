/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.ui;

import com.jme3.math.Vector3f;
import com.jme3.texture.Image;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.QuadBackgroundComponent;

import e.g.jme3hudl.ControlLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * A container for letters.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class UILetters extends Container {
    /** Texture map of letters {@code 0 – 9}. */
    private static final Map<Character, QuadBackgroundComponent> LETTERS = new HashMap<>();

    /** Texture resolution. */
    private static final int IMG_HIGHT = 315;

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            char car = (char)i;
            LETTERS.put(car, new QuadBackgroundComponent(
                    GuiGlobals.getInstance()
                            .loadTexture("Textures/Letters/%s.png".formatted(String.valueOf(car)), true, false)
            ));
        }
    }

    /** Font size. */
    private float fontSize;
    /** The value. */
    private String value;
 
    /**
     * Constructor.
     *
     * @param rootPane RootPane
     */
    public UILetters(ControlLayout.RootPane rootPane) {
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
    public void setValue(String value) {        
        this.value = value;
        this.updateText();
    }

    /**
     * Return the value.
     *
     * @return String
     */
    public String getValue() {
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
     * Update the letters to be displayed on the screen.
     */
    private void updateText() {
        detachAllChildren();
        ControlLayout layout = (ControlLayout) getLayout();
        if (value == null) {
            return;
        }

        int count = value.length();
        float offset = 0;
        for (int i = 0; i < count; i++) {
            char car = value.charAt(i);
            Label string = createNewLabel(Character.toLowerCase(car));
            layout.setAttribute(ControlLayout.POSITION, string, new Vector3f(offset, 0, 0));
            offset += string.getPreferredSize().x + (fontSize * 10f);
        }
        setPreferredSize(new Vector3f(offset, IMG_HIGHT * fontSize, 0)); 
    }

    /**
     * Create a new label with the given character.
     *
     * @param value int
     * @return Label
     */
    private Label createNewLabel(char value) {
        ControlLayout layout = (ControlLayout) getLayout();
        QuadBackgroundComponent component = LETTERS.get(value).clone();
        Image image = component.getTexture().getImage();

        Label label = new Label(" ");
        label.setBackground(component);
        label.setPreferredSize(new Vector3f(image.getWidth() * fontSize, image.getHeight() * fontSize, 0f));
        layout.addChild(label, ControlLayout.Alignment.LeftCenter);
        return label;
    }
}
