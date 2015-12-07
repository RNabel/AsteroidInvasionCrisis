package asteroids3d.gamestate;

import org.rajawali3d.scene.RajawaliScene;
import org.rajawali3d.util.RajLog;

import asteroids3d.Asteroids3DRenderer;
import asteroids3d.gamestate.objects.Asteroids.AsteroidManager;
import asteroids3d.gamestate.objects.Level;
import asteroids3d.gamestate.objects.ProgramState;
import asteroids3d.gamestate.objects.topLevel.TopLevelManager;

// The main game state of the game, is used by the rendering engine,
public class GameState {
    private static GameState state = null;
    private final Asteroids3DRenderer renderer;
    private final RajawaliScene currentScene;

    private TopLevelManager topLevelManager;
    private AsteroidManager asteroidManager;
    private Points points;

    private ProgramState currentProgramState;
    private Level currentLevel;
    private long currentTotalTime = 0l;

    public String displayString;

    public GameState(RajawaliScene currentState, Asteroids3DRenderer rajawaliVRExampleRenderer) { // TODO fix this.
        GameState.state = this;
        this.renderer = rajawaliVRExampleRenderer;
        this.currentScene = currentState;
        currentProgramState = ProgramState.MENU;
        displayString = "TEST STRING HELLO!";
    }

    /**
     * Method called before rendering every frame.
     * Only rendering-related code is placed in this method, all set-up related code is
     * placed in setStateType.
     *
     */
    public void updateGameState(long totalTime) {
        this.currentTotalTime = totalTime;

        // Transition between game states. TODO at later point.
        switch (currentProgramState) {
            case IN_LEVEL:
                displayString = "Rockets: " + topLevelManager.getRocketsAvailable() + "\n\n\n\n\n\n\n\nAsteroids: " + asteroidManager.getAsteroids().size() + "\tPoints: " + points.getTotalPoints();
                asteroidManager.update(totalTime);
                topLevelManager.update();
                break;

            case MENU: // Check whether any option was selected.
                // TODO Render option menu.
                String MENU_STRING = "Welcome to Asteroid Invasion Crisis! \n\n\n\n\n\n\nPress A to continue";
                displayString = MENU_STRING;
                if (renderer.nextState) {
                    setStateType(ProgramState.IN_LEVEL);
                    renderer.nextState = false;
                }
                break;

            case AFTER_LEVEL: // Check whether fire button clicked, if so transition to IN_LEVEL
                // Wait for fire-button to be clicked.
                displayString = "Level " + (currentLevel.getLevel() + 1) + " is finished!\n\n\n\n\n\n\nReady for the next level?\nPress A.";
                topLevelManager.update();
                if (renderer.nextState) {
                    // Create Level.
                    setStateType(ProgramState.IN_LEVEL);
                    renderer.nextState = false;
                }
                // TODO Render points.
                break;

            case GAME_OVER:
                topLevelManager.update();
                // Wait for fire button to be clicked, to return to main menu.
                if (renderer.nextState) {
                    setStateType(ProgramState.MENU);
                    renderer.nextState = false;
                }
                break;
        }
    }

    private void setupFirstLevel() {
        points = new Points(this);
        currentLevel = new Level(0, currentTotalTime); // TODO test.

        // Instantiate all managers.
        if (asteroidManager != null) {
            asteroidManager.tearDown();
        }
        if (topLevelManager != null) {
            topLevelManager.tearDown();
        }

        asteroidManager = new AsteroidManager(currentScene);
        topLevelManager = new TopLevelManager(currentScene); // TODO add raj scene here.
    }

    // Getters and Setters.
    public AsteroidManager getAsteroidManager() {
        return asteroidManager;
    }

    public TopLevelManager getTopLevelManager() {
        return topLevelManager;
    }

    public static GameState getState() {
        return GameState.state;
    }

    public void setStateType(ProgramState newType) {
        RajLog.i("Game state changed: " + newType.toString());
        ProgramState previousState = currentProgramState;
        this.currentProgramState = newType;

        // Update points at game over and display.
        switch (newType) {
            case GAME_OVER: // Update points and display, reset level.
                String tmpString;
                if (points.getTotalPoints() < 0) {
                    tmpString = "You missed too many asteroids!";
                } else {
                    tmpString = "An asteroid hit you!";
                }
                displayString = "GAME OVER - " + tmpString + "\n\nTotal points: " + points.getTotalPoints() + "\n\n\n\n\nPress A to continue.";
                break;
            case AFTER_LEVEL: // Display points and start next level.
                break;
            case MENU: // No rendering show panes offering to start. (with spinner?)
                break;
            case IN_LEVEL: // Normal rendering. If from menu, create first level.
                if (previousState == ProgramState.MENU) { // Set-up first level.
                    setupFirstLevel();
                } else {
                    currentLevel = new Level(currentLevel.getLevel() + 1, currentTotalTime);
                }
                break;
        }

    }

    public Points getPoints() {
        return points;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
