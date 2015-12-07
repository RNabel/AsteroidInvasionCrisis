package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;
import org.rajawali3d.util.RajLog;

import asteroids3d.gamestate.objects.Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author rn30.
 */
class ExplosionManager extends Manager {
    public static final int explosionSize = 5;

    private final List<Explosion> explosions = new ArrayList<>();

    public ExplosionManager(RajawaliScene currentScene) {
        super(currentScene);
    }

    public void update() {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion explosion = it.next();
            boolean isFinished = explosion.updateSize();

            if (isFinished) {
                getCurrentScene().removeChild(explosion.getShape());
                it.remove();
            }
        }
    }

    public void tearDown() {
        for (Explosion explosion :
                explosions) {
            getCurrentScene().removeChild(explosion.getShape());
        }
    }

    public void createExplosion(Vector3 location) {
        RajLog.i("Created new Explosion [ExplosionManager]");
        Explosion explosion = new Explosion(this, location);
        explosions.add(explosion);
    }
}
