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
public enum SmokeType {
    
    SMOKE_01("Textures/Effects/smoke_01.png", new Vector2f(56, 49)),
    SMOKE_02("Textures/Effects/smoke_02.png", new Vector2f(59, 55)),
    SMOKE_03("Textures/Effects/smoke_03.png", new Vector2f(52, 50)),
    SMOKE_04("Textures/Effects/smoke_04.png", new Vector2f(63, 65)),
    SMOKE_05("Textures/Effects/smoke_05.png", new Vector2f(87, 100)),
    SMOKE_06("Textures/Effects/smoke_06.png", new Vector2f(100, 102)),
    SMOKE_07("Textures/Effects/smoke_07.png", new Vector2f(69, 71));
    
    private final String path;
    private final Vector2f size;

    private SmokeType(String path, Vector2f size) {
        this.path = path;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public Vector2f getSize() {
        return new Vector2f(size.x/size.y, 1.0f);
    }
}
