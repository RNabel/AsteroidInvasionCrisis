package asteroids3d.representation.rendering;

import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.RajawaliVRExampleRenderer;
import asteroids3d.gamestate.GameState;

import java.util.Random;

/**
 * Author rn30.
 */
public abstract class RenderObject {
    public RenderObject(GameState state) {
        super();
        this.state = state;
    }

    public abstract double render();

    private RajawaliVRExampleRenderer renderer = RajawaliVRExampleRenderer.getCurrentRenderer();
    private RajawaliScene scene = RajawaliVRExampleRenderer.getCurrentRenderer().getCurrentScene();

    private GameState state;
    public GameState getState() { return state; }

    public static int[] getNextColour() {
        Random random = new Random();
        int[] colour = new int[3];
        colour[0] = random.nextInt(255);
        colour[1] = random.nextInt(255);
        colour[2] = random.nextInt(255);
        return colour;
    }

    public RajawaliVRExampleRenderer getRenderer() {
        return renderer;
    }

    public RajawaliScene getScene() {
        return scene;
    }
}
