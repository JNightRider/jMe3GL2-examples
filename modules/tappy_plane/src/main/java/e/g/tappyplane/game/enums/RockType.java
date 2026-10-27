/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game.enums;

/**
 * Enumerated class responsible for listing the rock types present in the
 * terrain.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public enum RockType {

    /** Rock tyoe: Dirt. */
    DIRT("Textures/rock_01.png", "Textures/rock_06.png"),
    /** Rock tyoe: Grass. */
    GRASS("Textures/rock_04.png", "Textures/rock_08.png"),
    /** Rock tyoe: Ice. */
    ICE("Textures/rock_03.png", "Textures/rock_07.png"),
    /** Rock tyoe: Rock. */
    ROCK("Textures/rock_02.png", "Textures/rock_06.png"),
    /** Rock tyoe: Snow. */
    SNOW("Textures/rock_02.png", "Textures/rock_06.png");

    /** Texture path. */
    private final String pathUp;
    /** Texture path. */
    private final String pathDown;

    /**
     * Constructor.
     *
     * @param pathUp String
     * @param pathDown String
     */
    private RockType(String pathUp, String pathDown) {
        this.pathUp = pathUp;
        this.pathDown = pathDown;
    }

    /**
     * Returns the path to the texture without inverting it
     *
     * @return String
     */
    public String getPathUp() {
        return pathUp;
    }

    /**
     * Returns the path to the inverted texture
     *
     * @return String
     */
    public String getPathDown() {
        return pathDown;
    }
}
