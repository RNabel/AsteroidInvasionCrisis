package asteroids3d.gamestate.objects.Asteroids;

import edu.wlu.cs.levy.CG.KDTree;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;
import org.rajawali3d.util.RajLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.util.IntervalRandom;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class AsteroidManager extends Manager {
    private final List<Asteroid> asteroids;
    private final IntervalRandom random = new IntervalRandom();
    private KDTree<Asteroid> asteroidLocationMap;

    public AsteroidManager(RajawaliScene scene) {
        super(scene);
        this.asteroids = new ArrayList<>();
        asteroidLocationMap = new KDTree<>(3);
    }


    private void initAsteroid() {
        RajLog.i("Created Asteroid.");
        // Create random starting location at top of screen. TODO update to be random x and z within bounds.
        Vector3 min = this.getBoundingBox().getMin();
        Vector3 max = this.getBoundingBox().getMax();
        int xPos = random.nextInt(min.x, max.x);
        int zPos = random.nextInt(min.z, max.z);


        Asteroid newAsteroid = new Asteroid(this,
                new Vector3(xPos, 100, zPos), // Position.
                new Vector3(0, -0.01, 0),   // Acceleration.
//                createRandomVelocity(true, 2),  // Velocity.
                new Vector3(0, -0.05, 0),   // Velocity.
                3); // Radius.

        // Add Asteroid to RajawaliScene.
        asteroids.add(newAsteroid);

        // Add asteroid to location map.
        insertInLocationTree(newAsteroid.getLocation(), newAsteroid);
    }

    public void update(long totalTime) {
        Iterator<Long> longIt = getGameState().getCurrentLevel().getStartTimes().iterator();

        // Check if level over as no asteroids falling any more.
        if (asteroids.size() == 0) {
            boolean isFinished = getGameState().getCurrentLevel().checkIfEndOfLevel(totalTime);
            if (isFinished) {
                // Notify the GameState that the level is over.
                getGameState().setStateType(ProgramState.AFTER_LEVEL);
            }
        }

        while (longIt.hasNext() &&
                longIt.next() <= totalTime) {
            initAsteroid();
            longIt.remove();
        }

        // Update location of all asteroids. Delete asteroids which are off-screen.
        Iterator<Asteroid> it = asteroids.iterator();
        asteroidLocationMap = new KDTree<>(3);
        while (it.hasNext()) {
            Asteroid asteroid = it.next();
            boolean isOnScreen = asteroid.updatePosition();
//            boolean isOnScreen = true;

            // Update location in KDTree.
//            updateLocationTree(oldLocation, asteroid.getLocation().clone(), asteroid);
            insertInLocationTree(asteroid.getLocation(), asteroid);

            // Check if asteroids are on screen.
            if (!isOnScreen) {
                it.remove();
                getCurrentScene().removeChild(asteroid.getShape());

                // Update points.
                getGameState().getPoints().asteroidImpact(getGameState().getCurrentLevel().getLevel());
            }
            // TODO add collision with vehicle and other asteroids.
        }
    }

    public void tearDown() {
        // Remove all asteroids from the scene.
        for (Asteroid asteroid :
                this.getAsteroids()) {
            getCurrentScene().removeChild(asteroid.getShape());
        }
    }

    // Getters and setters.
    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public void deleteAsteroid(Asteroid asteroid) {
        // TODO make sure asteroid is removed from scene element and location tree.
//        deleteFromLocationTree(asteroid.getLocation());
        getCurrentScene().removeChild(asteroid.getShape());
        this.asteroids.remove(asteroid);
    }

    public KDTree<Asteroid> getAsteroidLocationMap() {
        return asteroidLocationMap;
    }

    private void insertInLocationTree(Vector3 location, Asteroid asteroid) {
        double[] newArr = vectorToArray(location);
        KDTree<Asteroid> tree = getAsteroidLocationMap();
        try {
            tree.insert(newArr, asteroid);
        } catch (KeySizeException | KeyDuplicateException e) {
            e.printStackTrace();
        }
    }

    private static double[] vectorToArray(Vector3 inputVector) {
        return new double[]{inputVector.x, inputVector.y, inputVector.z};
    }


}
