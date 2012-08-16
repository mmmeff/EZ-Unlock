package com.mmmeff.ez.unlock;

import java.io.DataInputStream;
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
	 * Run a single command while disregarding any output given
	 * @param command
	 */
	private void ExecSingle(String command){
		try {
			input.writeBytes(command + "\n");
			input.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
	
	public void Lock(){
		ExecSingle("dd if=/sdcard/ezunlock/lock.img of=/dev/block/mmcblk0p5");
		PreferencesSingleton.getInstance(context).prefs.edit().putString("status", "locked").commit();
		Toast toast = Toast.makeText(context,
				"Bootloader succesfully locked!", Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void UnLock(){
		ExecSingle("dd if=/sdcard/ezunlock/unlock.img of=/dev/block/mmcblk0p5");
		PreferencesSingleton.getInstance(context).prefs.edit().putString("status", "unlocked").commit();
		Toast toast = Toast.makeText(context,
				"Bootloader succesfully unlocked!", Toast.LENGTH_LONG);
		toast.show();
	}
}
