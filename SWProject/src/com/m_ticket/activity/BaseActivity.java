package com.m_ticket.activity;

import com.example.m_ticket.R;
import com.m_ticket.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		 MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.main_activity_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.action_logout:
			logout();break;
		}
		
		
		
		return super.onOptionsItemSelected(item);
		
	}
	public void logout()
	{
		    SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
		    editor.putBoolean(Constants.IS_LOGIN, false);
			editor.commit();
            // After logout redirect user to Loing Activity
	        Intent i = new Intent(getApplicationContext(), MainActivity.class);
	       // i.putExtra("finish", true);
	       // Closing all the Activities
	       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	         
	        // Add new Flag to start new Activity
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	         
	        // Staring Login Activity
	        startActivity(i);
	        finish();
	        
	        
	}
	
	
	

}
