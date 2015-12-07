package asteroids3d.gamestate.objects;

import java.util.Random;
import java.util.TreeSet;

// Holds the state of the level.
public class Level {
    private long levelLength = 30 * 1000000000L; // In wall-clock time in ns.
    private long timeOffset;
    public int rocketsToStart = 100;
    private int totalAsteroids;
    private Random random;
    private TreeSet<Long> startTimes;
    private int level;

    /**
     * Creates a new level object which keeps track of level difficulty and timing.
     * @param levelNum The number of the level.
     * @param timeOffset The beginning time of the level.
     */
    public Level(int levelNum, long timeOffset) {
        this.level = levelNum;
        this.totalAsteroids = calculateAsteroidNumber();
        this.random = new Random();
        this.timeOffset = timeOffset;
        this.rocketsToStart = calculateRocketNumber();

        // Create wave start times.
        startTimes = new TreeSet<>();
        createRockStartingTimes();
    }

    public int getRocketsToStart() {
        return rocketsToStart;
    }

    // Helper methods.
    private int calculateAsteroidNumber() {
        return (int) Math.round(this.level * 5 + 10);
    }

    public int calculateRocketNumber () {
        return 20 + Math.round(4 * level);
    }

    // Creates normally distributed value in [-totalInterval, 0]. Used to create relative timings for rock waves
    private long nextGaussian(long totalInterval) {
        long standardDeviation = (long) ((totalInterval) / 2.0);
        long gaussian;

        // Create a Gaussian which is within [- totalInterval, 0].
        do {
            gaussian = (long) (random.nextGaussian() * standardDeviation);
        } while (gaussian > 0 || gaussian < -1 * totalInterval);
        return gaussian + this.levelLength;
    }

    private void createRockStartingTimes() {
        for (int i = 0; i < totalAsteroids; i++) {
            long timeStamp = nextGaussian(levelLength) + timeOffset;
            startTimes.add(timeStamp);
        }
    }

    public TreeSet<Long> getStartTimes() {
        return startTimes;
    }

    public boolean checkIfEndOfLevel(long time) {
        return time > this.levelLength + timeOffset;
    }

    public int getLevel() {
        return level;
    }

    public void decrementRocketsLeft() {
        this.rocketsToStart--;
    }
}
