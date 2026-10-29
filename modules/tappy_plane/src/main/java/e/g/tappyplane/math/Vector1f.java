/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.math;

/**
 * A class responsible for managing a single direction.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Vector1f {

    /**
     * Shared instance of the all-zero vector (0,0,0). Do not modify!
     */
    public static final Vector1f ZERO = new Vector1f(0.0f);

    /** value. */
    public float x;

    /** Constructor. */
    public Vector1f() {
    }

    /**
     * Constructor.
     *
     * @param vec Vector1f
     */
    public Vector1f(Vector1f vec) {
        this(vec.x);
    }

    /**
     * Constructor.
     *
     * @param x float
     */
    public Vector1f(float x) {
        this.x = x;
    }

    /**
     * Interpolates linearly between the current instance and the specified
     * vector, returning the (modified) current instance.
     *
     * <p>
     * this = (1 - changeAmount) * this + changeAmount * finalVec
     *
     * @param finalVec the desired value when changeAmount=1 (not null,
     *                 unaffected unless it's {@code this})
     * @param changeAmount the fractional change amount
     * @return the (modified) current instance (for chaining)
     */
    public Vector1f interpolateLocal(Vector1f finalVec, float changeAmount) {
        this.x = (1 - changeAmount) * this.x + changeAmount * finalVec.x;
        return this;
    }

    /**
     * Interpolates linearly between the specified beginning and final vectors,
     * returning the (modified) current instance.
     *
     * <p>
     * this = (1 - changeAmount) * beginVec + changeAmount * finalVec
     *
     * @param beginVec the desired value when changeAmount=0 (not null,
     *                  unaffected unless it's {@code this})
     * @param finalVec the desired value when changeAmount=1 (not null,
     *                  unaffected unless it's {@code this})
     * @param changeAmount the fractional change amount
     * @return the (modified) current instance (for chaining)
     */
    public Vector1f interpolateLocal(Vector1f beginVec, Vector1f finalVec,
            float changeAmount) {
        this.x = (1 - changeAmount) * beginVec.x + changeAmount * finalVec.x;
        return this;
    }

    /**
     * Tests for approximate equality with the specified vector, using the
     * specified tolerance. If {@code other} is null, false is returned. Either
     * way, the current instance is unaffected.
     *
     * @param other the vector to compare (unaffected) or null for none
     * @param epsilon the tolerance for each component
     * @return true if both components are within tolerance, otherwise false
     */
    public boolean isSimilar(Vector1f other, float epsilon) {
        if (other == null) {
            return false;
        }
        if (Float.compare(Math.abs(other.x - x), epsilon) > 0) {
            return false;
        }
        return true;
    }
}
