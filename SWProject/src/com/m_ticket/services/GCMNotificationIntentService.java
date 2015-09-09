package com.m_ticket.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.m_ticket.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.m_ticket.activity.NotificationActivity;
import com.m_ticket.utils.Constants;

public class GCMNotificationIntentService extends IntentService {
	private static final String TAG = "ABC";
	
	// Sets an ID for the notification
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;
	String LOG = "com.MTicket.GCMNotificationIntentService";

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
	
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString());
			} 
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) 
			{
				sendNotification("Deleted messages on server: "+ extras.toString());
			} 
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
			{
				
				 // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras.getString("Notice"));
                Log.i(TAG, "Received: " + extras.toString());
			}
		}
		   // Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
	        Intent resultIntent = new Intent(this, NotificationActivity.class);
	        resultIntent.putExtra("msg", msg);
	        
	      
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this).setContentTitle("M-Ticket").setContentText("You've received new message.").setSmallIcon(R.drawable.alarm);
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification 
	        mNotifyBuilder.setContentText("New message from Server");
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}
}
