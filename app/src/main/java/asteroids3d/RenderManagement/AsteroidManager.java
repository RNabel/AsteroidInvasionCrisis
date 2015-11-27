package asteroids3d.RenderManagement;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;

import java.util.HashMap;

public class AsteroidManager {
    RajawaliScene currentScene;
    HashMap<String, Asteroid> asteroids = new HashMap<>();

    public AsteroidManager(RajawaliScene currentScene) {
        this.currentScene = currentScene;
    }

    public void addAsteroid(Vector3 position) {
        // TODO set random orientation
        Asteroid newAsteroid = new Asteroid(position);
        this.asteroids.put(newAsteroid.toString(), newAsteroid);
    }
}
