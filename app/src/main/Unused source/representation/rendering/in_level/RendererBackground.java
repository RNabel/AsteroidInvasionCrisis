package asteroids3d.representation.rendering.in_level;

import asteroids3d.gamestate.GameState;
import asteroids3d.representation.rendering.RenderObject;

import java.util.List;

/**
 * Author rn30.
 */
// Rendering split up into different layers depending on z-index.
//      Every layer has access to the game state.
public class RendererBackground extends RenderObject {

    public RendererBackground(GameState state) {
        super(state);
    }

    @Override
    public double render() {
        return 0;
    }
}
