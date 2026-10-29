/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.anim;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import e.g.tappyplane.TappyPlane;
import org.je3gl.scene.control.AnimatedSprite2D;

import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;
import static org.je3gl.utilities.TextureUtilities.*;

/**
 * A short animated guide.
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class TapAnimation extends AbstractControl {

    /** Target of the guide.*/
    private Spatial target;

    /** Constructor */
    public TapAnimation() { }

    /**
     * Set a new target.
     * 
     * @param target Spatial
     */
    public void setTarget(Spatial target) {
        this.target = target;
    }

    /**
     * Manage the visibility of the guide.
     *
     * @param visible 
     */
    public void setVisible(boolean visible) {
        spatial.setCullHint(visible ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
    }

    /**
     * Simple update.
     *
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (target != null && spatial.getCullHint() == Spatial.CullHint.Always) {
            spatial.setLocalTranslation(target.getLocalTranslation());
        }
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

    /**
     * Returns a new instance of the {@link TapAnimation}.
     *
     * @param app main application
     * @return TapAnimation
     */
    public static TapAnimation getNewInstanceTapAnimation(TappyPlane app, float discance) {
        AssetManager assetManager = app.getAssetManager();
        Node node = new Node("TapAnimation");
        node.setLocalScale(0.5f);
        {
            Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/tap_l.png");
            mat.setFloat("AlphaDiscardThreshold", 0.5F);

            Sprite sprite = new Sprite(194f / 97f, 1.0f);
            Geometry geom = new Geometry("G0", sprite);
            geom.setMaterial(mat);
            geom.move(-sprite.getWidth() / 2f, 0, 0);
            node.attachChild(geom);
        }
        {
            Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/tap_r.png");
            mat.setFloat("AlphaDiscardThreshold", 0.5F);

            Sprite sprite = new Sprite(194f / 97f, 1.0f);
            Geometry geom = new Geometry("G1", sprite);
            geom.setMaterial(mat);
            geom.move((sprite.getWidth() / 2f) + discance, 0, 0);
            node.attachChild(geom);
        }
        {
            Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/cursor_01.png");
            mat.setFloat("AlphaDiscardThreshold", 0.5F);

            Sprite sprite = new Sprite(1.0f, 1.0f);
            Geometry geom = new Geometry("G1", sprite);
            geom.setMaterial(mat);
            geom.move(sprite.getWidth() * 2f + 0.25f, -(sprite.getHeight() + 0.25f), 1);

            AnimatedSprite2D animated = new AnimatedSprite2D();
            animated.addAnimation("idle", getArrayTextureFromClassPath(assetManager,
                    "Textures/cursor_01.png",
                    "Textures/cursor_02.png"
            ));
            animated.setAnimationSpeed(0.6f);
            animated.playAnimation("idle", 0.5f);
            geom.addControl(animated);
            node.attachChild(geom);
        }

        ScalingAnimation animation = new ScalingAnimation(new Vector3f(0.75f, 0.75f, 0.75f), new Vector3f(1, 1, 1));
        animation.setSpeed(1);

        TapAnimation ta = new TapAnimation();
        node.addControl(animation);
        node.addControl(ta);
        return ta;
    }
}
