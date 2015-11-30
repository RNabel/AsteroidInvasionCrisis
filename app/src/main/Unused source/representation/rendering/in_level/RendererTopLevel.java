package asteroids3d.representation.rendering.in_level;

import gamestate.GameState;
import gamestate.objects.topLevel.Explosion;
import gamestate.objects.topLevel.Rocket;
import gamestate.objects.topLevel.TopLevelManager;
import processing.core.*;
import representation.rendering.RenderObject;

import java.util.List;

/**
 * Author rn30.
 */

// Renders rockets, explosions, crosshair, statistics (TODO)
public class RendererTopLevel extends RenderObject {
    private PShape mouseIcon;
    private PFont font;

    public RendererTopLevel(GameState state) {
        super(state);
        // load the mouse image
        this.mouseIcon = this.getApplet().loadShape("crosshair.svg");
        this.font = this.getApplet().createFont("dot_font.ttf", 20);
        getApplet().textFont(font);
        getApplet().textAlign(PConstants.CENTER, PConstants.CENTER);
    }

    @Override
    public double render() {
        // Render rockets.
        renderRockets();

        // Render explosions.
        renderExplosions();

        // Render Points.
        renderStats();

        // Render cross hair.
        renderCrosshair();

        return 0;
    }

    private void renderStats() {
        int width = getApplet().getWidth();

        int totPoints = this.getState().getTotalPoints();

        // Render in middle of screen.
        getApplet().text("Points: " + totPoints, 70, 50);

        // Render Level.
        getApplet().text("Level: " + getState().getCurrentLevel().getLevel(), width / 2, 10);

        // Render Rockets remaining.
        getApplet().text("Rockets remaining: " + getState().getTopLevelManager().getRocketsAvailable(),
                width - 120, 50);
    }

    private void renderRockets() {
        // loop through rockets.
        TopLevelManager manager = this.getState().getTopLevelManager();
        List<Rocket> rockets = manager.getRockets();
        PApplet applet = this.getApplet();

        for (Rocket rocket : rockets) {
            applet.shape(rocket.getShape());
        }
    }

    private void renderExplosions() {
        TopLevelManager manager = this.getState().getTopLevelManager();
        List<Explosion> explosions = manager.getExplosions();
        PApplet applet = this.getApplet();

        for (Explosion expl : explosions) {
            PShape shape = expl.getShape();
            PVector loc = expl.getLocation();
            int size = expl.getSize();
            int halfSize = size / 2;

            applet.shape(shape, loc.x - halfSize, loc.y - halfSize, size, size);
        }
    }

    private void renderCrosshair() {
        PApplet applet = this.getApplet();

        int x = applet.mouseX;
        int y = applet.mouseY;
        int height = 40;
        int width = 40;

        x -= width/2.0;
        y -= height/2.0;
        applet.shape(this.mouseIcon, x, y, width, height);
    }
}
