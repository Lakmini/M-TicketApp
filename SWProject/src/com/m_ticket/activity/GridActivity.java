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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.m_ticket.R;
import com.m_ticket.bean.Ticket;
import com.m_ticket.customcomponent.Adapter;
import com.m_ticket.utils.Constants;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


public class GridActivity extends BaseActivity  {
	private Bundle bundle;
	private String regId;
	private ArrayList<Ticket>ticketList;
	private String registrationId;
	Editor editor;
	
	public Class[] activities={OnlineReservationActivity.class,ViewTicketActivity.class,TrainScheduleActivity.class,NotificationActivity.class,MainActivity.class};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridlayout);
		if(bundle!=null)
		{
			bundle=getIntent().getExtras();
			regId=bundle.getString("regId");
		}
		 SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
         registrationId = prefs.getString(Constants.REG_ID, "");
         
         editor = prefs.edit();
		  GridView gridView = (GridView) findViewById(R.id.grid_view);
		
	        gridView.setAdapter(new Adapter(this));
	        
	        gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					 String activityName = Adapter.getClass(position);    
					  if (activityName.equals("Online Reservation") ){
			                Intent intent = new Intent(GridActivity.this, OnlineReservationActivity.class);
			                startActivity(intent);
			            }  
					 
					  if (activityName.equals("View Tickets") ){
						
			               getTicketDetails();
			            }  
					  if (activityName.equals("Train Schedule") ){
			                Intent intent = new Intent(GridActivity.this, TrainScheduleActivity.class);
			                startActivity(intent);
			            }  
					  if (activityName.equals("Notifications") ){
			                Intent intent = new Intent(GridActivity.this, NotificationActivity.class);
			                intent.putExtra("regId", regId);
			                startActivity(intent);
			            }  

				}
	        	
	        	
			});
	}
	
	public void getTicketDetails()
	{
		new ticketDetailsRequestHandler().execute();
	}
	
	private class ticketDetailsRequestHandler extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {

	           String response1 = null;
	          

	           HttpClient httpclient = new DefaultHttpClient();
	           HttpPost  httppost = new HttpPost(Constants.URL + Constants.TICKET_DATA);
	           
	           try {

	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                nameValuePairs.add(new BasicNameValuePair("regId",registrationId));
	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity httpEntity = response.getEntity();
	                if(httpEntity != null){
	                	response1 = EntityUtils.toString(httpEntity);
	                }	
	                } 
	          catch (ClientProtocolException e) {
	                 e.printStackTrace();
	                } 
	          catch (IOException e) {
	                e.printStackTrace();
	               }
		     return response1;
			
		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			ticketList=new ArrayList<Ticket>();
			if(result!=null)
			{
				
				try {
					JSONArray jsonarray=new JSONArray(result);
					for(int i=0;i<jsonarray.length();i++)
					{
						JSONObject obj = jsonarray.getJSONObject(i);
						Ticket ticket=new Ticket();
						ticket.setTicket_id(obj.getString("ticket_id"));
						ticket.setDate(obj.getString("date"));
						ticket.setClass_name(obj.getString("class_name"));
						ticket.setDestination(obj.getString("destination"));
						ticket.setSource(obj.getString("source"));
						ticket.setFare(obj.getInt("fare"));
						ticket.setJourney_id(obj.getString("journey_id"));
						ticket.setTrain_name(obj.getString("train_name"));
						ticket.setArrival_time(obj.getString("arrival_time"));
						ticketList.add(ticket);
						Intent intent = new Intent(GridActivity.this, ViewTicketActivity.class);
						intent.putExtra("ticketList", (ArrayList<Ticket>) ticketList);
			            startActivity(intent);
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				
			}
			else
			{
				 Toast.makeText(getApplicationContext(), "No tickets to show", Toast.LENGTH_LONG).show();
			}
			
		}
	
	}

	
	
	
	
	

}
