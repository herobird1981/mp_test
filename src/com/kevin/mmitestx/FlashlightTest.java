package com.kevin.mmitestx;

import android.app.Activity;
import android.hardware.Camera;
//import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.List;

public class FlashlightTest extends Activity
{
  //private static final boolean DBG = true;
  //private static final String TAG = "FlashlightTest";
  private Button button_test_good;
  private Button button_test_ng;
  private Camera mCamera;
  private Camera.Parameters mParameters;
  List<String> supportedFlashmodes;
  private ToggleButton togFlashlight;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.flashlight);
    findViews();
    setListeners();
    
     mCamera = Camera.open();
	 mParameters = mCamera.getParameters();
	 supportedFlashmodes = mParameters.getSupportedFlashModes();
  }
  
  private void findViews()
  {
    togFlashlight = (ToggleButton)findViewById(R.id.toggleButton1);
    button_test_ng = (Button)findViewById(R.id.button2);
    button_test_good = (Button)findViewById(R.id.button3);
  }
  
  private void setListeners()
  {
    togFlashlight.setOnCheckedChangeListener(toggleFlashlight);
    button_test_ng.setOnClickListener(pressTestNG);
    button_test_good.setOnClickListener(pressTestGood);
  }
  
  private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
          setResult(MMITestX.TEST_NOT_GOOD);
			FlashlightTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
          setResult(MMITestX.TEST_GOOD);
          FlashlightTest.this.finish();
		}
	};
	
	private CompoundButton.OnCheckedChangeListener toggleFlashlight = new CompoundButton.OnCheckedChangeListener() 
	{		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				openFlashlight(mCamera);
			}
			else closeFlashlight(mCamera);
		}
	};
  
  protected void onDestroy()
  {
    mCamera.release();
    super.onDestroy();
  }
  
  private void closeFlashlight(Camera paramCamera)
  {
      if ((supportedFlashmodes != null) && (!"off".equals(mParameters.getFlashMode())))
      {
          if (supportedFlashmodes.contains("off"))
          {
            mParameters.setFlashMode("off");
            paramCamera.setParameters(mParameters);
            Log.d("FlashlightTest", "Torch Turned Off!");
          }
          else
          {
            Log.d("FlashlightTest", "FLASH_MODE_OFF not supported");
          }
      }
   }

  private void openFlashlight(Camera paramCamera)
  {    
	  if ((supportedFlashmodes != null) && (!"torch".equals(mParameters.getFlashMode())))
         if (supportedFlashmodes.contains("torch"))
         {
            mParameters.setFlashMode("torch");
            paramCamera.setParameters(mParameters);
            Log.d("FlashlightTest", "Torch Turned On!");
          }
          else
          {
            Log.d("FlashlightTest", "FLASH_MODE_TORCH not supported");
          }
   }
}