package com.moto.pixelr.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.moto.pixelr.R;
import com.moto.pixelr.ui.CameraPreview;
import com.moto.pixelr.ui.NoCamera;
import com.moto.pixelr.ui.NoSDCard;
import com.moto.pixelr.ui.PixelAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

	/** CLASS VARIABLES ________________________________________________________________________ **/

	// CAMERA VARIABLES
	private Camera mCamera;
	private CameraPreview mPreview;
	private SensorManager sensorManager = null;
	public boolean isFlashOn = false;

	// DEVICE VARIABLES
	private int degrees = -1;
	private int deviceHeight;
	private int orientation;

	// I/O VARIABLES
	private ExifInterface exif;
	private File sdRoot;
	private static final String DIRECTORY_PATH = "/DCIM/Camera/";
	private String fileName;

	// VIEW VARIABLES
	private Unbinder unbinder;

	// VIEW INJECTION VARIABLES
	@BindView(R.id.camera_flash_seek_bar) SeekBar flashSeekBar;
	@BindView(R.id.pixel_selector_recycler_view) RecyclerView pixelRecyclerView;
	@BindView(R.id.ibCapture) Button ibCapture;

	// CLICK METHODS
	@OnClick(R.id.ibCapture)
	public void captureImage() {
		turnOnFlash(0); // TODO: the parameter refers to the type of flash we are using (color combination).
		// We can use some property of the passed View v and use the same function for all of them

		mCamera.takePicture(null, null, mPicture);
	}

	public void turnOnFlash (int type)
	{
		// Code to turn on Flash
	}

	public void turnOffFlash ()
	{
		// Code to turn off Flash
	}


	public void toggleFlash (View v)
	{
		isFlashOn = !isFlashOn;
		if (isFlashOn)
		{
			Camera.Parameters p = mCamera.getParameters();
			p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			mCamera.setParameters(p);
		}
		else
		{
			Camera.Parameters p = mCamera.getParameters();
			p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(p);
		}
	}

	/** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		unbinder = ButterKnife.bind(this);

		// Setting all the path for the image
		sdRoot = Environment.getExternalStorageDirectory();

		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		initDisplay();
		initView();
	}

	@Override
	protected void onResume () {
		super.onResume();

		// Test if there is a camera on the device and if the SD card is
		// mounted.
		if (!checkCameraHardware(this)) {
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		}

		else if (!checkSDCard()) {
			Intent i = new Intent(this, NoSDCard.class);
			startActivity(i);
			finish();
		}

		// Creating the camera.
		createCamera();

		// Register this class as a listener for the accelerometer sensor.
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause () {
		super.onPause();

		// release the camera immediately on pause event
		releaseCamera();

		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
		preview.removeViewAt(0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	/** INITIALIZATION METHODS _________________________________________________________________ **/

	private void initDisplay() {

		// Selecting the resolution of the Android device so we can create a
		// proportional preview
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		deviceHeight = display.getHeight();
	}

	private void initView() {
		initRecyclerView();
		initSeekbar();
	}

	private void initRecyclerView() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		pixelRecyclerView.setHasFixedSize(true);
		pixelRecyclerView.setLayoutManager(layoutManager);
		PixelAdapter pixelAdapter = new PixelAdapter(this);
		pixelRecyclerView.setAdapter(pixelAdapter);
	}

	private void initSeekbar() {
		flashSeekBar.setProgress(50); // Sets the progress bar at 50%.
	}

	/** CAMERA METHODS _________________________________________________________________________ **/

	private void createCamera () {
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Setting the right parameters in the camera
		Camera.Parameters params = mCamera.getParameters();

		// Turn on flash.
		params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

		// Needed to determine maximum size supported by camera phone.
		List<Camera.Size> allSizes = params.getSupportedPictureSizes();
		Camera.Size size = allSizes.get(0); // get top size
		for (int i = 0; i < allSizes.size(); i++) {
			if (allSizes.get(i).width > size.width)
				size = allSizes.get(i);
		}

		//set max Picture Size
		params.setPictureSize(size.width, size.height);

		params.setPictureFormat(PixelFormat.JPEG);
		params.setJpegQuality(85);
		mCamera.setParameters(params);
		mCamera.setDisplayOrientation(90);

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);

		// Resizing the LinearLayout so we can make a proportional preview. This
		// approach is not 100% perfect because on devices with a really small
		// screen the the image will still be distorted - there is place for
		// improvment.
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mPreview.setLayoutParams(layoutParams);

		// Adding the camera preview after the FrameLayout and before the button
		// as a separated element.
		preview.addView(mPreview, 0);
	}

	private void releaseCamera () {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/**
	 * Check if this device has a camera
	 */
	private boolean checkCameraHardware (Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance () {
		Camera c = null;
		try
		{
			// attempt to get a Camera instance
			c = Camera.open();
		}
		catch (Exception e)
		{
			// Camera is not available (in use or does not exist)
			Log.e(MainActivity.class.getSimpleName(), "getCameraInstance(): Camera instance was null.");
		}

		// returns null if camera is unavailable
		return c;
	}

	private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

		public void onPictureTaken (byte[] data, Camera camera) {
			turnOffFlash();
			// File name of the image that we just took.
			fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";

			// Creating the directory where to save the image. Sadly in older
			// version of Android we can not get the Media catalog name
			File mkDir = new File(sdRoot, DIRECTORY_PATH);
			mkDir.mkdirs();

			// Main file where to save the data that we recive from the camera
			File pictureFile = new File(sdRoot, DIRECTORY_PATH + fileName);

			try {
				FileOutputStream purge = new FileOutputStream(pictureFile);
				purge.write(data);
				purge.close();
			} catch (FileNotFoundException e) {
				Log.d("DG_DEBUG", "File not found: " + e.getMessage());
			}
			catch (IOException e) {
				Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
			}

			// Adding Exif data for the orientation. For some strange reason the
			// ExifInterface class takes a string instead of a file.
			try {
				exif = new ExifInterface("/sdcard/" + DIRECTORY_PATH + fileName);
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + orientation);
				exif.saveAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/** I/O METHODS ____________________________________________________________________________ **/

	private boolean checkSDCard () {
		boolean state = false;

		String sd = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sd))
		{
			state = true;
		}

		return state;
	}

	/**
	 * Putting in place a listener so we can get the sensor data only when
	 * something changes.
	 */
	public void onSensorChanged (SensorEvent event) {

		synchronized (this) {

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
					} else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;}
					else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
					}
				}
			}
		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 *
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation (float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	/** OVERRIDE METHODS _______________________________________________________________________ **/
	@Override
	public void onAccuracyChanged (Sensor sensor, int accuracy) {}
}
