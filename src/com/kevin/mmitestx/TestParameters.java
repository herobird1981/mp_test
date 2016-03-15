package com.kevin.mmitestx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;


public class TestParameters extends ListActivity {
	private static final boolean DBG = false;
	private static final String TAG = "TestParameters";
	private static int readBuffer = 8*1024;
	private static List<TestItem> list;
	static int nTestResult;
	SharedPreferences result ;


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_READABLE);
		list = readXml(this);
		setListAdapter(new ConfigAdapter(this));
	}
	
	public void onResume() {
		super.onResume();
	}

	private class ConfigAdapter extends BaseAdapter{
		LayoutInflater inflater;
		
		public ConfigAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			TestItem ti = list.get(position);
			if (convertView == null){
				convertView = inflater.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.icon = (ImageView)convertView.findViewById(R.id.imageView1);
				holder.field_title = (TextView)convertView.findViewById(R.id.textView1);
				holder.icon_status = (ImageView)convertView.findViewById(R.id.imageView2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setImageResource(setIcon(ti.getName()));
			holder.field_title.setText(ti.getTitle());
			holder.icon_status.setImageResource(android.R.drawable.ic_menu_edit);
			nTestResult = result.getInt(ti.getName(), MMITestX.TEST_NONE);
			if (DBG) Log.d(TAG, "getView: ti.getName()=" + ti.getName() +
				", nTestResult=" + nTestResult +
				", result=" + result.toString());
			return convertView;
		}
		
		class ViewHolder {
			ImageView icon;
			TextView field_title;
			ImageView icon_status;
		}
		
		private int setIcon(String name){
			if (name.equals("AccelerometerSensorTest")){
				return R.drawable.menu_accelerometersensor;
			} else if (name.equals("BacklightTest")){
				return R.drawable.menu_backlight;
			} if (name.equals("BluetoothTest")){
				return R.drawable.menu_bluetooth;
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
			} else if (name.equals("FMTest")){
				return R.drawable.menu_fm;
			} else if (name.equals("WiFiTest")){
				return R.drawable.menu_wifi;
			} else if (name.equals("ReceiverMicTest")){
				return R.drawable.menu_receivermic;
			} else if (name.equals("GPSTest")){
				return R.drawable.menu_gps;
			} else if (name.equals("TouchPanelTest")){
				return R.drawable.menu_touchpanel;
			} else if (name.equals("AntennaTest")){
				return R.drawable.menu_antenna;
			} else if (name.equals("SDCardTest")){
				return R.drawable.menu_sdcard;
			} else if (name.equals("FlashlightTest")){
				return R.drawable.menu_flashlight;
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
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("configurable-en.xml")),readBuffer);
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
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("configurable-zh.xml")),readBuffer);
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
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("configurable-zh-tw.xml")),readBuffer);
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
		String item = list.get(position).getName();
		Intent intent = new Intent();
		intent.setClassName(getPackageName(), getPackageName() + "." + item + "_Setting");
		try {
			startActivityForResult(intent, position);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
