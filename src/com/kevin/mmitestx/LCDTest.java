package com.kevin.mmitestx;

import com.kevin.mmitestx.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LCDTest extends Activity {
	private Button button_test_ng;
	private Button button_test_good;
	//SharedPreferences result;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lcd);
        
        //result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
        findViews();
        setListeners();
	}
	
	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button1);
		button_test_good = (Button)findViewById(R.id.button2);
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("LCDTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			LCDTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("LCDTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			LCDTest.this.finish();
		}
	};
}
