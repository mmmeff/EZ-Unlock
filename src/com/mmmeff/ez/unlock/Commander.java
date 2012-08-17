package com.mmmeff.ez.unlock;

import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/** serves as an interface for running console commands in the android os **/
public class Commander {
	
	/** logcat tag **/
	private static final String TAG = "ez_unlock";
	
	private Process process;
	private DataOutputStream input;
	private Context context; 

	public Commander(Context context){
		Initialize();
		this.context = context;
	}
	
	private void Initialize(){
		try {
			//create a process thread and ask for root permissions
			process = Runtime.getRuntime().exec("su");
			input = new DataOutputStream(process.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()); 
		}
	}
	
	/**
	 * Run a single command while disregarding any output given besides errors
	 * @param command
	 */
	private boolean ExecSingle(String command){
		boolean result = true;
		try {
			input.writeBytes(command + "\n");
			input.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			result = false;
		}
		return result;
	}
	
	public boolean Lock(){
		if (ExecSingle("dd if=/sdcard/ezunlock/lock.img of=/dev/block/mmcblk0p5")){
			PreferencesSingleton.getInstance(context).prefs.edit().putString("status", "locked").commit();
			Toast toast = Toast.makeText(context,
					"Bootloader succesfully locked!", Toast.LENGTH_LONG);
			toast.show();
			return true;
		} else {
			PreferencesSingleton.getInstance(context).prefs.edit().putString("status", null).commit();
			Toast toast = Toast.makeText(context,
					"Bootloader lock failed!!! Are you rooted?", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		
		
	}
	
	public boolean UnLock(){
		if (ExecSingle("dd if=/sdcard/ezunlock/unlock.img of=/dev/block/mmcblk0p5")){
			PreferencesSingleton.getInstance(context).prefs.edit().putString("status", "unlocked").commit();
			Toast toast = Toast.makeText(context,
					"Bootloader succesfully unlocked!", Toast.LENGTH_LONG);
			toast.show();
			return true;
		} else {
			PreferencesSingleton.getInstance(context).prefs.edit().putString("status", null).commit();
			Toast toast = Toast.makeText(context,
					"Bootloader unlock failed! Are you rooted?", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		
	}
}
