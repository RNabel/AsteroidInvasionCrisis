package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.WorldParameters;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.gamestate.objects.Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author rn30.
 */
public class RocketManager extends Manager {
    Manager manager;

    public RocketManager(Manager manager, RajawaliScene scene) {
        super(scene);
        this.manager = manager;
    }

    List<Rocket> rockets = new ArrayList<>();

    @Override
    public void update(double currentFrame, long totalTime) {
        // Update all rockets.
        Iterator<Rocket> it = rockets.iterator();

        while (it.hasNext()) {
            Rocket rocket = it.next();
            boolean isOnScreen = rocket.updatePosition();

            // Remove rocket if off screen.
            boolean removeRocket = false;
            if (!isOnScreen) {
                removeRocket = true;

                // Detect whether either stationary or at target.
            } else if (Vector3.distanceTo(rocket.getOrigin(), rocket.getLocation())
                       > rocket.getDistance()) {

                this.createExplosion(rocket);
                removeRocket = true;
            }

            if (removeRocket) {

                it.remove();
            }
        }
    }

    // Launch an unguided rocket.
    public void rocketLaunched(Quaternion direction, Vector3 origin) {
        TopLevelManager manager = (TopLevelManager) this.manager;
        if (manager.isRocketAvailable()) {
            manager.decreaseRocketsAvailable();

            // Set acceleration and direction.
            Vector3 acceleration = new Vector3(0, 0, 0);

            // Get initial orientation and work out velocity.
            Vector3 velocity = WorldParameters.FORWARD_AXIS.clone();
            velocity.rotateBy(getRenderer().getCurrentCamera().getOrientation());
            velocity.inverse();

            Rocket newRocket = new Rocket(this, origin, acceleration, velocity);
            rockets.add(newRocket);
        }
    }

    public void createExplosion(Rocket rocket) {
        ((TopLevelManager) this.manager).createExplosion(rocket.getLocation());
    }

    // Getters and Setters.
    public List<Rocket> getRockets() {
        return rockets;
    }
}
