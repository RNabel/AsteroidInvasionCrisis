package asteroids3d.gamestate;

import java.util.HashMap;
import java.util.Map;

/**
 * Author rn30.
 */
public class Points {
    private static final float LEVEL_FACTOR = 1.1f;

    public enum pointTypes {
        DAMAGE, ROCK_DESTROYED
    }

    // Mappings of values to their point value.
    private static final Map<pointTypes, Integer> valueMappings;
    static {
        valueMappings = new HashMap<>();
        valueMappings.put(pointTypes.DAMAGE, -100);
        valueMappings.put(pointTypes.ROCK_DESTROYED, 10);
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

    public void increasePoints(pointTypes type, int number, int currentLevel) {
        int existingPoints = this.pointMappings.get(type);
        this.pointMappings.put(type, Math.round (existingPoints + number *
                        valueMappings.get(type) *
                        (1 + LEVEL_FACTOR + currentLevel))
        );
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
}
