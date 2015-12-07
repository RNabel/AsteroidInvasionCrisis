package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;
import org.rajawali3d.util.RajLog;

import asteroids3d.gamestate.Points;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Asteroids.AsteroidManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author rn30.
 */
public class ExplosionManager extends Manager {
    private Manager manager;
    public static int explosionSize = 5;

    Object3D bangShape;
    private List<Explosion> explosions = new ArrayList<>();

    public ExplosionManager(Manager manager, RajawaliScene currentScene) {
        super(currentScene);
        this.manager = manager;
    }

    @Override
    public void update(double deltaTime, long totalTime) {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion expl = it.next();
            boolean isFinished = expl.updateSize();

            if (isFinished) {
                getCurrentScene().removeChild(expl.getShape());
                it.remove();
            }
        }
    }

    @Override
    public void tearDown() {
        for (Explosion explosion :
                explosions) {
            getCurrentScene().removeChild(explosion.getShape());
        }
    }

    public void createExplosion(Vector3 location) {
        RajLog.i("Created new Explosion [ExplosionManager]");
        Explosion expl = new Explosion(this, location);
        explosions.add(expl);
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public Object3D getExplosionShape() {
        return this.bangShape;
    }

    public void clearExplosions() {
        this.explosions.clear();
    }
}
