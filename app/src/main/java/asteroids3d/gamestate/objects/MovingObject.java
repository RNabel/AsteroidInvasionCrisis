package asteroids3d.gamestate.objects;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;

public abstract class MovingObject extends StationaryObject {

    private final Vector3 velocity; // Current velocity.
    private final Vector3 acceleration; // Current velocity.

    // Acceleration and Velocity forces from collisions or gravity, reset after each frame.
    private final Vector3 outerAcceleration = new Vector3(0, 0, 0);
    private final Vector3 outerVelocity = new Vector3(0, 0, 0);
    private final Vector3 gravity = new Vector3(0, -0.0001, 0);

    private boolean isInfluencedByGravity = true;

    protected MovingObject(Manager manager, Vector3 location, Vector3 acceleration, Vector3 velocity) {
        super(manager);
        this.setLocation(location);
        this.acceleration = acceleration;
        this.velocity = velocity;
    }

    // Returns if new element is still on screen.
    public boolean updatePosition() {
        // Update position.
        Vector3 location = this.getLocation();

        // Add current velocity in specified direction to location.
        Vector3 velocity = calculateCurrentVelocity(this.velocity);
        location.add(velocity);

        // Update position of asteroid.
        this.getShape().setPosition(location);

        // Check if on screen.
        Vector3 max = this.getManager().getBoundingBox().getMax();
        Vector3 min = this.getManager().getBoundingBox().getMin();
        return location.x >= min.x &&
                location.x <= max.x &&
                location.y >= min.y &&
                location.y <= max.y &&
                location.z >= min.z &&
                location.z <= max.z;
    }

    protected abstract Object3D getShape();

    protected abstract Vector3 calculateCurrentVelocity(Vector3 currentVelocity);

    protected void setIsInfluencedByGravity(boolean isInfluencedByGravity) {
        this.isInfluencedByGravity = isInfluencedByGravity;
    }

    @Override
    public String toString() {
        return "MovingObject{" +
                "velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", outerAcceleration=" + outerAcceleration +
                ", outerVelocity=" + outerVelocity +
                ", gravity=" + gravity +
                ", isInfluencedByGravity=" + isInfluencedByGravity +
                '}';
    }
}
