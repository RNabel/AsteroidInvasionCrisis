package asteroids3d.gamestate;

import java.util.HashMap;
import java.util.Map;

/**
 * Author rn30.
 */
public class Points {
    private static final float LEVEL_FACTOR = 1.1f;
    private int numOfLives;
    boolean dirty = true;

    public enum pointTypes {
        ASTEROID_MISSED, ASTEROID_DESTROYED
    }

    // Mappings of values to their point value.
    private static final Map<pointTypes, Integer> valueMappings;
    static {
        valueMappings = new HashMap<>();
        valueMappings.put(pointTypes.ASTEROID_MISSED, -50);
        valueMappings.put(pointTypes.ASTEROID_DESTROYED, 10);
    }

    // Points achieved are associated with how they were received.
    private Map<pointTypes, Integer> pointMappings;

    public Points() {
        // Initialize map based on static map.
        pointMappings = new HashMap<>();
        for (pointTypes type : valueMappings.keySet()) {
            pointMappings.put(type, 0);
        }
    }

    private void increasePoints(pointTypes type, int number, int currentLevel) {
        int existingPoints = this.pointMappings.get(type);
        this.pointMappings.put(type, Math.round (existingPoints + number *
                        valueMappings.get(type) *
                        (1 + LEVEL_FACTOR + currentLevel))
        );
    }

    // Point short hands.
    public void asteroidDestroyed(int level) {
        pointTypes type = pointTypes.ASTEROID_DESTROYED;
        increasePoints(type, 1, level);
    }
    public void asteroidImpact(int level) {
        pointTypes type = pointTypes.ASTEROID_MISSED;
        increasePoints(type, 1, level);
    }

    public int getTotalPoints() {
        int totalPoints = 0;

        for (pointTypes type : pointMappings.keySet()) {
            totalPoints += pointMappings.get(type);
        }

        return totalPoints;
    }

    public Map<pointTypes, Integer> getPointMappings() {
        return pointMappings;
    }

    public void endOfLevelPointUpdate(GameState state, int level) {
        // TODO Code to count up points.
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
