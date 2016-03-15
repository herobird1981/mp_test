package com.kevin.mmitestx;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Settings extends PreferenceActivity
{
	private static final String TAG = "Settings_Activity";
	private boolean DBG = false;
	WakeLock wl;
	SharedPreferences checkwakeLock;
	private PowerManager pm;
	
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        checkwakeLock = getSharedPreferences("com.kevin.mmitestx_preferences", MODE_WORLD_READABLE);
        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(checkwakeLock.getBoolean("LCMTimeout", false))
		{
			wl.acquire();
			if(DBG) Log.d(TAG, "Wakelock acquired!");
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
		super.onStop();		
	}
}