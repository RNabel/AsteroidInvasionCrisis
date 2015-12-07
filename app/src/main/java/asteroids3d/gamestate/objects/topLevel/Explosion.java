package asteroids3d.gamestate.objects.topLevel;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.util.RajLog;

import asteroids3d.R;
import asteroids3d.gamestate.objects.Manager;
import asteroids3d.gamestate.objects.StationaryObject;

class Explosion extends StationaryObject {
    private double scale = 1;

    private final Object3D explosion;

    public Explosion(Manager manager, Vector3 location) {
        super(manager);
        this.setLocation(location);

        // Create the shape. TODO finish add material.
        int radius = 50;
        explosion = new Sphere(radius, 10, 10); // Make non magical.
        Material material = new Material();
        try {
            material.addTexture(new Texture("explosion", R.drawable.explosion));
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        explosion.setMaterial(material);
//        explosion.setColor(Color.MAGENTA);
        explosion.setPosition(location);
        // Add explosion to scene.
        getManager().getCurrentScene().addChild(explosion);
        RajLog.i("Created new explosion at:" + getLocation());
    }

    public boolean updateSize() {
        getShape().setScale(scale);
        scale -= 0.2;
        return scale <= 0;
    }

    public Object3D getShape() {
        return explosion;
    }


}
