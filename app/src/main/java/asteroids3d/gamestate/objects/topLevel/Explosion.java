package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.StationaryObject;

public class Explosion extends StationaryObject {
    private int radius = 15;
    private static final int limit = 1;

    private Object3D explosion;

    public Explosion(Manager manager, Vector3 location) {
        super(manager);
        this.setLocation(location);

        // Create the shape. TODO finish add material.
        explosion = new Sphere(radius, 12, 12); // Make non magical.

        // Add explosion to scene.
        manager.getCurrentScene().addChild(this.getShape());
    }

    public boolean updateSize() {
        radius -= 2;
        return radius < limit;
    }

    @Override
    public Object3D getShape() {
        return ((ExplosionManager)this.getManager()).getExplosionShape();
    }

    @Override
    public boolean contains(double x, double y) {
        double xPart = Math.pow(x - getLocation().x, 2);
        double yPart = Math.pow(y - getLocation().y, 2);
        double radPart = Math.pow(radius / 2.0, 2);

        return (xPart + yPart) <= radPart;
    }

    @Override
    public void handleCollision(Object collidingObject) {  }

    public int getRadius() {
        return radius;
    }
}
