package com.m_ticket.activity;

import java.util.ArrayList;

import com.example.m_ticket.R;
import com.m_ticket.bean.Train;
import com.m_ticket.customcomponent.CustomListViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/*this class generate the train list for a requested schedule*/
public class DynamicTrainListActivity extends Activity{


     private ArrayList<Train> trains;
     private String travel_date;
     private String source;
     private String destination;
     Bundle bundle;
     
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_train_list_layout);
		
		 trains=(ArrayList<Train>) getIntent().getSerializableExtra("trainList");
		 if(trains.isEmpty())
		 {
			 Toast.makeText(getApplicationContext(), "No train schedule for a entered destination on this day", Toast.LENGTH_LONG).show();
		 }
		 bundle=getIntent().getExtras();
		 travel_date=bundle.getString("travel_date");
		 source=bundle.getString("source");
		 destination=bundle.getString("destination");
		 
		 
	     ListView  listView = (ListView) findViewById(R.id.list);
         CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.row_single, trains);
         listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
            	@Override
            	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(trains.get(position).getAvailable_seats_first_class()>0 &&trains.get(position).getAvailable_seats_second_class()>0)
                    {
                    	Intent intent=new Intent(DynamicTrainListActivity.this,OnlineReservationActivity.class);
                    	intent.putExtra("train", trains.get(position));
                    	intent.putExtras(bundle);
                    	startActivity(intent);
                    }
                    else
                    {
                    	Toast.makeText(getApplicationContext(), "Sorry!! No seats are available !!", Toast.LENGTH_LONG).show();
                    }
                }
				
			});
      	   
	        
	        
	}
	
         

}
