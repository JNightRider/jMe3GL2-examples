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
 *
 * @author wil
 */
public final class UIScore extends Container {

    private Label currentScore;
    private Label lastScore;

    private GameData dataGame = GameData.getInstance();

    /**
     * Constructor.
     *
     * @param rootPane RootPane
     */
    public UIScore(ControlLayout.RootPane rootPane) {
        super(new ControlLayout(rootPane));
        this.initComponents();
    }

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

        currentScore = new Label("Score: 0");
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

        lastScore = new Label("Latest score: 0");
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

    public void addAmount(long value) {
        dataGame.lastScore += value;
        currentScore.setText("Score: " + amountToString(dataGame.lastScore , 9));
    }

    public void updateScores() {
        dataGame.checkBestScore();
        dataGame.save();
        lastScore.setText("Latest score: " + amountToString(dataGame.bestScore, 19));
    }

    public void resetScores() {        
        dataGame.lastScore = 0;
        currentScore.setText("Score: 0");
    }
    
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
