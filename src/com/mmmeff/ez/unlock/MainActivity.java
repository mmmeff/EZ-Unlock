package com.mmmeff.ez.unlock;

import com.stericson.RootTools.RootTools;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView statusText;
	Button lockButton, unlockButton;
	private Context context = this;
	private final boolean DEBUG = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// check for root
		if (RootTools.isRootAvailable()) {
			// su exists, continue on
		} else {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Intent browserIntent = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://forum.xda-developers.com/showthread.php?t=1792342"));
						startActivity(browserIntent);
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						finish();
						break;
					}
				}
			};

			Dialog rootDialog = new AlertDialog.Builder(context)
					.setTitle("Fail!").setMessage(getString(R.string.noroot))
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener)
					.setCancelable(false).create();
			rootDialog.show();
		}

		@SuppressWarnings("unused")
		FileMan fileman = new FileMan(this);
		final Commander commander = new Commander(this);

		if (DEBUG)
			PreferencesSingleton.getInstance(this).prefs.edit().clear()
					.commit();

		final MediaPlayer easySound = MediaPlayer.create(context, R.raw.easy);

		statusText = (TextView) findViewById(R.id.unlocktext);

		lockButton = (Button) findViewById(R.id.lockbutton);
		lockButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (commander.Lock())
								easySound.start();
							refreshStatus();
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(
						"Are you sure you want to lock your bootloader?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			}
		});

		unlockButton = (Button) findViewById(R.id.unlockbutton);
		unlockButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (commander.UnLock())
								easySound.start();
							refreshStatus();
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(
						"Are you sure you want to unlock your bootloader?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			}
		});

		refreshStatus();
	}

	private void refreshStatus() {
		String status = PreferencesSingleton.getInstance(this).prefs.getString(
				"status", null);

		if (status != null) {
			if (status.equals("locked")) {
				statusText.setText("Locked");
				statusText.setTextColor(Color.RED);
				lockButton.setEnabled(false);
				unlockButton.setEnabled(true);
			} else if (status.equals("unlocked")) {
				statusText.setText("Unlocked");
				statusText.setTextColor(Color.GREEN);
				lockButton.setEnabled(true);
				unlockButton.setEnabled(false);
			}
		} else {
			statusText.setText("Unknown");
			statusText.setTextColor(Color.YELLOW);
			lockButton.setEnabled(true);
			unlockButton.setEnabled(true);
		}
	}

}
