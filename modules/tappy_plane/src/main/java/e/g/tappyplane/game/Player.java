/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import e.g.tappyplane.TappyPlane;
import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.RigidBody2D;
import org.je3gl.scene.control.AnimatedSingleSprite2D;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * Class <code>Player</code> that manages the player character.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class Player extends RigidBody2D {

    /**
     * Returns a new instance of the player; Set up the character model.
     *
     * @param app main application
     * @return Player
     */
    public static Player getNewInstancePlayer(TappyPlane app) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/planes.png");
        mat.setFloat("AlphaDiscardThreshold", 0.0F);

        Sprite sprite = new Sprite(1.3f, 1.0f, 3, 1, 0, 0);
        sprite.applyScale(2.0f);
        Geometry geom = new Geometry("Player", sprite);

        // Add the following animations.
        AnimatedSingleSprite2D animatedSprite = new AnimatedSingleSprite2D();
        animatedSprite.addAnimation("idle", 0);
        animatedSprite.addAnimation("fly", 0, 1, 2);

        animatedSprite.setAnimationSpeed(0.60F);
        animatedSprite.playAnimation("fly", 0.1f);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Player player = new Player();
        physicsSpace.addBody(player);
        
        {
            
        }
        
        geom.addControl(player);
        geom.addControl(animatedSprite);
        return player;
    }
}
