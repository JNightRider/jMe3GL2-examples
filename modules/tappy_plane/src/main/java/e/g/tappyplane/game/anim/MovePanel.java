/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.anim;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.anim.AbstractTween;
import com.simsilica.lemur.anim.Tween;
import e.g.jme3hudl.ControlLayout;

/**
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class MovePanel extends AbstractTween {

    private final Panel target;
    private final Vector3f from;
    private final Vector3f to;
    private final Vector3f value;

    public MovePanel(Panel target, Vector3f from, Vector3f to) {
        this(target, from, to, to.distance(from));
    }

    public MovePanel(Panel target, Vector3f from, Vector3f to, double length) {
        super(length);
        this.target = target;
        this.from = from.clone();
        this.to = to.clone();
        this.value = new Vector3f(from);
    }

    @Override
    protected void doInterpolate(double t) {
        value.interpolateLocal(from, to, (float) t);
        
        Node parent = target.getParent();
        if ((parent instanceof Container) && (((Container) parent).getLayout() instanceof ControlLayout)) {
            ControlLayout layout = (ControlLayout) ((Container) parent).getLayout();
            layout.setAttribute(ControlLayout.POSITION, target, value);
        } else {
            target.setLocalTranslation(value);
        }
    }
    
    public static Tween move(Panel target, Vector3f from, Vector3f to) {
        from = from != null ? from : target.getLocalTranslation();
        to = to != null ? to : target.getLocalTranslation();
        return new MovePanel(target, from, to);
    }
    
    public static Tween move(Panel target, Vector3f from, Vector3f to, double length) {
        from = from != null ? from : target.getLocalTranslation();
        to = to != null ? to : target.getLocalTranslation();
        return new MovePanel(target, from, to, length);
    }
}
