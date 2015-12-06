package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.WorldParameters;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.math.vector.Vector3;

import java.util.List;

import asteroids3d.RajawaliVRExampleRenderer;
import asteroids3d.gamestate.objects.Asteroids.Asteroid;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.gamestate.objects.StationaryObject;
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeySizeException;

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
        RajawaliVRExampleRenderer renderer = this.getManager().getRenderer();
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
    }

    // Check whether an asteroid hit.
    public boolean hasAndsteroidHit() {
        Vector3 location = getLocation();
        double[] loc = vectorToArray(location);
        KDTree tree = getManager().getGameState().getAsteroidManager().getAsteroidLocationMap();
        try {

            if (tree.size() > 0) {
                Object result = tree.nearest(loc);

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
        } catch (KeySizeException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void setLocation(Vector3 location) {
        super.setLocation(location);
        RajawaliVRExampleRenderer renderer = this.getManager().getRenderer();

        // Set camera position.
        renderer.setCameraPosition(location);
    }
}
