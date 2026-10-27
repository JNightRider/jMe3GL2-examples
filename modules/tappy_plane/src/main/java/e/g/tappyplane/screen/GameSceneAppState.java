/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Ground;
import e.g.tappyplane.game.Player;
import e.g.tappyplane.game.Smoke;
import e.g.tappyplane.game.enums.GroundType;
import org.je3gl.plugins.input.InputHandlerAppState;
import org.je3gl.renderer.Camera2DAppSate;

/**
 * A state for the game logic.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class GameSceneAppState extends AbstractScreen {

    private Camera2DAppSate camera2D;
    
    /** Root node for this state. */
    private Node rootNode;

    /** Player body. */
    private Player player;

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
        handlerAppState.addInputHandler(Player.VK_JUMP).install();
        handlerAppState.addInputHandler(Player.VM_JUMP).install();
        
        camera2D = getState(Camera2DAppSate.class);
    }
    
    

    /**
     * Start with the game screen
     */
    @Override
    protected void onEnable() {
        super.onEnable();
        TappyPlane app = (TappyPlane) getApplication();

        player = Player.getNewInstancePlayer(app, true);
        rootNode.attachChild(player.getJmeObject());
        camera2D.setTarget(player.getJmeObject());

        Ground ground = Ground.getNewInstanceGround(app, GroundType.GRASS, 2);
        rootNode.attachChild(ground.getJmeObject());
    }
}
