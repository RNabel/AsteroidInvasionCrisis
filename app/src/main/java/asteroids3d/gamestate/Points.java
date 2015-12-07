package asteroids3d.gamestate;

import java.util.HashMap;
import java.util.Map;

import asteroids3d.gamestate.objects.ProgramState;

/**
 * Author rn30.
 */
public class Points {
    private static final float LEVEL_FACTOR = 1.1f;
    private final GameState state;

    private int runningTotal = 0;

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
    private final Map<pointTypes, Integer> pointMappings;

    public Points(GameState state) {
        this.state = state;
        // Initialize map based on static map.
        pointMappings = new HashMap<>();
        for (pointTypes type : valueMappings.keySet()) {
            pointMappings.put(type, 0);
        }
    }

    private void increasePoints(pointTypes type, int currentLevel) {
        int existingPoints = this.pointMappings.get(type);
        int newPoints = Math.round(existingPoints + valueMappings.get(type) *
                (1 + LEVEL_FACTOR + currentLevel));
        runningTotal += newPoints;

        if (runningTotal < 0) {
            state.setStateType(ProgramState.GAME_OVER);
        }

        this.pointMappings.put(type, newPoints);
    }

    // Point short hands.
    public void asteroidDestroyed(int level) {
        pointTypes type = pointTypes.ASTEROID_DESTROYED;
        increasePoints(type, level);
    }
    public void asteroidImpact(int level) {
        pointTypes type = pointTypes.ASTEROID_MISSED;
        increasePoints(type, level);
    }

    public int getTotalPoints() {
        return runningTotal;
    }

}
