package asteroids3d.gamestate.objects.background;

import org.rajawali3d.primitives.Cube;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.gamestate.objects.Manager;

/**
 * Author rn30.
 */
public class BackgroundManager extends Manager {

    // Bounding box
    Cube boundingCube;

    public BackgroundManager(RajawaliScene scene) {
        super(scene);
        // Create bounding box of game.
        boundingCube = new Cube(1000);
        boundingCube.setPosition(-500, -500, 0);

        // TODO add material to the cube -- needs to be created first.
    }

    @Override
    public void update(double currentFrame, long totalTime) {
        // Any code relating to updates of the background.
    }
}
