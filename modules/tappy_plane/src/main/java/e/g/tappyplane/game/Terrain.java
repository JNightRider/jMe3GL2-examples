/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import e.g.tappyplane.TappyPlane;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wil
 */
public class Terrain extends AbstractControl {
    
    private static final class GroundSegment extends Node {
        
    }
    
    private final List<GroundSegment> groundList = new ArrayList<>();
    
    private TappyPlane applicatio;

    public Terrain(TappyPlane applicatio) {
        this.applicatio = applicatio;
    }

    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }    
}
