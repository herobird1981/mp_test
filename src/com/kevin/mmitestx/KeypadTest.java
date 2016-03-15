package com.kevin.mmitestx;
import android.app.Activity;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kevin.mmitestx.R;

public class KeypadTest extends Activity 
{
	private static String TAG = "Keypad_Test";
	private boolean DBG = false;
	private boolean bVolumeUpTested = false;
	private boolean bVolumeDownTested = false;
	private boolean bMenuTested = false;
	private boolean bBackTested = false;
    private Button button_volume_up;
    private Button button_volume_down;
    private Button button_menu;
    private Button button_back;
    private Button button_test_ng;
    private Button button_test_good;
    static private int backcount = 0;
    Toast mToast;
	//SharedPreferences result;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypad);
		
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setKeyMaps();
		setListeners();
	}
	
	private void findViews(){
		button_volume_up = (Button)findViewById(R.id.button1);
		button_volume_down = (Button)findViewById(R.id.button2);
		button_menu = (Button)findViewById(R.id.button3);
		button_back = (Button)findViewById(R.id.Button02);
		button_test_ng = (Button)findViewById(R.id.button4);
		button_test_good = (Button)findViewById(R.id.button5);
		
		button_test_good.setEnabled(false);
	}
	
	private void setKeyMaps(){
	}
	
	private void setListeners(){
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			KeypadTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			KeypadTest.this.finish();
		}
	};
	
	public boolean dispatchKeyEvent(KeyEvent event){
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
			button_volume_up.setEnabled(false);
			bVolumeUpTested = true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
			button_volume_down.setEnabled(false);
			bVolumeDownTested = true;
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			button_back.setEnabled(false);
			showToast(R.string.terminate_keypad_toast, Toast.LENGTH_SHORT);
			forcedterminate();
			bBackTested = true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU){
			button_menu.setEnabled(false);
			bMenuTested = true;
		}
		else{
			return false;
		}
		
		if (bVolumeUpTested && bVolumeDownTested && bBackTested && bMenuTested){
            setResult(MMITestX.TEST_GOOD);
			KeypadTest.this.finish();
		}
		return true;
	}

	private void forcedterminate(){
		if(backcount>=6){
			KeypadTest.this.finish();
			backcount = 0;
			//System.exit(0);
		}
		else myHandler.sendEmptyMessage(100);
	}
	
	Handler myHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 100){
				backcount +=1;
			}
			if (DBG) Log.d(TAG, ""+backcount);
			super.handleMessage(msg);
		}	
	};
	
	Runnable r = new Runnable() 
	{
		public void run() 
		{
			mToast.cancel();
		}
	}; 
	
	public final void showToast(int strRes, int duration) 
	{
		myHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(strRes);
		else
			mToast = Toast.makeText(getApplicationContext(), strRes, duration);
			mToast.setGravity(Gravity.CENTER,0,0);
			mToast.show();
			myHandler.postDelayed(r, 3000);		
	}
}
