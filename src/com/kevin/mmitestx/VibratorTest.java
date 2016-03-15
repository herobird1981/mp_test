package com.kevin.mmitestx;


import android.app.Activity;
import android.content.SharedPreferences;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.kevin.mmitestx.R;

public class VibratorTest extends Activity {
	private static boolean bTested = false; 
	private boolean DBG = false;
	private static final String TAG = "Vibrator_Test";
	//SharedPreferences result;
	private Button button_control_vibrator;
	private Button button_test_ng;
	private Button button_test_good;
	private Vibrator mVibrator=null;
	SharedPreferences getVibrationMode;
	private String vib_mode;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vibrator);
		getVibrationMode = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
		vib_mode = getVibrationMode.getString("vibration_mode", "");
		mVibrator = (Vibrator)getApplicationContext().getSystemService(VIBRATOR_SERVICE);
		Log.d(TAG, "vibration mode is: "+ vib_mode);
	}

	private void findViews() {
		button_control_vibrator = (Button)findViewById(R.id.button1);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		
		// Set initial Test Good buttons state to "Disable"
		button_test_good.setEnabled(false);
		button_test_ng.setEnabled(false);
	}
	
	private void setListeners() {
		button_control_vibrator.setOnClickListener(controlVibrator);
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener controlVibrator = new Button.OnClickListener() 
	{
		public void onClick(View v) 
		{		
			long[] pattern = {100L, 200L, 100L, 500L, 100L, 1000L};
			if (vib_mode == "Always")
			{
				mVibrator.vibrate(5000);
				bTested = true;
			}
			else if (vib_mode == "Pattern")
			{
				mVibrator.vibrate(pattern,0);
				bTested = true;
			}
			if (bTested) {
				button_test_ng.setEnabled(true);
				button_test_good.setEnabled(true);
				button_test_ng.setEnabled(true);
			}
		}
	};
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			mVibrator.cancel();
			//result.edit().putInt("VibratorTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			VibratorTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			mVibrator.cancel();
			//result.edit().putInt("VibratorTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			VibratorTest.this.finish();
		}
	};

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(bTested){
			mVibrator.cancel();
		}
		if (DBG) Log.d(TAG, "Vibrator has not vibrated, cancel() has not been called.");
		super.onStop();
	}
	
	
}
