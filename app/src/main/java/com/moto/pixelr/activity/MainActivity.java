package com.moto.pixelr.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import com.huhx0015.hxgselib.audio.HXGSEDolbyEffects;
import com.huhx0015.hxgselib.audio.HXGSEMusicEngine;
import com.moto.pixelr.constants.Constants;
import com.moto.pixelr.R;
import com.moto.pixelr.data.Global;
import com.moto.pixelr.mods.FirmwarePersonality;
import com.moto.pixelr.mods.Personality;
import com.moto.pixelr.raw.RawPersonalityService;
import com.moto.pixelr.ui.CameraPreview;
import com.moto.pixelr.ui.NoCamera;
import com.moto.pixelr.ui.NoSDCard;
import com.moto.pixelr.ui.PixelAdapter;
import com.motorola.mod.ModDevice;
import com.motorola.mod.ModManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{

	/**
	 * CLASS VARIABLES ________________________________________________________________________
	 **/

	// AUDIO VARIABLES
	private String currentSong = "NONE"; // Sets the default song for the activity.
	private boolean isPlaying = false; // Indicates that a song is currently playing in the background.

	// CAMERA VARIABLES
	private Camera mCamera;
	private CameraPreview mPreview;
	private SensorManager sensorManager = null;
	public boolean isFlashOn = false;
	public int flashType = 0;

	// DEVICE VARIABLES
	private int degrees = -1;
	private int deviceHeight;
	private int orientation;

	// INTENT VARIABLES
	public static final String INTENT_VISUALIZER_SONG = "visualizer_song";

	// I/O VARIABLES
	private ExifInterface exif;
	private File sdRoot;
	private static final String DIRECTORY_PATH = "/DCIM/Camera/";
	private String fileName;

	// MENU VARIABLES
	private boolean isDebugMenu = false;

	// MOTO MOD VARIABLES
	private FirmwarePersonality fwPersonality;
	private RawPersonalityService rawService;

	// PERMISSIONS VARIABLE
	private static final int PERMISSIONS_CAMERA_REQUEST_CODE = 6775;
	private static final int PERMISSIONS_RAW_PROTOCOL_CODE = 121;
	private static final int PERMISSIONS_RECORD_AUDIO_CODE = 5360;
	private static final int PERMISSIONS_WRITE_STORAGE_CODE = 55442;

	// VIEW VARIABLES
	private LinearLayoutManager layoutManager;
	private PixelAdapter pixelAdapter;
	private Unbinder unbinder;

	// VISUALIZER VARIABLES
	private boolean isVisualizerOn = false;
	private int mDivisions = 8;
	private double[] maxPts = new double[mDivisions];
	private double[] mult = {1.0, 1.3, 1.7, 2.1, 2.5, 3.0, 4.5, 6.0};
	private boolean mFlash = false;
	private byte[] data;
	private Visualizer mVisualizer;

	// VIEW INJECTION VARIABLES
	@BindView(R.id.camera_flash_seek_bar) SeekBar flashSeekBar;
	@BindView(R.id.pixel_selector_recycler_view) RecyclerView pixelRecyclerView;
	@BindView(R.id.debug_subcontainer) LinearLayout debugMenuContainer;
	@BindView(R.id.ibCapture) FloatingActionButton ibCapture;
	@BindView(R.id.pixel_selector_left_arrow) ImageView leftArrow;
	@BindView(R.id.pixel_selector_right_arrow) ImageView rightArrow;
	@BindView(R.id.pixel_emoji_container) LinearLayout emojiContainer;
	@BindView(R.id.pixel_music_container) LinearLayout musicContainer;

	// CLICK METHODS
	@OnClick(R.id.ibDebug)
	public void displayDebugMenu() {
		if (isDebugMenu) {
			debugMenuContainer.setVisibility(View.GONE);
			isDebugMenu = false;
		} else {
			debugMenuContainer.setVisibility(View.VISIBLE);
			isDebugMenu = true;
		}
	}

	@OnClick(R.id.ibCapture)
	public void captureImage ()
	{
		// WRITE STORAGE PERMISSIONS:
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{

			// Requests permission for camera.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					PERMISSIONS_WRITE_STORAGE_CODE);
		}
		else
		{
			turnOnFlash(); // TODO: the parameter refers to the type of flash we are using (color combination).
			// We can use some property of the passed View v and use the same function for all of them
			mCamera.takePicture(null, null, mPicture);
		}
	}

	@OnClick(R.id.ibVisualizer)
	public void toggleVisualizer ()
	{

		// RECORD AUDIO PERMISSIONS:
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
		{

			// Requests permission for record audio.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
					PERMISSIONS_RECORD_AUDIO_CODE);
		}
		else
		{

			if (isVisualizerOn)
			{
				releaseVisualizer();
				isVisualizerOn = false;
			}
			else
			{
				initVisualizer();
				isVisualizerOn = true;
			}
		}
	}

	@OnClick(R.id.pixel_emoji_icon_1)
	public void displayEmojiIcon1 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (2)};
		sendPixelCode(); // TODO: Change code later.
	}

	@OnClick(R.id.pixel_emoji_icon_2)
	public void displayEmojiIcon2 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (2)};
		sendPixelCode(); // TODO: Change code later.
	}

	@OnClick(R.id.pixel_emoji_icon_3)
	public void displayEmojiIcon3 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (2)};
		sendPixelCode(); // TODO: Change code later.
	}

	@OnClick(R.id.pixel_emoji_icon_4)
	public void displayEmojiIcon4 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (2)};
		sendPixelCode(); // TODO: Change code later.
	}

	@OnClick(R.id.pixel_music_1_container)
	public void playMusic1 ()
	{
		currentSong = "AllOfTheLights";
		HXGSEMusicEngine.getInstance().playSongName(currentSong, true, this);
		isPlaying = true;
	}

	@OnClick(R.id.pixel_music_2_container)
	public void playMusic2 ()
	{
		currentSong = "SONG 1";
		HXGSEMusicEngine.getInstance().playSongName(currentSong, true, this);
		isPlaying = true;
	}

	@OnClick(R.id.pixel_music_3_container)
	public void playMusic3 ()
	{
		currentSong = "SONG 2";
		HXGSEMusicEngine.getInstance().playSongName(currentSong, true, this);
		isPlaying = true;
	}

	@OnClick(R.id.moto_command_button_1)
	public void sendCommand1 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (100)};
		sendPixelCode();
	}

	@OnClick(R.id.moto_command_button_2)
	public void sendCommand2 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (101)};
		sendPixelCode();
	}

	@OnClick(R.id.moto_command_button_3)
	public void sendCommand3 ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[]{(byte) (101)};
		sendPixelCode();
	}

	/**
	 * Handler for events from mod device
	 */
	private Handler handler = new Handler()
	{
		@TargetApi(Build.VERSION_CODES.M)
		public void handleMessage (Message msg)
		{
			switch (msg.what)
			{
				case Personality.MSG_MOD_DEVICE:
					/** Mod attach/detach */
					ModDevice device = fwPersonality.getModDevice();
					onModDevice(device);
					break;
				case Personality.MSG_RAW_REQUEST_PERMISSION:
					/** Request user to grant RAW protocol permission */
					requestPermissions(new String[]{ModManager.PERMISSION_USE_RAW_PROTOCOL},
							PERMISSIONS_RAW_PROTOCOL_CODE);
					break;
				case RawPersonalityService.EXIT_APP:
					/** Exit main activity UI */
					finish();
					break;
				default:
					Log.i(Constants.TAG, "MainActivity - Un-handle events: " + msg.what);
					break;
			}
		}
	};

	/**
	 * Bind to the background service
	 */
	private ServiceConnection mConnection = new ServiceConnection()
	{
		public void onServiceConnected (ComponentName className, IBinder service)
		{
			rawService = ((RawPersonalityService.LocalBinder) service).getService();
			if (rawService != null)
			{
				rawService.registerListener(handler);
				rawService.checkRawInterface();
			}
		}

		public void onServiceDisconnected (ComponentName className)
		{
			rawService = null;
		}
	};

	/**
	 * ACTIVITY LIFECYCLE METHODS _____________________________________________________________
	 **/

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		unbinder = ButterKnife.bind(this);

		// Setting all the path for the image
		sdRoot = Environment.getExternalStorageDirectory();

		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// AUDIO CLASS INITIALIZATION:
		HXGSEMusicEngine.getInstance().initializeAudio(); // Initializes the HXGSEMusic class object.
		HXGSEDolbyEffects.getInstance().initializeDolby(this); // Initializes the HXGSEDolby class object.

		initDisplay();
		initView();
		initService();
	}

	@Override
	protected void onResume ()
	{
		super.onResume();

		initPersonality();

		// Test if there is a camera on the device and if the SD card is
		// mounted.
		if (!checkCameraHardware(this))
		{
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		}

		else if (!checkSDCard())
		{
			Intent i = new Intent(this, NoSDCard.class);
			startActivity(i);
			finish();
		}

		// CAMERA PERMISSIONS:
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
		{

			// Requests permission for camera.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
					PERMISSIONS_CAMERA_REQUEST_CODE);
		}
		else
		{
			// Creating the camera.
			createCamera();
		}

		// Register this class as a listener for the accelerometer sensor.
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

		// Resumes audio playback if music was playing in the background.
		if (isPlaying)
		{
			HXGSEMusicEngine.getInstance().playSongName(currentSong, true, this);
		}
	}

	@Override
	protected void onPause ()
	{
		super.onPause();

		HXGSEMusicEngine.getInstance().pauseSong(); // Pauses any song that is playing in the background.

		// release the camera immediately on pause event
		releaseCamera();

		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
		preview.removeAllViews();
	}

	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
		unbinder.unbind();
		releasePersonality();
		releaseVisualizer();

		// Releases all audio-related instances if the application is terminating.
		HXGSEMusicEngine.getInstance().releaseMedia();
		HXGSEDolbyEffects.getInstance().releaseDolbyEffects();
	}

	/**
	 * INITIALIZATION METHODS _________________________________________________________________
	 **/

	private void initDisplay ()
	{

		// Selecting the resolution of the Android device so we can create a
		// proportional preview
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		deviceHeight = display.getHeight();
	}

	private void initView ()
	{
		initRecyclerView();
		initScrollListener();
		initSeekbar();
	}

	private void initRecyclerView ()
	{
		layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		pixelRecyclerView.setHasFixedSize(true);
		pixelRecyclerView.setLayoutManager(layoutManager);
		pixelAdapter = new PixelAdapter(this);
		pixelRecyclerView.setAdapter(pixelAdapter);
	}

	private void initSeekbar ()
	{
		flashSeekBar.setProgress(50); // Sets the progress bar at 50%.
	}

	/**
	 * Call RawPersonalityService to toggle LED
	 */
	private void initService ()
	{
		Intent serviceIntent = new Intent(MainActivity.this, RawPersonalityService.class);
		startService(serviceIntent);
	}

	private void initScrollListener ()
	{

		pixelRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged (RecyclerView recyclerView, int newState)
			{
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled (RecyclerView recyclerView, int dx, int dy)
			{
				super.onScrolled(recyclerView, dx, dy);

				// LEFT ARROW:
				if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
				{
					leftArrow.setVisibility(View.INVISIBLE);
				}
				else
				{
					leftArrow.setVisibility(View.VISIBLE);
				}

				// RIGHT ARROW:
				if (layoutManager.findLastCompletelyVisibleItemPosition() == pixelAdapter.getItemCount() - 1)
				{
					rightArrow.setVisibility(View.INVISIBLE);
				}
				else
				{
					rightArrow.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public void displayEmojiOptions (boolean isShow)
	{

		if (isShow)
		{
			emojiContainer.setVisibility(View.VISIBLE);
		}
		else
		{
			emojiContainer.setVisibility(View.GONE);
		}
	}

	public void displayMusicOptions (boolean isShow)
	{

		if (isShow)
		{
			musicContainer.setVisibility(View.VISIBLE);
		}
		else
		{
			musicContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * CAMERA METHODS _________________________________________________________________________
	 **/

	private void createCamera ()
	{
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Setting the right parameters in the camera
		Camera.Parameters params = mCamera.getParameters();

		// Turn on flash.
		params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		//params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

		// Needed to determine maximum size supported by camera phone.
		List<Camera.Size> allSizes = params.getSupportedPictureSizes();
		Camera.Size size = allSizes.get(0); // get top size
		for (int i = 0; i < allSizes.size(); i++)
		{
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
		// improvement.
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mPreview.setLayoutParams(layoutParams);

		// Adding the camera preview after the FrameLayout and before the button
		// as a separated element.
		preview.addView(mPreview);
	}

	private void releaseCamera ()
	{
		if (mCamera != null)
		{
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/**
	 * Check if this device has a camera
	 */
	private boolean checkCameraHardware (Context context)
	{
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			// this device has a camera
			return true;
		}
		else
		{
			// no camera on this device
			return false;
		}
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance ()
	{
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

	public void turnOnFlash ()
	{
		sendPixelCode();
	}

	public void turnOffFlash ()
	{
		Global.cmd_key = 1;
		Global.info = new byte[] {0};
		sendPixelCode();
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

	private Camera.PictureCallback mPicture = new Camera.PictureCallback()
	{

		@Override
		public void onPictureTaken (byte[] data, Camera camera)
		{

			turnOffFlash();

			File pictureFileDir = getDir();

			if (!pictureFileDir.exists() && !pictureFileDir.mkdirs())
			{
				Log.e(MainActivity.class.getSimpleName(), "Can't create directory to save image.");
				Toast.makeText(MainActivity.this, "Can't create directory to save image.",
						Toast.LENGTH_LONG).show();
				return;
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			String date = dateFormat.format(new Date());
			String photoFile = "Picture_" + date + ".jpg";

			String filename = pictureFileDir.getPath() + File.separator + photoFile;

			File pictureFile = new File(filename);

			try
			{
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				Toast.makeText(MainActivity.this, "New Image saved:" + photoFile,
						Toast.LENGTH_LONG).show();
			}
			catch (Exception error)
			{
				Log.e(MainActivity.class.getSimpleName(), "File" + filename + "not saved: "
						+ error.getMessage());
				Toast.makeText(MainActivity.this, "Image could not be saved.",
						Toast.LENGTH_LONG).show();
			}

			mCamera.stopPreview();
			mCamera.startPreview();
		}
	};

	private File getDir ()
	{
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(sdDir, getString(R.string.app_name));
	}

	/**
	 * I/O METHODS ____________________________________________________________________________
	 **/

	private boolean checkSDCard ()
	{
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
	public void onSensorChanged (SensorEvent event)
	{

		synchronized (this)
		{

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4)
				{
					if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90)
					{
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
					}
					else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270)
					{
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
					}
				}
				else if (event.values[1] < 4 && event.values[1] > -4)
				{
					if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL)
					{
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
					}
					else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180)
					{
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
	private RotateAnimation getRotateAnimation (float toDegrees)
	{
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180)
		{
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0)
		{
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

	/**
	 * MOTO MOD METHODS _______________________________________________________________________
	 **/

	private void initPersonality ()
	{
		if (null == fwPersonality)
		{
			fwPersonality = new FirmwarePersonality(this);

			/** Register handler to get event and data update */
			fwPersonality.registerListener(handler);
		}

		if (null == rawService)
		{
			bindService(new Intent(this, RawPersonalityService.class), mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	private void releasePersonality ()
	{
		if (null != fwPersonality)
		{
			fwPersonality.onDestroy();
			fwPersonality = null;
		}

		if (null != rawService)
		{
			unbindService(mConnection);
			rawService = null;
		}
	}

	/**
	 * Mod device attach/detach
	 */
	public void onModDevice (ModDevice device)
	{

		Log.d(MainActivity.class.getSimpleName(), "onModeDevice() called.");

		/** Request RAW permission for Blinky Personality Card, to create RAW I/O */
		if (device != null)
		{
			if ((device.getVendorId() == Constants.VID_MDK
					&& device.getProductId() == Constants.PID_BLINKY)
					|| device.getVendorId() == Constants.VID_DEVELOPER)
			{

				Log.d(MainActivity.class.getSimpleName(), "onModeDevice(): Checking raw permissions.");

				checkRawPermission();
			}
		}
	}

	/**
	 * Pre Android 6.0 permission police, need check and ask granting permissions
	 */
	@TargetApi(Build.VERSION_CODES.M)
	public void checkRawPermission ()
	{
		if (checkSelfPermission(ModManager.PERMISSION_USE_RAW_PROTOCOL)
				!= PackageManager.PERMISSION_GRANTED)
		{
			handler.sendEmptyMessage(Personality.MSG_RAW_REQUEST_PERMISSION);
		}
	}

	/**
	 * PIXEL METHODS __________________________________________________________________________
	 **/

	public void sendPixelCode () {
		Log.d(MainActivity.class.getSimpleName(), "sendPixelCode(): CMD_KEY: " + Global.cmd_key);
		Log.d(MainActivity.class.getSimpleName(), "sendPixelCode(): INDEX: " + Global.info[0]);
		Intent serviceIntent = new Intent(MainActivity.this, RawPersonalityService.class);
		startService(serviceIntent);
	}

	/**
	 * VISUALIZER METHODS _____________________________________________________________________
	 **/

	private void initVisualizer () {

		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(0);

		// mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

		Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
			@Override
			public void onWaveFormDataCapture (Visualizer visualizer, byte[] bytes, int samplingRate) {}

			@Override
			public void onFftDataCapture (Visualizer visualizer, byte[] bytes, int samplingRate) {
				updateVisualizerFFT(bytes);
			}
		};

		mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 2, true, true);
		mVisualizer.setEnabled(true);
	}

	private void releaseVisualizer ()
	{
		if (mVisualizer != null)
		{
			mVisualizer.release();
		}

		// Stops the song that is currently playing in the background.
		if (HXGSEMusicEngine.getInstance().isSongPlaying())
		{
			HXGSEMusicEngine.getInstance().stopSong();
			currentSong = "NONE"; // Indicates no song has been selected.
		}
	}

	public void updateVisualizerFFT (byte[] bytes)
	{
		data = bytes;
		draw();
	}

	// To use when changing songs
	public void flash ()
	{
		mFlash = true;
	}

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

			byte[] boxes = new byte[mDivisions];
			String s = "";
			for (int i = 0; i < mDivisions; i++)
			{
				mFFTPoints[i] = mFFTPoints[i] * mult[i];
				boxes[i] = (byte) Math.floor((mFFTPoints[i] + 2.5) / 5.0);
				if (boxes[i] > 5) boxes[i] = 5;
				if (boxes[i] < 0) boxes[i] = 0;
				s += String.valueOf(boxes[i]) + " x ";
			}
			Log.d("Visualizer Max", s);
			// DRAW FFT DATA
			Global.cmd_key = 4;
			Global.info = boxes;
			sendPixelCode();
		}
	}

	/** OVERRIDE METHODS _______________________________________________________________________ **/

	/**
	 * Handler the permission requesting result
	 */
	public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{

		// RAW PROTOCOL:
		if (requestCode == PERMISSIONS_RAW_PROTOCOL_CODE && grantResults.length > 0)
		{

			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				Intent serviceIntent = new Intent(this, RawPersonalityService.class);
				startService(serviceIntent);
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("RAW PROTOCOL Permissions Rejected");
				alertDialog.setMessage("This app requires RAW PROTOCOL permissions to function.");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		}

		// CAMERA PROTOCOL:
		if (requestCode == PERMISSIONS_CAMERA_REQUEST_CODE && grantResults.length > 0)
		{

			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				createCamera();
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("CAMERA Permissions Rejected");
				alertDialog.setMessage("This app requires CAMERA permissions to function.");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		}

		// RECORD AUDIO:
		if (requestCode == PERMISSIONS_RECORD_AUDIO_CODE && grantResults.length > 0)
		{

			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				initVisualizer();
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("RECORD AUDIO Permissions Rejected");
				alertDialog.setMessage("This app requires RECORD AUDIO permissions to function.");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		}

		// WRITE STORAGE:
		if (requestCode == PERMISSIONS_WRITE_STORAGE_CODE && grantResults.length > 0)
		{

			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				// TODO: Do nothing, allow user to press photo button again.
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("WRITE STORAGE Permissions Rejected");
				alertDialog.setMessage("This app requires WRITE STORAGE permissions to function.");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		}
	}

	@Override
	public void onAccuracyChanged (Sensor sensor, int accuracy)
	{
	}
}
