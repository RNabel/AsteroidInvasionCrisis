package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

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
    Object3D bangShape;
    private List<Explosion> explosions = new ArrayList<>();

    public ExplosionManager(Manager manager, RajawaliScene currentScene) {
        super(currentScene);
        this.manager = manager;

    }

    @Override
    public void update(Long currentFrame) {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion expl = it.next();
            boolean isFinished = expl.updateSize();

            // Check whether explosion contains rock.


            if (isFinished) {
                it.remove();
            } else {
                Vector3 location = expl.getLocation();
                Object3D shape = expl.getShape();
                AsteroidManager rManager = getGameState().getAsteroidManager();
                Vector3 min = getBoundingBox().getMin(); // TODO find correct subset of coordinates.
                Vector3 max = getBoundingBox().getMax();

                List<Asteroid> overlappingRocks = rManager.getRocksInInterval(
                        min, max
                );

                Iterator<Asteroid> rockIt = overlappingRocks.iterator();
                List<Asteroid> toBeDeleted = new ArrayList<>();
                while(rockIt.hasNext()) {
                    Asteroid rock = rockIt.next();
                    boolean isExplContainsRock = expl.contains(rock.getLocation());
                    if (isExplContainsRock) {
                        toBeDeleted.add(rock);
                    }
                }

                this.getGameState().manipulatePoints(Points.pointTypes.ROCK_DESTROYED, toBeDeleted.size());
                // Delete the relevant objects from scene as well.
                for (Asteroid ast : toBeDeleted) {
                    getCurrentScene().removeChild(ast.getShape());
                }

                rManager.getAsteroids().removeAll(toBeDeleted);
            }
        }
    }

    public void createExplosion(Vector3 location) {
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
