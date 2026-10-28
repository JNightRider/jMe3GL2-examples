/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.anim;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 * A class that applies a scale animation.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class ScalingAnimation extends AbstractControl {

    /**
     * The scale of the initial or original size that the sprite may have, which
     * is 100%.
     */
    public static final Vector3f VF_100 = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * The scale of the final or minimum size that the sprite can reach, which
     * is 50%.
     */
    public static final Vector3f VF_50 = new Vector3f(0.5f, 0.5f, 1.0f);

    /**
     * An indicator that determines the direction of the scale; if {@code true},
     * the scale will increase up to the value of {@link #VF_100}; otherwise, it
     * will decrease down to the value of {@link #VF_50}.
     */
    private boolean increment = true;
    /** Minimum and maximum scale. */
    private Vector3f minScale, maxScale;

    /** Sprite size scale. */
    private final Vector3f scale = new Vector3f(VF_50);
    /** Scaling animation speed. */
    private float speed = 3.5f * 0.6f;

    /**
     * Constructor.
     */
    public ScalingAnimation() {
        this(VF_50, VF_100);
    }

    /**
     * Constructor.
     *
     * @param minScale Vector3f
     * @param maxScale Vector3f
     */
    public ScalingAnimation(Vector3f minScale, Vector3f maxScale) {
        this.minScale = minScale;
        this.maxScale = maxScale;
    }

    /**
     * Sets the animation speed
     *
     * @param speed float
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Simple update.
     *
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (increment) {
            scale.interpolateLocal(maxScale, tpf * speed);
            if (scale.isSimilar(maxScale, 0.1f)) {
                increment = false;
            }
        } else {
            scale.interpolateLocal(minScale, tpf * speed);
            if (scale.isSimilar(minScale, 0.1f)) {
                increment = true;
            }
        }
        spatial.setLocalScale(scale);
    }

    /**
     * control render
     *
     * @param rm RenderManager
     * @param vp ViewPort
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
