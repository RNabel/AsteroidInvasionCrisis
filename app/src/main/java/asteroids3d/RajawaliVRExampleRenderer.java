package asteroids3d;

//import android.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.rajawali3d.WorldParameters;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture.TextureException;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.SphereMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.Terrain;
import org.rajawali3d.terrain.TerrainGenerator;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.renderer.RajawaliVRRenderer;
import org.w3c.dom.Text;

public class RajawaliVRExampleRenderer extends RajawaliVRRenderer {
    private SquareTerrain mTerrain;
    private Sphere mLookatSphere;

    public int isTriggered = 0;

    private Quaternion orientation;
    private boolean move;

    private Vector3 cameraPosition = new Vector3(0, 0, 1);

    public RajawaliVRExampleRenderer(Context context) {
        super(context);
    }

    @Override
    public void initScene() {
        DirectionalLight light = new DirectionalLight(0.2f, -1f, 0f);
        light.setPower(.7f);
        getCurrentScene().addLight(light);

        light = new DirectionalLight(0.2f, 1f, 0f);
        light.setPower(1f);
        getCurrentScene().addLight(light);

        getCurrentCamera().setFarPlane(1000);

        getCurrentScene().setBackgroundColor(0xdddddd);

        createTerrain();



        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Asteroid", R.drawable.asteroid);
        try{
            material.addTexture(earthTexture);

        } catch (TextureException error){
            RajLog.i("DEBUG TEXTURE ERROR");
        }

        mLookatSphere = new Sphere(1, 12, 12);
        Material sphereMaterial = new Material();
        sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        sphereMaterial.enableLighting(true);
//        sphereMaterial.setColorInfluence(0);
//        Texture asteroidTexture = new Texture("Sphere texture", R.drawable.earthtruecolor_nasa_big);
//
//        try {
//            sphereMaterial.addTexture(asteroidTexture);
//            RajLog.i("Successfully set texture.");
//        } catch (TextureException e) {
//            e.printStackTrace();
//            RajLog.i("Terrible error occurred when setting texture");
//        }
        mLookatSphere.setMaterial(material);
        mLookatSphere.setColor(Color.YELLOW);
        mLookatSphere.setPosition(0, 0, 6);
        getCurrentScene().addChild(mLookatSphere);

        super.initScene();
    }

    public void createTerrain() {
        //
        // -- Load a bitmap that represents the terrain. Its color values will
        //    be used to generate heights.
        //

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.grid);

        try {
//            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
            getCurrentScene().setSkybox(R.drawable.right, R.drawable.left, R.drawable.top, R.drawable.bottom, R.drawable.front, R.drawable.back);
            SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
            // -- set terrain scale
            terrainParams.setScale(4f, 54f, 4f);
            // -- the number of plane subdivisions
            terrainParams.setDivisions(128);
            // -- the number of times the textures should be repeated
            terrainParams.setTextureMult(4);
//            terrainParams.setTextureMult(1);
            //
            // -- Terrain colors can be set by manually specifying base, middle and
            //    top colors.
            //
            terrainParams.setBasecolor(Color.argb(0, 3, 54, 62));
            terrainParams.setMiddleColor(Color.argb(0, 18, 158, 180));
            terrainParams.setUpColor(Color.argb(0, 0, 0, 0));
            //
            // -- However, for this example we'll use a bitmap
            //
            terrainParams.setColorMapBitmap(bmp);
            //
            // -- create the terrain
            //
            mTerrain = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // -- The bitmap won't be used anymore, so get rid of it.
        //
        bmp.recycle();

        //
        // -- A normal map material will give the terrain a bit more detail.
        //
        Material material = new Material();
        material.enableLighting(true);
        material.useVertexColors(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        try {
            Texture groundTexture = new Texture("ground", R.drawable.grid);
            groundTexture.setInfluence(.5f);
            material.addTexture(groundTexture);
            material.addTexture(new NormalMapTexture("groundNormalMap", R.drawable.groundnor));
            material.setColorInfluence(0);
        } catch (TextureException e) {
            e.printStackTrace();
        }

        //
        // -- Blend the texture with the vertex colors
        //
        material.setColorInfluence(.5f);
        mTerrain.setY(-100);
        mTerrain.setMaterial(material);

        getCurrentScene().addChild(mTerrain);
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        handleCameraMovement(10);
//        getCurrentCamera().setPosition(cameraPosition);
        // Update position of sphere
        super.onRender(elapsedTime, deltaTime);
        boolean isLookingAt = isLookingAtObject(mLookatSphere);
        if (isLookingAt) {
            if (isTriggered < 100) {
                mLookatSphere.setColor(Color.GREEN);
            } else {
                mLookatSphere.setColor(Color.RED);
            }
        } else {
            mLookatSphere.setColor(Color.YELLOW);
        }

        isTriggered = (isTriggered + 1) % Integer.MAX_VALUE;
    }

    public void incrementCameraPosition() {
        // Extract orientation looked at.
        move = true;
    }

    public void handleCameraMovement(double units) {
        if (move) {
            String output = "Old position:" + getCurrentCamera().getPosition().toString() + "\t";
            final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
            movement.rotateBy(getCurrentCamera().getOrientation()).multiply(units);
            movement.inverse();
            cameraPosition = cameraPosition.add(movement);
            // set globe position
            Vector3 spherePos = cameraPosition.clone();
            spherePos.add(movement);
            mLookatSphere.setPosition(spherePos);
//            getCurrentCamera().setPosition(getCurrentCamera().getPosition().add(movement));
            output += "New position: " + movement;
            RajLog.i(output);
            move = false;
        }
    }
}
