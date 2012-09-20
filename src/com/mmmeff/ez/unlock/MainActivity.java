package com.mmmeff.ez.unlock;

import com.stericson.RootTools.RootTools;

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
	private Commander commander;
	private FileMan fileman;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// check for root
		if (RootTools.isRootAvailable()) {
			// su exists, continue on
		} else { // warn about lack of root
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

		fileman = new FileMan(this);
		commander = new Commander(this);

		statusText = (TextView) findViewById(R.id.unlocktext);

		lockButton = (Button) findViewById(R.id.lockbutton);
		lockButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							commander.Lock();
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
							commander.UnLock();
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
		boolean locked = commander.isLocked(fileman);

		if (locked) {
			statusText.setText("Locked");
			statusText.setTextColor(Color.RED);
			lockButton.setEnabled(false);
			unlockButton.setEnabled(true);
		} else {
			statusText.setText("Unlocked");
			statusText.setTextColor(Color.GREEN);
			lockButton.setEnabled(true);
			unlockButton.setEnabled(false);
		}

	}

}
