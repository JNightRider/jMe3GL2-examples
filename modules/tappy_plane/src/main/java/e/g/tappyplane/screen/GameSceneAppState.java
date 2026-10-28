/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Ground;
import e.g.tappyplane.game.Player;
import e.g.tappyplane.game.Smoke;
import e.g.tappyplane.game.Terrain;
import e.g.tappyplane.game.anim.TapAnimation;
import e.g.tappyplane.game.enums.GroundType;
import org.je3gl.plugins.input.BooleanStateMouseInputHandler;
import org.je3gl.plugins.input.InputHandlerAppState;
import org.je3gl.plugins.input.MouseButton;
import org.je3gl.renderer.Camera2DAppSate;

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
        //                              SCENE
        //----------------------------------------------------------------------
        rootNode = new Node("MyRootNode");
        application.getRootNode().attachChild(rootNode);

        //----------------------------------------------------------------------
        //                              SCENE
        //----------------------------------------------------------------------
        InputHandlerAppState handlerAppState = getState(InputHandlerAppState.class);
        handlerAppState.addInputHandler(Player.VM_JUMP).install();
        
        camera2D = getState(Camera2DAppSate.class);
        setEnabled(false);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void gameOver() {
        
    }

    /**
     * Start with the game screen
     */
    @Override
    protected void onEnable() {
        super.onEnable();
        TappyPlane app = (TappyPlane) getApplication();

        if (player == null) {
            player = Player.getNewInstancePlayer(app, true);
            camera2D.setTarget(player.getJmeObject());
        }
        if (terrain == null) {
            terrain = Terrain.getNewInstanceTerrain(app);
        }
        
        player.setEnabled(true);
        rootNode.attachChild(player.getJmeObject());
        
        terrain.setEnabled(true);
        rootNode.attachChild(terrain.getSpatial());
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        rootNode.detachAllChildren();
    }
}
