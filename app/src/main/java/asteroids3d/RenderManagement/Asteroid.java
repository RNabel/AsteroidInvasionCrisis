package asteroids3d.RenderManagement;

import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

public class Asteroid {

    private Vector3 position = new Vector3(0,0,1);
    private Quaternion velocity; // TODO find out how they work.
    private Sphere shape;

    public Asteroid(Vector3 position) {
        this.position = position;
    }
}
