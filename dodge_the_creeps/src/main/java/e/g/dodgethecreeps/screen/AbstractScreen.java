/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.screen;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.style.ElementId;

import e.g.dodgethecreeps.Dodgethecreeps;
import e.g.jme3hudl.ControlLayout;

/**
 * Abstract class for all game screens.
 * @author wil
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractScreen extends BaseAppState {
    
    /** UI root container. */
    protected Container rootContainer;
    
    
    /** Label for messages. */
    protected Label message;
    
    /**
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#initialize(com.jme3.app.Application) 
     * @param app application
     */
    @Override
    protected void initialize(Application app) {
        AppSettings settings = ((Dodgethecreeps) app).getSettings();
        ControlLayout layout = new ControlLayout(ControlLayout.onCreateRootPane(new Vector3f(settings.getWidth(), settings.getHeight(), 0),
                                                                                       new Vector3f(settings.getWidth(), settings.getHeight(), 0)));
        
        rootContainer = new Container(new ElementId("null"));
        rootContainer.setPreferredSize(new Vector3f(layout.getRootPane().getResolution().clone()));
        rootContainer.setLayout(layout);
        
        message = layout.addChild(new Label(""), ControlLayout.Alignment.Center);
        message.setTextHAlignment(HAlignment.Center);
        message.setTextVAlignment(VAlignment.Center);
        message.setFont(GuiGlobals.getInstance().loadFont("Interface/Fonts/Xolonium.fnt"));
        message.setColor(new ColorRGBA(1.0F, 1.0F, 1.0F, 1.0F));
        message.setPreferredSize(new Vector3f(rootContainer.getPreferredSize().x, 200, 0));
        
        layout.setAttribute(ControlLayout.FONT_SIZE, message, 55.0F);
    }
    
    /** 
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#cleanup() 
     */
    @Override protected void cleanup(Application app) { }

    /** 
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#onDisable() 
     */
    @Override
    protected void onEnable() {
        Dodgethecreeps app = (Dodgethecreeps) getApplication();        
        app.getGuiNode().attachChild(rootContainer);
        
        AppSettings settings = app.getSettings();
        rootContainer.setPreferredSize(new Vector3f(settings.getWidth(), settings.getHeight(), 0));
        rootContainer.setLocalTranslation(0, settings.getHeight(), 0);
    }

    /** 
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#onDisable() 
     */
    @Override
    protected void onDisable() {
        rootContainer.removeFromParent();
    }
}
