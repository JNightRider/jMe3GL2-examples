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
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Player;
import e.g.tappyplane.game.Terrain;
import e.g.tappyplane.game.anim.TapAnimation;
import org.je3gl.plugins.input.BooleanStateMouseInputHandler;
import org.je3gl.plugins.input.InputHandlerAppState;
import org.je3gl.plugins.input.MouseButton;
import org.je3gl.renderer.Camera2DAppSate;


/**
 * A state for the main scene.
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class MainSceneAppState extends AbstractScreen {
    public static final BooleanStateMouseInputHandler START = new BooleanStateMouseInputHandler(new MouseButton(MouseInput.BUTTON_LEFT, "START"));

    private Camera2DAppSate camera2D;
    
    private InputManager inputManager;
    
    /** Root node for this state. */
    private Node rootNode;

    /** Player body. */
    private Player player;
    
    private Terrain terrain;
    
    private TapAnimation tap;

    /**
     * Initializes the screen properties.
     *
     * @param app 
     */
    @Override
    protected void initialize(Application app) {
        super.initialize(app);
        TappyPlane application = (TappyPlane) app;
        inputManager = application.getInputManager();
        
        //----------------------------------------------------------------------
        //                              SCENE
        //----------------------------------------------------------------------
        rootNode = new Node("MyRootNode");
        application.getRootNode().attachChild(rootNode);
        
        tap = TapAnimation.getNewInstanceTapAnimation(application, 2.5f);
        tap.getSpatial().move(-1, 0, 0);
        tap.setVisible(false);

        //----------------------------------------------------------------------
        //                              SCENE
        //----------------------------------------------------------------------
        InputHandlerAppState handlerAppState = getState(InputHandlerAppState.class);
        handlerAppState.addInputHandler(START).install();        
        camera2D = getState(Camera2DAppSate.class);
    }

    @Override
    public void update(float tpf) {
        if (START.isActiveButNotHandled()) {
            START.setHasBeenHandled(true);

            Camera cam = getApplication().getCamera();
            CollisionResults results = new CollisionResults();

            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

            Ray ray = new Ray(click3d, dir);
            rootNode.collideWith(ray, results);

            if (results.size() > 0) {
                Geometry target = results.getClosestCollision().getGeometry();
                if (target != null && target.getControl(Player.class) != null) {
                    
                    GameSceneAppState game = getState(GameSceneAppState.class);
                    game.setTerrain(terrain);
                    game.setPlayer(player);
                    
                    setEnabled(false);
                    game.setEnabled(true);
                }
            }
        }
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

        if (player != null) {
            player.queueFree();
        }
        if (terrain != null) {
            terrain.queueFree();
        }
        
        player = Player.getNewInstancePlayer(app, true);
        player.setEnabled(false);
        rootNode.attachChild(player.getJmeObject());
        camera2D.setTarget(player.getJmeObject());

        terrain = Terrain.getNewInstanceTerrain(app);
        terrain.setEnabled(false);
        rootNode.attachChild(terrain.getSpatial());
        
        tap.setTarget(player.getJmeObject());
        tap.setVisible(true);
        rootNode.attachChild(tap.getSpatial());
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        rootNode.detachAllChildren();
    }
}
