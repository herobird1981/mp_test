package com.kevin.mmitestx;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kevin.mmitestx.R;
import android.app.Activity;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SDCardTest extends Activity {
	private static final boolean DBG = false;
	private static final String MSG = "SDCardTest";
	//SharedPreferences result;
    private Button button_test_ng;
    private Button button_test_good;
    private Button btnWrite;
    private Button btnRead;
    private EditText etWrite;
    private EditText etRead;

	
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcard);
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
	}

	private void findViews() {
		btnWrite = (Button)findViewById(R.id.button1);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		btnRead = (Button)findViewById(R.id.button4);
		etWrite = (EditText)findViewById(R.id.editText1);
		etRead = (EditText)findViewById(R.id.editText2);
	}
	
	private void setListeners() {
		btnWrite.setOnClickListener(WriteSD);
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		btnRead.setOnClickListener(ReadSD);
	}
	
	private void showToast(int strRes,int style)
	{
		Toast mToast;
		mToast = Toast.makeText(SDCardTest.this, strRes, style);
		mToast.setGravity(Gravity.CENTER,0,0);
		mToast.show();
	}
	
	private Button.OnClickListener WriteSD = new Button.OnClickListener() {
		public void onClick(View v) {
			File path = Environment.getExternalStoragePublicDirectory(getPackageName());
			boolean isMkDirsOK = false;
			if (!path.exists()) {
				isMkDirsOK = path.mkdirs();
			}
			File file = new File(path, "SDCardTestFile.txt");
			if (DBG) Log.d(MSG, "WriteSD: isMkDirsOK=" + isMkDirsOK +
						", file=" + file);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				Editable e = etWrite.getText();
				fos.write(e.toString().getBytes());
				fos.close();
				
			if (DBG) Log.d(MSG, "Write done!!!");
			showToast(R.string.write_sdcard, Toast.LENGTH_SHORT);
			} catch (FileNotFoundException e) {
		
			} catch (IOException e) {
				
			}
		}
	};
	
	private Button.OnClickListener ReadSD = new Button.OnClickListener() {
		public void onClick(View v) {
			File path = Environment.getExternalStoragePublicDirectory(getPackageName());
			File file = new File(path, "SDCardTestFile.txt");
			if (DBG) Log.d(MSG, "ReadSD: file=" + file);
			try {
				FileInputStream fis = new FileInputStream(file);
				byte[] stringByte = new byte[fis.available()];
				fis.read(stringByte);
				etRead.setText(new String(stringByte));
				fis.close();
				boolean isDelOK = file.delete();
				if (DBG) Log.d(MSG, "ReadSD: isDelOK=" + isDelOK +
						", stringByte=" + stringByte.toString());
			} catch(IOException e) {
				
			}
			showToast(R.string.read_sdcard, Toast.LENGTH_SHORT);
		}
	};
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			SDCardTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			SDCardTest.this.finish();
		}
	};
}
