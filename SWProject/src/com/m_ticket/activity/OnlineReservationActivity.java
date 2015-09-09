package com.m_ticket.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.m_ticket.R;
import com.m_ticket.bean.Train;
import com.m_ticket.utils.Constants;


public class OnlineReservationActivity extends Activity implements OnItemSelectedListener{

	private EditText travel_date_txt;
	private AutoCompleteTextView source_txt;
	private AutoCompleteTextView destination_txt;
	private EditText number_of_tickets_txt;
	private EditText train_name_txt;
	private EditText arrival_time_txt;
	private Train train;
	private Bundle bundle;
	private String travel_date;
	private String source;
	private String destination;
	private String train_name;
	private String class_name;
	private Button paybtn;
	private String temp;
	private String arrival_time;
	private int  mHour, mMinute;
	private DatePickerDialog travel_date_picker;
	private TimePickerDialog tpd;
	private SimpleDateFormat dateFormatter;
	private String registrationId;
	private Spinner spinner;
	private String[] state = {"First", "Second"};
     String[] stations;
     private ArrayAdapter<String> adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlinereservation);
		SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
	    registrationId = prefs.getString(Constants.REG_ID, "");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		initialize();
		setDateTimeField();
		setTimeField();
	}
	
	public void initialize()
	{
		stations = getResources().getStringArray(R.array.stationList);
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,stations);
	    
		travel_date_txt=(EditText)findViewById(R.id.ed_date);
		travel_date_txt.setInputType(InputType.TYPE_NULL);
		travel_date_txt.requestFocus();
		source_txt=(AutoCompleteTextView)findViewById(R.id.ed_from);
		source_txt.setAdapter(adapter);
		source_txt.setThreshold(1);
		source_txt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				source=(String) parent.getItemAtPosition(position);
				
			}
			
		});
		
		destination_txt=(AutoCompleteTextView)findViewById(R.id.ed_To);
		destination_txt.setAdapter(adapter);
		destination_txt.setThreshold(1);
		destination_txt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				destination=(String) parent.getItemAtPosition(position);
			}
		});
		
		number_of_tickets_txt=(EditText)findViewById(R.id.ed_tickets);
		train_name_txt=(EditText)findViewById(R.id.ed_name);
		arrival_time_txt=(EditText) findViewById(R.id.ed_time);
		paybtn=(Button) findViewById(R.id.btnPay);
		train=(Train) getIntent().getSerializableExtra("train");
		
		bundle=getIntent().getExtras();
	    if(bundle!=null)
		{
			travel_date_txt.setText(bundle.getString("travel_date"));
			source=bundle.getString("source");
			source_txt.setText(source);
			destination=bundle.getString("destination");
			destination_txt.setText(destination);
			train_name_txt.setText(train.getName());
			arrival_time_txt.setText(train.getArrival_time());
		}
	   // Spinner element
       spinner = (Spinner) findViewById(R.id.spinner);
        
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,R.layout.doubleline_spinner, state);
        adapter_state.setDropDownViewResource(R.layout.doubleline_spinner);
        spinner.setPrompt("Class");
        spinner.setAdapter(adapter_state);
        spinner.setOnItemSelectedListener(this);
        

		paybtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				travel_date=travel_date_txt.getText().toString();
				temp= number_of_tickets_txt.getText().toString();
				train_name=train_name_txt.getText().toString();
				arrival_time=arrival_time_txt.getText().toString();
				payRequest();
				
				
			}
		});
		
	}

	
	 private void setDateTimeField() {
	        travel_date_txt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					 if(v == travel_date_txt) {
				            travel_date_picker.show();}
				}
			});
	        
	        
	        Calendar newCalendar = Calendar.getInstance();
	        travel_date_picker = new DatePickerDialog(this, new OnDateSetListener() {
	 
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	                Calendar newDate = Calendar.getInstance();
	                newDate.set(year, monthOfYear, dayOfMonth);
	                travel_date_txt.setText(dateFormatter.format(newDate.getTime()));
	            }
	 
	        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
	        
	        travel_date_picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
	    }
	 private void setTimeField()
	 {
		 arrival_time_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 // Process to get Current Time
	            final Calendar c = Calendar.getInstance();
	            mHour = c.get(Calendar.HOUR_OF_DAY);
	            mMinute = c.get(Calendar.MINUTE);
	 
	            // Launch Time Picker Dialog
	            
	            tpd=new TimePickerDialog(OnlineReservationActivity.this,new TimePickerDialog.OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						
						 // Display Selected time in textbox
                      arrival_time_txt.setText(hourOfDay + ":" + minute+":00");
					}
				},mHour,mMinute,true);
	          

	            tpd.show();
				
			}
		});
	 }

	 
	 private void payRequest()
	 {
		 if((!TextUtils.isEmpty(travel_date))&&(!TextUtils.isEmpty(source))&&(!TextUtils.isEmpty(destination))&&(!TextUtils.isEmpty(temp))&&(!TextUtils.isEmpty(train_name))&&(!TextUtils.isEmpty(class_name)))
		 {
			 new reservationHandler().execute();
		 }
		 else
		 {
			 Toast.makeText(getApplicationContext(),
						"Please fill the form, don't leave any field blank",
						Toast.LENGTH_LONG).show();
		 }
	 }
	 
	 private class reservationHandler extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			   String response1 = null;
		       

		           HttpClient httpclient = new DefaultHttpClient();
		           HttpPost  httppost = new HttpPost(Constants.URL + Constants.RESERVE);
		           
		           try {

		                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		               
		                nameValuePairs.add(new BasicNameValuePair("date",travel_date));
		                nameValuePairs.add(new BasicNameValuePair("source", source));
		                nameValuePairs.add(new BasicNameValuePair("destination", destination));
		                nameValuePairs.add(new BasicNameValuePair("train_name", train_name));
		                nameValuePairs.add(new BasicNameValuePair("class", class_name));
		                nameValuePairs.add(new BasicNameValuePair("time", arrival_time));
		                nameValuePairs.add(new BasicNameValuePair("count", temp));
		                nameValuePairs.add(new BasicNameValuePair("reg_Id", registrationId));
		                
		               
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
			if(result==null)
			{
				Toast.makeText(getApplicationContext(), "Please check your data connection!", Toast.LENGTH_LONG).show();
			}
			 
			
			else if(result.equals("invalid data"))
			{
				Toast.makeText(getApplicationContext(), "Please recheck your details!", Toast.LENGTH_LONG).show();
			}
			else if(result.equals("no sufficient seats"))
			{
				Toast.makeText(getApplicationContext(), "No sufficient seats!", Toast.LENGTH_LONG).show();
			}
			else if(result.equals("server error") || result.equals("connection failure"))
			{
				Toast.makeText(getApplicationContext(), "Please retry!", Toast.LENGTH_LONG).show();
			}
			else if(result.equals("success"))
			{
				Toast.makeText(getApplicationContext(), "Reservation successfully done!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(OnlineReservationActivity.this,GridActivity.class);
                startActivity(intent);
			}
		}
		
		
		 
	 }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		 spinner.setSelection(position);
		 class_name = spinner.getSelectedItem().toString();
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}



}
