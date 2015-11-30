package asteroids3d.representation.rendering.after_level;

import asteroids3d.gamestate.GameState;
import asteroids3d.gamestate.Points;
import asteroids3d.representation.rendering.RenderObject;

import java.util.Map;

/**
 * Author rn30.
 */
public class AfterLevelRenderer extends RenderObject {
    int[] textColour = new int[] {255, 255, 255};

    public AfterLevelRenderer(GameState state) {
        super(state);
    }

    @Override
    public double render() {
        int totalPoints = getState().getTotalPoints();

        // Print total points.
        int level = getState().getCurrentLevel().getLevel();
        String message = "LEVEL " + level + ": DONE!\n";
        message += "TOTAL POINTS: " + totalPoints;

        // TODO create bitmap and show points.
//        .text(message, width / 2, height / 3, textColour[0], textColour[1], textColour[2]);

        renderBreakdown();

        renderContinueMessage();

        return 0;
    }

    private void renderContinueMessage() {
        int[] textColour = getNextColour();
        String message = "CLICK TO START THE NEXT LEVEL";
//        ePoint.text(message, width / 2, 5 * height / 6, textColour[0], textColour[1], textColour[2]);
    }

    private void renderBreakdown() {
        Map<Points.pointTypes, Integer> mapping = getState().getPoints().getPointMappings();
        String printString = "";

        // Add rocks
//        printString += "POINTS FROM ROCKS: " + mapping.get(Points.pointTypes.ROCK) + "\n";
//
//        // Add cities.
//        printString += "POINTS FROM CITIES: " + mapping.get(Points.pointTypes.CITY) + "\n";
//
//        // Add silos.
//        printString += "POINTS FROM ROCKET SILOS: " + mapping.get(Points.pointTypes.ROCKET_SILO) + "\n";
//
//        // Add missiles.
//        printString += "POINTS FROM ROCKETS: " + mapping.get(Points.pointTypes.ROCKET) + "\n";

//        ePoint.text(printString, width / 2, 2 * height / 3, textColour[0], textColour[1], textColour[2]);
    }
}
