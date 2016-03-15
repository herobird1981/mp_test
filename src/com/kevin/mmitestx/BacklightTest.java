package com.kevin.mmitestx;
import android.app.Activity;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.kevin.mmitestx.R;

public class BacklightTest extends Activity {
	private static int nTestCnt = 0; 
	private static final String TAG = "Backlight_Test";
	private Boolean DBG = false;
	//SharedPreferences result;
    private Button button_change_backlight;
    private Button button_test_ng;
    private Button button_test_good;
    private static Boolean bBrightness = true;
	
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backlight);	
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
	}

	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		button_change_backlight = (Button)findViewById(R.id.button1);
		// Set initial Test Good buttons state to "Disable"
		button_test_good.setEnabled(false);
	}
	
	private void setListeners() {
		button_change_backlight.setOnClickListener(changeBacklight);
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener changeBacklight = new Button.OnClickListener() {
		public void onClick(View v) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			if (bBrightness) {
				lp.screenBrightness = 1.0f;
				bBrightness = false;
				button_change_backlight.setText(R.string.darken);
				nTestCnt++;
				if (DBG)Log.v(TAG,"Current brightness level: 1.0f");
			}
			else {
				lp.screenBrightness = 0.1f;
				bBrightness = true;
				button_change_backlight.setText(R.string.brighten);
				nTestCnt++;
				if (DBG)Log.v(TAG,"Current brightness level: 0.1f");
			}
			getWindow().setAttributes(lp);
			
			if (nTestCnt >= 2) {
				//button_test_ng.setEnabled(true);
				button_test_good.setEnabled(true);
			}
		}
	};
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			BacklightTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			BacklightTest.this.finish();
		}
	};
}
