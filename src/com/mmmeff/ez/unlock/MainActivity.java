package com.mmmeff.ez.unlock;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Menu;
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

		FileMan fileman = new FileMan(this);
		final Commander commander = new Commander(this);

		statusText = (TextView) findViewById(R.id.unlocktext);

		/*
		 * lockButton = (Button) findViewById(R.id.lockbutton);
		 * lockButton.setOnClickListener(new OnClickListener() { public void
		 * onClick(View arg0) { commander.Lock(); refreshStatus(); } });
		 */
		if (DEBUG) PreferencesSingleton.getInstance(this).prefs.edit().clear().commit();

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
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				builder.setMessage(
						"Are you sure?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener)
						.show();
			}
		});

		refreshStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void refreshStatus() {
		String status = PreferencesSingleton.getInstance(this).prefs.getString(
				"status", null);

		if (status != null) {
			if (status.equals("locked")) {
				statusText.setText("Locked");
				statusText.setTextColor(Color.RED);
				// lockButton.setEnabled(false);
				unlockButton.setEnabled(true);
			} else if (status.equals("unlocked")) {
				statusText.setText("Unlocked");
				statusText.setTextColor(Color.GREEN);
				// lockButton.setEnabled(true);
				unlockButton.setEnabled(false);
			}
		} else {
			statusText.setText("Unknown");
			statusText.setTextColor(Color.YELLOW);
			// lockButton.setEnabled(true);
			unlockButton.setEnabled(true);
		}
	}

}
