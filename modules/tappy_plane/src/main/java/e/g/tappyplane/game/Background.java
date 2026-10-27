/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

import e.g.tappyplane.TappyPlane;

import java.util.ArrayList;
import java.util.List;

import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * A background for the game's scenes.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Background extends AbstractControl {

    /**
     * A {@code Panel} is a section of the canvas that forms the background for
     * the scenes.
     */
    public static final class Panel extends AbstractControl {

        /** The world's camera.  */
        private final Camera camera;
        /** A fragment of the background. */
        Geometry drawable;

        /**
         * A reference to the previous background segment, used to calculate the
         * next position of this segment as it moves off-screen.
         */
        Geometry beforeDrawable;

        /**
         * Constructor.
         *
         * @param assetManager AssetManager
         * @param camera Camera
         */
        public Panel(AssetManager assetManager, Camera camera) {
            Sprite sprite = new Sprite(1.5f, 1.0f);
            sprite.applyScale(10);

            drawable = new Geometry("G1", sprite);
            Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/background.png");
            mat.setFloat("AlphaDiscardThreshold", 0.0F);

            drawable.setMaterial(mat);
            drawable.setQueueBucket(RenderQueue.Bucket.Translucent);
            drawable.addControl(Panel.this);
            this.camera = camera;
        }

        /**
         * Returns the mesh used by this panel.
         *
         * @return Sprite
         */
        public Sprite getSprite() {
            return (Sprite) drawable.getMesh();
        }

        /**
         * Simple update
         *
         * @param tpf float
         */
        @Override
        protected void controlUpdate(float tpf) {
            Vector3f myPos = drawable.getLocalTranslation();
            Vector3f location = camera.getLocation();
            myPos.setY(location.y);
            drawable.setLocalTranslation(myPos);
        }

        /**
         * Simple render
         *
         * @param rm RenderManager
         * @param vp ViewPort
         */
        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {

        }
    }

    /** List of fragments that make up the background.  */
    private final List<Panel> panels = new ArrayList<>();
    /** The world's camera.  */
    private final Camera camera;

    /**
     * Rotating index that manages the positions of each fragment
     * ({@link Panel}) in the scene background.
     */
    private int index = 0;

    /**
     * Constructor.
     *
     * @param camera Camera
     */
    public Background(Camera camera) {
        this.camera = camera;
    }

    /**
     * Add a new panel for the background.
     *
     * @param panel Panel
     */
    public void addPanel(Panel panel) {
        if (panel != null) {
            panels.add(panel);
            getSpatial().attachChild(panel.drawable);
        }
    }

    /**
     * Configure the links for each segment that makes up the background.
     */
    public void flush() {
        for (int i = 0; i < panels.size(); i++) {
            int nextIndex = i - 1;
            if (nextIndex < 0) {
                nextIndex = panels.size() - 1;
            }
            panels.get(i).beforeDrawable = panels.get(nextIndex).drawable;
        }
    }

    /**
     * Simple update.
     *
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {
        Panel current = panels.get(index);
        Vector3f position = current.drawable.getLocalTranslation();
        Vector3f location = camera.getLocation();

        float w = current.getSprite().getWidth();
        if ((position.x + w / 2.0f) < (location.x + camera.getFrustumLeft())) {
            nextIndex();

            Vector3f newPos = current.beforeDrawable.getLocalTranslation();
            position = new Vector3f(newPos.x + w, location.y, 0f);
            current.drawable.setLocalTranslation(position);
        }
    }

    /**
     * Obtain and verify the next index; if it falls outside the range of the
     * list of fragments, return to index 0.
     */
    private void nextIndex() {
        index++;
        if (index >= panels.size()) {
            index = 0;
        }
    }

    /**
     * Simple render.
     *
     * @param rm RenderManager
     * @param vp ViewPort
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    /**
     * Returns the drawing object.
     *
     * @return Node
     */
    @Override
    public Node getSpatial() {
        return (Node) spatial;
    }

    /**
     * Returns a new instance of the background.
     *
     * @param app main application
     * @return Background
     */
    public static Background getNewInstanceSmoke(TappyPlane app) {
        AssetManager assetManager = app.getAssetManager();

        Background background = new Background(app.getCamera());
        Node rootNode = new Node("Background");
        rootNode.move(0, 0, -1f);
        rootNode.addControl(background);

        float offset;
        for (int i = 0; i < 3; i++) {
            Panel panel = new Panel(assetManager, app.getCamera());
            offset = panel.getSprite().getWidth() * i;

            panel.drawable.move(offset - panel.getSprite().getWidth(), 0.0f, 0.0f);
            background.addPanel(panel);
        }
        background.flush();
        return background;
    }
}
