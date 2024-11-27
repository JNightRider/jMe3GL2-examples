/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.game;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

import e.g.dodgethecreeps.Dodgethecreeps;
import e.g.dodgethecreeps.screen.GameSceneAppState;

import java.util.List;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.TimeStep;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Interval;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.listener.StepListener;
import org.dyn4j.world.listener.StepListenerAdapter;
import org.je3gl.listener.SpaceListener;

import org.je3gl.physics.PhysicsSpace;
import org.je3gl.physics.control.KinematicBody2D;
import org.je3gl.physics.control.PhysicsBody2D;
import org.je3gl.scene.control.AnimatedSprite2D;
import org.je3gl.scene.shape.Sprite;
import static org.je3gl.utilities.GeometryUtilities.*;
import static org.je3gl.utilities.MaterialUtilities.*;
import static org.je3gl.utilities.TextureUtilities.*;

/**
 * Class <code>Player</code> that manages the player character.
 * @author wil
 * @version 1.0.5
 * @since 1.0.0
 */
public final class Player extends KinematicBody2D {
    
    /** The "right" key is now associated with the move_right action. */
    public static final String MOVE_RIGHT = "move_right";
    
    /** "move_left" mapped to the left arrow key. */
    public static final String MOVE_LEFT  = "move_left";
    
    /** "move_up" mapped to the up arrow key. */
    public static final String MOVE_UP    = "move_up";
    
    /** And "move_down" mapped to the down arrow key. */
    public static final String MOVE_DOWN  = "move_down";
    
    
    /** Main application. */
    private final Dodgethecreeps app;
    
    /** Player speed. */
    private final double speed = 4.0;
    
    /** Camera scene. */
    private final Camera cam;
    
    /** 
     * Flags used to indicate the direction the player will take depending 
     * on the key pressed.
     */
    private boolean right, 
                    left, 
                    up, 
                    down;
    
    /**
     * Default constructor.
     * @param app application
     */
    public Player(Dodgethecreeps app) {
        this.cam = app.getCamera();
        this.app = app;
    }
    
    /**
     * Interface for player input (via keyboard).
     */
    private final ActionListener _on_Action_Listener = (name, isPressed, tpf) -> {
        if ( isEnabled() ) {
            if (MOVE_DOWN.equals(name)) {
                down = isPressed;
            }
            if (MOVE_LEFT.equals(name)) {
                left = isPressed;
            }
            if (MOVE_RIGHT.equals(name)) {
                right = isPressed;
            }
            if (MOVE_UP.equals(name)) {
                up = isPressed;
            }
        }
    };
    
    /**
     * Contact detector with this body (player).
     */
    private final StepListener<PhysicsBody2D> _on_Step_Listener = new StepListenerAdapter<>() {
        @Override
        public void begin(TimeStep step, PhysicsWorld<PhysicsBody2D, ?> world) {
            List<ContactConstraint<PhysicsBody2D>> contacts = world.getContacts(Player.this);
            for (final ContactConstraint<PhysicsBody2D> cc : contacts) {
                PhysicsBody2D otherBody = cc.getOtherBody(Player.this);
                
                if (otherBody instanceof Mob) {
                    app.enqueue(() -> {
                        queueFree();
                        return null;
                    });                    
                    break;
                }
            }
        }
    };
    
    /**
     * Listener of physical space.
     */
    private final SpaceListener<PhysicsBody2D> _on_Space_Listener = new SpaceListener<PhysicsBody2D>() {
        @Override
        public void spaceAttached(PhysicsSpace<PhysicsBody2D> physicsSpace) {
            physicsSpace.addStepListener(_on_Step_Listener);
        }
        
        @Override
        public void spaceDetached(PhysicsSpace<PhysicsBody2D> physicsSpace) {
            physicsSpace.removeStepListener(_on_Step_Listener);
        }
    };
    
