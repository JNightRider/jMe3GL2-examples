/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.screen;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioNode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;

import e.g.dodgethecreeps.Dodgethecreeps;
import e.g.dodgethecreeps.game.Mob;
import e.g.dodgethecreeps.game.MobSpawnLocation;
import e.g.dodgethecreeps.game.Player;
import e.g.jme3hudl.ControlLayout;

import java.util.concurrent.ThreadLocalRandom;

import jme3gl2.physics.Dyn4jAppState;
import jme3gl2.physics.control.PhysicsBody2D;
import jme3gl2.util.Timer;
import jme3gl2.util.TimerAppState;
import jme3gl2.util.TimerTask;

import org.dyn4j.geometry.Vector2;

/**
 * A state for the game logic.
 * @author wil
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameSceneAppState extends AbstractScreen {
    
    /** State that manages the physical engine. */
    private Dyn4jAppState<PhysicsBody2D> dyn4jAppState;
    
    /** State that controls the timers. */
    private TimerAppState timerAppState;
    
    /** Label where the starting points are shown. */
    private Label scoreLabel;
    
    /** Root node for this state. */
    private Node rootNode;
    
    /** Sound node; when the game is over. */
    private AudioNode deathSound;
    
    /** music node. */
    private AudioNode music;
    
    /** Player body. */
    private Player player;

    /**
     * Object in charge of managing enemy paths; Generates random paths 
     * around the screen.
     */
    private MobSpawnLocation mobSpawnLocation;
    
    /** Timers for each game state. */
    private Timer mobTimer,     // Enemy timer; Manage the spawn time of enemies.
                  scoreTimer,   // Score timer; Manage time to earn a score.
                  startTimer,   // Manage some preparation time before starting the game.
                  messageTimer; // Timer to display a "Game Over" message.
    
    /** score (int). */
    private int score = 0;

    /**
     * Default constructor of class <code>GameSceneAppState</code>.
     */
    public GameSceneAppState() {
        setEnabled(false);
    }
    
    /**
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#initialize(com.jme3.app.Application) 
     * @param app application
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void initialize(Application app) {
        super.initialize(app);
        Dodgethecreeps application = (Dodgethecreeps) app;
        AssetManager assetManager  = app.getAssetManager();
        ControlLayout layout = (ControlLayout) rootContainer.getLayout();
        
        //----------------------------------------------------------------------
        //                              HUD
        //----------------------------------------------------------------------
        scoreLabel = layout.addChild(new Label("0"), true, ControlLayout.Alignment.CenterTop);
        scoreLabel.setTextHAlignment(HAlignment.Center);
        scoreLabel.setTextVAlignment(VAlignment.Center);        
        scoreLabel.setFont(GuiGlobals.getInstance().loadFont("Interface/Fonts/Xolonium.fnt"));
        scoreLabel.setColor(new ColorRGBA(1.0F, 1.0F, 1.0F, 1.0F));
        scoreLabel.setPreferredSize(new Vector3f(300, 45, 0));
        
        layout.setAttribute(ControlLayout.POSITION, scoreLabel, new Vector3f(0, 10, 0));
        layout.setAttribute(ControlLayout.FONT_SIZE, scoreLabel, 45.0F);
        
        //----------------------------------------------------------------------
        //                              AUDIO
        //----------------------------------------------------------------------
        music = new AudioNode(assetManager.loadAudio("Sounds/House_In_a_Forest_Loop.ogg"), new AudioKey("Music", true));
        music.setPositional(false);
        music.setDirectional(false);
        music.setLooping(true);
        
        deathSound = new AudioNode(assetManager.loadAudio("Sounds/gameover.wav"), new AudioKey("DeadthSound", false));
        deathSound.setPositional(false);
        deathSound.setDirectional(false);
        deathSound.setLooping(false);
        
        //----------------------------------------------------------------------
        //                              SCENE
        //----------------------------------------------------------------------
        rootNode = new Node("MyRootNode");
        application.getRootNode().attachChild(rootNode);
        
        dyn4jAppState = getState(Dyn4jAppState.class);
        timerAppState = getState(TimerAppState.class);
        
        mobTimer     = timerAppState.attachTimer("MobTimer",     new Timer(0.5F).attachTask(_on_MobTimer_timeout), 0.60F);
        scoreTimer   = timerAppState.attachTimer("ScoreTimer",   new Timer(0.75F).attachTask(_on_ScoreTimer_timeout), 0.60F);
        startTimer   = timerAppState.attachTimer("StartTimer",   new Timer(1.05F).attachTask(_on_StartTimer_timeou), 0.60F);
        messageTimer = timerAppState.attachTimer("MessageTimer", new Timer(1.0F).attachTask(_on_MessageTimer_timeout), 0.60F);
        
        Camera cam = application.getCamera();
        mobSpawnLocation = new MobSpawnLocation();
        mobSpawnLocation.add(new Vector2(cam.getFrustumLeft(), cam.getFrustumTop()), 
                             new Vector2(cam.getFrustumRight(), cam.getFrustumTop()));
        
        mobSpawnLocation.add(new Vector2(cam.getFrustumRight(), cam.getFrustumTop()),
                             new Vector2(cam.getFrustumRight(), cam.getFrustumBottom()));
        
        mobSpawnLocation.add(new Vector2(cam.getFrustumRight(), cam.getFrustumBottom()),
                             new Vector2(cam.getFrustumLeft(), cam.getFrustumBottom()));
        
        mobSpawnLocation.add(new Vector2(cam.getFrustumLeft(), cam.getFrustumBottom()),
                             new Vector2(cam.getFrustumLeft(), cam.getFrustumTop()));
    }
    
    /**
     * Timer task that spawns {@link e.g.dodgethecreeps.game.Mob} (enemies)
     */
    private final TimerTask _on_MobTimer_timeout = () -> {
        Mob mob = Mob.getNewInstanceMod((Dodgethecreeps) getApplication());
        
        Vector2 position = mobSpawnLocation.getRandomPath();
        
        double direction = position.getDirection() + Math.PI;
        direction += ThreadLocalRandom.current().nextDouble(-Math.PI / 4.0, Math.PI / 4.0);   
        
        mob.getTransform().rotate(direction);
        mob.getTransform().setTranslation(position);
        
        Vector2 velocity = new Vector2(ThreadLocalRandom.current().nextDouble(1.50, 2.50), 0.0);
        mob.setLinearVelocity(velocity.rotate(direction));
        
        dyn4jAppState.getPhysicsSpace().addBody(mob);
        rootNode.attachChild(mob.getJmeObject());
        
        mobTimer.reset();
    };

    /**
     * Timer task that increases the player's score.
     */
    private final TimerTask _on_ScoreTimer_timeout = () -> {
        score += 1;
        scoreLabel.setText(String.valueOf(score));
        scoreTimer.reset();
    };
    
    /**
     * Timer task where the following timers are started: <code>mobTimer</code>
     * and <code>scoreTimer</code>; Responsible for managing enemy spawn time
     * and player score.
     */
    private final TimerTask _on_StartTimer_timeou = () -> {
        player.setEnabled(true);
        message.setText("");
        
        mobTimer.start();
        scoreTimer.start();
        
        startTimer.stop();
    };
    
    /**
     * Timer task indicating "game over";
     */
    private final TimerTask _on_MessageTimer_timeout = () -> {
        setEnabled(false);
        messageTimer.stop();
    };
    
    /**
     * Responsible method to start the event: 'game over'
     */
    public void gameOver() {
        player = null;
        
        mobTimer.stop();
        scoreTimer.stop();
        
        message.setText("Game Over");
        messageTimer.start();
        
        music.stop();
        deathSound.playInstance();
    }

    /** 
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#onDisable() 
     */
    @Override
    protected void onEnable() {
        super.onEnable();        
        score = 0;
        
        player = Player.getNewInstancePlayer((Dodgethecreeps) getApplication());
        player.translate(0, -1.5);
        player.setEnabled(false);
        
        dyn4jAppState.getPhysicsSpace().addBody(player);
        rootNode.attachChild(player.getJmeObject());
        
        message.setText("Get Ready!");
        scoreLabel.setText("0");
        
        startTimer.start();
        
        music.play();
        deathSound.stop();
    }

    /** 
     * (non-JavaDoc) 
     * @see  com.jme3.app.state.BaseAppState#onDisable() 
     */
    @Override
    protected void onDisable() {
        super.onDisable();        
        dyn4jAppState.getPhysicsSpace().removeAll(false);
        rootNode.detachAllChildren();
        
        getState(MainSceneAppState.class).setEnabled(true);
    }    
}
