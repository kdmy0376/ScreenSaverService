package dw.koo.android.screensaverservice;

import android.content.ContentResolver;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.service.dreams.DreamService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ScreenSaverService extends DreamService {

    private String TAG = ScreenSaverService.class.getSimpleName();

    private static final int MSG_DREAMING = 1;

    /* 10,800,000 milliseconds == 180 minutes */
    private static final long mAPDTimeOut = 3 * 60 * 60 * 1000;

    private static final long mMoveInterval = 6650;
    private static long mCurrentTimeout = 0;

    private ImageView mLogo;
    private AnimationDrawable mAniD;
    private DisplayMetrics metrics;

    private Handler mHandler = null;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);

        setContentView(R.layout.activity_main);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mLogo = (ImageView) findViewById(R.id.logo);
        mAniD = (AnimationDrawable) mLogo.getDrawable();

        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                if (msg.what == MSG_DREAMING) {
                    removeMessages(MSG_DREAMING);
                    mAniD.stop();

                    Random rand = new Random();
                    int x = rand.nextInt((metrics.widthPixels - 346) - 1);
                    int y = rand.nextInt((metrics.heightPixels - 346) - 1);

                    mLogo.setX(x);
                    mLogo.setY(y);
                    mAniD.start();
                }
                sendEmptyMessageDelayed(MSG_DREAMING, mMoveInterval);
                super.dispatchMessage(msg);
            }
        };
    }

    @Override
    public void onDreamingStarted() {
        ContentResolver resolver = getContentResolver();
        mCurrentTimeout = mAPDTimeOut - Settings.System.getLong(resolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000);

        Log.d(TAG, "onDreamingStarted: " +
                "mCurrentTimeout: " + mCurrentTimeout +
                "System Timeout: " + Settings.System.getLong(resolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000));

        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_DREAMING);
        }
        super.onDreamingStarted();
    }

    @Override
    public void onDreamingStopped() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_DREAMING);
        }
        super.onDreamingStopped();
    }
}