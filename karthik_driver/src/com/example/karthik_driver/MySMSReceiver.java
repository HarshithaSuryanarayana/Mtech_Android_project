package com.example.karthik_driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MySMSReceiver extends BroadcastReceiver {

	final private int NO_OF_REPETITION = 10;
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();

	public void onReceive(Context context, Intent intent) {

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

					final String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

					Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message,Toast.LENGTH_SHORT).show();

					if(message.compareTo("LOC")==0)	{

						final LocationDetector locDetect= new LocationDetector(context);			
						String linkMsg="LINK>"+"\n"+"https://maps.google.co.uk/maps?q="+locDetect.getLatitude()+","+locDetect.getLongitude();
						sms.sendTextMessage(senderNum, null,linkMsg,null,null);

						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int count = 0;
								do
								{
									try {
										Address addr = locDetect.getAddress();
										if(addr!=null)
										{
											String addrMsg="ADDR>"+"\n"+addr.getAddressLine(0)+"\n"+addr.getAddressLine(1)+"\n"+addr.getAddressLine(2);
											sms.sendTextMessage(senderNum, null,addrMsg,null,null);
											break;
										}
										else
										{
											Log.d("addr","null");
										}
									} catch (Exception e) {
										// TODO: handle exception
										Log.d("addrExcep",e.getMessage());
										e.printStackTrace();
									}
									count++;
									Log.d("count",count+"");
								}while(count<=NO_OF_REPETITION);
							}
						}).start();














						//						new Thread(new Runnable() {
						//
						//							@Override
						//							public void run() {
						//								try {
						//									// TODO Auto-generated method stub
						//									int count = 0; 
						//									Address addr;
						//									do {
						//
						//										addr=locDetect.getAddress();
						//
						//										count++;
						//										if(addr!=null)
						//											break;
						//									}while(count>=NO_OF_REPETITION);
						//
						//									if(addr!=null) {
						//										sms.sendTextMessage(senderNum, null,addr.getAddressLine(0)+addr.getAddressLine(1)+addr.getAddressLine(2),null,null);
						//									}
						//									else {
						//										
						//										Toast.makeText(mContext,"Connection timed out",Toast.LENGTH_LONG).show();
						//									}
						//									
						//								} catch (Exception e) {
						//									// TODO Auto-generated catch block
						//									e.printStackTrace();
						//								}

					}

					//						}).start();
					//					}

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);

		}
	}
}
