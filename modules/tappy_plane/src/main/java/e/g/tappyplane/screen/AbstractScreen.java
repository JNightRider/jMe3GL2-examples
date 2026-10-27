/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Background;

/**
 * Abstract class for all game screens.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public abstract class AbstractScreen extends BaseAppState {

    protected Background background;
    
    /**
     * (non-JavaDoc)
     *
     * @see com.jme3.app.state.BaseAppState#initialize(com.jme3.app.Application)
     * @param app application
     */
    @Override
    protected void initialize(Application app) {
        TappyPlane application = (TappyPlane) app;
        background = Background.getNewInstanceSmoke(application);
    }

    /**
     * (non-JavaDoc)
     *
     * @param app the application
     * @see com.jme3.app.state.BaseAppState#cleanup()
     */
    @Override
    protected void cleanup(Application app) {
    }

    /**
     * (non-JavaDoc)
     *
     * @see com.jme3.app.state.BaseAppState#onDisable()
     */
    @Override
    protected void onEnable() {
        TappyPlane application = (TappyPlane) getApplication();
        application.getRootNode().attachChild(background.getSpatial());
    }

    /**
     * (non-JavaDoc)
     *
     * @see com.jme3.app.state.BaseAppState#onDisable()
     */
    @Override
    protected void onDisable() {
        background.getSpatial().removeFromParent();
    }
}
