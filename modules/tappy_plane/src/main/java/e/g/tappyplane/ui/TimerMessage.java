/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.tappyplane.ui;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.functional.VoidFunction;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.anim.Animation;
import com.simsilica.lemur.anim.SpatialTweens;
import com.simsilica.lemur.anim.Tween;
import com.simsilica.lemur.anim.TweenAnimation;
import com.simsilica.lemur.anim.Tweens;
import com.simsilica.lemur.effect.AbstractEffect;
import com.simsilica.lemur.effect.EffectInfo;

import e.g.jme3hudl.ControlLayout;

import e.g.tappyplane.game.anim.AlphaPanel;
import e.g.tappyplane.game.anim.MovePanel;

/**
 * A short game-opening animation.
 *
 * @author wil
 * @version 1.0.0
 * @since 1.1.0
 */
public final class TimerMessage extends Container {

    /** Numbers*/
    private final UINumber[] numbers = new UINumber[3];
    /** Mesage "GO" */
    private UILetters message;

    /** An event call. */
    private VoidFunction<Void> callback;
    /** Timer index. */
    private int index;

    /**
     * Constructor.
     *
     * @param rootPane RootPane
     */
    public TimerMessage(ControlLayout.RootPane rootPane) {
        super(new ControlLayout(rootPane));
        this.initComponents();
    }

    /**
     * Simple init
     */
    private void initComponents() {
        ControlLayout layout = (ControlLayout) getLayout();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = new UINumber(layout.getRootPane());
            numbers[i].setFontSize(0.7f);
            numbers[i].setValue(i + 1);
            numbers[i].addEffect("hidden", new AbstractEffect<>("open/close") {
                @Override
                public Animation create(Panel target, EffectInfo existing) {
                    Tween alpha = AlphaPanel.alpha(target, new Vector2f(1, 1), new Vector2f(0, 0), 2, 1),
                          remov = SpatialTweens.detach(target);

                    Tween action = Tweens.callMethod(TimerMessage.this, "nextStep");
                    return new TweenAnimation(Tweens.sequence(alpha, remov, action));
                }
            });
        }

        message = new UILetters(layout.getRootPane());
        message.setFontSize(0.6f);
        message.setValue("GO");
        message.addEffect("go", new AbstractEffect<>("open/close") {
            @Override
            public Animation create(Panel target, EffectInfo existing) {
                Tween down  = MovePanel.move(target, new Vector3f(0, -100, 0), new Vector3f(0, 100, 0), 0.01f),
                      right = MovePanel.move(target, new Vector3f(0, 100, 0), new Vector3f(40, 100, 0), 0.5f),
                      left  = MovePanel.move(target, new Vector3f(20, 100, 0), new Vector3f(-500, 100, 0), 0.1f),
                      remov = SpatialTweens.detach(target);

                Tween action = Tweens.callMethod(TimerMessage.this, "doAction");
                return new TweenAnimation(Tweens.sequence(down, action, right, left, remov));
            }
        });
    }

    /**
     * Sets a new event callback.
     *
     * @param callback VoidFunction
     */
    public void setCallback(VoidFunction<Void> callback) {
        this.callback = callback;
    }

    /**
     * Trigger the event.
     */
    protected void doAction() {
        if (callback != null) {
            callback.eval(null);
        }
    }

    /**
     * Move on to the next step of the animation
     */
    protected void nextStep() {
        index++;
        if (index >= numbers.length) {
            ControlLayout layout = (ControlLayout) getLayout();
            layout.addChild(message, ControlLayout.Alignment.CenterTop);
            message.runEffect("go");
            return;
        }
        addCurretNumber();
    }

    /**
     * Start the animation
     */
    public void start() {
        index = 0;
        addCurretNumber();
    }

    /**
     * Add the next object in the animation
     */
    private void addCurretNumber() {
        ControlLayout layout = (ControlLayout) getLayout();
        layout.addChild(numbers[index], ControlLayout.Alignment.CenterTop);
        layout.setAttribute(ControlLayout.POSITION, numbers[index], new Vector3f(0, 10, 0));
        numbers[index].runEffect("hidden");
    }
}
