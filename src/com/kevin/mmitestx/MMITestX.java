package com.kevin.mmitestx;

import com.kevin.mmitestx.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MMITestX extends PreferenceActivity {
	
	private static final String TAG = "MMITestX_Main_Activity";
	//Value to return if this preference does not exist.
	private static final int DefaultValue = 0;
	private boolean bCleared = false;
	private boolean DBG = false;
	static final int TEST_NONE = 0;
	static final int TEST_GOOD = 1;
	static final int TEST_NOT_GOOD = 2;
	private boolean isExit = false;
	SharedPreferences sp,checkwakeLock,checkconfigurable;
	private PowerManager pm;
	LayoutInflater mLayoutInflater;
	View aboutView;
	ViewGroup mViewGroup;
	WakeLock wl;
	Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_READABLE);
        checkwakeLock = getSharedPreferences("com.kevin.mmitestx_preferences", MODE_WORLD_READABLE);
        checkconfigurable = getSharedPreferences("com.kevin.mmitestx_preferences", MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.menu_root); 
        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
        mLayoutInflater = LayoutInflater.from(this);
        aboutView = mLayoutInflater.inflate(R.layout.about, null);
        Debug.startMethodTracing(TAG);
    }
   
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(checkwakeLock.getBoolean("LCMTimeout", false))
		{
			wl.acquire();
			if(DBG) Log.d(TAG, "Wakelock acquired!");
		}
		
		if(!checkconfigurable.getBoolean("Configurable", false))
		{
			findPreference("test_config").setEnabled(false);
		}
		else findPreference("test_config").setEnabled(true);
		
		int lcd = sp.getInt("LCDTest", DefaultValue);
    	int tp = sp.getInt("TouchPanelTest", DefaultValue);
    	int bl = sp.getInt("BacklightTest", DefaultValue);
    	int fl = sp.getInt("FlashlightTest", DefaultValue);
    	int kp = sp.getInt("KeypadTest", DefaultValue);
    	int sar = sp.getInt("SARADCTest", DefaultValue);
    	int sk = sp.getInt("SpeakerTest", DefaultValue);
    	int hp = sp.getInt("HeadphoneTest", DefaultValue);
    	int video = sp.getInt("VideoTest", DefaultValue);
    	int vi = sp.getInt("VibratorTest", DefaultValue);
    	int re = sp.getInt("ReceiverMicTest", DefaultValue);
    	int fc = sp.getInt("FrontCameraTest", DefaultValue);
    	int rc = sp.getInt("RearCameraTest", DefaultValue);
    	int acc = sp.getInt("AccelerometerSensorTest", DefaultValue);
    	int mag = sp.getInt("MagneticSensorTest", DefaultValue);
    	int or = sp.getInt("OrientationSensorTest", DefaultValue);
    	int li = sp.getInt("LightSensorTest", DefaultValue);
    	int pro = sp.getInt("ProximitySensorTest", DefaultValue);
    	int blu = sp.getInt("BluetoothTest", DefaultValue);
    	int wifi = sp.getInt("WiFiTest", DefaultValue);
    	int gps = sp.getInt("GPSTest", DefaultValue);
    	int sd = sp.getInt("SDCardTest", DefaultValue);
    	
    	if((lcd+tp+bl+fl+kp+sar+sk+hp+video+vi+re+fc+rc+acc+mag+or+li+pro+blu+wifi+gps+sd)==0)	
    	{
    		bCleared = true;
    	}
    	if(bCleared){
    		findPreference("clear_report").setEnabled(false);
    	}
    	else{
    		findPreference("clear_report").setEnabled(true);
    	}
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(wl.isHeld())
		{
			wl.release();
			if (DBG) Log.d(TAG,"Wakelock released!");
		}
		Debug.stopMethodTracing();
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode==RESULT_OK)
		{
			bCleared = false;
			if (DBG) Log.d(TAG, "receive RESULT_OK");
			//findPreference("clear_report").setEnabled(true);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if(preference.getKey().equals("engineering_mode")){
			startActivityForResult(new Intent(MMITestX.this,EngineeringModeTest.class), 0);
		}
		if(preference.getKey().equals("production_line_mode")){
			startActivityForResult(new Intent(MMITestX.this,ProductionLineModeTest.class), 0);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, Menu.FIRST, 1,"About").setIcon(R.drawable.menu_about_icon);
		menu.add(0, Menu.FIRST+1, 4, R.string.app_quit).setIcon(R.drawable.exit);	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case Menu.FIRST:
				if (aboutView.getParent() != null){
					mViewGroup = (ViewGroup) aboutView.getParent();
					mViewGroup.removeView(aboutView);
					showAboutDialog();
				}
				else showAboutDialog();
				break;
			case Menu.FIRST+1:
				MMITestX.this.finish();
				break;
			
		}
		return super.onOptionsItemSelected(item);
	}	

    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            ToQuitTheApp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    
    private void showAboutDialog()
    {
      new AlertDialog.Builder(MMITestX.this).setIcon(R.drawable.infomax_logo).setTitle(R.string.app_name).setView(aboutView).show();
    }

    private void ToQuitTheApp() 
    {
        if (isExit) {
            //MMITestX.this.finish();
        	System.exit(0);
        } else {
            isExit = true;
            showToast(R.string.quit_toast,Toast.LENGTH_SHORT);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    Handler mHandler = new Handler() 
    {
        public void handleMessage(Message msg) 
        {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
            if (DBG) Log.d(TAG, "Message received in main Handler! isExit = false");
        }
    };
    
    Handler myHandler = new Handler();	
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