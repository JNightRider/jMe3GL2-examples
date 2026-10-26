/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.system.AppSettings;

import e.g.tappyplane.screen.GameSceneAppState;

import org.je3gl.box2d.Box2dAppState;
import org.je3gl.box2d.ThreadingType;
import org.je3gl.renderer.Camera2DAppSate;
import org.je3gl.renderer.UnitComparator;
import org.je3gl.util.TimerAppState;

/**
 * Main class for the game <b>Tappy Plane</b>.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class TappyPlane extends SimpleApplication {

    /**
     * The main method; uses zero arguments in the args array.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TappyPlane app = new TappyPlane();
        
        // This game is designed for portrait mode, so we need to adjust the 
        // size of the game window.
        AppSettings settings = new AppSettings(true);
        settings.setGammaCorrection(false);
        
        // set "Width" to 1024 and "Height" to 576.
        settings.setResolution(1024, 576);
        
        // Set a title for the window and an icon.
        settings.setTitle("Tappy Plane");

        app.setSettings(settings);
        app.setShowSettings(false); //Settings dialog not supported on mac
        app.start();
    }

    /**
     * (non-JavaDoc)
     * @see com.jme3.app.SimpleApplication#simpleInitApp() 
     */
    @Override
    public void simpleInitApp() {
        // The physical state is initialized.
        Box2dAppState box2dAppState = new Box2dAppState(ThreadingType.SEQUENTIAL);
        box2dAppState.setDebugEnabled(true);
        stateManager.attach(box2dAppState);

        // Set the camera to 2D, with a distance of 5 units.
        Camera2DAppSate camera2DRenderer = new Camera2DAppSate(1.0f);
        camera2DRenderer.setUnitComparator(Vector3f.UNIT_Z, UnitComparator.UType.World, RenderQueue.Bucket.Transparent);
        stateManager.attach(camera2DRenderer);

        // auxiliary states
        TimerAppState timerAppState = new TimerAppState();
        stateManager.attach(timerAppState);

        // scene states; where the game is managed
        GameSceneAppState gameAppState  = new GameSceneAppState();
        stateManager.attach(gameAppState);
    }
}
