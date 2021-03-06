package com.example.karthik_driver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context)
	{
		this._context = context;
	}

	/*
	 * Checking for all possible internet providers
	 */
	public boolean isConnectingToInternet()
	{
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}
	
	
	/*
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 */
	public void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);

		// Setting Dialog Title
		alertDialog.setTitle("Mobile Data");

		// Setting Dialog Message
		alertDialog.setMessage("Mobile data is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
				_context.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
