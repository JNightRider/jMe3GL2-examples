/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.enums.GroundType;

import java.util.concurrent.ThreadLocalRandom;

import org.box2d.jni.*;
import org.box2d.jni.system.*;

import static org.box2d.jni.include.Collision.*;
import static org.box2d.jni.include.MathTypes.b2Rot_identity;
import static org.box2d.jni.include.Types.*;
import static org.box2d.jni.system.ArenaAlloc.*;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.KinematicBody2D;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * The ground that appears in the scenes.
 *
 * @see GroundType
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Ground extends KinematicBody2D {

    /** The world's camera.  */
    private final Camera camera;

    /** Main application. */
    private final TappyPlane app;

    /** The type of ground */
    private final GroundType type;

    /**
     * The amount of rocks present in this soil when terraforming it.
     */
    private final int rockCount;

    /**
     * Constructor.
     *
     * @param app TappyPlane
     * @param type GroundType
     * @param rockCount int
     */
    public Ground(TappyPlane app, GroundType type, int rockCount) {    
        this.camera = app.getCamera();
        this.app = app;
        this.type = type;
        this.rockCount = rockCount;
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

        Sprite sprite = getSprite();
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
            physicsSpace.enqueue(() -> {
                physicsSpace.removeBody(this);
            });
        }
    }

    /**
     * (non-Javadoc)
     * @param position Vector2f
     */
    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        Node node = getJmeObject();
        if (node == null) {
            return;
        }

        for (Spatial child : node.getChildren()) {
            if (Rock.NAME.equals(child.getName())) {
                Rock rock = child.getControl(Rock.class);
                Vector3f location = rock.getPosition();

                rock.setPosition(new Vector2f(position.x + location.x, location.y));
            }
        }
    }

    /**
     * Returns the mesh of this body.
     *
     * @return Sprite
     */
    public Sprite getSprite() {
        return (Sprite) ((Geometry) ((Node) spatial).getChild("Ground")).getMesh();
    }

    /**
     * Return the main application
     *
     * @return TappyPlane
     */
    public TappyPlane getApp() {
        return app;
    }

    /**
     * Returns the ground type
     *
     * @return GroundType
     */
    public GroundType getType() {
        return type;
    }

    /**
     * Returns the amount of rock
     *
     * @return int
     */
    public int getRockCount() {
        return rockCount;
    }

    /**
     * Returns a new instance of the ground.
     *
     * @param app main application
     * @param type the type of ground (texture)
     * @param rockCount quantity of rocks the ground will contain
     * @return Ground
     */
    public static Ground getNewInstanceGround(TappyPlane app, GroundType type, int rockCount) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, type.getPath());
        mat.setFloat("AlphaDiscardThreshold", 0.5F);

        float w = 1845f, h = 164;

        Sprite sprite = new Sprite(w / h, 1.0f);
        sprite.applyScale(2.0f);

        Node rootNode = new Node();
        Geometry geom = new Geometry("Ground", sprite);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Ground ground = new Ground(app, type, rockCount);
        ground.setPosition(new Vector2f(0, app.getCamera().getFrustumBottom() + (sprite.getHeight() / 2.0f)));
        physicsSpace.addBody(ground);
        try (ArenaAlloc arena = allocPush()) {
            b2ShapeDef shapeDef = b2DefaultShapeDef(b2ShapeDef.calloc(arena));
            ground.addPolygonShape(
                shapeDef,
                b2MakeOffsetBox(sprite.getWidth() / 2f, 0.5f, b2Vec2.calloc(arena).set(0, -0.5f), b2Rot_identity, b2Polygon.calloc(arena))
            );            
            ground.addPolygonShape(
                shapeDef,
                b2MakeOffsetBox(sprite.getWidth() / 5f, 0.5f, b2Vec2.calloc(arena).set(-3f, 0.5f), b2Rot_identity, b2Polygon.calloc(arena))
            );            
            ground.addPolygonShape(
                shapeDef,
                b2MakeOffsetBox(2, 0.5f, b2Vec2.calloc(arena).set(8f, 0.5f), b2Rot_identity, b2Polygon.calloc(arena))
            );
        }
        rootNode.addControl(ground);
        rootNode.attachChild(geom);
        if (rockCount > 0) {
            terraformGround(ground, false);
            terraformGround(ground, true);
        }
        return ground;
    }

    /**
     * It modifies the terrain; this causes some rocks to be added.
     *
     * @param myGround Ground
     * @param isDown boolean
     */
    public static void terraformGround(Ground myGround, boolean isDown) {
        Node rootNode = myGround.getJmeObject();
        Sprite ground = (Sprite) ((Geometry) rootNode.getChild("Ground")).getMesh();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        float maxWidth = ground.getWidth();
        float xInit = -maxWidth / 2.0f,
              yInit = isDown ? myGround.getApp().getCamera().getFrustumTop() : myGround.getPosition().y;

        int[] cache = new int[myGround.getRockCount()];
        for (int i = 0; i < myGround.getRockCount(); i++) {
            Rock rock = Rock.getNewInstanceRock(myGround.getApp(), myGround.getType().getRockType(), isDown);
            Sprite sprite = rock.getSprite();

            float offsetY = yInit;
            if (isDown) {
                offsetY -= sprite.getHeight() / 2.0f - 1;
            }
            offsetY += random.nextFloat(-0.2f, 1.15f);
            
            if (!isDown && i == 0) {
                rock.setPosition(new Vector2f(xInit, offsetY));
                rootNode.attachChild(rock.getJmeObject());
            } else {
                int idx = nexrRandPos(cache);
                cache[i] = idx;
                rock.setPosition(new Vector2f(xInit + (sprite.getWidth() * idx), offsetY + random.nextFloat(-0.15f, 1.0f)));
                rootNode.attachChildAt(rock.getJmeObject(), 0);
            }
        }
    }

    /**
     * Find the next position randomly.
     *
     * @param cache occupied positions.
     * @return position (index)
     */
    private static int nexrRandPos(int[] cache) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int rand = random.nextInt(1, 10);
        for (int i : cache) {
            if (i == rand) {
                rand++;
            }
        }
        return rand;
    }
}
