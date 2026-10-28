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

public final class AlphaPanel extends AbstractTween {
    
    private final Panel target;
    private final Vector2f from;
    private final Vector2f to;
    private final Vector2f value;
    private final float scale;
    
    public AlphaPanel(Panel target, Vector2f from, Vector2f to, float scale) {
        this(target, from, to, scale, to.distance(from));
    }
    
    public AlphaPanel(Panel target, Vector2f from, Vector2f to, float scale, double length) {
        super(length);
        this.target = target;
        this.from  = from.clone().mult(scale);
        this.to    = to.clone().mult(scale);
        this.value = from.clone().mult(scale);
        this.scale = scale;
        this.simpleInit();
    }
    
    private void simpleInit() {
        target.setAlpha(from.divide(scale).x);
    }
    
    @Override
    protected void doInterpolate(double t) {
        value.interpolateLocal(from, to, (float)t);
        target.setAlpha(value.divide(scale).x);
    }
    
    public static Tween alpha( Panel target, Vector2f from, Vector2f to, float scale) {
        final Vector3f v = target.getLocalTranslation();        
        from = from != null ? from : new Vector2f(v.x, v.y);
        to = to != null ? to : new Vector2f(v.x, v.y);
        return new AlphaPanel(target, from, to, scale);
    }
    
    public static Tween alpha( Panel target, Vector2f from, Vector2f to, float scale, double length ) {
        final Vector3f v = target.getLocalTranslation();        
        from = from != null ? from : new Vector2f(v.x, v.y);
        to = to != null ? to : new Vector2f(v.x, v.y);
        return new AlphaPanel(target, from, to, scale, length);
    }
}

