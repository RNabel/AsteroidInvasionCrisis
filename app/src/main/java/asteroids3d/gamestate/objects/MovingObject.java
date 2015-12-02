package asteroids3d.gamestate.objects;

import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

public abstract class MovingObject extends StationaryObject {

    private static final float AIR_DRAG = 0.997f;

    private Vector3 velocity; // Current velocity.
    private Vector3 acceleration; // Current velocity.

    // Acceleration and Velocity forces from collisions or gravity, reset after each frame.
    private Vector3 outerAcceleration = new Vector3(0, 0, 0);
    private Vector3 outerVelocity = new Vector3(0, 0, 0);
    private final Vector3 gravity = new Vector3(0, -0.0001, 0);

    private boolean isInfluencedByGrav = true;

    public MovingObject(Manager manager, Vector3 location, Vector3 acceleration, Vector3 velocity) {
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
        Vector3 velocity = calculateCurrentVelocity(this.velocity, 0);
        location.add(velocity);

        // Update position of asteroid.
        this.getShape().setPosition(location);

        // Update velocity
//        Vector3 acceleration = calculateCurrentAcceleration(this.acceleration, 0);
//        this.acceleration.add(acceleration);
//        velocity.add(this.acceleration);

        // Calculate and apply drag.
//        velocity.multiply(AIR_DRAG);

        // Add and apply gravity.
//        if (isInfluencedByGrav) {
//            acceleration.add(gravity);
//        }

        // Check if on screen. TODO write basic tests.
        Vector3 max = this.getManager().getBoundingBox().getMax();
        Vector3 min = this.getManager().getBoundingBox().getMin();
        return location.x >= min.x &&
                location.x <= max.x &&
                location.y >= min.y &&
                location.y <= max.y &&
                location.z >= min.z &&
                location.z <= max.z;
    }

    public abstract Vector3 calculateCurrentVelocity(Vector3 currentVelocity, double time);
    public abstract Vector3 calculateCurrentAcceleration(Vector3 currentAcceleration, double time);

    private Vector3 calculateDrag() {
        return new Vector3(0, 0, 0); // TODO implement drag.
    }

    // Public methods to change direction and acceleration of the particle.
    public void addAcceleration(Vector3 additionalAcc) {
        this.outerAcceleration.add(additionalAcc);
    }

    public void addVelocity(Vector3 additionalVel) {
        this.outerVelocity.add(additionalVel);
    }

    // Getters and Setters.
    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public Vector3 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3 acceleration) {
        this.acceleration = acceleration;
    }

    public void setIsInfluencedByGrav(boolean isInfluencedByGrav) {
        this.isInfluencedByGrav = isInfluencedByGrav;
    }

    public void setOuterAcceleration(Vector3 outerAcceleration) {
        this.outerAcceleration = outerAcceleration;
    }

    public void setOuterVelocity(Vector3 outerVelocity) {
        this.outerVelocity = outerVelocity;
    }
}
