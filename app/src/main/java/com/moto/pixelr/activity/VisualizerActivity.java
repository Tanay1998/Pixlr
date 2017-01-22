package com.moto.pixelr.activity;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.moto.pixelr.R;

import java.io.IOException;

public class VisualizerActivity extends AppCompatActivity {

    public int mDivisions = 8;
    MediaPlayer mediaPlayer;

    private int selectedSong = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            selectedSong = getIntent().getIntExtra(MainActivity.INTENT_VISUALIZER_SONG, 0);
        }

//      mediaPlayer = MediaPlayer.create(this, R.raw.song);
//      try
//      {
//          mediaPlayer.prepare();
//      }
//      catch (IOException e)
//      {
//          Log.d("Dammit", "This sucks");
//          e.printStackTrace();
//      }
//          mediaPlayer.start();
        link();
    }

    private static final String TAG = "VisualizerView";

    private byte[] data;
    private Visualizer mVisualizer;

    public void link ()
    {
        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(0);
        // mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
        {
            @Override
            public void onWaveFormDataCapture (Visualizer visualizer, byte[] bytes, int samplingRate)
            {
            }

            @Override
            public void onFftDataCapture (Visualizer visualizer, byte[] bytes, int samplingRate)
            {
                updateVisualizerFFT(bytes);
            }
        };

        mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 2, true, true);
        mVisualizer.setEnabled(true);
    }

    public void release ()
    {
        mVisualizer.release();
    }

    public void updateVisualizerFFT (byte[] bytes)
    {
        data = bytes;
        draw();
    }

    boolean mFlash = false;

    // To use when changing songs
    public void flash ()
    {
        mFlash = true;
    }

    double[] maxPts = new double[mDivisions];
    double[] mult = {1.0, 1.3, 1.7, 2.1, 2.5, 3.0, 4.5, 6.0};
    public void draw ()
    {
        if (mFlash)
        {
            mFlash = false;
            // DRAW FLASH
        }
        else if (data != null)
        {
            double[] mFFTPoints = new double[mDivisions];
            int length = data.length / mDivisions;

            for (int i = 0; i < data.length; i += length)
            {
                int id = i / length;
                int j = i;

                for (j = i; j < data.length && j < i + length; j += 2)
                {
                    byte rfk = data[j];
                    byte ifk = data[j + 1];
                    float magnitude = (rfk * rfk + ifk * ifk);
                    int dbValue = (int) (10 * Math.log10(magnitude));
                    mFFTPoints[id] += dbValue * 2;
                }
                mFFTPoints[id] /= (j - i);
                maxPts[id] = (maxPts[id] > mFFTPoints[id]) ? maxPts[id] : mFFTPoints[id];
            }

            int[] boxes = new int[mDivisions];
            String s = "";
            for (int i = 0; i < mDivisions; i++)
            {
                mFFTPoints[i] = mFFTPoints[i] * mult[i];
                boxes[i] = (int) Math.floor((mFFTPoints[i] + 2.5) / 5.0);
                if (boxes[i] > 5) boxes[i] = 5;
                if (boxes[i] < 0) boxes[i] = 0;
                s += String.valueOf(boxes[i]) + " x ";
            }
            Log.d("Visualizer Max", s);
            // DRAW FFT DATA

        }
    }

    @Override
    protected void onDestroy ()
    {
        release();
        super.onDestroy();
    }
}