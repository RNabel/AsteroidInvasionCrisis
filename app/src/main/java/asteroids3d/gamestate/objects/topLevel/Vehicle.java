package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.WorldParameters;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

import asteroids3d.Asteroids3DRenderer;
import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.gamestate.objects.StationaryObject;
import edu.wlu.cs.levy.CG.KDTree;

public class Vehicle extends StationaryObject {
    private final double VEHICLE_SIZE = 10;
    double distanceToNextAsteroid = -1;

    public Vehicle(Manager manager) {
        super(manager);
        setLocation(getManager().getRenderer().getCameraPosition());
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public void handleCollision(Object collidingObject) {
    }

    public void updateState() {
        // TODO Read input from bluetooth controller.
        Asteroids3DRenderer renderer = this.getManager().getRenderer();
        if (renderer.moveForward) {
            Camera currentCam = renderer.getCurrentCamera();
            String output = "Old position:" + currentCam.getPosition().toString() + "\t";
            final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
            movement.rotateBy(currentCam.getOrientation()).multiply(3);
            movement.inverse();
            movement.y = 0;
            Vector3 newPos = renderer.getCameraPosition().clone().add(movement);
            setLocation(newPos);

            // Reset the flag
            renderer.moveForward = false;
        }
        hasAndsteroidHit(); // Handle asteroid collision

        // Check for rocket fired.
        if (getManager().getRenderer().fireRocket) {
            fireRocket();
            getManager().getRenderer().fireRocket = false;
        }
    }

    // Check whether an asteroid hit.
    public boolean hasAndsteroidHit() {
        Vector3 location = getLocation();
        double[] loc = vectorToArray(location);
        KDTree tree = getManager().getGameState().getAsteroidManager().getAsteroidLocationMap();

        if (tree.size() > 0) {
            Object result = null;
            try {
                result = tree.nearest(loc);
            } catch (Exception e) {
                return false;
            }
            if (result != null && result instanceof Asteroid) {
                Asteroid asteroidResult = (Asteroid) result;
                // Check distance.
                double dist = Vector3.distanceTo(asteroidResult.getLocation(), getLocation());

                if (dist < 10) {
                    // TODO hook to switch to end level. TEST.
                    getManager().getGameState().setStateType(ProgramState.GAME_OVER);
                    System.out.println("Asteroid hit vehicle, you die.");
                }
            }
        }
        // TODO Could add distance display.

        return true;
    }

    private void fireRocket() {
        Vector3 cameraPosition = getManager().getRenderer().getCameraPosition().clone();
        Quaternion cameraOrientation = getManager().getRenderer().getCurrentCamera().getOrientation().clone();
        cameraPosition.y = cameraPosition.y / 2;
        getManager().getGameState().getTopLevelManager().getrManager().
                rocketLaunched(cameraOrientation, cameraPosition);
    }

    @Override
    public void setLocation(Vector3 location) {
        super.setLocation(location);
        Asteroids3DRenderer renderer = this.getManager().getRenderer();

        // Set camera position.
        renderer.setCameraPosition(location);
    }
}
