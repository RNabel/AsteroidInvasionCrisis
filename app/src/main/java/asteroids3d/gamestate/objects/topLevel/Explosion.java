package asteroids3d.gamestate.objects.topLevel;

import android.graphics.Color;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.util.RajLog;

import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.StationaryObject;

public class Explosion extends StationaryObject {
    private int radius = 100;
    private static final int limit = 1;
    private double scale = 1;

    private Object3D explosion;

    public Explosion(Manager manager, Vector3 location) {
        super(manager);
        this.setLocation(location);

        // Create the shape. TODO finish add material.
        explosion = new Sphere(radius, 12, 12); // Make non magical.
        Material material = Asteroid.asteroidMaterial;;
//        material.enableLighting(true);
//        material.setColorInfluence(1f);
//        material.setDiffuseMethod(new DiffuseMethod.Lambert());
//        material.setColor(Color.MAGENTA);
        explosion.setMaterial(material);
        explosion.setColor(Color.MAGENTA);
        explosion.setPosition(location);
        // Add explosion to scene.
        getManager().getCurrentScene().addChild(explosion);
        RajLog.i("Created new explosion at:" + getLocation());
    }

    public boolean updateSize() {
        getShape().setScale(scale);
        scale -= 0.1;
        return scale <= 0;
    }

    @Override
    public Object3D getShape() {
        return explosion;
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
