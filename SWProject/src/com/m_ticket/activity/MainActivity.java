package com.m_ticket.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.m_ticket.R;
import com.m_ticket.utils.Constants;
import com.m_ticket.utils.Utility;
import com.m_ticket.customcomponent.Slide;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends Activity implements OnClickListener{
	
	private EditText phoneText;
	private EditText passwordText;
    private Button loginBtn;
    private Slide customSlide;
    private TextView appTitleTxt;
    private Boolean isloggedin;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
	    isloggedin= prefs.getBoolean(Constants.IS_LOGIN,false);
	    if(isloggedin)
	    {
	    	 navigateMainMenue();
	    	 finish();
	    }
        initialize();
       
    }
    
  

private void initialize() {
		
			  
	        phoneText = (EditText) findViewById(R.id.etPhone);
	        passwordText=(EditText) findViewById(R.id.etPass);
	        loginBtn = (Button) findViewById(R.id.btnLogin);
	        customSlide = (Slide) findViewById(R.id.slide);
	        appTitleTxt = (TextView) findViewById(R.id.app_title);
	        Typeface face1= Typeface.createFromAsset(getAssets(), "fonts/Capture_it.ttf");
	        appTitleTxt.setTypeface(face1);
            customSlide.setTypeface(face1);
	        customSlide.setText(getResources().getString(R.string.register));
	        loginBtn.setOnClickListener(this);
	        customSlide.setOnClickListener(this);

	   
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. 
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) { 
		case R.id.btnLogin:
			loginRequest();
			break;
		case R.id.slide:
			registerRequest();
			break;
		default:
			break;
		}
	}
    private void registerRequest()
	{
		Intent myIntent = new Intent(MainActivity.this,RegisterActivity.class);
     	startActivity(myIntent);
     	finish();
	}
	private void navigateMainMenue()
	{
		Intent intent = new Intent(MainActivity.this,GridActivity.class);
		startActivity(intent);
     	finish();
	}
	
    private void loginRequest(){
    	 // Get Phone number Edit View Value
		String phone=phoneText.getText().toString();
		  // Get Password Edit View Value
		String password= passwordText.getText().toString();
		
        if(Utility.isNotNull(phone) && Utility.isNotNull(password)){
            // When Phone number entered is Valid
            if(Utility.validatePhone(phone)){
              new LoginHandler(phone,password).execute();
                
            } 
            // When Phone number is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid phone number", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Please fill the form, Don't leave any field blank", Toast.LENGTH_LONG).show();
        }
	}
    
   private class LoginHandler extends AsyncTask<String, Void, String>{

	   private String phoneNumber;
	   private String password;
	   
	   public LoginHandler(String pNumber,String pwd)
	   {
		   this.phoneNumber=pNumber;
		   this.password=pwd;
	   }
		@Override
		protected String doInBackground(String... params) {
			
			 
		            String response1 = null;
		        

		           HttpClient httpclient = new DefaultHttpClient();
		           HttpPost  httppost = new HttpPost(Constants.URL + Constants.LOGIN);
		           
		           try {

		                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		               
		                nameValuePairs.add(new BasicNameValuePair("phoneNumber",phoneNumber));
		                nameValuePairs.add(new BasicNameValuePair("Password",password));

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
			

	         if(result!=null){

	            try {
	            	 // JSON Object
                    JSONObject obj = new JSONObject(result);
                    
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        storeRegIdinSharedPref(getApplicationContext());
                        // Navigate to Home screen
                        navigateMainMenue();
                    } 
                    // Else display error message
                    else{
                       
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }

	                } catch (JSONException e) {
	                e.printStackTrace();
	                }
	               catch (Exception e) {
	                e.printStackTrace();
	               }
	             }

	         else{  
	        	 
	        	 Toast.makeText(getApplicationContext(), "We have some problem in processing request, try again.", Toast.LENGTH_LONG).show();
	               
	            }

	    }
		}

// Store RegId  entered by User in SharedPref
	private void storeRegIdinSharedPref(Context context) {
			SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(Constants.IS_LOGIN, true);
			editor.commit();
			

		}
		
		
	}
    
    
    
    
    
    
    
    
    

