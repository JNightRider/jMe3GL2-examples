/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import com.simsilica.lemur.GuiGlobals;

import e.g.dodgethecreeps.screen.MainSceneAppState;
import e.g.dodgethecreeps.screen.GameSceneAppState;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

import jme3gl2.physics.Dyn4jAppState;
import jme3gl2.physics.ThreadingType;
import jme3gl2.physics.control.PhysicsBody2D;
import jme3gl2.renderer.Camera2DRenderer;
import jme3gl2.util.TimerAppState;

/**
 * Main class for the game <b>Dodge The Creeps</b>.
 * @author wil
 * @version 1.0.5
 * @since 1.0.0
 */
public final class Dodgethecreeps extends SimpleApplication {

    /**
     * The main method; uses zero arguments in the args array.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dodgethecreeps app = new Dodgethecreeps();
        
        // This game is designed for portrait mode, so we need to adjust the 
        // size of the game window.
        AppSettings settings = new AppSettings(true);
        settings.setGammaCorrection(false);
        
        // set "Width" to 480 and "Height" to 720.
        settings.setResolution(480, 720);
        
        // Set a title for the window and an icon.
        settings.setTitle("Dodge the Creeps");
        try {
            settings.setIcons(new Image[] {
                ImageIO.read(JmeSystem.getResource("/Interface/Icons/icon_x120.png")),
                ImageIO.read(JmeSystem.getResource("/Interface/Icons/icon_x186.png")),
                ImageIO.read(JmeSystem.getResource("/Interface/Icons/icon_x80.png")),
                ImageIO.read(JmeSystem.getResource("/Interface/Icons/icon_x25.png"))
            });
        } catch (IOException e) { }
        
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
        setDisplayFps(false);
        setDisplayStatView(false);
        
        // Initialize the globals access so that the default
        // components can find what they need.
        GuiGlobals.initialize(this);
        
        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        LemurGuiStyle.loadAppStyle(assetManager);
        
        // The physical state is initialized.
        Dyn4jAppState<PhysicsBody2D> dyn4jAppState = new Dyn4jAppState<>(ThreadingType.PARALLEL);
        dyn4jAppState.setDebug(false);
        stateManager.attach(dyn4jAppState);
        
        // Set the camera to 2D, with a distance of 5 units.
        Camera2DRenderer camera2DRenderer = new Camera2DRenderer(Camera2DRenderer.GLRendererType.GL_2D, 5.0F, 0.01F);
        camera2DRenderer.getJme3GL2Camera().setProperty("InterpolationByTPF", false);
        stateManager.attach(camera2DRenderer);
        
        // auxiliary states
        TimerAppState timerAppState = new TimerAppState();
        stateManager.attach(timerAppState);
        
        // scene states; where the game is managed
        MainSceneAppState sceneAppState = new MainSceneAppState();
        GameSceneAppState gameAppState  = new GameSceneAppState();
        stateManager.attach(sceneAppState);
        stateManager.attach(gameAppState);
        
        // set the background color, a type of green.
        viewPort.setBackgroundColor(new ColorRGBA(0.224f, 0.427f, 0.439f, 1.0f));
    }

    /**
     * Returns the application settings.
     * @return AppSettings
     */
    public AppSettings getSettings() {
        return settings;
    }
}
