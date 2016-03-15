package com.kevin.mmitestx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.PreferenceActivity;
import android.util.Log;

public class DeviceInfo extends PreferenceActivity
{
	private static final String TAG = "DeviceInfoSettings";
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.device_info);
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setStringSummary("firmware_version", Build.VERSION.RELEASE);
		setStringSummary("device_model", Build.MODEL);
		setValueSummary("bsp_version", "ro.build.bsp");
		setValueSummary("baseband_version", "gsm.version.baseband");
		setStringSummary("cpu_type", Build.CPU_ABI);
		findPreference("build_number").setSummary(getFormattedBuildNumber());
        findPreference("kernel_version").setSummary(getFormattedKernelVersion());
		super.onResume();
	}

	private void setStringSummary(String preference, String value) {
        try {
            findPreference(preference).setSummary(value);
        } catch (RuntimeException e) {
            findPreference(preference).setSummary(
                getResources().getString(R.string.device_info_default));
        }
    }
	
	 private void setValueSummary(String preference, String property) {
	        try {
	            findPreference(preference).setSummary(SystemProperties.get(property,getResources().getString(R.string.device_info_default)));
	        } catch (RuntimeException e) {

	        }
	    }
	 
	 private String getFormattedKernelVersion() {
	        String procVersionStr;

	        try {
	            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
	            try {
	                procVersionStr = reader.readLine();
	            } finally {
	                reader.close();
	            }

	            final String PROC_VERSION_REGEX =
	                "\\w+\\s+" + /* ignore: Linux */
	                "\\w+\\s+" + /* ignore: version */
			"(.+)-.+-.+\\s+" + /* group 1: 2.6.22-omap1 */
	                "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
	                "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
	                "([^\\s]+)\\s+" + /* group 3: #26 */
	                "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
	                "(.+)"; /* group 4: date */

	            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
	            Matcher m = p.matcher(procVersionStr);

	            if (!m.matches()) {
	                Log.e(TAG, "Regex did not match on /proc/version: " + procVersionStr);
	                //return "Unavailable";
	                return getdKernelVersion(procVersionStr);
	            } else if (m.groupCount() < 4) {
	                Log.e(TAG, "Regex match on /proc/version only returned " + m.groupCount()
	                        + " groups");
	                return "Unavailable";
	            } else {
			// ARKT.20120202, Only give the kernel version, not detail format
			return (new StringBuilder(m.group(1)).append("\n").append(m.group(4))).toString();
			/*
	                return (new StringBuilder(m.group(1)).append("\n").append(
	                        m.group(2)).append(" ").append(m.group(3)).append("\n")
	                        .append(m.group(4))).toString();
			*/
	            }
	        } catch (IOException e) {  
	            Log.e(TAG,
	                "IO Exception when getting kernel version for Device Info screen",
	                e);

	            return "Unavailable";
	        }
	    }

		private String getFormattedBuildNumber() {
			String procVersionStr;
			procVersionStr = SystemProperties.get("ro.build.version.incremental", getResources().getString(R.string.device_info_default));
			String rst[] = procVersionStr.split("\\.");
			if(rst.length >= 2){
				return (new StringBuilder(rst[rst.length-2]).append(".").append(rst[rst.length-1])).toString();
			}else{
				return "Unavaliable";
			}
		}

		private String getdKernelVersion(String procVersionStr)
		{
		 	final String PROC_VERSION_REGEX =
	            "\\w+\\s+" + /* ignore: Linux */
	            "\\w+\\s+" + /* ignore: version */
				"(.+)\\s+" + /* group 1: 2.6.22-omap1 */
	            "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
	            "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
	            "([^\\s]+)\\s+" + /* group 3: #26 */
	            "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
	            "(.+)"; /* group 4: date */

	        Pattern p = Pattern.compile(PROC_VERSION_REGEX);
	        Matcher m = p.matcher(procVersionStr);

	        if (!m.matches()) {
	            Log.e(TAG, "[getdKernelVersion] Regex did not match on /proc/version: " + procVersionStr);
	            return "Unavailable";
	        } else if (m.groupCount() < 4) {
	            Log.e(TAG, "[getdKernelVersion] Regex match on /proc/version only returned " + m.groupCount()
	                    + " groups");
	            return "Unavailable";
	        } else {
				// ARKT.20120202, Only give the kernel version, not detail format
				return (new StringBuilder(m.group(1)).append("\n").append(m.group(4))).toString();
	        }
		}
}