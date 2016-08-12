package com.example.karthik_driver;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class LocationDetector implements LocationListener {

	private final Context mContext;
	private Location location;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 0; // 1 minute
	// 2 minutes
	private static final long TWO_SECONDS = 1000 * 2;
	// Declaring a Location Manager
	protected LocationManager locationManager;
	//Declaring connectionDetector
	protected ConnectionDetector conDetector;

	public LocationDetector(Context context) {
		this.mContext = context;
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		conDetector = new ConnectionDetector(mContext);
		getLocationInfo();
	}



	/*
	 * get the location based on provider 
	 * @param@ LocationManager.NETWORK_PROVIDER or LocationManager.GPS_PROVIDER
	 */
	private Location getLocation(String provider) 
	{
		Location location;
		locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
		if(locationManager != null)
		{
			location = locationManager.getLastKnownLocation(provider);
			return location;
		}
		return null;
	}



	/*
	 * Funtion gets the location from GPS if it is null or gps is not switched on
	 * then gets from network
	 */
	private void getLocationInfo()	
	{
		Location gpsLocation = null;
		Location networkLocation = null;

		if(isGPSEnabled())
			gpsLocation = getLocation(LocationManager.GPS_PROVIDER);
		if(isNetworkEnabled())
			networkLocation = getLocation(LocationManager.NETWORK_PROVIDER);

		if (gpsLocation==null && networkLocation==null)	{
			location = null;
		}
		else if(gpsLocation==null) {
			location = networkLocation;
		}		
		else if(networkLocation==null){
			location = gpsLocation;
		}
		else {
			long timeDelta = networkLocation.getTime() - gpsLocation.getTime();
			if(timeDelta > 0 && timeDelta <= TWO_SECONDS)
				location = gpsLocation;
			else if(timeDelta > TWO_SECONDS )
				location = networkLocation;
			else
				location = gpsLocation;	
			Log.d("loc",gpsLocation.toString());
		}
		Log.d("loc",location.toString());
	}




	/* 
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */
	public void stopUsingGPS()
	{
		if(locationManager != null)
		{
			locationManager.removeUpdates(LocationDetector.this);
		}		
	}



	/*
	 * Function to get latitude
	 */
	public double getLatitude()
	{
		if(location != null)
		{
			return location.getLatitude();
		}
		return 0;
	}



	/*
	 * Function to get longitude
	 */
	public double getLongitude()
	{
		if(location != null)
		{
			return location.getLongitude();
		}
		return 0;
	}


	//	/*
	//	 * Function to get latlng
	//	 */
	//	public LatLng getLatLng()
	//	{
	//		if(location != null)
	//		{
	//			return new LatLng(getLatitude(),getLongitude());
	//		}
	//		return null;
	//	}

	/*
	 * Funtion to get address
	 */
	public Address getAddress() throws Exception
	{   
		if(location!=null && conDetector.isConnectingToInternet())
		{
			Geocoder geocoder = new Geocoder(mContext,Locale.getDefault());
			List<Address> addresses;

			addresses = geocoder.getFromLocation(this.getLatitude(),this.getLongitude(),1);
			Log.d("addr",addresses.get(0).toString());
			return addresses.get(0);

		}
		return null;
	}



	/*
	 * Funtion to check gps
	 */
	public boolean isGPSEnabled()
	{
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}


	/*
	 * Funtion to check network
	 */
	public boolean isNetworkEnabled()
	{
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}



	/*
	 * Function to show settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 * to switch on gps
	 */
	public void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
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



	@Override
	public void onLocationChanged(Location location) {
		Log.d("loc2",location.toString());
		this.location = location;

	}


	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
