/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.enums;

import com.jme3.math.Vector2f;

/**
 *
 * @author wil
 */
public enum GroundType {

    DIRT("Textures/groundDirt.png"),
    GRASS("Textures/groundGrass.png"),
    ICE("Textures/groundIce.png"),
    ROCK("Textures/groundRock.png"),
    SNOW("Textures/groundSnow.png");

    private final String path;
    private GroundType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
