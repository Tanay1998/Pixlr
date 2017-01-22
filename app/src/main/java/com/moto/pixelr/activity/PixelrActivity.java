package com.moto.pixelr.activity;

import android.app.Presentation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.moto.pixelr.R;
//import com.moto.pixelr.mods.DisplayPersonality;
import com.moto.pixelr.mods.Personality;
import com.motorola.mod.ModDevice;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class PixelrActivity extends AppCompatActivity {

    /**
     * Remote view to show on mod device display. For future details,
     * refer to https://developer.android.com/reference/android/app/Presentation.html
     */
    private Presentation presentation;

    /**
     * Instance of MDK Personality Card interface
     */
    private Personality personality;

    /**
     * Handler for events from mod device
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Personality.MSG_MOD_DEVICE:
                    /** Mod attach/detach */
                    ModDevice device = personality.getModDevice();
                    onModDevice(device);
                    break;
                default:
                    Log.i(PixelrActivity.class.getSimpleName(), "MotoActivity - Un-handle events: " + msg.what);
                    break;
            }
        }
    };

    private Unbinder unbinder;

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPersonality();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        /** Clean up presentation view */
        if (presentation != null) {
            presentation.dismiss();
            presentation = null;
        }

        /** Clean up mod interface */
        releasePersonality();
    }

    private void initView() {

    }

    /** MOTO MODS METHODS ______________________________________________________________________ **/

    /** Initial mod personality interface */
    private void initPersonality() {

        Log.d(PixelrActivity.class.getSimpleName(), "initPersonality() called.");

        if (null == personality) {

            Log.d(PixelrActivity.class.getSimpleName(), "new personality created.");

//            personality = new DisplayPersonality(this);
//            personality.registerListener(handler);
        }
    }

    /** Clean up mod personality interface */
    private void releasePersonality() {
        if (null != personality) {
            personality.onDestroy();
            personality = null;
        }
    }

    /** Mod device attach/detach */
    public void onModDevice(ModDevice device) {

        Log.d(PixelrActivity.class.getSimpleName(), "onModDevice() called");

        /** Clean up the remote presentation view */
        if (presentation != null) {
            presentation.dismiss();
            presentation = null;
        }
    }
}