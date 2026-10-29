/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.screen;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.style.ElementId;

import e.g.jme3hudl.ControlLayout;

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.Background;
import e.g.tappyplane.ui.UIScore;

/**
 * Abstract class for all game screens.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public abstract class AbstractScreen extends BaseAppState {

    /**  UI root container. */
    protected Container rootContainer;
    /** The background game. */
    protected Background background;

    /** UI Score*/
    protected UIScore scores;
    /** Rank icon */
    private Button button;
    
    /**
     * (non-JavaDoc)
     *
     * @see com.jme3.app.state.BaseAppState#initialize(com.jme3.app.Application)
     * @param app application
     */
    @Override
    protected void initialize(Application app) {
        TappyPlane application = (TappyPlane) app;
        AppSettings settings = application.getSettings();
        ControlLayout layout = new ControlLayout(ControlLayout.onCreateRootPane(
                new Vector3f(settings.getWidth(), settings.getHeight(), 0),
                new Vector3f(1024, 576, 0)
        ));

        rootContainer = new Container(new ElementId("null"));
        rootContainer.setPreferredSize(new Vector3f(layout.getRootPane().getResolution().clone()));
        rootContainer.setLayout(layout);
        
        button = new Button("");
        button.setPreferredSize(new Vector3f(260, 271, 0));
        button.scale(0.25f);
        button.setBackground(UIScore.getScoreIcon());
        layout.addChild(button, ControlLayout.Alignment.LeftTop);
        layout.setAttribute(ControlLayout.POSITION, button, new Vector3f(10, 10, 0));
        
        scores = new UIScore(layout.getRootPane());
        layout.addChild(scores, ControlLayout.Alignment.LeftTop);
        layout.setAttribute(ControlLayout.POSITION, scores, new Vector3f((button.getPreferredSize().x * 0.25f) + 15, 17, 0));
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
        background = Background.getNewInstanceBackground(application);
        application.getRootNode().attachChild(background.getSpatial());
        application.getGuiNode().attachChild(rootContainer);

        AppSettings settings = application.getSettings();
        rootContainer.setPreferredSize(new Vector3f(settings.getWidth(), settings.getHeight(), 0));
        rootContainer.setLocalTranslation(0, settings.getHeight(), 0);
        scores.updateScores();
        scores.resetScores();
        button.setBackground(UIScore.getScoreIcon());
    }

    /**
     * (non-JavaDoc)
     *
     * @see com.jme3.app.state.BaseAppState#onDisable()
     */
    @Override
    protected void onDisable() {
        if (background != null) {
            background.getSpatial()
                      .removeFromParent();
        }
        rootContainer.removeFromParent();
    }
}
