package asteroids3d.gamestate.objects;

import org.rajawali3d.bounds.BoundingBox;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.Asteroids3DRenderer;
import asteroids3d.gamestate.GameState;

/**
 * Author rn30.
 */
public abstract class Manager {
    private final RajawaliScene currentScene;

    public GameState getGameState() { return GameState.getState();  }

    public BoundingBox getBoundingBox() {
        return Asteroids3DRenderer.getBoundingBox();
    }

    protected Manager(RajawaliScene currentScene) {
        this.currentScene = currentScene;
    }

    public RajawaliScene getCurrentScene() {
        return currentScene;
    }

    public Asteroids3DRenderer getRenderer() {
        return Asteroids3DRenderer.getCurrentRenderer();
    }

}
