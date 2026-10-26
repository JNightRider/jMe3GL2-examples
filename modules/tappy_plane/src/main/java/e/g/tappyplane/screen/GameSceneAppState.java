/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Player;

/**
 * A state for the game logic.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class GameSceneAppState extends AbstractScreen {

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
    }
    
    

    /**
     * Start with the game screen
     */
    @Override
    protected void onEnable() {
        super.onEnable();
        TappyPlane app = (TappyPlane) getApplication();

        player = Player.getNewInstancePlayer(app);
        rootNode.attachChild(player.getJmeObject());
    }
}
