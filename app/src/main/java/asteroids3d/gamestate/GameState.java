package asteroids3d.gamestate;

import org.rajawali3d.scene.RajawaliScene;

import asteroids3d.RajawaliVRExampleRenderer;
import asteroids3d.gamestate.objects.Asteroids.AsteroidManager;
import asteroids3d.gamestate.objects.Level;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.gamestate.objects.background.BackgroundManager;
import asteroids3d.gamestate.objects.topLevel.TopLevelManager;

/**
 * Author rn30.
 */
// The main game state of the game, is used by the rendering engine,
public class GameState {
    private static GameState state = null;
    private RajawaliScene currentScene;

    private BackgroundManager bgManager;
    private TopLevelManager topLevelManager;
    private AsteroidManager asteroidManager;
    private Points points;

    private Level currentLevel;
    private Long currentFrame = 0l;

    public GameState(RajawaliScene currentState) { // TODO fix this.
        GameState.state = this;
        this.currentScene = currentState;
    }

    // Method called before rendering every frame.
    public void updateGameState(double time, boolean mouseClicked) {
        // Transition between game states. TODO at later point.
//        if (mouseClicked) {
//            EntryPoint entryPoint = (EntryPoint) getApplet();
//            switch (entryPoint.getGameStateType()) {
//                case IN_LEVEL:
//                    topLevelManager.mouseClicked();
//                    break;
//
//                case MAIN_MENU:
//                    setupFirstLevel();
//                    entryPoint.setStateType(EntryPoint.stateType.IN_LEVEL);
//                    return;
//
//                case AFTER_LEVEL:
//                    entryPoint.setStateType(EntryPoint.stateType.IN_LEVEL);
//                    return;
//
//                case GAME_OVER:
//                    // Transition to main menu.
//                    entryPoint.setStateType(EntryPoint.stateType.MAIN_MENU);
//                    return;
//            }
//        }
//
//        if (((EntryPoint) applet).getGameStateType() == EntryPoint.stateType.IN_LEVEL) {
//            // Update each part of the state.
//            topLevelManager.update(currentFrame);
//            asteroidManager.update(currentFrame);
//            bgManager.update(currentFrame);
//            currentFrame++;
//        }

    }

    private void setupFirstLevel() {
        currentFrame = 0l;
        points = new Points();
        currentLevel = new Level(1);

        // Instantiate all managers.
        bgManager = new BackgroundManager(currentScene);
        asteroidManager = new AsteroidManager(currentLevel.getStartTimes(), currentScene);
        topLevelManager = new TopLevelManager(Level.rocketsToStart, currentScene); // TODO add raj scene here.
    }

    // Getters and Setters.
    public BackgroundManager getBgManager() {
        return bgManager;
    }

    public AsteroidManager getAsteroidManager() {
        return asteroidManager;
    }

    public TopLevelManager getTopLevelManager() {
        return topLevelManager;
    }

    public static GameState getState() {
        return GameState.state;
    }

    // Control access to the point system.
    public void manipulatePoints(Points.pointTypes type, int number) {
        points.increasePoints(type, number, currentLevel.getLevel());
    }

    public void setStateType(ProgramState newType) {
        // Update points.
        if (newType == ProgramState.GAME_OVER) {
            this.points.endOfLevelPointUpdate(this, currentLevel.getLevel());
        }
    }

    public void notifyStateChanged(ProgramState newType) {
        // Only continue if next state is IN_LEVEL or AFTER_LEVEL.
        if (!(newType == ProgramState.IN_LEVEL ||
              newType == ProgramState.AFTER_LEVEL)) return;

//        EntryPoint.stateType current = ((EntryPoint) getApplet()).getGameStateType();
//
//        // Prepare level n + 1.
//        if (current == EntryPoint.stateType.AFTER_LEVEL) {
//            // Set up Level object;
//            currentLevel = new Level(currentLevel.getLevel() + 1);
//
//            // Give asteroidManager the start times for the stones.
//            asteroidManager = new AsteroidManager(currentLevel.getStartTimes());
//
//            // clear remaining explosions.
//            topLevelManager.clearExplosions();
//
//            // Update number of rockets available.
//            getState().getTopLevelManager().setRocketsAvailable(currentLevel.calculateRocketNumber());
//
//            // Reset time.
//            this.currentFrame = 0l;
//
//        } else if (current == EntryPoint.stateType.IN_LEVEL) {
//            // Update points.
//            this.points.endOfLevelPointUpdate(state, currentLevel.getLevel());
//        }
        System.err.println("GameState:notifyStateChanged is not implemented!");
    }

    public int getTotalPoints() {
        return this.points.getTotalPoints();
    }

    public Points getPoints() {
        return points;
    }

    public Long getCurrentFrameNumber() {
        return this.currentFrame;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
