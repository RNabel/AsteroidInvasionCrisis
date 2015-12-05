package asteroids3d.gamestate.objects.Asteroids;

import android.graphics.Color;

import net.sf.javaml.core.kdtree.KDTree;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.util.RajLog;

import asteroids3d.R;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.MovingObject;
import asteroids3d.util.IntervalRandom;

public class Asteroid extends MovingObject {
    public static Material asteroidMaterial;

    static {
        asteroidMaterial = new Material();
        asteroidMaterial.enableLighting(true);
        asteroidMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        asteroidMaterial.setColor(0);

        Texture earthTexture = new Texture("Asteroid", R.drawable.asteroid);
        try {
            asteroidMaterial.addTexture(earthTexture);

        } catch (ATexture.TextureException error) {
            RajLog.i("DEBUG TEXTURE ERROR");
        }

    }


    private final int SEGMENTS_W = 12;
    private final int SEGMENTS_H = 12;

    private int rockHeight = 2;
    private boolean splits = false;
    private int altitude;
    private Sphere shape;
    private int radius;

    public Asteroid(Manager manager,
                    Vector3 location,
                    Vector3 acceleration,
                    Vector3 velocity,
                    int radius) {
        super(manager, location, acceleration, velocity);
        this.radius = radius;
        this.shape = new Sphere(radius, SEGMENTS_W, SEGMENTS_H);
        shape.setMaterial(asteroidMaterial);
        shape.setColor(Color.YELLOW);
        shape.setPosition(location);

        // Add the asteroid to the
        getManager().getCurrentScene().addChild(shape);

        // Add to location map.
        updateLocationTree(null, location);
    }

    // To be called after location is updated.
    @Override
    public Object3D getShape() {
        return shape;
    }

    @Override
    public boolean contains(double x, double y) {
        double xComp = Math.pow(x - this.getLocation().x, 2.0);
        double yComp = Math.pow(y - this.getLocation().y, 2.0);
        double radiusSquared = Math.pow(rockHeight / 2.0, 2.0);
        return xComp + yComp <= radiusSquared;
    }

    @Override
    public void handleCollision(Object collidingObject) {
    }

    @Override
    public boolean updatePosition() {
        Vector3 oldLocation = getLocation().clone();
        if (splits && getLocation().y > altitude) {
            // Split the rock.
            // TODO
        }
        boolean onScreen = super.updatePosition();
        if (onScreen) {
            // Update map.
            updateLocationTree(oldLocation, getLocation().clone());
        } else {
            // Remove from map.
            deleteFromLocationTree(oldLocation);
        }
        return onScreen;
    }

    @Override
    public Vector3 calculateCurrentVelocity(Vector3 currentVelocity, double time) {
        return currentVelocity;
    }

    @Override
    public Vector3 calculateCurrentAcceleration(Vector3 currentAcceleration, double time) {
        return currentAcceleration;
    }

    private void updateLocationTree(Vector3 old, Vector3 newV) {
        try {
            deleteFromLocationTree(old);
        } catch (Exception e) {
            // If key not already inserted.
        }
        insertInLocationTree(newV);
    }

    private void insertInLocationTree(Vector3 location) {
        double[] newArr = vectorToArray(location);
        KDTree tree = ((AsteroidManager) getManager()).getAsteroidLocationMap();
        tree.insert(newArr, this);
    }

    private void deleteFromLocationTree(Vector3 location) {
        double[] loc = vectorToArray(location);
        KDTree tree = ((AsteroidManager) getManager()).getAsteroidLocationMap();
        tree.delete(loc);
    }


    public void setSplits(boolean splits) {
        // Get the altitude at which to split.
        IntervalRandom rand = new IntervalRandom();
        Vector3 max = getManager().getBoundingBox().getMax();
        Vector3 min = getManager().getBoundingBox().getMin();
        altitude = rand.nextInt(min.y, max.y);

        this.splits = splits;
    }
}
