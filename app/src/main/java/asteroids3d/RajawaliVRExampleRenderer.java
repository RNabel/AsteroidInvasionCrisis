package asteroids3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.rajawali3d.Object3D;
import org.rajawali3d.WorldParameters;
import org.rajawali3d.bounds.BoundingBox;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture.TextureException;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.TerrainGenerator;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.renderer.RajawaliVRRenderer;

import asteroids3d.gamestate.GameState;
import asteroids3d.gamestate.objects.ProgramState;

public class RajawaliVRExampleRenderer extends RajawaliVRRenderer {
    private static RajawaliVRExampleRenderer currentRenderer;
    private static BoundingBox boundingBox;

    private ProgramState currentState;
    private SquareTerrain mTerrain;
    private Sphere mLookatSphere;

    private GameState state;

    public int isTriggered = 0;
    private boolean isTabbed;

    private Vector3 cameraPosition = new Vector3(0, 0, 1);

    public RajawaliVRExampleRenderer(Context context) {
        super(context);
    }

    @Override
    public void initScene() {
        // Set singleton renderer to current object to be referenced by various Managers.
        currentRenderer = this;

        // Set frame rate.
        setFrameRate(30);

        // Setup bounding box.
        boundingBox = new BoundingBox(new Cube(1000, false).getGeometry());
        boundingBox.setMin(new Vector3(-100, 0, -100));
        boundingBox.setMax(new Vector3(100, 300, 100));

        // Set up lights.
        DirectionalLight light = new DirectionalLight(0.2f, -1f, 0f);
        light.setPower(.7f);
        getCurrentScene().addLight(light);

        light = new DirectionalLight(0.2f, 1f, 0f);
        light.setPower(1f);
        getCurrentScene().addLight(light);

        // Set cropping plane.
        getCurrentCamera().setFarPlane(1000);

        // Background.
        getCurrentScene().setBackgroundColor(0x000000);

        // Create the surrounding.
        createTerrain();

        // Instantiate game state to kick off the game.
        state = new GameState(getCurrentScene());

        // Create sample asteroid
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

//        Material sphereMaterial = new Material();
//        sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
//        sphereMaterial.enableLighting(true);
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
        mLookatSphere = new Sphere(1, 12, 12);
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
        Plane mPlane = new Plane();
        Material material1 = new Material();
        material1.setColorInfluence(1);
        mPlane.setMaterial(material1);
        mPlane.setColor(0xffffff00);
//        mPlane.setDrawingMode(GLES20.GL_LINE_LOOP);
        getCurrentScene().addChild(mPlane);
        try {
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
        // Update position of camera.
        getCurrentCamera().setPosition(cameraPosition);

        // Update game state.
        state.updateGameState(deltaTime, elapsedTime, false); // TODO pass in touch.

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

    public void handleTab() {
        // Extract orientation looked at.
        isTabbed = true;
    }

    public void handleCameraMovement(double units) {
        if (isTabbed) {
            String output = "Old position:" + getCurrentCamera().getPosition().toString() + "\t";
            final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
            movement.rotateBy(getCurrentCamera().getOrientation()).multiply(units);
            movement.inverse();
            cameraPosition = cameraPosition.clone().add(movement);

            // set globe position
            Vector3 spherePos = cameraPosition.clone();
            spherePos.add(movement);
            mLookatSphere.setPosition(spherePos);
            output += "New position: " + movement;
            RajLog.i(output);
            isTabbed = false;
        }
    }

    public static RajawaliVRExampleRenderer getCurrentRenderer() {
        return currentRenderer;
    }

    public static BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
