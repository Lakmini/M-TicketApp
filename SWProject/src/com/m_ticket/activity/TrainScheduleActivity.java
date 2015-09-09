package com.m_ticket.activity;

import java.io.IOException;
//import java.sql.Date;
import java.text.ParseException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.m_ticket.R;
import com.m_ticket.bean.Train;
import com.m_ticket.utils.Constants;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class TrainScheduleActivity  extends Activity{
	private EditText travel_date_txt;
	private AutoCompleteTextView source_txt;
	private AutoCompleteTextView destination_txt;
	private Button submitbtn;
	private String travel_date;
	private String source;
	private String destination;
	private ArrayList<Train> trains;
	private DatePickerDialog travel_date_picker;
	private SimpleDateFormat dateFormatter;
	private Bundle bundle;
	 private ArrayAdapter<String> adapter;
	 String[] stations;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainschedule);
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		try {
			initialize();
			setDateTimeField();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	}

	
	private void initialize() throws ParseException
	{
		stations = getResources().getStringArray(R.array.stationList);
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,stations);
		travel_date_txt=(EditText)findViewById(R.id.date_edtxt);
		travel_date_txt.setInputType(InputType.TYPE_NULL);
		travel_date_txt.requestFocus();
		source_txt=(AutoCompleteTextView) findViewById(R.id.source_edtext);
		source_txt.setAdapter(adapter);
		source_txt.setThreshold(1);
		source_txt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				source=(String) parent.getItemAtPosition(position);
				
			}
		});
		
		destination_txt=(AutoCompleteTextView) findViewById(R.id.ed_to);
		destination_txt.setAdapter(adapter);
		destination_txt.setThreshold(1);
		destination_txt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				destination=(String) parent.getItemAtPosition(position);
				
			}
			
		});
		submitbtn=(Button) findViewById(R.id.btnSearch);
		
		submitbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				travel_date=travel_date_txt.getText().toString();
				submit_request();
				
			}
		});
		
		
	}
	private void submit_request()
	{
		if((!TextUtils.isEmpty( travel_date))&&(!TextUtils.isEmpty(source))&&(!TextUtils.isEmpty( destination)))
		{
			//when all the text fields are filled
			new SubmitHandler().execute();
		}
		else
		{
			//if not
			Toast.makeText(getApplicationContext(),"Please fill the form, don't leave any field blank",Toast.LENGTH_LONG).show();
		}
		
	}
	private class SubmitHandler extends AsyncTask<String, Void, String>{

		
		@Override
		protected String doInBackground(String... params) {

	        String response1 = null;
	        

	           HttpClient httpclient = new DefaultHttpClient();
	           HttpPost  httppost = new HttpPost(Constants.URL + Constants.SUBMIT);
	           
	           try {

	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	               
	                nameValuePairs.add(new BasicNameValuePair("journeyDate",travel_date));
	                nameValuePairs.add(new BasicNameValuePair("source",source));
	                nameValuePairs.add(new BasicNameValuePair("destination",destination));

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
			trains=new ArrayList<Train>();
			 if(result!=null){

		            try {
		            	// JSON Object
		            	JSONArray jsonarray=new JSONArray(result);
		            	for(int i=0;i<jsonarray.length();i++)
				        {
		            		    JSONObject obj = jsonarray.getJSONObject(i);
								
		            		    Train train=new Train();
		            		    train.setName(obj.getString("name"));
		            		    train.setType(obj.getString("type"));
		            		    train.setArrival_time(obj.getString("arrival_time"));
		            		    train.setDepature_time(obj.getString("depature_time"));
		            		    train.setAvailable_seats_first_class(obj.getInt("available_seat_first_class"));
		            		    train.setAvailable_seats_second_class(obj.getInt("availabale_seat_second_class"));
								trains.add(train);
						}
		            	Intent intent=new Intent(TrainScheduleActivity.this,DynamicTrainListActivity.class);
     	                intent.putExtra("trainList", (ArrayList<Train>) trains);
      	            	bundle=new Bundle();
      	            	bundle.putString("travel_date", travel_date);
      	            	bundle.putString("source", source);
      	            	bundle.putString("destination", destination);
      	            	intent.putExtras(bundle);
		            	startActivity(intent);
		            
		                } catch (JSONException e) {
		                e.printStackTrace();
		                }
		               catch (Exception e) {
		                e.printStackTrace();
		               }
		             }

		         else{  
		        	 
		        	 Toast.makeText(getApplicationContext(), "No train schedule for a entered destination on this day", Toast.LENGTH_LONG).show();
		               
		            }
                  }
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

	 
	
	
}
