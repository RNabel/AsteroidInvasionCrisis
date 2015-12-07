package asteroids3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;

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
import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.renderer.RajawaliVRRenderer;

import asteroids3d.gamestate.GameState;

public class Asteroids3DRenderer extends RajawaliVRRenderer {
    private static Asteroids3DRenderer currentRenderer;
    private static final BoundingBox boundingBox;

    private Plane centralPane;
    private Plane leftTopPlane;
    private Plane leftBottomPlane;

    private GameState state;

    public int isTriggered = 0;
    public boolean moveForward;
    public boolean moveRight;
    public boolean moveLeft;
    public boolean moveBack;
    public boolean fireRocket;
    public boolean nextState;

    private Vector3 cameraPosition = new Vector3(0, 5, 0);

    public Asteroids3DRenderer(Context context) {
        super(context);
    }

    static {
        // Setup bounding box.
        boundingBox = new BoundingBox(new Cube(1000, false).getGeometry());
        boundingBox.setMin(new Vector3(-200, 0, -200));
        boundingBox.setMax(new Vector3(200, 300, 200));
    }

    @Override
    public void initScene() {
        // Set singleton renderer to current object to be referenced by various Managers.
        currentRenderer = this;

        // Set frame rate.
        setFrameRate(30);

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
        state = new GameState(getCurrentScene(), this);

        // Create sample asteroid
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Asteroid", R.drawable.asteroid_small);
        try {
            material.addTexture(earthTexture);

        } catch (TextureException error) {
            RajLog.i("DEBUG TEXTURE ERROR");
        }

        // Set up crosshair.
        centralPane = new Plane(1, 1, 1, 1);
        Material crosshairMaterial = getStandardMaterial();
        Bitmap crosshairBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.crosshair);
//        Bitmap crosshairBitmap = drawTextToBitmap(mContext, "HELLO POINTS!!!");

        Texture tex = (Texture) crosshairMaterial.getTextureList().get(0);
        tex.setBitmap(crosshairBitmap);

        centralPane.setMaterial(crosshairMaterial);
        centralPane.setDoubleSided(true);
        centralPane.setTransparent(true);
        getCurrentScene().addChild(centralPane);

        leftTopPlane = new Plane(2, 2, 1, 1);
        leftTopPlane.setMaterial(getStandardMaterial());
        leftTopPlane.setTransparent(true);
        leftTopPlane.setDoubleSided(true);
        getCurrentScene().addChild(leftTopPlane);

        leftBottomPlane = new Plane(1, 1, 1, 1);
        leftBottomPlane.setMaterial(getStandardMaterial());
        leftBottomPlane.setTransparent(true);
        leftBottomPlane.setDoubleSided(true);
//        getCurrentScene().addChild(leftBottomPlane);


        super.initScene();
    }

    private Material getStandardMaterial() {
        Material newMaterial = new Material();
        newMaterial.enableLighting(true);
//        newMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        newMaterial.setColorInfluence(0);
        Bitmap transparentBitmap = drawTextToBitmap("");
        try {
            newMaterial.addTexture(new Texture("placeholder", transparentBitmap));
        } catch (TextureException e) {
            e.printStackTrace();
        }
        return newMaterial;
    }

    private void createFloor() {
        //
        // -- Load a bitmap that represents the terrain. Its color values will
        //    be used to generate heights.
        //
        Plane mPlane = new Plane(500, 500, 100, 100);
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
        // Update position of camera.
        getCurrentCamera().setPosition(cameraPosition);
        // Show HUD.
        showHudComponents();
        // Update game state.
        state.updateGameState(elapsedTime); // TODO pass in touch.

        super.onRender(elapsedTime, deltaTime);

        isTriggered = (isTriggered + 1) % Integer.MAX_VALUE;
    }

    /**
     * Displays different heads-up display components. These are rendered on different planes which are positioned depending on current camera position and orientation.
     */
    private void showHudComponents() {
        Quaternion currentOrientation = getCurrentCamera().getOrientation();
        Vector3 currentPosition = getCurrentCamera().getPosition();

        showHudHelper(currentOrientation.clone(), currentPosition);
        showHudPositionHelper(currentOrientation);

        // Show points and remaining rockets.
//        int rockets = state.getTopLevelManager().getRocketsAvailable();
//        int totalAsteroids = state.getAsteroidManager().getAsteroids().size();
        Bitmap pointBitmap = drawTextToBitmap(state.displayString);
        Texture currentTexture = (Texture) leftTopPlane.getMaterial().getTextureList().get(0);
        currentTexture.getBitmap().recycle();
        currentTexture.setBitmap(pointBitmap);
        getTextureManager().replaceTexture(currentTexture);
//            leftTopPlane.getMaterial().addTexture(new Texture("123", pointBitmap));
        // TODO

        // Show remaining rockets.
        // TODO.
    }

    private void showHudHelper(Quaternion orientation, Vector3 position) {
        Quaternion currentOrientation = orientation.clone();

        // Central plane.
        centralPane.setOrientation(currentOrientation);
        centralPane.setPosition(position);
        getCurrentScene().removeChild(centralPane);
        getCurrentScene().addChild(centralPane);

        // Left top plane.
        leftTopPlane.setOrientation(currentOrientation);
        leftTopPlane.setPosition(position);
        getCurrentScene().removeChild(leftTopPlane);
        getCurrentScene().addChild(leftTopPlane);

        // Left bottom plane.
        leftBottomPlane.setOrientation(currentOrientation);
        leftBottomPlane.setPosition(position);
        getCurrentScene().removeChild(leftBottomPlane);
        getCurrentScene().addChild(leftBottomPlane);
    }

    private void showHudPositionHelper(Quaternion currentOrientation) {
        final Vector3 movement = WorldParameters.FORWARD_AXIS.clone();
        Quaternion relative = currentOrientation.clone();
        movement.rotateBy(relative).multiply(3);
        movement.inverse();
        centralPane.getPosition().add(movement);

        // TODO move off to the side.
        leftTopPlane.getPosition().add(movement);
//        leftBottomPlane.getPosition().add(movement);
    }

    public void handleTab() {
        // Extract orientation looked at.
        moveForward = true;
        fireRocket = true;
    }

    public static Asteroids3DRenderer getCurrentRenderer() {
        return currentRenderer;
    }

    public static BoundingBox getBoundingBox() {
        return boundingBox;
    }

    private Bitmap drawTextToBitmap(String gText) {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        bitmap.setHasAlpha(true);

//        bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(35);
        RectF rect = new RectF(0, 0, 300, 300);
        StaticLayout sl = new StaticLayout(gText,
                textPaint, (int) rect.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        sl.draw(canvas);
        canvas.restore();

        // Flip the image.
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap output = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
        output.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        return output;
    }

    public Vector3 getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(Vector3 cameraPosition) {
        this.cameraPosition = cameraPosition;
    }
}
