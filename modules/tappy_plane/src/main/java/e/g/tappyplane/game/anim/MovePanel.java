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
 * Effect for the graphical interface.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public class MovePanel extends AbstractTween {

    /** the target */
    private final Panel target;
    /** init value. */
    private final Vector3f from;
    /** end value. */
    private final Vector3f to;
    /** current value.*/
    private final Vector3f value;

    /**
     * Constructor.
     *
     * @param target Panel
     * @param from Vector3f
     * @param to Vector3f
     */
    public MovePanel(Panel target, Vector3f from, Vector3f to) {
        this(target, from, to, to.distance(from));
    }

    /**
     * Constructor.
     *
     * @param target Panel
     * @param from Vector3f
     * @param to Vector3f
     * @param length double
     */
    public MovePanel(Panel target, Vector3f from, Vector3f to, double length) {
        super(length);
        this.target = target;
        this.from = from.clone();
        this.to = to.clone();
        this.value = new Vector3f(from);
    }

    /**
     * Simple update
     *
     * @param t double
     */
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

    /**
     * Creates a new instance of {@link MovePanel}
     *
     * @param target Panel
     * @param from Vector3f
     * @param to Vector3f
     * @return Tween
     */
    public static Tween move(Panel target, Vector3f from, Vector3f to) {
        from = from != null ? from : target.getLocalTranslation();
        to = to != null ? to : target.getLocalTranslation();
        return new MovePanel(target, from, to);
    }

    /**
     * Creates a new instance of {@link MovePanel}
     *
     * @param target Panel
     * @param from Vector3f
     * @param to Vector3f
     * @param length double
     * @return Tween
     */
    public static Tween move(Panel target, Vector3f from, Vector3f to, double length) {
        from = from != null ? from : target.getLocalTranslation();
        to = to != null ? to : target.getLocalTranslation();
        return new MovePanel(target, from, to, length);
    }
}
