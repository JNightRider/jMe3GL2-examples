/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.functional.VoidFunction;
import e.g.jme3hudl.ControlLayout;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Player;
import e.g.tappyplane.game.Terrain;
import e.g.tappyplane.game.anim.Effects;
import e.g.tappyplane.ui.TimerMessage;
import org.je3gl.listener.AnimationChangeListener;
import org.je3gl.listener.AnimationEvent;
import org.je3gl.plugins.input.InputHandlerAppState;
import org.je3gl.renderer.Camera2DAppSate;
import org.je3gl.scene.control.AnimatedSprite2D;
import org.je3gl.scene.control.SpriteAnimation2D;
import org.je3gl.scene.shape.Sprite;
import org.je3gl.util.Timer;
import org.je3gl.util.TimerAppState;
import org.je3gl.util.TimerTask;
import org.je3gl.utilities.MaterialUtilities;

/**
 * A state for the game logic.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class GameSceneAppState extends AbstractScreen {
    
    public static final Geometry NULL_TARGET = new Geometry();
    
    private Camera2DAppSate camera2D;
    
    /** Root node for this state. */
    private Node rootNode;

    /** Player body. */
    private Player player;
    
    private Terrain terrain;
    
    private TimerMessage timerMessage;
    
    private Geometry gameoverMessage;
    
    private Timer timer;

    /**
     * Initializes the screen properties.
     *
     * @param app 
     */
    @Override
    protected void initialize(Application app) {
        super.initialize(app);
        TappyPlane application = (TappyPlane) app;
        //----------------------------------------------------------------------
        //                              HUD
        //----------------------------------------------------------------------
        ControlLayout layout = (ControlLayout) rootContainer.getLayout();
        timerMessage = new TimerMessage(layout.getRootPane());
        timerMessage.setPreferredSize(rootContainer.getPreferredSize().clone());
        timerMessage.setCallback(startGame);
        layout.addChild(timerMessage, ControlLayout.Alignment.CenterTop);
        
        //----------------------------------------------------------------------
        //                              SCENE
        //----------------------------------------------------------------------
        rootNode = new Node("MyRootNode");
        application.getRootNode().attachChild(rootNode);

        Material mat = MaterialUtilities.getUnshadedMaterialFromClassPath(app.getAssetManager(), "Textures/game_over.png");
        mat.setFloat("AlphaDiscardThreshold", 0.2F);
        Sprite sprite = new Sprite(940f/177f, 1);

        gameoverMessage = new Geometry("Game Over", sprite);
        gameoverMessage.setMaterial(mat);
        gameoverMessage.setQueueBucket(RenderQueue.Bucket.Transparent);

        TimerAppState timerAppState = getState(TimerAppState.class);
        timer = timerAppState.attachTimer("TimerExit", new Timer(0.25f).attachTask(timerTaskExit), 0.6f);
        
        InputHandlerAppState handlerAppState = getState(InputHandlerAppState.class);
        handlerAppState.addInputHandler(Player.VM_JUMP).install();
        
        camera2D = getState(Camera2DAppSate.class);
        setEnabled(false);
    }
    
    private final AnimationChangeListener<Spatial, SpriteAnimation2D, AnimatedSprite2D>  changeListener = new AnimationChangeListener<Spatial, SpriteAnimation2D, AnimatedSprite2D>() {
        @Override
        public void beforeAnimation2DChange(AnimationEvent<Spatial, SpriteAnimation2D, AnimatedSprite2D> event) {
            
        }

        @Override
        public void afterAnimation2DChange(AnimationEvent<Spatial, SpriteAnimation2D, AnimatedSprite2D> event) {
            if (event.getFrame() == 8) {
                timer.start();
            }
        }
    };
    
    private final TimerTask timerTaskExit = (t) -> {
        setEnabled(false);
        getState(MainSceneAppState.class).setEnabled(true);
        t.stop();
    };
    
    private final VoidFunction<Void> startGame = (t) -> {
        player.setEnabled(true);
        terrain.setEnabled(true);
    };
    
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void gameOver(Vector3f position) {
        float offset;
        if (position.y >= getApplication().getCamera().getFrustumTop()) {
            offset = -1;
        } else {
            offset = 1;
        }
        
        gameoverMessage.setLocalTranslation(position.x, position.y + offset, 2);
        rootNode.attachChild(gameoverMessage);
        
        Spatial explosion = Effects.getNewInstaceExplosion((TappyPlane) getApplication(), changeListener);
        explosion.setLocalTranslation(position.x, position.y, 1f);
        rootNode.attachChild(explosion);
    }

    public void addScore(int value) {
        scores.addAmount(value);
    }
    
    /**
     * Start with the game screen
     */
    @Override
    protected void onEnable() {
        super.onEnable();
        rootNode.attachChild(player.getJmeObject());
        rootNode.attachChild(terrain.getSpatial());
        timerMessage.start();
        scores.resetScores();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        rootNode.detachAllChildren();
    }
}
