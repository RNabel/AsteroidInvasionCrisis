package asteroids3d.gamestate.objects.topLevel;

import android.graphics.Color;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.MovingObject;

/**
 * Author rn30.
 */
public class Rocket extends MovingObject {
    private double distance;
    private int radius;
    private int segmentsH = 12;
    private int segmentsW = 12;
    private Vector3 origin;

    private Object3D shape;

    // Unguided rocket.
    public Rocket(Manager manager, Vector3 location, Vector3 acceleration, Vector3 velocity) {
        super(manager, location, acceleration, velocity);
        this.setIsInfluencedByGrav(false);
        this.origin = location.clone();

        // Instantiate 3D object.
        shape = new Sphere(radius, segmentsW, segmentsH);
        Material rocketMaterial = new Material();
        rocketMaterial.enableLighting(true);
        rocketMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        rocketMaterial.setColor(0);
        shape.setMaterial(rocketMaterial);
        shape.setColor(Color.RED);

        getManager().getCurrentScene().addChild(shape);
    }

    @Override
    public Object3D getShape() {
        Vector3 loc = this.getLocation();
        return this.shape;
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public void handleCollision(Object collidingObject) {
    }

    public double getDistance() {
        return distance;
    }

    public Vector3 getOrigin() {
        return origin;
    }


    @Override
    public Vector3 calculateCurrentVelocity(Vector3 currentVelocity, double time) {
        return currentVelocity;
    }

    @Override
    public Vector3 calculateCurrentAcceleration(Vector3 currentAcceleration, double time) {
        return currentAcceleration;
    }
}