    /**
     * (non-Javadoc)
     * @see jme3gl2.physics.control.PhysicsBody2D#ready() 
     */
    @Override
    protected void ready() {
        InputManager inputManager = app.getInputManager();
        inputManager.addMapping(Player.MOVE_DOWN,   new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(Player.MOVE_UP,     new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(Player.MOVE_LEFT,   new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(Player.MOVE_RIGHT,  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(_on_Action_Listener, new String[] {
            Player.MOVE_DOWN, Player.MOVE_LEFT, Player.MOVE_RIGHT, Player.MOVE_UP
        });
        addSpaceListener(_on_Space_Listener);
    }

    /**
     * (non-Javadoc)
     * @see jme3gl2.physics.control.PhysicsBody2D#physicsProcess(float) 
     * @param delta float
     */
    @Override
    protected void physicsProcess(float delta) {        
        Vector2 velocity = new Vector2(0, 0);
        if ( right ) {
            velocity.x += 1;
        }
        if ( left ) {
            velocity.x -= 1;
        }
        if ( down ) {
            velocity.y -= 1;
        }
        if ( up ) {
            velocity.y += 1;
        }
        
        if (velocity.getMagnitude() > 0) {
            velocity = velocity.getNormalized().multiply(speed);
            spatial.getControl(AnimatedSprite2D.class).setEnabled(true);
        } else {
            spatial.getControl(AnimatedSprite2D.class).setEnabled(false);
        }
        
        Vector2 position = getTransform().getTranslation();
        position = position.add(velocity.multiply(delta));
        
        position.x = Interval.clamp(position.x, cam.getFrustumLeft(), cam.getFrustumRight());
        position.y = Interval.clamp(position.y, cam.getFrustumBottom(), cam.getFrustumTop());

        getTransform().setRotation(0);
        getTransform().setTranslation(position);
        
        Sprite sprite = (Sprite) ((Geometry) spatial).getMesh();
        if ( velocity.x != 0 ) {
            spatial.getControl(AnimatedSprite2D.class).playAnimation("walk", 0.15f);            
            sprite.flipV(false);
            
            sprite.flipH(velocity.x < 0);
        } else if ( velocity.y != 0 ) {
            spatial.getControl(AnimatedSprite2D.class).playAnimation("up", 0.15f);
            sprite.flipV(velocity.y < 0);
        }
    }
    
    /**
     * Release all the resources that this physical body uses.
     */
    @Override
    public void queueFree() {
        if (spatial.removeFromParent()) {
            physicsSpace.removeStepListener(_on_Step_Listener);
            physicsSpace.removeBody(this);
            
            InputManager inputManager = app.getInputManager();
            if (inputManager.hasMapping(MOVE_DOWN)) {
                inputManager.deleteMapping(MOVE_DOWN);
                inputManager.deleteMapping(MOVE_LEFT);
                inputManager.deleteMapping(MOVE_RIGHT);
                inputManager.deleteMapping(MOVE_UP);
            }
            inputManager.removeListener(_on_Action_Listener);
            app.getStateManager().getState(GameSceneAppState.class).gameOver();
        }
    }
    
    /**
     * Returns a new instance of the player; Set up the character model.
     * @param app main application
     * @return Player
     */
    public static Player getNewInstancePlayer(Dodgethecreeps app) {
        AssetManager assetManager = app.getAssetManager();
        
        Material mat = getUnshadedMaterialFromClassPath(assetManager, "Textures/playerGrey_walk1.png");
        mat.setFloat("AlphaDiscardThreshold", 0.0F);
        
        Sprite sprite = new Sprite(1.5F, 1.5F);        
        Geometry geom = new Geometry("Player", sprite);
        
        // Add the following animations.
        AnimatedSprite2D animatedSprite = new AnimatedSprite2D();
        animatedSprite.addAnimation("walk", new Texture[] {
            getTextureFromClassPath(assetManager, "Textures/playerGrey_walk1.png"),
            getTextureFromClassPath(assetManager, "Textures/playerGrey_walk2.png")
        });
        animatedSprite.addAnimation("up", new Texture[] {
            getTextureFromClassPath(assetManager, "Textures/playerGrey_up1.png"),
            getTextureFromClassPath(assetManager, "Textures/playerGrey_up2.png")
        });
        
        animatedSprite.setAnimationSpeed(0.60F);
        
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        BodyFixture fixture = new BodyFixture(dyn4jCreateCapsule(0.8, 1.1));
        fixture.setFilter(new LayerFilter(0));
        
        Player player = new Player(app);
        player.addFixture(fixture);
        player.setMass(MassType.NORMAL);
        
        geom.addControl(player);
        geom.addControl(animatedSprite);
        return player;
    }
}