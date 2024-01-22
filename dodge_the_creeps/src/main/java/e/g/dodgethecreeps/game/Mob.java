/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.util.IntMap;

import e.g.dodgethecreeps.Dodgethecreeps;

import java.util.ArrayList;
import java.util.List;

import jme3gl2.physics.control.RigidBody2D;
import jme3gl2.scene.control.AnimatedSprite;
import jme3gl2.scene.shape.Sprite;
import jme3gl2.utilities.GeometryUtilities;
import jme3gl2.utilities.MaterialUtilities;
import jme3gl2.utilities.TextureUtilities;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Class <code>Mob</code> that manages enemies.
 * @author wil
 * @version 1.0.5
 * @since 1.0.0
 */
public final class Mob extends RigidBody2D {
    
    /** Camera scene. */
    private final Camera cam;
    
    /** Map with physical shapes. */
    private final IntMap<List<BodyFixture>> bodyFixtures = new IntMap<>();
    
    /** Index-animation. */
    private int oldIndex;

    /**
     * Default constructor.
     * @param cam camera
     */
    public Mob(Camera cam) {
        this.cam = cam;
    }
    
    /**
     * (non-Javadoc)
     * @see jme3gl2.physics.control.PhysicsBody2D#ready() 
     */
    @Override
    protected void ready() {
        String[] names = spatial.getControl(AnimatedSprite.class).getAnimations().toArray(new String[3]);
        int rand = (int) (Math.random() * names.length);
        if (rand >= names.length) {
            rand = names.length - 1;
        }
        
        BodyFixture bf_def;
        if ("fly".equals(names[rand])) {
            //------------------------------------------------------------------
            bf_def = new BodyFixture(GeometryUtilities.createCircle(0.5));
            bf_def.setFilter(new LayerFilter(1));
            addMapBodyFixture(0, bf_def);
            //------------------------------------------------------------------
            
            
            //------------------------------------------------------------------
            bf_def = new BodyFixture(GeometryUtilities.createCircle(0.3));
            bf_def.setFilter(new LayerFilter(1));
            bf_def.setSensor(true);
            addMapBodyFixture(1, bf_def);
            
            Capsule capsule = GeometryUtilities.createCapsule(0.1, 1.5);
            capsule.translate(-0.2, 0);
            
            bf_def = new BodyFixture(capsule);
            bf_def.setFilter(new LayerFilter(1));
            bf_def.setSensor(true);
            addMapBodyFixture(1, bf_def);
            //------------------------------------------------------------------
            
            spatial.getControl(AnimatedSprite.class).addSpriteAnimationChangeListener((body, index) -> {
                setMapBodyFixture(oldIndex, true);
                setMapBodyFixture(index, false);
                oldIndex = index;
            });
        } else {
            bf_def = new BodyFixture(GeometryUtilities.createCapsule(1, 0.8));
            bf_def.setFilter(new LayerFilter(1));
            addMapBodyFixture(0, bf_def);
        }
        
        spatial.getControl(AnimatedSprite.class).playAnimation(names[oldIndex = rand], 0.15F);
    }
    
    /**
     * Add an {@code org.dyn4j.dynamics.BodyFixture} to this body; The map of 
     * physical bodies records them.
     * 
     * @param key key index
     * @param bf physical shape
     */
    private void addMapBodyFixture(int key, BodyFixture bf) {
        List<BodyFixture> bfs = this.bodyFixtures.get(key);
        if (bfs == null) {
            bfs = new ArrayList<>();
            this.bodyFixtures.put(key, bfs);
        }
        bfs.add(bf);
        addFixture(bf);
    }
    
    /**
     * Sets the status of a previously registered physical shape.
     * @param key key index
     * @param sensor <code>true</code> to disable physical shape; otherwise
     * <code>false</code>.
     */
    private void setMapBodyFixture(int key, boolean sensor) {
        List<BodyFixture> bfs = this.bodyFixtures.get(key);
        if (bfs == null) {
            return;
        }        
        for (final BodyFixture bf : bfs) {
            bf.setSensor(sensor);
        }
    }
    
    /**
     * (non-Javadoc)
     * @see jme3gl2.physics.control.PhysicsBody2D#physicsProcess(float) 
     * @param delta float
     */
    @Override
    protected void physicsProcess(float delta) {   
        Vector2 pos = getTransform().getTranslation();
        double x = pos.x, y = pos.y;
        
        if (x < (cam.getFrustumLeft() - 1) 
                || x > (cam.getFrustumRight() + 1)) {
            
            physicsSpace.removeBody(this);
            spatial.removeFromParent();
        } else if (y < (cam.getFrustumBottom() - 1) 
                        || y > (cam.getFrustumTop() + 1)) {
            
            physicsSpace.removeBody(this);
            spatial.removeFromParent();
        }
    }
    
    /**
     * Returns a new snapshot of the <code>Mob</code> class; manages the models.
     * @param app application
     * @return Mob
     */
    public static Mob getNewInstanceMob(Dodgethecreeps app) {
        AssetManager assetManager = app.getAssetManager();
        Material mat = MaterialUtilities.getUnshadedMaterialFromClassPath(assetManager, "Textures/enemyFlyingAlt_1.png");
        mat.setFloat("AlphaDiscardThreshold", 0.0F);
        
        Sprite sprite = new Sprite(1.5F, 1.5F);
        Geometry geom = new Geometry("Mob", sprite);
        
        // Add the following animations.
        AnimatedSprite as = new AnimatedSprite();
        as.addAnimation("fly", new Texture[] {
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemyFlyingAlt_1.png"),
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemyFlyingAlt_2.png")
        });
        as.addAnimation("swim", new Texture[] {
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemySwimming_1.png"),
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemySwimming_2.png")
        });
        as.addAnimation("walk", new Texture[] {
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemyWalking_1.png"),
            TextureUtilities.getTextureFromClassPath(assetManager, "Textures/enemyWalking_2.png")
        });        
        as.setSpeed(0.60F);
        
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        Mob mob = new Mob(app.getCamera());
        mob.setMass(MassType.FIXED_LINEAR_VELOCITY);
        
        geom.addControl(as);
        geom.addControl(mob);
        return mob;
    }
}
