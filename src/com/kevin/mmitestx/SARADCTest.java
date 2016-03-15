package com.kevin.mmitestx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.mmitestx.R;

public class SARADCTest extends Activity {
	private static final String TAG = "SARADC_Test";
	private boolean DBG = false;
	private boolean bFirstStatus = false;
	private int nFirstStatus;
    private Button button_test_ng;
    private Button button_test_good;
    private TextView field_level;
    private TextView field_voltage;
    private TextView field_status;
	//SharedPreferences result;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saradc);
		
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
	}

	protected void onResume(){
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(BVMReceiver, filter);
		if (DBG) Log.d(TAG, "BVMReceiver has been registered!!!");
	}
	
	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		field_level = (TextView)findViewById(R.id.field_level);
		field_voltage = (TextView)findViewById(R.id.field_voltage);
		field_status = (TextView)findViewById(R.id.field_status);
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		
		// Set initial Test Good buttons state to "Disable"
		button_test_good.setEnabled(false);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("SARADCTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			SARADCTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("SARADCTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			SARADCTest.this.finish();
		}
	};
	
	private BroadcastReceiver BVMReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				int nLevel = intent.getIntExtra("level", 0);
				int nVoltage = intent.getIntExtra("voltage", 0);
				int nStatus = intent.getIntExtra("status", 0);
				
				field_level.setText(String.valueOf(nLevel));
				field_voltage.setText(String.valueOf(nVoltage));
				
				switch (nStatus) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						field_status.setText(R.string.BCCM_charging);
						showToast(R.string.BCCM_charging, Toast.LENGTH_SHORT);
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						field_status.setText(R.string.BCCM_not_charging);
						showToast(R.string.BCCM_not_charging, Toast.LENGTH_SHORT);
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						field_status.setText(R.string.BCCM_battery_status_full);
						showToast(R.string.BCCM_battery_status_full, Toast.LENGTH_SHORT);
						break;
					default:
						field_status.setText(Integer.toString(nStatus));	
				}
				
				if (!bFirstStatus) {
					nFirstStatus = nStatus;
					bFirstStatus = true;
				} else if (nFirstStatus != nStatus) {
					button_test_good.setEnabled(true);
				}
			}
		}
	};
	
	private void showToast(int strRes,int duration){
		Toast.makeText(SARADCTest.this, strRes, duration).show();
	}
	
	protected void onPause() {
		super.onPause();
		unregisterReceiver(BVMReceiver);
		if (DBG) Log.d(TAG, "BVMReceiver has been unregistered!!!");
	}
}
