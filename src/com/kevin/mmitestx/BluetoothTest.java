package com.kevin.mmitestx;
import com.kevin.mmitestx.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BluetoothTest extends Activity {
    private static final String TAG = "BluetoothTest";
    private static final boolean DBG_MSG = false;
	// Intent request codes
    //private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
	private static final int STATE_ON = 1;
	private static final int STATE_OFF = 2;
	private int BLUETOOTH_INITIAL_STATE;	
	//private StringBuffer message;
	// Array adapter for the log
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	// Layout Views
	private ListView mLogView;
	private Button button_test_ng;
	private Button button_test_good;
	private Button button_scan;
	private ToggleButton toggleButton_bluetooth;
	BluetoothAdapter mBluetoothAdapter;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		// Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.bluetooth);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null){
			Toast.makeText(this, R.string.device_not_found, Toast.LENGTH_SHORT).show();
		}
		if (mBluetoothAdapter.isEnabled()){
			BLUETOOTH_INITIAL_STATE = STATE_ON;
		} else {
			BLUETOOTH_INITIAL_STATE = STATE_OFF;
		}
		findViews();
		setListeners();
	}
	
	private void findViews(){
		button_scan = (Button)findViewById(R.id.button1);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		toggleButton_bluetooth = (ToggleButton)findViewById(R.id.toggle1);
		mLogView = (ListView)findViewById(R.id.listView1);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		mLogView.setAdapter(mNewDevicesArrayAdapter);
		button_test_good.setEnabled(false);
	}
	
	private void setListeners(){
		button_scan.setOnClickListener(pressScan);
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		toggleButton_bluetooth.setOnCheckedChangeListener(toggle_BT);
        // Set inital state of Scan button
        button_scan.setEnabled(false);
	}
	
	public void onResume(){
		super.onResume();
        if (!mBluetoothAdapter.isEnabled()) {
        	toggleButton_bluetooth.setChecked(false);
        	button_scan.setEnabled(false);
        } else {
        	toggleButton_bluetooth.setChecked(true);
        	button_scan.setEnabled(true);
        }
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
	}
	
	protected void onStop(){
		super.onStop();
		if (DBG_MSG) Log.d(TAG, "onStop");
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
        	mBluetoothAdapter.cancelDiscovery();
        	if (BLUETOOTH_INITIAL_STATE == STATE_OFF){
        		mBluetoothAdapter.disable();
        	}
        }
        this.unregisterReceiver(mReceiver);
	}
	
	private Button.OnClickListener pressScan = new Button.OnClickListener() {
		public void onClick(View v) {
	        if (DBG_MSG) Log.d(TAG, "pressScan");
	        // If BT is not on, request that it be enabled.
	        // setupChat() will then be called during onActivityResult
	        if (!mBluetoothAdapter.isEnabled()) {
	        	Toast.makeText(getApplicationContext(), R.string.turn_on_device_first, Toast.LENGTH_SHORT).show();
	        	return;
	        }
			mNewDevicesArrayAdapter.clear();
			doDiscovery();
			button_scan.setEnabled(false);
		}
	};

	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			BluetoothTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			BluetoothTest.this.finish();
		}
	};
	
	private CompoundButton.OnCheckedChangeListener toggle_BT = new CompoundButton.OnCheckedChangeListener(){
		public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
			if (isChecked){
				if (!mBluetoothAdapter.isEnabled()) {
					boolean isAirplaneMode = isAirplaneModeOn(getApplicationContext());
					if (isAirplaneMode) {
						Toast.makeText(getApplicationContext(), R.string.switch_off_airplane_mode, Toast.LENGTH_SHORT).show();
						toggleButton_bluetooth.setChecked(false);
					} else {
			            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					}
		        }
			} else {
				if (mBluetoothAdapter.isEnabled()) {
					mBluetoothAdapter.disable();
					button_scan.setEnabled(false);
		        }
			}
		}
	};
	
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (DBG_MSG) Log.d(TAG, "mReceiver");
        	
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
//                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.device_not_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                button_scan.setEnabled(true);
            }
            if (mNewDevicesArrayAdapter.getCount() > 0) {
            	button_test_good.setEnabled(true);
            }
        }
    };
    
    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (DBG_MSG) Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);


        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
        	mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }
    
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
    	if (requestCode == REQUEST_ENABLE_BT){
    		if (resultCode == Activity.RESULT_OK){
    			toggleButton_bluetooth.setChecked(true);
    			button_scan.setEnabled(true);
    		} else {
    			toggleButton_bluetooth.setChecked(false);
    			button_scan.setEnabled(false);
    		}
    	}
    }
    
    private static boolean isAirplaneModeOn(Context context) {
    	return Settings.System.getInt(context.getContentResolver(),
    			Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }
}
