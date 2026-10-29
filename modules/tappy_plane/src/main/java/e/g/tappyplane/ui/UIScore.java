/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.ui;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.QuadBackgroundComponent;

import e.g.jme3hudl.ControlLayout;
import e.g.tappyplane.GameData;
import java.text.DecimalFormat;

/**
 * Score container.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class UIScore extends Container {

    /** Score required to reach Bronze. */
    public static final long BRONZE_SCORE = 0;
    /** Bronze rank icon.  */
    public static final QuadBackgroundComponent BRONZE = new QuadBackgroundComponent(
            GuiGlobals.getInstance()
                    .loadTexture("Textures/score_01.png", true, false)
    );

    /** Score required to reach Silver. */
    public static final long SILVER_SCORE = 5000;
    /** Silver rank icon.  */
    public static final QuadBackgroundComponent SILVER = new QuadBackgroundComponent(
            GuiGlobals.getInstance()
                    .loadTexture("Textures/score_02.png", true, false)
    );

    /** Score required to reach Gold. */
    public static final long GOLD_SCORE = 1000;
    /** Gold rank icon.  */
    public static final QuadBackgroundComponent GOLD = new QuadBackgroundComponent(
            GuiGlobals.getInstance()
                    .loadTexture("Textures/score_03.png", true, false)
    );

    /**
     * Returns the icon corresponding to the rank.
     *
     * @return QuadBackgroundComponent
     */
    public static QuadBackgroundComponent getScoreIcon() {
        GameData data = GameData.getInstance();
        long score = data.bestScore;
        if (score >= GOLD_SCORE) {
            return GOLD.clone();
        }
        if (score >= SILVER_SCORE && score < GOLD_SCORE) {
            return SILVER.clone();
        }
        return BRONZE.clone();
    }

    /** laberl current score */
    private Label currentScore;
    /** label last socore.*/
    private Label lastScore;

    /** the game data. */
    private final GameData dataGame = GameData.getInstance();

    /**
     * Constructor.
     *
     * @param rootPane RootPane
     */
    public UIScore(ControlLayout.RootPane rootPane) {
        super(new ControlLayout(rootPane));
        this.initComponents();
    }

    /**
     * Simple init
     */
    private void initComponents() {
        ControlLayout layout = (ControlLayout) getLayout();
        setPreferredSize(new Vector3f(200, 50, 10));
        {
            Label label = new Label("");
            label.setPreferredSize(getPreferredSize().clone());
            label.setBackground(new QuadBackgroundComponent(
                    GuiGlobals.getInstance()
                            .loadTexture("Textures/panel.png", true, false)
            ));
            layout.addChild(label, ControlLayout.Alignment.LeftTop);

        }

        currentScore = new Label("Score: " + amountToString(dataGame.lastScore, 9));
        currentScore.setPreferredSize(new Vector3f(180, 20, 1));
        currentScore.setTextVAlignment(VAlignment.Center);
        currentScore.setColor(ColorRGBA.White);
        currentScore.setFont(
                GuiGlobals.getInstance()
                        .loadFont("Interface/Fonts/KenVectorFuture.fnt")
        );

        layout.addChild(currentScore, ControlLayout.Alignment.LeftTop);
        layout.setAttribute(ControlLayout.POSITION, currentScore, new Vector3f(10, 5, 1));
        layout.setAttribute(ControlLayout.FONT_SIZE, currentScore, 15.0f);

        lastScore = new Label("Latest score: " + amountToString(dataGame.bestScore, 19));
        lastScore.setPreferredSize(new Vector3f(180, 20, 1));
        lastScore.setTextVAlignment(VAlignment.Center);
        lastScore.setColor(ColorRGBA.White);
        lastScore.setFont(
                GuiGlobals.getInstance()
                        .loadFont("Interface/Fonts/KenneyMiniSquare.fnt")
        );

        layout.addChild(lastScore, ControlLayout.Alignment.LeftTop);
        layout.setAttribute(ControlLayout.POSITION, lastScore, new Vector3f(10, 25, 1));
        layout.setAttribute(ControlLayout.FONT_SIZE, lastScore, 10.0f);
    }

    /**
     * Add up the points earned in the game
     *
     * @param value long
     */
    public void addAmount(long value) {
        dataGame.lastScore += value;
        currentScore.setText("Score: " + amountToString(dataGame.lastScore, 9));
    }

    /**
     * Update data
     */
    public void updateScores() {
        dataGame.checkBestScore();
        dataGame.save();
        lastScore.setText("Latest score: " + amountToString(dataGame.bestScore, 19));
    }

    /**
     * Reset scores
     */
    public void resetScores() {
        dataGame.lastScore = 0;
        currentScore.setText("Score: 0");
    }

    /**
     * Pass a very big value using "kilo" notation.
     *
     * @param number long
     * @param maxlen int
     * @return String
     */
    private static String amountToString(long number, int maxlen) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        // Divide por 1000 y formatea con un decimal máximo
        double value = number / 1000.0;
        DecimalFormat df = new DecimalFormat("#.#");

        String str = df.format(value) + "k";
        if (str.length() > maxlen) {
            return str.substring(0, maxlen - 3) + "...";
        }
        return str;
    }
}
