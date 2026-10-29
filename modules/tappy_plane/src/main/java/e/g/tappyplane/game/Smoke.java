/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.anim.ScalingAnimation;
import e.g.tappyplane.game.enums.SmokeType;
import e.g.tappyplane.math.Vector1f;

import java.util.concurrent.ThreadLocalRandom;

import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * Smoke particle.
 *
 * <p>
 * This creates a small smoke particle that expands and contracts (changes
 * scale), while gradually fading away until it disappears from the scene.
 * </p>
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Smoke extends AbstractControl {

    /** An atomic vector for the alpha channel of the smoke color. */
    private static final Vector1f ALPHA = new Vector1f(0);

    /** Smoke alpha channel (color). */
    private final Vector1f alpha = new Vector1f(1.0f);
    
    /** The color of the smoke. */
    private final ColorRGBA color = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);

    /** The world's camera.  */
    private final Camera camera;

    /**
     * Constructor.
     *
     * @param camera Camera
     */
    public Smoke(Camera camera) {
        this.camera = camera;
    }

    /**
     * Simple update.
     *
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {        
        alpha.interpolateLocal(ALPHA, tpf * 0.6f);
        color.setAlpha(alpha.x);

        if (checkOutOfBounds()) {
            spatial.removeFromParent();
        }
    }

    /**
     * Check whether the smoke particle is at a point in its life cycle (i.e.,
     * it is neither off-screen nor invisible).
     *
     * @return boolean
     */
    private boolean checkOutOfBounds() {
        if (alpha.isSimilar(ALPHA, 0.05f)) {
            return true;
        }
        float x = spatial.getLocalTranslation().x;
        return x < camera.getFrustumLeft();
    }

    /**
     * control render
     *
     * @param rm RenderManager
     * @param vp ViewPort
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        Material mat = ((Geometry) spatial).getMaterial();
        mat.setColor("Color", color);
    }

    /**
     * Returns a new instance of the smoke.
     *
     * @param app main application
     * @param position an initial position
     * @return Smoke
     */
    public static Smoke getNewInstanceSmoke(TappyPlane app, Vector3f position) {
        AssetManager assetManager = app.getAssetManager();
        SmokeType[] types = SmokeType.values();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        SmokeType smokeType = types[random.nextInt(0, types.length)];
        Material mat = getUnshadedMaterialFromClassPath(assetManager, smokeType.getPath());
        mat.setFloat("AlphaDiscardThreshold", 0.1F);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Vector2f size = smokeType.getSize();
        Sprite sprite = new Sprite(size.x, size.y);
        Geometry geom = new Geometry("Player", sprite);
        geom.setMaterial(mat);
        geom.setLocalScale(ScalingAnimation.VF_50);
        geom.setLocalTranslation(position);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Smoke smoke = new Smoke(app.getCamera());
        geom.addControl(new ScalingAnimation());
        geom.addControl(smoke);        
        return smoke;
    }
}
