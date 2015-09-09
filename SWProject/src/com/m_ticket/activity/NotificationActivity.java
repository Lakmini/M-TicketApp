package com.m_ticket.activity;

import com.example.m_ticket.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends Activity {
	TextView msgET;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		 // Intent Message sent from Broadcast Receiver
        String str = getIntent().getStringExtra("msg");
        if (str != null) {
            // Set the message
            msgET = (TextView) findViewById(R.id.message);
            msgET.setText(str);
        }
	}
	
	

}
