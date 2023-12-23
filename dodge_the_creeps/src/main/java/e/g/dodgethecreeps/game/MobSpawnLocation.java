/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.dyn4j.geometry.Vector2;

/**
 * This class <code>MobSpawnLocation</code> will provide the random routes of
 * the {@link e.g.dodgethecreeps.game.Mob}.
 * 
 * @author wil
 * @version 1.0.5
 * @since 1.0.0
 */
public final class MobSpawnLocation {
    
    /**
     * Class in charge of managing the coordinates of a line.
     */
    class Line {
        
        /** Starting point. */
        Vector2 start;
        
        /** End-Point. */
        Vector2 end;

        /**
         * Class <code>Line</code> constructor where the 2 vectors are initialized.
         * @param start vector-start
         * @param end vector-end
         */
        public Line(Vector2 start, Vector2 end) {
            this.start = start;
            this.end = end;
        }
    }
    
    /** Paths-list. */
    private final List<Line> paths = new ArrayList<>();

    /**
     * Default constructor
     */
    public MobSpawnLocation() {
    }
    
    /**
     * Add a point to the path-list.
     * @param p1 start
     * @param p2 end
     */
    public void add(Vector2 p1, Vector2 p2) {
        this.paths.add(new Line(p1, p2));
    }
    
    /**
     * Returns a random path.
     * @return path-vector2
     */
    public Vector2 getRandomPath() {
        int lenght = this.paths.size();
        int index  = (int) (Math.random() * lenght);
        
        if (index >= lenght) {
            index = lenght - 1;
        }
        
        Line line = this.paths.get(index);
        if (line == null) {
            return new Vector2();
        }
        
        double x, y;
        if (line.start.y == line.end.y) {
            x = ThreadLocalRandom.current().nextDouble(Math.min(line.start.x, line.end.x), Math.max(line.start.x, line.end.x));
            y = line.start.y;            
        } else {
            x = line.start.x;
            y = ThreadLocalRandom.current().nextDouble(Math.min(line.start.y, line.end.y), Math.max(line.start.y, line.end.y));
        }
        return new Vector2(x, y);
    }
}
