package com.kevin.mmitestx;
import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.kevin.mmitestx.R;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class EngineeringModeTest extends ListActivity {
	private static final boolean DBG = false;
	private static final String TAG = "EngineeringModeTest";
	private static int readBuffer = 8*1024;
	private static List<TestItem> list,list2;
	static int nTestResult;
	private boolean isCommitOK = false;
	SharedPreferences result,sp ;
	Toast mToast;
	ProductionLineModeTest plmt;
	//static Locale locale;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_READABLE);
		sp = getSharedPreferences("Config_Item", MODE_WORLD_READABLE);
		list2 = configuedList();
		setListAdapter(new TestAdapter(this));
	}

	private class TestAdapter extends BaseAdapter{
		LayoutInflater inflater;
		
		public TestAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return list2.size();
		}
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			//InputStream in = null;
			TestItem ti = list2.get(position);
			if (convertView == null){
				convertView = inflater.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.icon = (ImageView)convertView.findViewById(R.id.imageView1);
				holder.field_title = (TextView)convertView.findViewById(R.id.textView1);
				holder.icon_status = (ImageView)convertView.findViewById(R.id.imageView2);
				holder.field_status = (TextView)convertView.findViewById(R.id.textView2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				holder.icon.setImageResource(setIcon(ti.getName()));
				holder.field_title.setText(ti.getTitle());
				nTestResult = result.getInt(ti.getName(), MMITestX.TEST_NONE);
				if (nTestResult == MMITestX.TEST_GOOD) {
					holder.icon_status.setImageResource(R.drawable.checked);
					holder.field_status.setText(R.string.test_good);
					holder.field_status.setTextColor(Color.GREEN);
				} else if (nTestResult == MMITestX.TEST_NOT_GOOD) {
					holder.icon_status.setImageResource(R.drawable.cross);
					holder.field_status.setText(R.string.test_ng);
					holder.field_status.setTextColor(Color.RED);
				} else if (nTestResult == MMITestX.TEST_NONE) {
					holder.icon_status.setImageResource(R.drawable.question);
					holder.field_status.setText(R.string.test_none);
					holder.field_status.setTextColor(Color.DKGRAY);				
			}
			
			if (DBG) Log.d(TAG, "getView: ti.getName()=" + ti.getName() +
				", nTestResult=" + nTestResult +
				", result=" + result.toString());
			return convertView;
		}
		
		class ViewHolder {
			ImageView icon;
			TextView field_title;
			ImageView icon_status;
			TextView field_status;
		}
		
		private int setIcon(String name){
			if (name.equals("AccelerometerSensorTest")){
				return R.drawable.menu_accelerometersensor;
			} else if (name.equals("BacklightTest")){
				return R.drawable.menu_backlight;
			} else if (name.equals("BluetoothTest")){
				return R.drawable.menu_bluetooth;
			} else if (name.equals("FlashlightTest")){
				return R.drawable.menu_flashlight;
			} else if (name.equals("VideoTest")){
				return R.drawable.video;	
			} else if (name.equals("KeypadTest")){
				return R.drawable.menu_keypad;
			} else if (name.equals("LCDTest")){
				return R.drawable.menu_lcd;
			} else if (name.equals("LightSensorTest")){
				return R.drawable.menu_lightsensor;
			} else if (name.equals("MagneticSensorTest")){
				return R.drawable.menu_magneticsensor;
			} else if (name.equals("FrontCameraTest")){
				return R.drawable.menu_frontcamera;
			} else if (name.equals("RearCameraTest")){
				return R.drawable.menu_rearcamera;
			} else if (name.equals("OrientationSensorTest")){
				return R.drawable.menu_orientationsensor;
			} else if (name.equals("ProximitySensorTest")){
				return R.drawable.menu_proximitysensor;
			} else if (name.equals("SARADCTest")){
				return R.drawable.menu_saradc;
			} else if (name.equals("SpeakerTest")){
				return R.drawable.menu_speaker;
			} else if (name.equals("HeadphoneTest")){
				return R.drawable.menu_headphone;
			} else if (name.equals("VibratorTest")){
				return R.drawable.menu_vibrator;
			} else if (name.equals("WiFiTest")){
				return R.drawable.menu_wifi;
			} else if (name.equals("ReceiverMicTest")){
				return R.drawable.menu_receivermic;
			} else if (name.equals("GPSTest")){
				return R.drawable.menu_gps;
			} else if (name.equals("TouchPanelTest")){
				return R.drawable.menu_touchpanel;
			} else if (name.equals("SDCardTest")){
				return R.drawable.menu_sdcard;
			} 
			return R.drawable.ic_launcher;
		}
	}
	
	static List<TestItem> readXml(Context context) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			InputSource is = null;
			
			String loc = Locale.getDefault().toString();
			if (DBG) Log.d(TAG, loc);
			if(loc.endsWith("US")||loc.endsWith("CA")||loc.endsWith("AU")||loc.endsWith("GB")||loc.endsWith("NZ")||loc.endsWith("en_SG"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-en.xml")),readBuffer);
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
			else if (loc.endsWith("CN"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-zh.xml")),readBuffer);
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
			else if (loc.endsWith("TW"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-zh-tw.xml")),readBuffer);
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){
		if (DBG) Log.d(TAG, "onListItemClick");
		String item = list2.get(position).getName();
		Intent intent = new Intent();
		intent.setClassName(getPackageName(), getPackageName() + "." + item);
		if (sp.getBoolean(item, false))
		{
			try {
				startActivityForResult(intent, position);
				if (DBG) Log.d(TAG, "OpenActivity");
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
		else 
		{
			showToast(R.string.engineering_toast, Toast.LENGTH_SHORT);
		}
	}
	
	Handler myHandler = new Handler();	
	Runnable r = new Runnable() 
	{
		public void run() 
		{
			mToast.cancel();
		}
	}; 
    
    public void showToast(int strRes, int duration) 
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DBG) Log.d(TAG, "onActivityResult");
		onContentChanged();
		setSelection(resultCode);
		if (requestCode >= 0 && requestCode < list2.size()) 
		{
			if(resultCode>0){
				setResult(RESULT_OK);
				if(DBG) Log.d(TAG, "set RESULT_OK");
			}
			TestItem ti = list2.get(requestCode);
			isCommitOK = result.edit().putInt(ti.getName(), resultCode).commit();
			if (DBG) Log.d(TAG, "onActivityResult: isCommitOK=" + isCommitOK +
				", ti.getName()=" + ti.getName() +
				", requestCode=" + requestCode +
				", resultCode=" + resultCode +
				", result=" + result.toString());
		}
		if (requestCode == 100)
		{
			EngineeringModeTest.this.finish();
			startActivity(new Intent(EngineeringModeTest.this, EngineeringModeTest.class));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,Menu.FIRST,1,R.string.go_config).setIcon(R.drawable.ic_launcher);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
		{
			case Menu.FIRST:
				Intent intent = new Intent();
			try {
				intent.setClass(this, Class.forName("com.kevin.mmitestx.TestConfig"));
				startActivityForResult(intent, 100);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	//Generate a new List including those items added in configuration list.
	public List<TestItem> configuedList()
	{
		List<TestItem> blist = null;
		list = readXml(this);
		int mIndex;	
		if (DBG) Log.d(TAG, "Original list size: "+ list.size());
		for (mIndex=0; mIndex<list.size(); mIndex++)
		{
			TestItem ti1 = list.get(mIndex);
			if(!sp.getBoolean(ti1.getName(), false))
			{
				list.remove(mIndex);
				mIndex--;
			}
	    }
		if (DBG) Log.d(TAG, "Modified list size: "+ list.size());
		blist = list;
		return blist;	
	}
}
