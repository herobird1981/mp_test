package com.kevin.mmitestx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kevin.mmitestx.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ReceiverMicTest extends Activity {
	private final boolean DBG = false;
	private static final String TAG = "ReceiverMic_Test";
	private boolean bDoRecording = false;
	private Button button_test_ng;
	private Button button_test_good;
	private Button button_recording;
	private Button button_playing;
	private MediaRecorder mMediaRecorder = null;
	private MediaPlayer mMediaPlayer = null;
	private AudioManager mAudioManager = null;
	private final static boolean mWriteInternalStorage = true;
	private final static String mMicTestFile = "MicTestFile.amr";
	private final static int MODE_WAIT = 0;
	private final static int MODE_PLAYING = 1;
	private final static int MODE_RECORDING = 2;
	private int mMode = MODE_WAIT;
	SharedPreferences result;
	BroadcastsHandler bh = new BroadcastsHandler();
    

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (DBG) Log.d(TAG, "onCreate");
        setContentView(R.layout.receivermic);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
        findViews();
	setListeners();
    }
    
    public void onStart() {
    	super.onStart();
    	if (DBG) Log.d(TAG, "onStrat, new MediaRecorder() & MediaPlayer()");
    	mMediaRecorder = new MediaRecorder();
    	mMediaPlayer = new MediaPlayer();
    }
    
    public void onResume() {
    	super.onResume();
    	if (DBG) Log.d(TAG, "onResume, registerReceiver");
    	this.registerReceiver(bh, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }
    
    public void onPause() {
    	super.onPause();
    	if (DBG) Log.d(TAG, "onPause, unregisterReceiver");
    	this.unregisterReceiver(bh);
    }
    
    public void onStop() {
    	super.onStop();
    	if (DBG) Log.d(TAG, "onStop, Release mMediaPlayer & mMediaRecorder");
    	if (mMediaPlayer != null) {
        	mMediaPlayer.release();
        	mMediaPlayer = null;
    	}
    	if (mMediaRecorder != null) {
        	mMediaRecorder.release();
        	mMediaRecorder = null;
    	}
	File f;
	if (mWriteInternalStorage) {
		f = new File(getApplicationContext().getCacheDir() + File.separator + mMicTestFile);
	} else {	
		f = new File(getApplicationContext().getExternalCacheDir() + File.separator + mMicTestFile);
	}
    	if (f.exists()) {
    		if (DBG) Log.d(TAG, "onDestory, delete file");
    		f.delete();
    	}
    }

    private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button1);
		button_test_good = (Button)findViewById(R.id.button2);
		button_recording = (Button)findViewById(R.id.button3);
		button_playing = (Button)findViewById(R.id.button4);
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		button_recording.setOnClickListener(pressRecording);
		button_playing.setOnClickListener(pressPlaying);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("ReceiverMicTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			ReceiverMicTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("ReceiverMicTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			ReceiverMicTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressRecording = new Button.OnClickListener() 
	{
			public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressRecording, mMode=" + mMode);
			if (mMode == MODE_WAIT) {
				try {
					FileOutputStream fos;
					if (mWriteInternalStorage) {
						fos = new FileOutputStream(getApplicationContext().getCacheDir() + File.separator + mMicTestFile);
                        if (DBG) Log.d(TAG, "Output File=" + getApplicationContext().getCacheDir() + File.separator + mMicTestFile);
					} else {
                        if (DBG) {
                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {
                                Log.d(TAG, "MEDIA_MOUNTED");
                            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                                Log.d(TAG, "MEDIA_MOUNTED_READ_ONLY");
                            } else {
                                Log.d(TAG, "No Access");
                            }
                            Log.d(TAG, "Output File=" + getApplicationContext().getExternalCacheDir() + File.separator + mMicTestFile);
                        }
						fos = new FileOutputStream(getApplicationContext().getExternalCacheDir() + File.separator + mMicTestFile);
					}
					mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					mMediaRecorder.setOutputFile(fos.getFD());
					mMediaRecorder.prepare();
					mMediaRecorder.start();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), R.string.sd_card_not_ready, Toast.LENGTH_SHORT).show();
					button_recording.setText(R.string.record);
					return;
				}
				button_recording.setText(R.string.stop);
				mMode = MODE_RECORDING;
			} else if (mMode == MODE_RECORDING) {
				if(mMediaRecorder != null) {
					try {
						mMediaRecorder.stop();
						mMediaRecorder.reset();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
				button_recording.setText(R.string.record);
				mMode = MODE_WAIT;
				bDoRecording = true;
				if (DBG) Log.d(TAG, "bDoRecording=" + bDoRecording);
			} else if (mMode == MODE_PLAYING) {
				Toast.makeText(getApplicationContext(), R.string.playing, Toast.LENGTH_SHORT).show();
			}
			if (DBG) Log.d(TAG, "pressRecording, mMode=" + mMode);
		}
	};
	
	private Button.OnClickListener pressPlaying = new Button.OnClickListener() 
	{
		public void onClick(View v) {
			if (DBG) Log.d(TAG, "pressPlaying, mMode=" + mMode);
			if (mMode == MODE_WAIT) {
				try {
					if (mWriteInternalStorage) {
						if (DBG) Log.d(TAG, "Input File=" + getApplicationContext().getCacheDir() + File.separator + mMicTestFile);
					} else {
                        if (DBG) {
						    String state = Environment.getExternalStorageState();
						    if (Environment.MEDIA_MOUNTED.equals(state)) {
							    Log.d(TAG, "MEDIA_MOUNTED");
						    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
							    Log.d(TAG, "MEDIA_MOUNTED_READ_ONLY");
						    } else {
							    Toast.makeText(getApplicationContext(), R.string.insert_sd_card, Toast.LENGTH_SHORT);
							    Log.d(TAG, "No Access");
							    return;
					 	    }
						    Log.d(TAG, "Input File=" + getApplicationContext().getExternalCacheDir() + File.separator + mMicTestFile);
                        }
					}
						
                    if (!bDoRecording) {
                        Toast.makeText(getApplicationContext(), R.string.record_first, Toast.LENGTH_SHORT).show();
                        return;
                    }

    				FileInputStream fis;
	    			if (mWriteInternalStorage) {
		    			fis = new FileInputStream(getApplicationContext().getCacheDir() + File.separator + mMicTestFile);
			    	} else {
				   		fis = new FileInputStream(getApplicationContext().getExternalCacheDir() + File.separator + mMicTestFile);					
                    }
					
					mMediaPlayer.reset();
					mMediaPlayer.setDataSource(fis.getFD());
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
					mAudioManager.setMode(AudioManager.MODE_IN_CALL);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
					mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer arg0) {
							// TODO Auto-generated method stub
							mMode = MODE_WAIT;
							button_playing.setText(R.string.play);
							mAudioManager.setMode(AudioManager.MODE_NORMAL);
							if (DBG) Log.d(TAG, "onCompletion, mMode=" + mMode);
						}
					});
					button_playing.setText(R.string.playing);
					mMode = MODE_PLAYING;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			} else if (mMode == MODE_PLAYING) {
				Toast.makeText(getApplicationContext(), R.string.playing, Toast.LENGTH_SHORT).show();
			} else if (mMode == MODE_RECORDING) {
				Toast.makeText(getApplicationContext(), R.string.record, Toast.LENGTH_SHORT).show();
			}
			if (DBG) Log.d(TAG, "pressPlaying, mMode=" + mMode);
	}};
	
	class BroadcastsHandler extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		    if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
		        //String data = intent.getDataString();
		        //Bundle extraData = intent.getExtras();
	
		        int st = intent.getIntExtra("state", -1);
                if (DBG) {
                    String nm = intent.getStringExtra("name");
                    int mic = intent.getIntExtra("microphone", -1);
                    Log.d(TAG, "st=" + Integer.toString(st) + "; nm=" + nm + "; mic=" + Integer.toString(mic));
                }	
		    }
		}
	}
}
