package asteroids3d.gamestate.objects;

import org.rajawali3d.math.vector.Vector3;

// Class which sets a superclass for any rendered object in the game state.
public abstract class StationaryObject {
    private Vector3 location;

    private final Manager manager;

    protected StationaryObject(Manager manager) {
        this.manager = manager;
    }

    public Vector3 getLocation() {
        return location;
    }

    protected void setLocation(Vector3 location) {
        this.location = location;
    }

    protected Manager getManager() {
        return manager;
    }

    protected static double[] vectorToArray(Vector3 inputVector) {
        return new double[]{inputVector.x, inputVector.y, inputVector.z};
    }
}
