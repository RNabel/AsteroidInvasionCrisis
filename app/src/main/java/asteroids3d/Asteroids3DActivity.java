package asteroids3d;

import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.RajawaliVRActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class Asteroids3DActivity extends RajawaliVRActivity {
    private Asteroids3DRenderer mRenderer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mRenderer = new Asteroids3DRenderer(this);
        setRenderer(mRenderer);

        setConvertTapIntoTrigger(true);
    }

    /**
     * Called when the Cardboard trigger is pulled.
     */
    @Override
    public void onCardboardTrigger() {
        mRenderer.isTriggered = 0;
        mRenderer.handleTab();
        RajLog.i("onCardboardTrigger");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.isFromSource(InputDevice.SOURCE_GAMEPAD)) {
            if (event.getScanCode() == 9 && event.getAction() == KeyEvent.ACTION_DOWN) {
                // Fire button clicked.
                mRenderer.fireRocket = true;
            } else if (event.getScanCode() == 3 && event.getAction() == KeyEvent.ACTION_UP) { // A clicked.
                mRenderer.nextState = true;
            } else if (event.getScanCode() == 103 && event.getAction() == KeyEvent.ACTION_DOWN) { // Forward.
                mRenderer.moveForward = true;
            } else if (event.getScanCode() == 108 && event.getAction() == KeyEvent.ACTION_DOWN) { // Back.
                mRenderer.moveBack = true;
            } else if (event.getScanCode() == 106 && event.getAction() == KeyEvent.ACTION_DOWN) { // Right.
                mRenderer.moveRight = true;
            } else if (event.getScanCode() == 105 && event.getAction() == KeyEvent.ACTION_DOWN) { // Left.
                mRenderer.moveLeft = true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
