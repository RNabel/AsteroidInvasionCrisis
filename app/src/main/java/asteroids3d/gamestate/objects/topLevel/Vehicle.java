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

    public Vehicle(Manager manager) {
        super(manager);
        setLocation(getManager().getRenderer().getCameraPosition());
    }

    public void updateState() {
        Asteroids3DRenderer renderer = this.getManager().getRenderer();
        if (renderer.moveForward || renderer.moveBack || renderer.moveLeft || renderer.moveRight) {
            Camera currentCam = renderer.getCurrentCamera();
            final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
            movement.rotateBy(currentCam.getOrientation()).multiply(3);
            movement.inverse();

            if (renderer.moveRight) {
                movement.rotateBy(new Quaternion().fromEuler(90, 0, 0));
            } else if(renderer.moveLeft) {
                movement.rotateBy(new Quaternion().fromEuler(-90, 0, 0));
            } else if (renderer.moveBack) {
                movement.rotateBy(new Quaternion().fromEuler(180, 0, 0));
            }
            movement.y = 0;
            Vector3 newPos = renderer.getCameraPosition().clone().add(movement);

            // Check whether newPos is within the game bounds.
            Vector3 max = getManager().getBoundingBox().getMax();
            Vector3 min = getManager().getBoundingBox().getMin();

            if (max.z > newPos.z &&
                    min.z < newPos.z &&
                    max.x > newPos.x &&
                    min.x < newPos.x)
                setLocation(newPos);

            // Reset the flag
            renderer.moveForward = false;
            renderer.moveLeft = false;
            renderer.moveRight = false;
            renderer.moveBack = false;
        }
        handleAsteroidHit(); // Handle asteroid collision

        // Check for rocket fired.
        if (getManager().getRenderer().fireRocket) {
            fireRocket();
            getManager().getRenderer().fireRocket = false;
        }
    }

    // Check whether an asteroid hit.
    private void handleAsteroidHit() {
        Vector3 location = getLocation();
        double[] loc = vectorToArray(location);
        KDTree tree = getManager().getGameState().getAsteroidManager().getAsteroidLocationMap();

        if (tree.size() > 0) {
            Object result;
            try {
                result = tree.nearest(loc);
            } catch (Exception e) {
                return;
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

    }

    private void fireRocket() {
        Vector3 cameraPosition = getManager().getRenderer().getCameraPosition().clone();
        cameraPosition.y = cameraPosition.y / 2;
        getManager().getGameState().getTopLevelManager().getRManager().
                rocketLaunched(cameraPosition);
    }

    @Override
    protected void setLocation(Vector3 location) {
        super.setLocation(location);
        Asteroids3DRenderer renderer = this.getManager().getRenderer();

        // Set camera position.
        renderer.setCameraPosition(location);
    }
}
