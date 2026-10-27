/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.enums;

/**
 * Enumerated class responsible for listing the available ground types.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public enum GroundType {

    /** Ground type: Dirt */
    DIRT("Textures/groundDirt.png", RockType.DIRT),
    /** Ground type: Grass */
    GRASS("Textures/groundGrass.png", RockType.GRASS),
    /** Ground type: Ice */
    ICE("Textures/groundIce.png", RockType.ICE),
    /** Ground type: Rock */
    ROCK("Textures/groundRock.png", RockType.ROCK),
    /** Ground type: Snow */
    SNOW("Textures/groundSnow.png", RockType.SNOW);

    /**
     * The path to the texture that the terrain will have on-screen when
     * implemented via a {@code Ground}.
     */
    private final String path;

    /** The type of rock that this ground might contain. */
    private final RockType rockType;
    
    /**
     * Constructor
     *
     * @param path String
     * @param rockType RockType
     */
    private GroundType(String path, RockType rockType) {
        this.path = path;
        this.rockType = rockType;
    }

    /**
     * Returns the texture path
     *
     * @return String
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the rock type.
     *
     * @return RockType
     */
    public RockType getRockType() {
        return rockType;
    }
}
