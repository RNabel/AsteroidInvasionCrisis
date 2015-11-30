package asteroids3d.representation.rendering.game_over;

import asteroids3d.gamestate.GameState;
import asteroids3d.representation.rendering.RenderObject;

/**
 * Author rn30.
 */
public class GameOverRenderer extends RenderObject {

    private int[] colour = new int[] {255, 255, 255};

    private int currentFrame = 0;

    public GameOverRenderer(GameState state) {
        super(state);
    }

    @Override
    public double render() {
        // See After Level renderer to create bitmap.
//        EntryPoint ePoint = (EntryPoint)getApplet();
//        currentFrame++;
//        int height = ePoint.getHeight();
//        int width = ePoint.getWidth();
//
//        // Set to zero 10 times a second.
//        colour = getNextColour();
//
//        // "you lose"
//        ePoint.text("YOU LOST!", width / 2, height / 2, 255, 255, 255);
//
//        // "return to main menu"
//        ePoint.text("CLICK TO RETURN TO MAIN SCREEN", width / 2, 2 * height / 3, colour[0], colour[1], colour[2]);
//
        return 0d;
    }
}
