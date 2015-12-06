package asteroids3d.gamestate.objects;

import java.util.Random;
import java.util.TreeSet;

// Holds the state of the level.
public class Level {
    private long levelLength = 30; // In wall-clock time in ns.
    public static final int rocketsToStart = 100;
    private int totalAsteroids;
    private Random random;
    private TreeSet<Double> startTimes;
    private int level;


    public Level(int levelNum) {
        this.level = levelNum;
        this.totalAsteroids = calculateAsteroidNumber();
        this.random = new Random();

        // Create wave start times.
        startTimes = new TreeSet<>();
        createRockStartingTimes();
    }

    // Helper methods.
    private int calculateAsteroidNumber() {
        return (int) Math.round(Math.pow(10d, (double)this.level * 0.2 + 1d));
    }

    public int calculateRocketNumber () {
        return 1 + (int)Math.round(1.3 * level);
    }

    // Creates normally distributed value in [-totalInterval, 0]. Used to create relative timings for rock waves
    private double nextGaussian(double totalInterval) {
        double standardDeviation = (totalInterval) / 2.0;
        double gaussian;

        // Create a Gaussian which is within [- totalInterval, 0].
        do {
            gaussian = random.nextGaussian() * standardDeviation;
        } while (gaussian > 0 || gaussian < -1 * totalInterval);
        return Math.round(gaussian) + this.levelLength;
    }

    private void createRockStartingTimes() {
        for (int i = 0; i < totalAsteroids; i++) {
            double timeStamp = nextGaussian(levelLength);
            startTimes.add(timeStamp);
        }
    }

    public TreeSet<Double> getStartTimes() {
        return startTimes;
    }

    public boolean checkIfEndOfLevel(double time) {
        return time > this.levelLength;
    }

    public int getLevel() {
        return level;
    }
}
