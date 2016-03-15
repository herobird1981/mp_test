package com.kevin.mmitestx;

//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.kevin.mmitestx.R;

public class GPSTest extends Activity {
	private final static boolean DBG = true;
	private final static String TAG = "GPSTest";
	private LocationManager locationManager;
	// View elements
	private TextView gpsView;
	private TextView latView;
	private TextView lonView;
	private TextView satView;
	private TextView utcView;
	private TextView fftimeView;
	private Button button_test_ng;
	private Button button_test_good;
	//SharedPreferences result;
	private double lat;
	private double lon;
	private int fftime;
	private int sat;
	private long utc;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();

		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		if (DBG) Log.d(TAG, "locationManager.addGpsStatusListener(gpsStatusListener)");
		locationManager.addGpsStatusListener(gpsStatusListener);
		
		if (Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER)) {
			gpsView.setText(R.string.turn_on);
		} else {
			gpsView.setText(R.string.turn_off);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.gps_comment).setCancelable(true)
			.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), R.string.turn_off, Toast.LENGTH_SHORT).show();
				}
			}).create().show();
		}
		
		if (DBG) {
			Log.d(TAG, "onCreate, isProviderEnabled(GPS_PROVIDER)=" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
			Log.d(TAG, "onCreate, isProviderEnabled(NETWORK_PROVIDER)=" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
			Log.d(TAG, "onCreate, isProviderEnabled(PASSIVE_PROVIDER)=" + locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER));
		}
	}

	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		if (Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER)) {
			gpsView.setText(R.string.turn_on);
		} else {
			gpsView.setText(R.string.turn_off);
		}
	}
	
	public void onDestroy() {
        locationManager.removeUpdates(locationListener);
		Log.d(TAG, "onDestory");
		super.onDestroy();	
	}
	
	private void findViews(){
		gpsView = (TextView)findViewById(R.id.gpsValue);
		latView = (TextView)findViewById(R.id.latValue);
		lonView = (TextView)findViewById(R.id.lonValue);
		satView = (TextView)findViewById(R.id.satValue);
		utcView = (TextView)findViewById(R.id.utcValue);
		fftimeView = (TextView)findViewById(R.id.fftimeValue);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		
		// Set initial status of View elements
		button_test_good.setEnabled(false);
	}
	
	private void setListeners(){
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}

	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressTestNG, PID=" + android.os.Process.myPid());
			//result.edit().putInt("GPSTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			GPSTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressTestGood, PID=" + android.os.Process.myPid());
			//result.edit().putInt("GPSTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			GPSTest.this.finish();
		}
	};
	
	GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() 
	{	
		public void onGpsStatusChanged(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == GpsStatus.GPS_EVENT_FIRST_FIX) {
				if (DBG) Log.d(TAG, "GPS_EVENT_FIRST_FIX");
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				fftime = gpsStatus.getTimeToFirstFix();
				fftimeView.setText(Integer.toString(fftime));
			} else if (arg0 == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
				if (DBG) Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
				sat = 0;
				while (it.hasNext()) {
					if (DBG) Log.d(TAG, "iteration#" + sat);
					it.next();
					sat++;
				}
				satView.setText(Integer.toString(sat));
			} else if (arg0 == GpsStatus.GPS_EVENT_STARTED) {
				if (DBG) Log.d(TAG, "GPS_EVENT_STARTED");
			} else if (arg0 == GpsStatus.GPS_EVENT_STOPPED) {
				if (DBG) Log.d(TAG, "GPS_EVENT_STOPPED");
			}
		}
	};

	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			if (DBG) Log.d(TAG, "onLocationChanged" + arg0);
			lat = arg0.getLatitude();
			lon = arg0.getLongitude();
			latView.setText(Double.toString(lat));
			lonView.setText(Double.toString(lon));
			button_test_good.setEnabled(true);
			utc = arg0.getTime();
                        if (DBG) Log.d(TAG, "getTime()=" + utc);
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			//utcView.setText(simpleDateFormat.format(new Date(utc)));
                        Date date = new Date(utc);
                        utcView.setText(date.toGMTString());
		}

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			if (DBG) Log.d(TAG, "onProviderDisabled" + arg0);
		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			if (DBG) Log.d(TAG, "onProviderEnabled" + arg0);
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			switch (arg1) {
			case LocationProvider.OUT_OF_SERVICE:
				if (DBG) Log.d(TAG, "onStatusChanged, OUT_OF_SERVICE");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				if (DBG) Log.d(TAG, "onStatusChanged, TEMPORARILY_UNAVAILABLE");
				break;
			case LocationProvider.AVAILABLE:
				if (DBG) Log.d(TAG, "onStatusChanged, AVAILABLE");
				break;
			}
		}
	};
}

