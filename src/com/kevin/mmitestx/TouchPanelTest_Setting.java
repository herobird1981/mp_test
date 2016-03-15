package com.kevin.mmitestx;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TouchPanelTest_Setting extends Activity
{
    private TextView colorTextView, fontTextView;
    private Spinner colorSpinner, fontSizeSpinner; 
    private ArrayAdapter<?> colorAdapter, fontSizeAdapter;
    SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touchpanel_setting);
		
		sp = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);
		colorTextView = (TextView) findViewById(R.id.color_prompt);
		fontTextView = (TextView) findViewById(R.id.font_prompt);
		colorSpinner = (Spinner) findViewById(R.id.colorspinner); 
		fontSizeSpinner = (Spinner) findViewById(R.id.fontsizespinner);
	    final String[] drawColors = { getResources().getString(R.string.red), 
									  getResources().getString(R.string.blue), 
									  getResources().getString(R.string.yellow), 
									  getResources().getString(R.string.green), 
									  getResources().getString(R.string.white)};
	    
	    final String[] fontSize = { getResources().getString(R.string.fontsize_small),
	    							getResources().getString(R.string.fontsize_medium),
	    							getResources().getString(R.string.fontsize_large)};
	    
	    colorAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_dropdown_item, drawColors);
	    fontSizeAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_dropdown_item, fontSize);
	    colorSpinner.setAdapter(colorAdapter);
	    fontSizeSpinner.setAdapter(fontSizeAdapter);
	    colorSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
	    { 
	    	 
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	        {
	          colorTextView.setText(R.string.color_prompt);
	          switch (arg2)
	          {
	          	case 0: 
	          		sp.edit().putString("draw_color", "red").commit();
	          		break;
	          	case 1:
	          		sp.edit().putString("draw_color", "blue").commit();
	          		break;
	          	case 2:
	          		sp.edit().putString("draw_color", "yellow").commit();
	          		break;
	          	case 3:
	          		sp.edit().putString("draw_color", "green").commit();
	          		break;
	          	case 4:
	          		sp.edit().putString("draw_color", "white").commit();
	          		break;
	          }
	          arg0.setVisibility(View.VISIBLE);
	        } 
	        public void onNothingSelected(AdapterView<?> arg0) 
	        { 
	          // TODO Auto-generated method stub 
	        }	         
	    });
	    
	    fontSizeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
	    {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				// TODO Auto-generated method stub
				fontTextView.setText(R.string.font_prompt);
		          switch (arg2)
		          {
		          	case 0: 
		          		sp.edit().putString("font_size", "small").commit();
		          		break;
		          	case 1:
		          		sp.edit().putString("font_size", "medium").commit();
		          		break;
		          	case 2:
		          		sp.edit().putString("font_size", "large").commit();
		          		break;
		          }
		          arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			} 
	    });
}
}
