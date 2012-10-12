package com.mmmeff.ez.unlock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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
			Log.e(TAG, e.getMessage());
		}
	}
	
<<<<<<< HEAD
	/**
	 * Lock the bootloader, duh!
	 * @return boolean representing success
	 */
	public boolean Lock(){
		if (ExecSingle("dd if=/sdcard/ezunlock/lock.img of=/dev/block/mmcblk0p5")){
			Toast toast = Toast.makeText(context,
					"Bootloader succesfully locked!", Toast.LENGTH_LONG);
			toast.show();
			return true;
		} else {
			Toast toast = Toast.makeText(context,
					"Bootloader lock failed!!! Are you rooted?", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		
		
	}
	
	/**
	 * Lock the bootloader...
	 * @return boolean representing success
	 */
	public boolean UnLock(){
		if (ExecSingle("dd if=/sdcard/ezunlock/unlock.img of=/dev/block/mmcblk0p5")){
			Toast toast = Toast.makeText(context,
					"Bootloader succesfully unlocked!", Toast.LENGTH_LONG);
			toast.show();
			return true;
		} else {
			Toast toast = Toast.makeText(context,
					"Bootloader unlock failed! Are you rooted?", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		
=======
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
>>>>>>> parent of 1006964... Added bootloader re-locking
	}

	/**
	 * compare sha1 hash of current bootloader to unlocked bootloader
	 * if no match, return false
	 * @return status of bootloader lock
	 */
	public boolean isLocked(FileMan fileman) {
		try {
			if (SHAsum(fileman.getCurrentBootloader(this)).equals(SHAsum(fileman.getLockedBootloader()))){
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static String SHAsum(byte[] convertme) throws NoSuchAlgorithmException{
	    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
	    return byteArray2Hex(md.digest(convertme));
	}

	private static String byteArray2Hex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    return formatter.toString();
	}

	public void backupBootloader() {
		ExecSingle("dd if=/dev/block/mmcblk0p5 of=/sdcard/ezunlock/backup.img");
	}
}
