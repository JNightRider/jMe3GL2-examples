/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.anim;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import com.simsilica.lemur.Panel;
import com.simsilica.lemur.anim.AbstractTween;
import com.simsilica.lemur.anim.Tween;

/**
 * Effect for the graphical interface.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class AlphaPanel extends AbstractTween {

    /** the target */
    private final Panel target;
    /** init value. */
    private final Vector2f from;
    /** end value. */
    private final Vector2f to;
    /** current value.*/
    private final Vector2f value;
    /** the scale value. */
    private final float scale;

    /**
     * Constructor.
     *
     * @param target Panel
     * @param from Vector2f
     * @param to Vector2f
     * @param scale float
     * @param length double
     */
    public AlphaPanel(Panel target, Vector2f from, Vector2f to, float scale) {
        this(target, from, to, scale, to.distance(from));
    }

    /**
     * Constructor.
     *
     * @param target Panel
     * @param from Vector2f
     * @param to Vector2f
     * @param scale float
     * @param length double
     */
    public AlphaPanel(Panel target, Vector2f from, Vector2f to, float scale, double length) {
        super(length);
        this.target = target;
        this.from   = from.clone().mult(scale);
        this.to     = to.clone().mult(scale);
        this.value  = from.clone().mult(scale);
        this.scale  = scale;
        this.simpleInit();
    }

    /**
     * Simple init
     */
    private void simpleInit() {
        target.setAlpha(from.divide(scale).x);
    }

    /**
     * Simple update.
     *
     * @param t double
     */
    @Override
    protected void doInterpolate(double t) {
        value.interpolateLocal(from, to, (float) t);
        target.setAlpha(value.divide(scale).x);
    }

    /**
     * Creates a new instance of {@link AlphaPanel}
     *
     * @param target Panel
     * @param from Vector2f
     * @param to Vector2f
     * @param scale float
     * @return Tween
     */
    public static Tween alpha(Panel target, Vector2f from, Vector2f to, float scale) {
        final Vector3f v = target.getLocalTranslation();
        from = from != null ? from : new Vector2f(v.x, v.y);
        to = to != null ? to : new Vector2f(v.x, v.y);
        return new AlphaPanel(target, from, to, scale);
    }

    /**
     * Creates a new instance of {@link AlphaPanel}
     *
     * @param target Panel
     * @param from Vector2f
     * @param to Vector2f
     * @param scale float
     * @param length double
     * @return Tween
     */
    public static Tween alpha(Panel target, Vector2f from, Vector2f to, float scale, double length) {
        final Vector3f v = target.getLocalTranslation();
        from = from != null ? from : new Vector2f(v.x, v.y);
        to = to != null ? to : new Vector2f(v.x, v.y);
        return new AlphaPanel(target, from, to, scale, length);
    }
}
