/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.screen;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.style.ElementId;

import e.g.jme3hudl.ControlLayout;

/**
 * A state for the main scene.
 * @author wil
 * @version 1.0.0
 * @since 1.0.0
 */
public final class MainSceneAppState extends AbstractScreen {
    
    /** Start button; starts the player's scene. */
    private Button startButton;
    
    /**
     * Default constructor of class <code>MainSceneAppState</code>.
     */
    public MainSceneAppState() { }

    /**
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#initialize(com.jme3.app.Application) 
     * @param app application
     */
    @Override
    protected void initialize(Application app) {        
        super.initialize(app);
        ControlLayout layout = (ControlLayout) rootContainer.getLayout();
        
        startButton = layout.addChild(new Button("Start", new ElementId("StartButton")), ControlLayout.Alignment.CenterBottom);
        startButton.setTextHAlignment(HAlignment.Center);
        startButton.setTextVAlignment(VAlignment.Center);
        startButton.setFont(GuiGlobals.getInstance().loadFont("Interface/Fonts/Xolonium.fnt"));
        startButton.setPreferredSize(new Vector3f(200, 100, 0));
        startButton.addClickCommands((source) -> {
            setEnabled(false);
            getApplication().getStateManager().getState(GameSceneAppState.class).setEnabled(true);
        });
        
        layout.setAttribute(ControlLayout.POSITION, startButton, new Vector3f(0, 100, 0));
        layout.setAttribute(ControlLayout.FONT_SIZE, startButton, 50.0F);
        
        message.setText("Dodge the\nCreeps!");
    }
}
