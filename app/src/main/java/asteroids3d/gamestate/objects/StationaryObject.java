package asteroids3d.gamestate.objects;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;

// Class which sets a superclass for any rendered object in the game state.
public abstract class StationaryObject {
    private Vector3 location;
    private Object3D shape;

    private Manager manager;

    public StationaryObject(Manager manager) {
        this.manager = manager;
    }

    public Object3D getShape() {
        return shape;
    }

    public void setShape(Object3D shape) {
        this.shape = shape;
    }

    public abstract boolean contains(double x, double y);

    public boolean contains(Vector3 location) {
        return contains(location.x, location.y);
    }

    public Vector3 getLocation() {
        return location;
    }

    public void setLocation(Vector3 location) {
        this.location = location;
    }

    public abstract void handleCollision(Object collidingObject);

    public Manager getManager() {
        return manager;
    }

    public static double[] vectorToArray(Vector3 inputVector) {
        return new double[]{inputVector.x, inputVector.y, inputVector.z};
    }
}
