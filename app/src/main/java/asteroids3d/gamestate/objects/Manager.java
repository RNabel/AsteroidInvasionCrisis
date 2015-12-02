package asteroids3d.gamestate.objects;

import org.rajawali3d.bounds.BoundingBox;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.scene.RajawaliScene;

import java.util.Random;

import asteroids3d.RajawaliVRExampleRenderer;
import asteroids3d.gamestate.GameState;

/**
 * Author rn30.
 */
public abstract class Manager {
    //    private PApplet applet = GameState.getApplet();
//    public PApplet getApplet() {
//        return applet;
//    }
    private RajawaliScene currentScene;

    public GameState getGameState() { return GameState.getState();  }

    public BoundingBox getBoundingBox() {
        return RajawaliVRExampleRenderer.getBoundingBox();
    }

    public abstract void update(double deltaTime, long totalTime);

    public Manager(RajawaliScene currentScene) {
        this.currentScene = currentScene;
    }

    public RajawaliScene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(RajawaliScene currentScene) {
        this.currentScene = currentScene;
    }

    public RajawaliVRExampleRenderer getRenderer() {
        return RajawaliVRExampleRenderer.getCurrentRenderer();
    }
}
