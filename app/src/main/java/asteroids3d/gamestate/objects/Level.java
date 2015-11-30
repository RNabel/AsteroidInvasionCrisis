package asteroids3d.gamestate.objects;

import java.util.Random;
import java.util.TreeSet;

/**
 * Author rn30.
 */
// Holds the state of the level.
public class Level {
    private int levelLength = 900; // In frames.
    public static final int rocketsToStart = 20;
    private int totalRocks;
    private Random random;
    private TreeSet<Long> startTimes;
    private int level;


    public Level(int levelNum) {
        this.level = levelNum;
        this.totalRocks = calculateRockNumber();
        this.random = new Random();

        // Create wave start times.
        startTimes = new TreeSet<>();
        createRockStartingTimes();
    }

    // Helper methods.
    private int calculateRockNumber() {
        return (int) Math.round(Math.pow(10d, (double)this.level * 0.2 + 1d));
    }

    public int calculateRocketNumber () {
        return 15 + (int)Math.round(1.3 * level);
    }

    // Creates normally distributed value in [-totalInterval, 0]. Used to create relative timings for rock waves
    private long nextGaussian(int totalInterval) {
        double standardDeviation = (totalInterval) / 2.0;
        double gaussian;

        // Create a Gaussian which is within [- totalInterval, 0].
        do {
            gaussian = random.nextGaussian() * standardDeviation;
        } while (gaussian > 0 || gaussian < -1 * totalInterval);
        return Math.round(gaussian) + this.levelLength;
    }

    private void createRockStartingTimes() {
        for (int i = 0; i < totalRocks; i++) {
            long frameNumber = nextGaussian(levelLength);
            startTimes.add(frameNumber);
        }
    }

    public TreeSet<Long> getStartTimes() {
        return startTimes;
    }

    public boolean checkIfEndOfLevel(long currentFrame) {
        return currentFrame > this.levelLength;
    }

    public int getLevel() {
        return level;
    }
}
