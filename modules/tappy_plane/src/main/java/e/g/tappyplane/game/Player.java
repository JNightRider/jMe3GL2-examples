/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.math.Vector1f;

import org.box2d.jni.*;
import org.box2d.jni.system.*;

import static org.box2d.jni.b2BodyType.*;

import static org.box2d.jni.include.Box2d.*;
import static org.box2d.jni.include.Collision.*;
import static org.box2d.jni.include.Id.*;
import static org.box2d.jni.include.MathFunctions.*;
import static org.box2d.jni.include.Types.*;
import static org.box2d.jni.system.ArenaAlloc.*;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.PhysicsSpace;
import org.je3gl.box2d.control.RigidBody2D;
import org.je3gl.plugins.input.BooleanStateKeyboardInputHandler;
import org.je3gl.plugins.input.BooleanStateMouseInputHandler;
import org.je3gl.plugins.input.Key;
import org.je3gl.plugins.input.MouseButton;
import org.je3gl.scene.control.AnimatedSingleSprite2D;
import org.je3gl.scene.shape.Sprite;
import org.je3gl.util.Timer;
import org.je3gl.util.TimerTask;
import static org.je3gl.utilities.MaterialUtilities.*;

/**
 * Class <code>Player</code> that manages the player character.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class Player extends RigidBody2D {

    public static final BooleanStateMouseInputHandler VM_JUMP = new BooleanStateMouseInputHandler(
            new MouseButton(MouseInput.BUTTON_LEFT, "MOUSE_JUMP")
    );

    public static final BooleanStateKeyboardInputHandler VK_JUMP = new BooleanStateKeyboardInputHandler(
            new Key(KeyInput.KEY_SPACE, "KEY_JUMP"),
            new Key(KeyInput.KEY_UP, "KEY_JUMP0")
    );

    /** Main application. */
    private final TappyPlane app;
    
    private final Timer timer;
    
    private final Vector1f angle;
    
    private boolean jump;
    
    private final float speedRot = 8.0f;
    private float speeedAnimation = 0.6f;

    private Vector1f linearVelocity = new Vector1f(1.5f);
    private Vector1f currentLinearVelcity = new Vector1f(1.5f);
    
    private b2ShapeId shapeId;
    
    public Player(TappyPlane app) {
        this.angle = new Vector1f();
        this.timer = new Timer(2.5f);
        this.app = app;
    }

    @Override
    public b2ShapeId addCapsuleShape(b2ShapeDef shapeDef, b2Capsule shape) {
        shapeId = super.addCapsuleShape(shapeDef, shape);
        return shapeId;
    }
    
    
    
    private final TimerTask onTimerTask = (t) -> {
        linearVelocity.x += 0.5f;
        speeedAnimation += 0.025f;
        t.reset();
    };

    @Override
    protected void ready() {
        try (ArenaAlloc arena = allocPush()) {
            b2Body_SetLinearVelocity(bodyId, b2Vec2.calloc(arena).set(currentLinearVelcity.x, 0.0f));
            b2Body_EnableContactEvents(bodyId, true);
        }
        timer.addTask(onTimerTask);
        timer.start();
    }
    

    @Override
    protected void physicsProcess(float delta) {
        timer.update(delta, 0.6f);
        angle.interpolateLocal(Vector1f.ZERO, delta * 0.6f * speedRot);
        if (jump) {
            jump = false;
            smoke();
        }
        spatial.getControl(AnimatedSingleSprite2D.class).setAnimationSpeed(speeedAnimation);
        checkContactEvents();
    }

    @Override
    protected void physicsParallel(float delta) {
        currentLinearVelcity.interpolateLocal(linearVelocity, delta * 0.6f);
        try (ArenaAlloc arena = allocPush()) {
            b2Vec2 velocity = b2Body_GetLinearVelocity(bodyId, b2Vec2.calloc(arena));
            velocity.x(currentLinearVelcity.x);
            b2Body_SetLinearVelocity(bodyId, velocity);

            
            if (VK_JUMP.isActiveButNotHandled() || VM_JUMP.isActiveButNotHandled()) {
                VK_JUMP.setHasBeenHandled(true);
                VM_JUMP.setHasBeenHandled(true);

                jump = true;
                angle.x = FastMath.PI / 4.0f;
                float mass = b2Body_GetMass(bodyId);
                b2Body_ApplyForceToCenter(bodyId, b2Vec2.calloc(arena).set(0.0f, 750.0f * mass), true);
            }

            b2WorldTransform transform = b2Body_GetTransform(bodyId, b2WorldTransform.calloc(arena));
            b2MakeRot(angle.x, transform.q());
            b2Body_SetTransform(bodyId, transform.p(), transform.q());
        }
    }

    private void checkContactEvents() {
        try (ArenaAlloc arena = allocPush()) {
            b2ContactEvents events = b2World_GetContactEvents(physicsSpace.getWorldId(), b2ContactEvents.calloc(arena));
            b2ContactBeginTouchEvent.Buffer pointer = new b2ContactBeginTouchEvent.Buffer(events.beginEvents(), events.beginCount());
            for (int i = 0; i < events.beginCount(); ++i) {
                b2ContactBeginTouchEvent event = pointer.get(i);

                b2ShapeId bodyA = event.shapeIdA();
                b2ShapeId bodyB = event.shapeIdB();

                if (B2_ID_EQUALS(bodyA, shapeId) || B2_ID_EQUALS(bodyB, shapeId)) {
                    queueFree();
                }
            }
        }
    }

    private void smoke() {
        Sprite sprite = (Sprite) ((Geometry) getJmeObject()).getMesh();
        Vector3f nPos = getPosition().add(-sprite.getWidth() / 2.0f, 0.0f, 0.0f);
        Smoke smoke = Smoke.getNewInstanceSmoke(app, nPos.setZ(-0.1f));
        spatial.getParent().attachChild(smoke.getSpatial());
    }

    @Override
    public void queueFree() {
        timer.stop();
        if (spatial.removeFromParent()) {
            physicsSpace.enqueue(() -> {
                physicsSpace.removeBody(this);
            });
        }
    }

    /**
     * Returns a new instance of the player; Set up the character model.
     *
     * @param app main application
     * @param isDynamic {@code true} if the character is playable, {@code false}
     *                  if static (non-playable)
     * @return Player
     */
    public static Player getNewInstancePlayer(TappyPlane app, boolean isDynamic) {
        AssetManager assetManager = app.getAssetManager();
        PhysicsSpace physicsSpace = app.getStateManager()
                                        .getState(Box2dAppState.class)
                                        .getPhysicsSpace();

        Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/planes.png");
        mat.setFloat("AlphaDiscardThreshold", 0.1F);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Sprite sprite = new Sprite(1.3f, 1.0f, 3, 1, 0, 0);
        sprite.applyScale(1.5f);
        Geometry geom = new Geometry("Player", sprite);

        // Add the following animations.
        AnimatedSingleSprite2D animatedSprite = new AnimatedSingleSprite2D();
        animatedSprite.addAnimation("idle", 0);
        animatedSprite.addAnimation("fly", 0, 1, 2);

        animatedSprite.setAnimationSpeed(0.60F);
        animatedSprite.playAnimation("fly", 0.1f);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        Player player = new Player(app);
        physicsSpace.addBody(player);

        if (isDynamic) {
            try (ArenaAlloc arena = allocPush()) {
                b2ShapeDef shapeDef = b2DefaultShapeDef(b2ShapeDef.calloc(arena));
                shapeDef.density(1.0f);
                shapeDef.material().friction(0.3f);
                
                b2Capsule capsule = b2Capsule.calloc(arena);
                capsule.center1().set(-0.25f, 0f);
                capsule.center2().set(0.25f, 0f);
                capsule.radius(0.5f);
                player.addCapsuleShape(shapeDef, capsule);
            }
        }

        geom.addControl(player);
        geom.addControl(animatedSprite);
        return player;
    }
}
