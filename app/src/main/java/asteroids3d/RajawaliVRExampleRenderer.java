package asteroids3d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.rajawali3d.WorldParameters;
import org.rajawali3d.bounds.BoundingBox;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture.TextureException;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.renderer.RajawaliVRRenderer;

import asteroids3d.gamestate.GameState;
import asteroids3d.gamestate.objects.ProgramState;

public class RajawaliVRExampleRenderer extends RajawaliVRRenderer {
    private static RajawaliVRExampleRenderer currentRenderer;
    private static BoundingBox boundingBox;

    private ProgramState currentState;
    private Plane mPlane;
    private Plane crosshairPlane;
    private Sphere mLookatSphere;

    private GameState state;

    public int isTriggered = 0;
    private boolean isTabbed;
    private boolean fireRocket;

    private Vector3 cameraPosition = new Vector3(0, 5, 0);

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
        createFloor();

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

        mLookatSphere = new Sphere(1, 12, 12);
        mLookatSphere.setMaterial(material);
        mLookatSphere.setColor(Color.YELLOW);
        mLookatSphere.setPosition(0, 0, 6);
        getCurrentScene().addChild(mLookatSphere);

        // Set up crosshair.
        crosshairPlane = new Plane(2, 2, 12, 12);
        Material crosshairMat = new Material();
        crosshairMat.enableLighting(true);
        crosshairMat.setDiffuseMethod(new DiffuseMethod.Lambert());
        crosshairMat.setColorInfluence(0);
        Bitmap crosshairBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.crosshair);
        try {
            crosshairMat.addTexture(new Texture("crosshair", crosshairBitmap));
        } catch (TextureException e) {
            e.printStackTrace();
        }
        crosshairPlane.setMaterial(crosshairMat);
        crosshairPlane.setTransparent(true);
        getCurrentScene().addChild(crosshairPlane);

        super.initScene();
    }

    public void createFloor() {
        //
        // -- Load a bitmap that represents the terrain. Its color values will
        //    be used to generate heights.
        //
        mPlane = new Plane(500, 500, 100, 100);
        mPlane.setPosition(0, 0, 0);
        mPlane.setDoubleSided(true);
        Material material1 = new Material();
        material1.setColorInfluence(0);
        Bitmap picTexture = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squares_big);
        try {
            material1.addTexture(new Texture("squares", picTexture));
        } catch (TextureException e) {
            e.printStackTrace();
        }
        mPlane.setMaterial(material1);

        // Set orientation of the plane.
        Quaternion q = new Quaternion();
        q.fromAngleAxis(Vector3.Axis.X, 90);
        mPlane.setOrientation(q);
        getCurrentScene().addChild(mPlane);
        try {
            getCurrentScene().setSkybox(R.drawable.right, R.drawable.left, R.drawable.top, R.drawable.bottom, R.drawable.front, R.drawable.back);
        } catch (TextureException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        handleCameraMovement(10);
        // Update position of camera.
        getCurrentCamera().setPosition(cameraPosition);
        // Show HUD.
        showHudComponents();
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

    /**
     * Displays different heads-up display components. These are rendered on different planes which are positioned depending on current camera position and orientation.
     */
    private void showHudComponents() {
        Quaternion currentOrientation = getCurrentCamera().getOrientation();
        Vector3 currentPosition = getCurrentCamera().getPosition();

        // Show crosshair.
        crosshairPlane.setOrientation(currentOrientation);
        crosshairPlane.setPosition(currentPosition);
        getCurrentScene().removeChild(crosshairPlane);
        getCurrentScene().addChild(crosshairPlane);

        final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
        movement.rotateBy(currentOrientation).multiply(3);
        movement.inverse();
        crosshairPlane.getPosition().add(movement);

        // Show points.
        // TODO

        // Show remaining rockets.
        // TODO.
    }

    public void handleTab() {
        // Extract orientation looked at.
        isTabbed = false;
        fireRocket = true;
    }

    public void handleCameraMovement(double units) {
        if (isTabbed) {
            String output = "Old position:" + getCurrentCamera().getPosition().toString() + "\t";
            final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
            movement.rotateBy(getCurrentCamera().getOrientation()).multiply(units);
            movement.inverse();
            movement.y = 0;
            cameraPosition = cameraPosition.clone().add(movement);

            // Set globe position.
            Vector3 spherePos = cameraPosition.clone();
            spherePos.add(movement);
            mLookatSphere.setPosition(spherePos);
            output += "New position: " + movement;
            RajLog.i(output);
            isTabbed = false;
        } else if (fireRocket) {
            Quaternion currentOrientation = getCurrentCamera().getOrientation().clone();
            Vector3 currentPosition = getCurrentCamera().getPosition().clone();
            this.state.getTopLevelManager().getrManager().rocketLaunched(currentOrientation, currentPosition);
            fireRocket = false;
        }
    }

    public static RajawaliVRExampleRenderer getCurrentRenderer() {
        return currentRenderer;
    }

    public static BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize(3);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.setTextAlign(Paint.Align.CENTER);

        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x * scale, y * scale, paint);

        return bitmap;
    }
}
