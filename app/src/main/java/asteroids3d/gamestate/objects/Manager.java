package asteroids3d.gamestate.objects;

import org.rajawali3d.bounds.BoundingBox;
import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.Asteroids3DRenderer;
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
        return Asteroids3DRenderer.getBoundingBox();
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

    public Asteroids3DRenderer getRenderer() {
        return Asteroids3DRenderer.getCurrentRenderer();
    }

    public abstract void tearDown();
}
