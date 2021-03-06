package asteroids3d.gamestate.objects.topLevel;


import org.rajawali3d.WorldParameters;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Manager;
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author rn30.
 */
public class RocketManager extends Manager {
    private final Manager manager;

    public RocketManager(Manager manager, RajawaliScene scene) {
        super(scene);
        this.manager = manager;
    }

    private final List<Rocket> rockets = new ArrayList<>();

    public void update() {
        // Update all rockets.
        Iterator<Rocket> it = rockets.iterator();

        // Get the tree map containing all asteroids.
        KDTree tree = getGameState().getAsteroidManager().getAsteroidLocationMap();

        while (it.hasNext()) {
            Rocket rocket = it.next();
            boolean isOnScreen = rocket.updatePosition();

            // Remove rocket if off screen.
            boolean removeRocket = false;
            if (!isOnScreen) {
                removeRocket = true;
            }

            // Detect whether it reached asteroid.
            double[][] limits = getLimits(rocket.getLocation());
            List objects = null;
            try {
                objects = tree.range(limits[1], limits[0]);
            } catch (KeySizeException e) {
                e.printStackTrace();
            }

            if (objects != null && objects.size() > 0) {
                removeRocket = true;

                // Remove the asteroids in the explosion radius.
                for (Object astObj :
                        objects) {
                    getGameState().getAsteroidManager().deleteAsteroid((Asteroid) astObj);
                }

                // Create Explosion.
                createExplosion(rocket);

                // Amend points.
                getGameState().getPoints().asteroidDestroyed(getGameState().getCurrentLevel().getLevel());
            }

            if (removeRocket) {
                getCurrentScene().removeChild(rocket.getShape());
                it.remove();
            }
        }

    }


    // Launch an unguided rocket.
    public void rocketLaunched(Vector3 origin) {
        TopLevelManager manager = (TopLevelManager) this.manager;
        if (manager.isRocketAvailable()) {
            manager.decreaseRocketsAvailable();

            // Set acceleration and direction.
            Vector3 acceleration = new Vector3(0, 0, 0);

            // Get initial orientation and work out velocity.
            Vector3 velocity = WorldParameters.FORWARD_AXIS.clone();
            velocity.rotateBy(getRenderer().getCurrentCamera().getOrientation());
            velocity.inverse();
            velocity.multiply(2);

            Rocket newRocket = new Rocket(this, origin, acceleration, velocity);
            rockets.add(newRocket);
        }
    }

    private void createExplosion(Rocket rocket) {
        ((TopLevelManager) this.manager).createExplosion(rocket.getLocation());
    }

    // --- Helpers. ---

    /**
     * Calculates the upper and lower bounds of an exploding rocket.
     *
     * @param location The origin from which to calculate the limits.
     * @return two 3-element double arrays, [0] upper limit, [1] lower limit.
     */
    private double[][] getLimits(Vector3 location) {
        double[][] output = new double[2][];
        int explosionSize = ExplosionManager.explosionSize;
        // Set max.
        output[0] = new double[]{location.x + explosionSize, location.y + explosionSize, location.z + explosionSize};
        // Set min.
        output[1] = new double[]{location.x - explosionSize, location.y - explosionSize, location.z - explosionSize};

        return output;
    }

    public void tearDown() {
        for (Rocket rocket :
                rockets) {
            getCurrentScene().removeChild(rocket.getShape());
        }
    }
}
