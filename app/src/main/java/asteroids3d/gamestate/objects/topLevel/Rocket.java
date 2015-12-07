package asteroids3d.gamestate.objects.topLevel;

import android.graphics.Color;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.MovingObject;

/**
 * Author rn30.
 */
public class Rocket extends MovingObject {

    private final Object3D shape;

    // Unguided rocket.
    public Rocket(Manager manager, Vector3 location, Vector3 acceleration, Vector3 velocity) {
        super(manager, location, acceleration, velocity);
        this.setIsInfluencedByGravity(false);

        // Instantiate 3D object.
        int segmentsW = 12;
        int segmentsH = 12;
        int radius = 1;
        shape = new Sphere(radius, segmentsW, segmentsH);
        Material rocketMaterial = Asteroid.asteroidMaterial;
        shape.setMaterial(rocketMaterial);
        shape.setColor(Color.RED);
        shape.setPosition(location);
        shape.setDoubleSided(true);
        getManager().getCurrentScene().addChild(shape);
    }

    public Object3D getShape() {
        return this.shape;
    }

    @Override
    public Vector3 calculateCurrentVelocity(Vector3 currentVelocity) {
        return currentVelocity;
    }

}
