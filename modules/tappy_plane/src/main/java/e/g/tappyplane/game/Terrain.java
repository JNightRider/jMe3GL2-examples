/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.game;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import e.g.tappyplane.TappyPlane;
import e.g.tappyplane.game.enums.GroundType;

import org.je3gl.scene.shape.Sprite;
import org.je3gl.util.Timer;
import org.je3gl.util.TimerTask;

/**
 * Class responsible for managing the terrain.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class Terrain extends AbstractControl {

    /** Main application. */
    private final TappyPlane applicatio;

    /**
     * A timer manages the difficulty related to the rocks; each time it
     * triggers, the number of rocks increases for every new {@link Ground} that
     * is added.
     */
    private final Timer timerRock = new Timer(10f);

    /**
     * A timer for managing the {@link Ground} type; it switches to the next one
     * in each cycle.
     */
    private final Timer timerGround = new Timer(30f);

    /** The tyoe. */
    private GroundType currentGroundType = GroundType.GRASS;

    /** Current maximum number of rocks */
    private int currentAmountRock = 2;

    /**
     * The minimum number of child elements, corresponding to the quantity of
     * {@link Ground} objects; if the number of segments is less than this
     * figure, an additional one will be added automatically.
     *
     * @see #addNexGround()
     */
    private int minAmountGround;

    /**
     * Constructor
     *
     * @param applicatio TappyPlane
     */
    public Terrain(TappyPlane applicatio) {
        this.applicatio = applicatio;

        timerRock.attachTask(onRockTimerTask).start();
        timerGround.attachTask(onGroundTimerTask).start();
    }

    /**
     * Timer task
     *
     * @see #timerRock
     */
    private final TimerTask onRockTimerTask = (handler) -> {
        currentAmountRock++;
        if (currentAmountRock > 8) {
            currentAmountRock = 8;
        }
        handler.reset();
    };

    /**
     * Timer task
     *
     * @see #timerGround
     */
    private final TimerTask onGroundTimerTask = (handler) -> {
        currentGroundType = GroundType.checkNextOf(currentGroundType);
        handler.reset();
    };

    /**
     * Sets the minimum number of {@link Ground}
     *
     * @param minAmountGround int
     */
    public void setMinAmountGround(int minAmountGround) {
        this.minAmountGround = minAmountGround;
    }

    /**
     * Simple update
     *
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {
        timerRock.update(tpf, 0.60f);
        timerGround.update(tpf, 0.60f);
        Node rootNode = (Node) spatial;
        if (rootNode.getQuantity() < minAmountGround) {
            addNexGround();
        }
    }

    /**
     * Adds a new {@link Ground} at the last position (in front of everyone)
     */
    private void addNexGround() {
        Node rootNode = (Node) spatial;
        Spatial last = rootNode.getChildren().getLast();

        Ground ground = Ground.getNewInstanceGround(applicatio, currentGroundType, minAmountGround);
        Vector3f pos  = ground.getPosition();
        Sprite sprite = ground.getSprite();

        ground.setPosition(new Vector2f(last.getControl(Ground.class).getPosition().x + sprite.getWidth(), pos.y));
        rootNode.attachChild(ground.getJmeObject());
    }

    /**
     * Simple render
     *
     * @param rm RenderManager
     * @param vp ViewPort
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    /**
     * Returns a new instance of the terrain.
     *
     * @param app main application
     * @return Terrain
     */
    public static Terrain getNewInstanceTerrain(TappyPlane app) {
        Node rootNode = new Node("Terrain");

        float offset;
        int i = 0;
        for (; i < 3; i++) {
            Ground ground = Ground.getNewInstanceGround(app, GroundType.GRASS, i > 0 ? 2 : 0);
            Vector3f pos  = ground.getPosition();
            Sprite sprite = ground.getSprite();

            offset = sprite.getWidth() * i;

            ground.setPosition(new Vector2f(offset, pos.y));
            rootNode.attachChild(ground.getJmeObject());
        }

        Terrain terrain = new Terrain(app);
        terrain.setMinAmountGround(i);
        rootNode.addControl(terrain);
        return terrain;
    }
}
