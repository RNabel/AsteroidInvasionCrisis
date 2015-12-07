package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.Asteroids3DRenderer;
import asteroids3d.gamestate.objects.Manager;

import java.util.List;

public class TopLevelManager extends Manager {
    private RocketManager rManager;
    private ExplosionManager eManager;
    private Vehicle vehicle;
    private int rocketsAvailable;

    public TopLevelManager(int rocketsAvailable, RajawaliScene scene) {
        super(scene);
        rManager = new RocketManager(this, scene);
        eManager = new ExplosionManager(this, scene);
        vehicle = new Vehicle(this);
        this.rocketsAvailable = rocketsAvailable;
    }

    @Override
    public void update(double deltaTime, long totalTime) {
        eManager.update(deltaTime, totalTime);
        rManager.update(deltaTime, totalTime);
        vehicle.updateState();
    }

    public List<Rocket> getRockets() {
        return rManager.getRockets();
    }

    public int getRocketsAvailable() {
        return getGameState().getCurrentLevel().getRocketsToStart();
    }

    public void decreaseRocketsAvailable() {
        if (rocketsAvailable > 0) {
            getGameState().getCurrentLevel().decrementRocketsLeft();
            this.rocketsAvailable--;
        } else {
            System.err.println("Tried to decrease num of rockets although not possible.");
        }
    }

    public boolean isRocketAvailable() {
        return rocketsAvailable > 0;
    }

    public List<Explosion> getExplosions() {
        return eManager.getExplosions();
    }

    public void mouseClicked() {
        // TODO handle tabs, will have to be more elaborate.

        // Get current orientation, and location.
        Quaternion orientation = getRenderer().getCurrentCamera().getOrientation();
        Vector3 origin = getRenderer().getCurrentCamera().getPosition();
        rManager.rocketLaunched(orientation, origin);
    }

    public Asteroids3DRenderer getRenderer() {
        return super.getRenderer();
    }

    @Override
    public void tearDown() {
        eManager.tearDown();
        rManager.tearDown();
    }

    public void createExplosion(Vector3 location) {
        eManager.createExplosion(location);
    }

    public void clearExplosions() {
        eManager.clearExplosions();
    }

    public void setRocketsAvailable(int rocketsAvailable) {
        this.rocketsAvailable += rocketsAvailable;
    }

    public RocketManager getrManager() {
        return rManager;
    }

    public ExplosionManager geteManager() {
        return eManager;
    }
}
