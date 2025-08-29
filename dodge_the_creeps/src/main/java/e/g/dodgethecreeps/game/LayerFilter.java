/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.game;

import org.dyn4j.collision.Filter;

/**
 * Class <code>LayerFilter</code> that implements the {@link org.dyn4j.collision.Filter} 
 * interface to check which objects can collide.
 * 
 * @author wil
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LayerFilter implements Filter {

    /**
     * layer; It is used to determine if the object is capable of detecting
     * collisions.
     */
    private final int layer;

    /**
     * Constructor of class <code>LayerFilter</code>.
     * @param layer layer (int)
     */
    public LayerFilter(int layer) {
        this.layer = layer;
    }
    
    /**
     * If the filter is an instance of <code>LayerFilter</code>, they have the
     * same layer, the collision is simply ignored. Otherwise you will be notified.
     * 
     * @param filter body filter
     * @return boolean
     */
    @Override
    public boolean isAllowed(Filter filter) {
        if (filter instanceof LayerFilter layerFilter) {
            if (this.layer == layerFilter.layer) {
                return false;
            }
        }
        return true;
    }

    /**
     * (non-JavaDoc)
     * @see java.lang.Object#toString() 
     * @return String
     */
    @Override
    public String toString() {
        return "LayerFilter[" + "layer=" + layer + ']';
    }
}
