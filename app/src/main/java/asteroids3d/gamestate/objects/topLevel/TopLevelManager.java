package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.gamestate.objects.Manager;

public class TopLevelManager extends Manager {
    private final RocketManager rManager;
    private final ExplosionManager eManager;
    private final Vehicle vehicle;

    public TopLevelManager(RajawaliScene scene) {
        super(scene);
        rManager = new RocketManager(this, scene);
        eManager = new ExplosionManager(scene);
        vehicle = new Vehicle(this);
    }

    public void update() {
        eManager.update();
        rManager.update();
        vehicle.updateState();
    }

    public int getRocketsAvailable() {
        return getGameState().getCurrentLevel().getRocketsToStart();
    }

    public void decreaseRocketsAvailable() {
        if (isRocketAvailable()) {
            getGameState().getCurrentLevel().decrementRocketsLeft();
        } else {
            System.err.println("Tried to decrease num of rockets although not possible.");
        }
    }

    public boolean isRocketAvailable() {
        return getGameState().getCurrentLevel().getRocketsToStart() > 0;
    }

    public void tearDown() {
        eManager.tearDown();
        rManager.tearDown();
    }

    public void createExplosion(Vector3 location) {
        eManager.createExplosion(location);
    }

    public RocketManager getRManager() {
        return rManager;
    }

}
