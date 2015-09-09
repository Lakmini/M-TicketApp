package com.m_ticket.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.m_ticket.R;
import com.m_ticket.utils.Constants;
import com.m_ticket.utils.Utility;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText phoneNumTxt;
	private EditText passwordTxt;
	private EditText emailTxt;
	private Button btnSubmit;
	private ProgressDialog prgDialog;
    private String phoneNumber;
	private String password;
	private String email;
	private String registrationId="" ;
    private GoogleCloudMessaging gcmObj=null;
	private String regId = "";

	@Override
	protected void onCreate(Bundle bundle_1) {
		
		super.onCreate(bundle_1);
		setContentView(R.layout.activity_signup);

		phoneNumTxt = (EditText) findViewById(R.id.etSignUpPhoneNumber);
		passwordTxt = (EditText) findViewById(R.id.etSignUpPass);
		emailTxt = (EditText) findViewById(R.id.etSignUpEmail);
		btnSubmit = (Button) findViewById(R.id.btnSignUp);

		

		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
	    registrationId = prefs.getString(Constants.REG_ID, "");



		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				phoneNumber = phoneNumTxt.getText().toString();
				password = passwordTxt.getText().toString();
				email = emailTxt.getText().toString();
				registerNewUser();
			}
		});

	}


	
private void registerNewUser() {
		
		if ((!TextUtils.isEmpty(phoneNumber)) && (!TextUtils.isEmpty(password))
				&& (!TextUtils.isEmpty(email)) ){
			// When Phone number and email entered is Valid
			if (Utility.validatePhone(phoneNumber) && Utility.validate(email)) {
				// Check if Google Play Service is installed in Device
				// Play services is needed to handle GCM stuffs
				if (checkPlayServices()) {

					// Register Device in GCM Server
					registerInBackground();
				}
			}
			// When Email or phone number is invalid
			else {
				Toast.makeText(getApplicationContext(),
						"Check your Email address or Phone number",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Please fill the form, don't leave any field blank",
					Toast.LENGTH_LONG).show();
		}

	}
	
//AsyncTask to register Device in GCM Server
	private void registerInBackground() {
		
	 new GCMRegistrationHandler().execute();
	}

	private class GCMRegistrationHandler extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			
			String msg = "";
			try {
				if (gcmObj==null) {
				 gcmObj = GoogleCloudMessaging.getInstance(getApplicationContext());
				}
				regId = gcmObj.register(Constants.GOOGLE_PROJ_ID);
				msg = "Registration ID :" + regId;

			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			
			super.onPostExecute(msg);
			if (!TextUtils.isEmpty(regId)) {
				// Store RegId created by GCM Server in SharedPref
				storeRegIdinSharedPref(getApplicationContext(), regId);
			//	Toast.makeText(getApplicationContext(),"Registered successfully.\n\n" + msg,Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."+ msg, Toast.LENGTH_LONG).show();
			}
		}
	}
	// Store RegId  entered by User in SharedPref
	private void storeRegIdinSharedPref(Context context, String regId) {
			SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(Constants.REG_ID, regId);
			editor.commit();
			new RegisterHandler().execute();

		}



	private class RegisterHandler extends AsyncTask<String, Void, String> {

		
		@Override
		protected String doInBackground(String... params) {
			
			String response1 = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.URL + Constants.REGISTRATION);

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("phoneNumber",phoneNumber));
				nameValuePairs.add(new BasicNameValuePair("Password", password));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("regId", regId));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				response1 = EntityUtils.toString(httpEntity);

			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			return response1;
		}

		@Override
		protected void onPostExecute(String result) {
		
			super.onPostExecute(result);

			if (result != null) {

				try {
					// JSON Object
					JSONObject obj = new JSONObject(result);

					// When the JSON response has status boolean value assigned
					// with true
					if (obj.getBoolean("status")) {
						// Set Default Values for Edit View controls
						setDefaultValues();
						// Display successfully registered message using Toast
						Toast.makeText(getApplicationContext(),"You are successfully registered!",Toast.LENGTH_LONG).show();
						// When reg ID is set in Sharedpref, User will be taken to
						// HomeActivity
						if (!TextUtils.isEmpty(registrationId)) {
							Intent i = new Intent(getApplicationContext(), GridActivity.class);
							i.putExtra("regId", registrationId);
							
							startActivity(i);
							finish();
						}
						
					}
					// Else display error message
					else {

						Toast.makeText(getApplicationContext(),obj.getString("error_msg"), Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(),"We have some problem in processing request, try again.",Toast.LENGTH_LONG).show();
			}
		}
	}

	// Set degault values for Edit View controls

	public void setDefaultValues() {
		phoneNumTxt.setText("");
		emailTxt.setText("");
		passwordTxt.setText("");
	}

	// Check if Google Playservices is installed in Device or not
		private boolean checkPlayServices() {
			int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			// When Play services not found in device
			if (resultCode != ConnectionResult.SUCCESS) {
				if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
					// Show Error dialog to install Play services
					GooglePlayServicesUtil.getErrorDialog(resultCode, this,Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
				} else {
					Toast.makeText(getApplicationContext(),"This device doesn't support Play services, App will not work normally",Toast.LENGTH_LONG).show();
					finish();
				}
				return false;
			} else {
				  //Toast.makeText(getApplicationContext(),"This device supports Play services, App will work normally",Toast.LENGTH_LONG).show();
			
			}
			return true;
		}

		

}
