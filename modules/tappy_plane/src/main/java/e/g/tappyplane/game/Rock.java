/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.enums.RockType;

import org.box2d.jni.*;
import org.box2d.jni.system.*;

import static org.box2d.jni.b2BodyType.*;
import static org.box2d.jni.include.Collision.*;
import static org.box2d.jni.include.Types.*;
import static org.box2d.jni.system.ArenaAlloc.*;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.KinematicBody2D;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * Manage the body of a rock.
 *
 * @see RockType
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Rock extends KinematicBody2D {
    /** Name */
    public static final String NAME = "Rock";

    /** The world's camera.  */
    private final Camera camera;
    /** Orientation */
    private final boolean isDown;

    /**
     * A flag that verifies the state of life of this body.
     */
    private boolean isDead;

    /**
     * Constructor.
     *
     * @param camera Camera
     * @param isDown boolean
     */
    public Rock(Camera camera, boolean isDown) {
        this.camera = camera;
        this.isDown = isDown;
    }

    /**
     * Simple update
     *
     * @param delta float
     */
    @Override
    protected void physicsProcess(float delta) {
        Vector3f position = getPosition();
        Vector3f location = camera.getLocation();

        Sprite sprite = (Sprite) ((Geometry) spatial).getMesh();
        if ((position.x + sprite.getWidth() / 2.0f) < (location.x + camera.getFrustumLeft())) {
            queueFree();
        }
    }

    /**
     * Free all the resources that this physical body uses.
     */
    @Override
    public void queueFree() {
        if (spatial.removeFromParent()) {
            isDead = true;
            physicsSpace.enqueue(() -> {
                physicsSpace.removeBody(this);
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
     * Returns the orientation.
     *
     * @return boolean
     */
    public boolean isDown() {
        return isDown;
    }

    /**
     * Returns the life indicator,
     *
     * @return boolean
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Returns a new instance of the rock.
     *
     * @param app main application
     * @param type the type of rock (texture)
     * @param isDown
     * @return Rock
     */
    public static Rock getNewInstanceRock(TappyPlane app, RockType type, boolean isDown) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, isDown ? type.getPathDown() : type.getPathUp());
        mat.setFloat("AlphaDiscardThreshold", 0.5F);

        float w = 247, h = 545;

        Sprite sprite = new Sprite(1.0f, h / w);
        sprite.applyScale(2.0f);
        Geometry geom = new Geometry(NAME, sprite);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Rock rock = new Rock(app.getCamera(), isDown);
        rock.setType(b2_staticBody);
        physicsSpace.addBody(rock);

        w = sprite.getWidth();
        h = sprite.getHeight();
        try (ArenaAlloc arena = allocPush()) {
            b2ShapeDef shapeDef = b2DefaultShapeDef(b2ShapeDef.calloc(arena));

            b2Vec2.Buffer vertices = b2Vec2.calloc(3, arena);
            if (!isDown) {
                vertices.get(0).set(-w / 2f, -h / 2f);
                vertices.get(1).set(w / 2f, -h / 2f);
                vertices.get(2).set(0.25f, h / 2f);
            } else {
                vertices.get(0).set(w / 2f, h / 2f);
                vertices.get(1).set(-w / 2f, h / 2f);
                vertices.get(2).set(0.25f, -h / 2f);
            }

            b2Hull hull = b2ComputeHull(vertices, 3, b2Hull.calloc(arena));
            b2Polygon polygon = b2MakePolygon(hull, 0.0f, b2Polygon.calloc(arena));

            rock.addPolygonShape(shapeDef, polygon);
        }

        geom.addControl(rock);
        return rock;
    }

    /**
     * Compute the number of stars that must be added to a rock.
     *
     * @param app TappyPlane
     * @param rock Rock
     * @param rootNode Node
     * @param amount int
     * @param isDown boolean
     */
    public static void computeStars(
            TappyPlane app,
            Rock rock,
            Node rootNode,
            int amount,
            boolean isDown
    ) {
        for (int i = 0; i < amount; i++) {
            Star star = Star.getNewInstancePlayer(app, rock, i);
            rootNode.attachChild(star.getJmeObject());
        }
    }
}
