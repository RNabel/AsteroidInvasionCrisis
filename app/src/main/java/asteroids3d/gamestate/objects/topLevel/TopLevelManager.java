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

    public TopLevelManager(RajawaliScene scene) {
        super(scene);
        rManager = new RocketManager(this, scene);
        eManager = new ExplosionManager(this, scene);
        vehicle = new Vehicle(this);
    }

    @Override
    public void update(double deltaTime, long totalTime) {
        eManager.update(deltaTime, totalTime);
        rManager.update(deltaTime, totalTime);
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

    public RocketManager getrManager() {
        return rManager;
    }

    public ExplosionManager geteManager() {
        return eManager;
    }
}
