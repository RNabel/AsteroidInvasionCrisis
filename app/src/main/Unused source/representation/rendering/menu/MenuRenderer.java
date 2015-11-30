package asteroids3d.representation.rendering.menu;

import asteroids3d.gamestate.GameState;
import main.EntryPoint;
import asteroids3d.representation.rendering.RenderObject;

/**
 * Author rn30.
 */
public class MenuRenderer extends RenderObject {
    public MenuRenderer(GameState state) {
        super(state);
    }

    @Override
    public double render() {
        EntryPoint ePoint = (EntryPoint) getApplet();
        int width = ePoint.getWidth();
        int height = ePoint.getHeight();

        // Background picture TODO

        // "WELCOME TO PARTICLE COMMAND"
        int[] welcomeColor = new int[]{200, 200, 200};
        ePoint.text("WELCOME TO PARTICLE COMMAND", width / 2, height / 3, welcomeColor[0], welcomeColor[1], welcomeColor[2]);

        // "CLICK TO CONTINUE"
        int[] clickToPlayColor = getNextColour();
        ePoint.text("CLICK TO PLAY", width / 2, 2 * height / 3, clickToPlayColor[0], clickToPlayColor[1], clickToPlayColor[2]);

        return 0;
    }
}
