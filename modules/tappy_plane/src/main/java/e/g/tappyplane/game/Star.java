/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;

import e.g.tappyplane.TappyPlane;

import org.box2d.jni.b2Circle;
import org.box2d.jni.b2ShapeDef;
import org.box2d.jni.include.Box2d;
import org.box2d.jni.system.ArenaAlloc;
import static org.box2d.jni.include.Types.*;
import static org.box2d.jni.system.ArenaAlloc.*;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.KinematicBody2D;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * A star that the player can collect to earn points (score)
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Star extends KinematicBody2D {
    /** An identifier for a star. */
    public static final long
            ID_STAR      = 0x0001l,
            ID_STAR_DEAD = 0x0002l;;

    /** Position. */
    private final Vector2f location = new Vector2f();
    /** The rock. */
    private final Rock rock;

    /**
     * Position or index that this star occupies on the rock.
     */
    private final int index;

    /**
     * A flag that verifies the state of life of this body.
     */
    private boolean isDead;

    /**
     * Constructor.
     *
     * @param rock Rock
     * @param index int
     */
    public Star(Rock rock, int index) {
        this.rock = rock;
        this.index = index;
    }

    /**
     * Simple update.
     *
     * @param delta float
     */
    @Override
    protected void physicsParallel(float delta) {
        if (rock.isDead() || isDead) {
            queueFree();
            return;
        }

        Vector3f rockPosition = rock.getPosition();
        float radius = 4;
        float rotation = rock.isDown() ? 270 : 90;
        float angleStep = 360f / 12f;

        // Angle of this object
        float angle = rotation + index * angleStep;
        float radians = (float) Math.toRadians(angle);

        // Calculate position
        location.x = rockPosition.x + radius * (float) Math.cos(radians);
        location.y = rockPosition.y + radius * (float) Math.sin(radians);
        setPosition(location);

        long userData = Box2d.b2Body_GetUserData(bodyId);
        if (userData == ID_STAR_DEAD) {
            isDead = true;
        }
    }

    /**
     * Free all the resources that this physical body uses.
     */
    @Override
    public void queueFree() {
        if (spatial.removeFromParent()) {
            physicsSpace.enqueue(() -> {
                physicsSpace.removeBody(this);
                close();
            });
        }
    }

    /**
     * Returns the mesh of this body.
     *
     * @return Sprite
     */
    public Sprite getSprite() {
        return (Sprite) ((Geometry) spatial).getMesh();
    }

    /**
     * Returns a new instance of the star
     *
     * @param app main application
     * @param rock target
     * @param index int
     * @return Star
     */
    public static Star getNewInstancePlayer(TappyPlane app, Rock rock, int index) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/star.png");
        mat.setFloat("AlphaDiscardThreshold", 0.5F);

        Sprite sprite = new Sprite(89f / 85f, 1.0f);
        sprite.applyScale(0.75f);
        Geometry geom = new Geometry("Star", sprite);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Star star = new Star(rock, index);
        physicsSpace.addBody(star);

        try (ArenaAlloc arena = allocPush()) {
            b2ShapeDef shapeDef = b2DefaultShapeDef(b2ShapeDef.calloc(arena));
            shapeDef.isSensor(true);
            shapeDef.enableSensorEvents(true);
            shapeDef.userData(ID_STAR);

            b2Circle circle = b2Circle.calloc(arena);
            circle.radius(0.4f);
            star.addCircleShape(shapeDef, circle);
        }

        geom.addControl(star);
        return star;
    }
}
