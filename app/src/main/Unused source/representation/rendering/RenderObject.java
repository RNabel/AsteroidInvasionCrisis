package asteroids3d.representation.rendering;

import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.Asteroids3DRenderer;
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

    private Asteroids3DRenderer renderer = Asteroids3DRenderer.getCurrentRenderer();
    private RajawaliScene scene = Asteroids3DRenderer.getCurrentRenderer().getCurrentScene();

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

    public Asteroids3DRenderer getRenderer() {
        return renderer;
    }

    public RajawaliScene getScene() {
        return scene;
    }
}
