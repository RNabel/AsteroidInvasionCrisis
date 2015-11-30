package asteroids3d.gamestate.objects.Asteroids;

import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.RajawaliVRExampleRenderer;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.gamestate.objects.StationaryObject;
import asteroids3d.util.IntervalRandom;

import java.util.*;

public class AsteroidManager extends Manager {
    private List<Asteroid> asteroids;
    private IntervalRandom random = new IntervalRandom();
    private TreeSet<Long> startTimes;

    public AsteroidManager(TreeSet<Long> rockTimes, RajawaliScene scene) {
        super(scene);
        this.asteroids = new ArrayList<>();
        this.startTimes = rockTimes;
    }

    private void initRock() {
        // Create random starting location at top of screen. TODO update to be random x and z within bounds.
        Vector3 min = this.getBoundingBox().getMin();
        Vector3 max = this.getBoundingBox().getMax();
        int xPos = random.nextInt(min.x, max.x);
        int zPos = random.nextInt(min.z, max.z);


        Asteroid newAsteroid = new Asteroid(this,
                new Vector3(xPos, 1000, zPos), // Position.
                new Vector3(0, 0, 0),   // Acceleration.
                createRandomVelocity(true, 2),  // Velocity.
                1); // Radius.

        // 5% chance that rock splits.
        boolean splits = random.nextDouble() < 0.05;
        newAsteroid.setSplits(splits);

        // Add Asteroid to RajawaliScene.
        getCurrentScene().addChild(newAsteroid.getShape());
        asteroids.add(newAsteroid);
    }


    @Override
    public void update(Long frameNumber) {
        Iterator<Long> longIt = startTimes.iterator();

        // Check if level over as no asteroids falling any more.
        if (asteroids.size() == 0) {
            boolean isFinished = getGameState().getCurrentLevel().checkIfEndOfLevel(frameNumber);
            if (isFinished) {
                // Notify the GameState that the level is over.
                getGameState().setStateType(ProgramState.AFTER_LEVEL);
            }
        }

        while (longIt.hasNext() &&
                longIt.next() <= frameNumber) {
            initRock();
            longIt.remove();
        }

        // Update location of all asteroids. Delete asteroids which are off-screen.
        Iterator<Asteroid> it = asteroids.iterator();

        while (it.hasNext()) {
            Asteroid asteroid = it.next();
            boolean isOnScreen = asteroid.updatePosition();

            // Check if asteroids are on screen.
            if (!isOnScreen) {
                it.remove();
            }

            // TODO add collision with vehicle and other asteroids.
        }

    }

    // Getters and setters.
    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    // Returns List within specific X interval.
    public List<Asteroid> getRocksInInterval(Vector3 min, Vector3 max) {
        return asteroids;
    }

    public void deleteRock(Asteroid asteroid) {
        // TODO make sure asteroid is removed from scene element.
        getCurrentScene().removeChild(asteroid.getShape());
        this.asteroids.remove(asteroid);
    }

    // Expects int to indicate whether to go right (0), left (1), or anywhere.
    public Vector3 createRandomVelocity(boolean downwards, int right) {
        IntervalRandom random = new IntervalRandom();
        Quaternion direction;
        float y = -1 * Math.abs(random.nextFloat()); // -1 to direct downwards.
        float z = random.nextFloat();
        float x = random.nextFloat();
        boolean swap;
        if (right == 0 || right == 1)
            swap = right == 0;
        else
            swap = random.nextBoolean();

        if (swap) x *= -1; // Flip to other side randomly.

        Vector3 initialVel = new Vector3(x, y, z);

        float speed = random.nextFloat() * 3;
        initialVel.multiply(speed);
        return initialVel;
    }
}
