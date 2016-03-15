package com.kevin.mmitestx;

import java.io.IOException;
import java.util.List;

import com.kevin.mmitestx.R;

import android.app.Activity;
//import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
//import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.os.AsyncTask;


public class RearCameraTest extends Activity implements SurfaceHolder.Callback {
    private final static boolean DBG = false;
    private final static String TAG = "RearCameraTest";
	 private Button button_test_ng;
	 private Button button_test_good;
	 private SurfaceView mPreview;
	 private  SurfaceHolder mHolder;
	 Camera mCamera;
	 Camera.Parameters mParameters;
	 SharedPreferences result;

		
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     // Hide the window title.
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	     setContentView(R.layout.camera);
		 getWindow().setFormat(PixelFormat.UNKNOWN);
		 result = getSharedPreferences("MMITestX2_TestResult", MODE_WORLD_WRITEABLE);
         findViews();
         setListeners();
	 }
	 
    @Override
    protected void onResume() {
        super.onResume();
        if (DBG) Log.d(TAG, "onResume: Camera.open()=" + mCamera);
        mCamera = Camera.open();
        if (DBG) Log.d(TAG, "onResume: Camera.open()=" + mCamera);
        mParameters = mCamera.getParameters();
        if (DBG) Log.d(TAG, "onResume: mParameters.setCameraSensor(2)");
        //mParameters.setCameraSensor(1);
        mCamera.setParameters(mParameters);
    }

    @Override
    protected void onPause() {
        if (DBG) Log.d(TAG, "onPause: mCamera.stopPreview()/release()");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        super.onPause();
    }

	private void findViews(){
		mPreview = (SurfaceView)findViewById(R.id.surfaceView1);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		mHolder = mPreview.getHolder();
	    mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	private void setListeners(){
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			RearCameraTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			RearCameraTest.this.finish();
		}
	};
	
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
	    // to draw.
        /*if (DBG) Log.d(TAG, "surfaceCreated: Camera.open()=" + mCamera);
        mCamera = Camera.open();
        if (DBG) Log.d(TAG, "surfaceCreated: Camera.open()=" + mCamera);
		mParameters = mCamera.getParameters();
        if (DBG) Log.d(TAG, "mParameters.setCameraSensor(1)");
		mParameters.setCameraSensor(1);
		mCamera.setParameters(mParameters);*/
	    try {
	       mCamera.setPreviewDisplay(holder);
	    } catch (IOException exception) {
	        // TODO: add more exception handling logic here
            mCamera.release();
            mCamera = null;
	    }
	 }

	 public void surfaceDestroyed(SurfaceHolder holder) {
	     // Surface will be destroyed when we return, so stop the preview.
	     // Because the CameraDevice object is not a shared resource, it's very
	     // important to release it when the activity is paused.
        if (DBG) Log.d(TAG, "surfaceDestroyed");
	     /*mCamera.stopPreview();
	     mCamera.release();
	     mCamera = null;*/
	 }

	 private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	     final double ASPECT_TOLERANCE = 0.05;
	     double targetRatio = (double) w / h;
	     if (sizes == null) return null;
	
	     Size optimalSize = null;
	     double minDiff = Double.MAX_VALUE;
	
	     int targetHeight = h;
	
	     // Try to find an size match aspect ratio and size
	     for (Size size : sizes) {
	         double ratio = (double) size.width / size.height;
	         if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	         if (Math.abs(size.height - targetHeight) < minDiff) {
	             optimalSize = size;
	             minDiff = Math.abs(size.height - targetHeight);
	         }
	     }
	
	     // Cannot find the one match the aspect ratio, ignore the requirement
	     if (optimalSize == null) {
	         minDiff = Double.MAX_VALUE;
	         for (Size size : sizes) {
	             if (Math.abs(size.height - targetHeight) < minDiff) {
	                 optimalSize = size;
	                 minDiff = Math.abs(size.height - targetHeight);
	             }
	         }
	     }
	     return optimalSize;
	 }

	 public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	     // Now that the size is known, set up the camera parameters and begin
	     // the preview.
        if (DBG) Log.d(TAG, "surfaceChanged:");
	     Camera.Parameters parameters = mCamera.getParameters();
	     List<Size> sizes = parameters.getSupportedPreviewSizes();
	     Size optimalSize = getOptimalPreviewSize(sizes, w, h);
	     parameters.setPreviewSize(optimalSize.width, optimalSize.height);
	     mCamera.setParameters(parameters);
	     try {
		     mCamera.startPreview();
	     } catch (Exception e) {
	    	 Toast.makeText(getApplicationContext(), "No Camera", Toast.LENGTH_SHORT).show();
	    	 e.printStackTrace();
	     }
	 }

    class OpenCameraTask extends AsyncTask<String, Integer, String> {
        private int mCount = 0;

        protected String doInBackground(String... params) {
            do {
                mCamera = Camera.open();
                try {
                    if (mCamera == null) Thread.sleep(300);
                } catch (InterruptedException e) {
                }
            } while (++mCount < 10);
            if (mCamera == null) {
                return "Open Failed";
            } else {
                return "Open Successfully";
            }
        }
    }
}
