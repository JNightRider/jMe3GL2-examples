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

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.enums.GroundType;

import org.box2d.jni.*;
import org.box2d.jni.system.*;

import static org.box2d.jni.include.Box2d.*;
import static org.box2d.jni.include.Types.*;
import static org.box2d.jni.system.ArenaAlloc.*;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.KinematicBody2D;
import org.je3gl.box2d.util.ParsePath;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * The terrain that appears in the scenes.
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

    /**
     * Constructor.
     *
     * @param camera Camera
     */
    public Ground(Camera camera) {
        this.camera = camera;
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
            physicsSpace.enqueue(() -> {
                physicsSpace.removeBody(this);
            });
        }
    }

    /**
     * Returns a new instance of the ground.
     *
     * @param app main application
     * @param type the type of ground (texture)
     * @return Ground
     */
    public static Ground getNewInstanceGround(TappyPlane app, GroundType type) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, type.getPath());
        mat.setFloat("AlphaDiscardThreshold", 0.5F);

        float w = 1845f, h = 164;

        Sprite sprite = new Sprite(w / h, 1.0f);
        sprite.applyScale(2.0f);
        Geometry geom = new Geometry("Player", sprite);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Ground ground = new Ground(app.getCamera());
        //ground.setType(b2_staticBody);
        ground.setPosition(new Vector2f(0, -5));
        physicsSpace.addBody(ground);
        try (ArenaAlloc arena = allocPush()) {
            String path ="""
                    M 0.001418,1.989986
                    L 0.000000,0.987124
                    L 0.934538,0.924178
                    L 1.152496,1.145922
                    L 2.522948,1.250358
                    L 3.739546,0.924178
                    L 4.370086,0.207440
                    L 6.916388,0.092990
                    L 8.583166,0.742490
                    L 9.719978,0.745350
                    L 10.400940,0.324750
                    L 12.154612,0.323320
                    L 13.094842,1.226038
                    L 14.163300,1.220314
                    L 14.864198,1.513590
                    L 15.919808,1.519314
                    L 16.654906,0.748212
                    L 17.705240,0.653792
                    L 18.317408,0.082976
                    L 20.703600,0.000000
                    L 21.254918,0.712446
                    L 22.497142,0.998570
                    L 22.500000,2.000000
                    """;

            b2Vec2.Buffer points = b2Vec2.calloc(64, arena);
            b2Vec2 offset = b2Vec2.calloc(arena).set(-sprite.getWidth()/2.0f, -sprite.getHeight()/2.0f);
            int count = ParsePath.parse(path, offset, points, 64, 1.0f, false );

            b2ChainDef chainDef = b2DefaultChainDef(b2ChainDef.calloc(arena));
            chainDef.points(points);
            chainDef.pointCount(count);
            chainDef.isLoop(true);
            b2CreateChain( ground.getBodyId(), chainDef, b2ChainId.calloc(arena));
        }
        
        geom.addControl(ground);
        return ground;
    }
}
