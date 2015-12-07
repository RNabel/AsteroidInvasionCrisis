package asteroids3d.gamestate.objects.Asteroids;

import android.graphics.Color;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.util.RajLog;

import asteroids3d.R;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.MovingObject;

public class Asteroid extends MovingObject {
    public static final Material asteroidMaterial;

    static {
        asteroidMaterial = new Material();
        asteroidMaterial.enableLighting(true);
        asteroidMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        asteroidMaterial.setColor(0);

        Texture earthTexture = new Texture("Asteroid", R.drawable.asteroid_small);
        try {
            asteroidMaterial.addTexture(earthTexture);

        } catch (ATexture.TextureException error) {
            RajLog.i("DEBUG TEXTURE ERROR");
        }

    }

    private final Sphere shape;

    public Asteroid(Manager manager,
                    Vector3 location,
                    Vector3 acceleration,
                    Vector3 velocity,
                    int radius) {
        super(manager, location, acceleration, velocity);
        int SEGMENTS_H = 12;
        int SEGMENTS_W = 12;
        this.shape = new Sphere(radius, SEGMENTS_W, SEGMENTS_H);
        shape.setMaterial(asteroidMaterial);
        shape.setColor(Color.YELLOW);
        shape.setPosition(location);

        // Add the asteroid to the
        getManager().getCurrentScene().addChild(shape);
    }

    // To be called after location is updated.
    public Object3D getShape() {
        return shape;
    }


    @Override
    public Vector3 calculateCurrentVelocity(Vector3 currentVelocity) {
        return currentVelocity;
    }

}
