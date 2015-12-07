/**
 * Author rn30.
 */
package asteroids3d.representation;

import asteroids3d.Asteroids3DRenderer;
import asteroids3d.gamestate.GameState;

import asteroids3d.representation.rendering.after_level.AfterLevelRenderer;
import asteroids3d.representation.rendering.game_over.GameOverRenderer;
import asteroids3d.representation.rendering.in_level.RendererBackground;
import asteroids3d.representation.rendering.in_level.RendererTopLevel;
import asteroids3d.representation.rendering.menu.MenuRenderer;


public class RenderEngine {
    private GameState state;
    private Asteroids3DRenderer renderer;
    private MenuRenderer menuRenderer;

    private RendererBackground backdrop;
    private RendererRocks rocks;
    private RendererTopLevel topLevel;

    private AfterLevelRenderer afterLevelRenderer;

    private GameOverRenderer gameOverRenderer;

    // TODO(rnabel) Initialize all rendering levels.
    public RenderEngine(GameState state, Asteroids3DRenderer renderer) {
        super();

        // Set the game state.
        this.renderer = renderer;
        this.state = state;

        menuRenderer = new MenuRenderer(state);

        // Initialize all renderer.
        backdrop = new RendererBackground(state);
        rocks = new RendererRocks(state);
        topLevel = new RendererTopLevel(state);

        afterLevelRenderer = new AfterLevelRenderer(state);

        gameOverRenderer = new GameOverRenderer(state);

    }

    // Function which updates the view of the game state.
    public double render() {
        // Reset background.
        if (getStateType() != EntryPoint.stateType.IN_LEVEL) {
            applet.image(bgImage, 0, 0, applet.getWidth(), applet.getHeight());
        } else {
            applet.background(192, 192, 192);
        }


        switch (getStateType()) {
            case IN_LEVEL:
                // Render all levels.
                backdrop.render();
                rocks.render();
                topLevel.render();
                break;

            case MAIN_MENU:
                menuRenderer.render();
                break;

            case AFTER_LEVEL:
                afterLevelRenderer.render();
                break;

            case GAME_OVER:
                gameOverRenderer.render();
                break;
        }

        return 0d;
    }

    public void setStateType(EntryPoint.stateType newType) {
        ((EntryPoint) this.applet).setStateType(newType);
    }

    public EntryPoint.stateType getStateType() {
        return ((EntryPoint) this.applet).getGameStateType();
    }
}
