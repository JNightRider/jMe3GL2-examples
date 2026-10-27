/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.enums;

/**
 * Enumerated class responsible for listing the available terrain types.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public enum GroundType {

    /** Terrain type: Dirt */
    DIRT("Textures/groundDirt.png"),
    /** Terrain type: Grass */
    GRASS("Textures/groundGrass.png"),
    /** Terrain type: Ice */
    ICE("Textures/groundIce.png"),
    /** Terrain type: Rock */
    ROCK("Textures/groundRock.png"),
    /** Terrain type: Snow */
    SNOW("Textures/groundSnow.png");

    /**
     * The path to the texture that the terrain will have on-screen when
     * implemented via a {@code Ground}.
     */
    private final String path;

    /**
     * Constructor
     *
     * @param path String
     */
    private GroundType(String path) {
        this.path = path;
    }

    /**
     * Returns the texture path
     *
     * @return String
     */
    public String getPath() {
        return path;
    }
}
