/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.system.JmeSystem;

import java.io.IOException;

import jme3tools.savegame.SaveGame;

/**
 * Game data.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class GameData implements Savable {

    /**
     * The path and name of the game data are typically stored in the user's
     * {@code Home} directory, within {@code .jme3}.
     * 
     * <p>
     * the path: {@code ~/.jme3/NightRide Games/jMe3GL2/tappyplane/tappyplane.gd}
     * </p>
     */
    private static final String
            GAME_PATH      = "NightRide Games/jMe3GL2/tappyplane",
            GAME_DATA_NAME = "tappyplane.gd";

    /**
     * Returns the instance of the game data.
     *
     * @return GameData
     */
    public static GameData getInstance() {
        return GameDataHolder.INSTANCE;
    }

    /**
     * Game data manager.
     */
    private static class GameDataHolder {
        /** Atomic data instance. */
        private static final GameData INSTANCE;

        static {
            Savable data = SaveGame.loadGame(GAME_PATH, GAME_DATA_NAME, JmeSystem.StorageFolderType.External);
            if (data == null || !(data instanceof GameData)) {
                INSTANCE = new GameData();
            } else {
                INSTANCE = (GameData) data;
            }
        }
    }

    /**
     * To avoid overloading the storage, data can only be saved once per second;
     * for this reason, this initial timestamp is stored.
     */
    private long lastTime = System.nanoTime();

    /**
     * If there are no changes to the data, what is on the disk will not be
     * updated.
     */
    private boolean changes;

    // -----------------------------

    /** the last score. */
    public long lastScore;
    /** the best score. */
    public long bestScore;

    /** Constructor. */
    private GameData() {
    }

    /**
     * Check for any potential changes to save or update.
     */
    public void checkBestScore() {
        if (bestScore < lastScore) {
            bestScore = lastScore;
            changes   = true;
        }
    }

    /**
     * Saves game data to the disk every second (if there are changes).
     * 
     * @see #GAME_DATA_NAME
     * @see #GAME_PATH
     */
    public void save() {
        long currentTime = System.nanoTime();
        long lapseTime = (currentTime - lastTime) / 1_000_000_000L;
        if (lapseTime >= 1 && changes) {
            SaveGame.saveGame(GAME_PATH, GAME_DATA_NAME, this, JmeSystem.StorageFolderType.External);
            lastTime = currentTime;
            changes  = false;
        }
    }

    /**
     * Simple write
     *
     * @param ex JmeExporter
     * @throws IOException error
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(lastScore, "lastScore", 0l);
        out.write(bestScore, "bestScore", 0l);
    }

    /**
     * Simple read
     *
     * @param im JmeImporter
     * @throws IOException error
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        lastScore = in.readLong("lastScore", 0l);
        bestScore = in.readLong("bestScore", 0l);
    }
}
