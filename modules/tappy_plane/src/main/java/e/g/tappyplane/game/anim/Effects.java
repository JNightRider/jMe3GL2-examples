/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.anim;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import e.g.tappyplane.TappyPlane;

import org.je3gl.listener.AnimationChangeListener;
import org.je3gl.scene.control.AnimatedSprite2D;
import org.je3gl.scene.control.SpriteAnimation2D;
import org.je3gl.scene.shape.Sprite;

import static org.je3gl.utilities.MaterialUtilities.*;
import static org.je3gl.utilities.TextureUtilities.*;

/**
 * Class responsible for providing effects.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Effects {

    /**
     * Returns a new instance of the explosion
     *
     * @param app main application
     * @param changeListener animation event
     * @return Player
     */
    public static Spatial getNewInstaceExplosion(TappyPlane app, AnimationChangeListener<Spatial, SpriteAnimation2D, AnimatedSprite2D> changeListener) {
        AssetManager assetManager = app.getAssetManager();
        Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/Effects/ex0.png");
        mat.setFloat("AlphaDiscardThreshold", 0.1F);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Sprite sprite = new Sprite(1f, 1f);
        sprite.applyScale(1.5f);
        Geometry geom = new Geometry("Player", sprite);

        AnimatedSprite2D animated = new AnimatedSprite2D();
        animated.addAnimation("explosion", getArrayTextureFromClassPath(assetManager,
                "Textures/Effects/ex0.png",
                "Textures/Effects/ex1.png",
                "Textures/Effects/ex2.png",
                "Textures/Effects/ex3.png",
                "Textures/Effects/ex4.png",
                "Textures/Effects/ex5.png",
                "Textures/Effects/ex6.png",
                "Textures/Effects/ex7.png",
                "Textures/Effects/ex8.png"
        ));
        animated.setAnimationLoop(false);
        animated.setAnimationSpeed(0.6f);
        animated.addAnimationChangeListener(changeListener);
        animated.playAnimation("explosion", 0.05f);

        geom.addControl(animated);
        geom.setMaterial(mat);
        return geom;
    }
}
