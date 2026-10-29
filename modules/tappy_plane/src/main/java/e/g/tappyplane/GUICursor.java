/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane;

import com.jme3.asset.AssetManager;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Utilities class; responsible for managing the game cursor in the scene.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class GUICursor implements ActionListener {

    /** Cursor texture in normal mode. */
    public static final String CURSOR_NORMAL = "Interface/Cursor/tap.png";
    /** Cursor texture when pressed. */
    public static final String CURSOR_PRESSED = "Interface/Cursor/tapTick.png";
    /** Input name. */
    private static final String GUI_NAME = "GUICursor";

    /** InputManager */
    private final InputManager inputManager;
    /** Normal cursor. */
    private final JmeCursor normal;
    /** Pressed cursor.*/
    private final JmeCursor pressed;

    /**
     * Constructor.
     *
     * @param inputManager InputManager
     * @param assetManager AssetManager
     */
    public GUICursor(InputManager inputManager, AssetManager assetManager) {
        this.inputManager = inputManager;
        this.normal = getJmeCursorFromClassPath(CURSOR_NORMAL, assetManager, false);
        this.pressed = getJmeCursorFromClassPath(CURSOR_PRESSED, assetManager, true);
    }

    /**
     * Configure the use of this custom cursor manager.
     */
    public void install() {
        inputManager.setMouseCursor(normal);
        inputManager.addMapping(GUI_NAME, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, GUI_NAME);
    }

    /**
     * Simple action.
     *
     * @param name String
     * @param isPressed boolean
     * @param tpf float
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (GUI_NAME.equals(name)) {
            inputManager.setMouseCursor(isPressed ? pressed : normal);
        }
    }

    /**
     * Returns a {@link JmeCursor} object that can be used as a mouse cursor
     * icon.
     *
     * @param path the icon path
     * @param assetManager jme3 resource manager
     * @return A {@code JmeCursor} object
     */
    public static JmeCursor getJmeCursorFromClassPath(String path, AssetManager assetManager) {
        return getJmeCursorFromClassPath(path, assetManager, true);
    }

    /**
     * Returns a {@link JmeCursor} object that can be used as a mouse cursor
     * icon.
     *
     * @param path the icon path
     * @param assetManager jme3 resource manager
     * @param isARGBA is argba
     * @return A {@code JmeCursor} object
     */
    public static JmeCursor getJmeCursorFromClassPath(String path, AssetManager assetManager, boolean isARGBA) {
        Texture cursorTexture = assetManager.loadTexture(path);

        Image image = cursorTexture.getImage();
        ByteBuffer imgByteBuff = (ByteBuffer) image.getData(0).rewind();
        IntBuffer curIntBuff = BufferUtils.createIntBuffer(image.getHeight() * image.getWidth());

        while (imgByteBuff.hasRemaining()) {
            int rgba = imgByteBuff.getInt();
            int argb = ((rgba & 255) << 24) | (rgba >> 8);
            curIntBuff.put(isARGBA ? argb : rgba);
        }

        JmeCursor c = new JmeCursor();
        c.setHeight(image.getHeight());
        c.setWidth(image.getWidth());
        c.setNumImages(1);
        c.setyHotSpot(image.getHeight() - 3);
        c.setxHotSpot(3);
        c.setImagesData((IntBuffer) curIntBuff.rewind());
        return c;
    }
}
