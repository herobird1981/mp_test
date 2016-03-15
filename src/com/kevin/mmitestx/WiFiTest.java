package com.kevin.mmitestx;
import java.util.List;
import com.kevin.mmitestx.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WiFiTest extends Activity 
{
    private static final boolean DBG = true;
    private static final String TAG = "WiFi_Test";  
	private WifiManager mWifiManager;
	private static final int STATE_ON = 1;
	private static final int STATE_OFF = 2;
	private int WIFI_INITIAL_STATE;	
	// Array adapter for the log
	private ArrayAdapter<String> mNewAPArrayAdapter;
	List<ScanResult> results;
	// Layout Views
	private ListView mLogView;
	private Button button_test_ng;
	private Button button_test_good;
	private Button button_scan;
	private ToggleButton toggleButton_wifi;
	IntentFilter filter;
	
	private static boolean isRegister = false;
	
    // Temporary dialog displayed while the application info opens or closes.
    private static final int DIALOG_BASE = 0;
    private static final int DIALOG_OPENING = DIALOG_BASE + 1;
    private static final int DIALOG_CLOSING = DIALOG_BASE + 2;
    //private static final int DIALOG_ALERTING = DIALOG_BASE + 3;
    //private static final int DIALOG_SCANING = DIALOG_BASE + 4;
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.wifi);
		
		mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager == null){
			Toast.makeText(this, R.string.device_not_found, Toast.LENGTH_SHORT).show();
		}
		
		// Get initial state of WiFi
		if (mWifiManager.isWifiEnabled()){
			WIFI_INITIAL_STATE = STATE_ON;
		} else {
			WIFI_INITIAL_STATE = STATE_OFF;
		}
		findViews();
		setListeners();
		filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
	}
	
	private void findViews(){
		button_scan = (Button)findViewById(R.id.button1);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		toggleButton_wifi = (ToggleButton)findViewById(R.id.toggle1);
		mLogView = (ListView)findViewById(R.id.listView1);
		
		mNewAPArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.message);
		mLogView.setAdapter(mNewAPArrayAdapter);
			
		// Button initial status
		button_test_good.setEnabled(false);
		if (WIFI_INITIAL_STATE == STATE_ON) {
			button_scan.setEnabled(true);
		} else if (WIFI_INITIAL_STATE == STATE_OFF) {
			button_scan.setEnabled(false);
		}
	}
	
	private void setListeners(){
		button_scan.setOnClickListener(pressScan);
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		toggleButton_wifi.setOnCheckedChangeListener(toggle_WiFi);		
	}
	
	public void onStart(){
		super.onStart();
		if (DBG) Log.d(TAG, "onStart");
	}
	
	protected void onStop(){
		super.onStop();
		if (DBG) Log.d(TAG, "onStop");
		
        if (mWifiManager != null) {       	
	        	if (mWifiManager.isWifiEnabled()){
	        		mWifiManager.setWifiEnabled(false);
		        }       	
        }
	}
	
	public void onResume(){
		super.onResume();
		boolean isWifiEnabled = mWifiManager.isWifiEnabled();
		if (DBG) Log.d(TAG, "onResume, isWifiEnabled=" + isWifiEnabled);
		if (isWifiEnabled){
			toggleButton_wifi.setChecked(true);
		} else {
			toggleButton_wifi.setChecked(false);
		}
	} 
	
	protected void onPause(){
		super.onPause();
		if (DBG) Log.d(TAG, "onPause");
        if (mWifiManager != null) {       	
        	if (mWifiManager.isWifiEnabled()){
        		mWifiManager.setWifiEnabled(false);
	        }       	
    }	
        // Unregister broadcast listeners
        if(isRegister) unregisterReceiver(mReceiver);
	}
	
	private Button.OnClickListener pressScan = new Button.OnClickListener() {
		public void onClick(View v) {
	        if (DBG) Log.d(TAG, "pressScan");
	        if (!isRegister)
			{
				registerReceiver(mReceiver, filter);
				isRegister = true;
			}
	        if (!mWifiManager.isWifiEnabled()) {
	        	Toast.makeText(getApplicationContext(), R.string.turn_on_device_first, Toast.LENGTH_SHORT).show();
	        	return;
	        }       
			mNewAPArrayAdapter.clear();
			mWifiManager.startScan();
			mainHandler.sendEmptyMessageDelayed(1000, 3000);
		}
	};

	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressTestNG");
			//result.edit().putInt("WiFiTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			WiFiTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressTestGood11");
			//result.edit().putInt("WiFiTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			WiFiTest.this.finish();
		}
	};
	
	private CompoundButton.OnCheckedChangeListener toggle_WiFi = new CompoundButton.OnCheckedChangeListener(){
		public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
			if (DBG) Log.d(TAG, "toggle_WiFi, isChecked=" + isChecked);
			if (isChecked) {
				mWifiManager.setWifiEnabled(true);
				showDialog(DIALOG_OPENING);
				mainHandler.sendEmptyMessageDelayed(1, 2000);
			} else {
				mWifiManager.setWifiEnabled(false);
				showDialog(DIALOG_CLOSING);
				mainHandler.sendEmptyMessageDelayed(0, 2000);
			}
		}
	};
	
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (DBG) Log.d(TAG, "mReceiver");
        	results = mWifiManager.getScanResults();       	
        	if (results.size() > 0) 
        	{
        		for (ScanResult result : results)
        		{
        			if (DBG) Log.d(TAG, result.toString());
        			mNewAPArrayAdapter.add(result.toString());
        		}
        		button_test_good.setEnabled(true);
        		mainHandler.sendEmptyMessage(1000);
        	}
        	else Toast.makeText(getApplicationContext(), "results is NULL!", Toast.LENGTH_LONG).show();
        }
    };
    
    Handler mainHandler = new Handler()
    {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case 1:
					dismissDialog(DIALOG_OPENING); 
					button_scan.setEnabled(true);
					registerReceiver(mReceiver, filter);
					if (DBG) Log.d(TAG, "Receive message 1, mReceiver registered!");
					isRegister = true;
					break;
				case 0:
					dismissDialog(DIALOG_CLOSING); 
					button_scan.setEnabled(false);
					mNewAPArrayAdapter.clear();
					if (DBG) Log.d(TAG, "Receive message 0, mReceiver unregistered!");
					if (isRegister){
						unregisterReceiver(mReceiver);
						isRegister = false;
					}					
				case 1000:
					if (isRegister){
						unregisterReceiver(mReceiver);
						isRegister = false;
					}
					if (DBG) Log.d(TAG, "Receive message 1000, mReceiver unregistered!");
					break;
			}
			super.handleMessage(msg);
		}
    	
    };
    
    public Dialog onCreateDialog(int id) {
        Log.d(TAG,"onCreateDialog, id =  "+id);
        switch (id)
        {
            case DIALOG_OPENING:
                ProgressDialog openingDlg = new ProgressDialog(this);
                openingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                openingDlg.setMessage(getText(R.string.opening));
                openingDlg.setIndeterminate(true);
                openingDlg.setCancelable(true);
                return openingDlg;

            case DIALOG_CLOSING:
                ProgressDialog closingDlg = new ProgressDialog(this);
                closingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                closingDlg.setMessage(getText(R.string.closing));
                closingDlg.setIndeterminate(true);
                closingDlg.setCancelable(true);
                return closingDlg;
        }
        return null;
    }
}
