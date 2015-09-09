package com.m_ticket.customcomponent;
// 2015.06.20

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_ticket.R;



public  class Adapter extends BaseAdapter {
	
	
	private Context context;
	 public Integer[] imageId = {
			    R.drawable.counter128,
	            R.drawable.tickets_256, 
	            R.drawable.schedule,
	            R.drawable.msg128,
	          	            
	    };
	 private final static String[] classes ={
			 "Online Reservation",
			 "View Tickets",
			 "Train Schedule",
			 "Notifications",
			
			 
			 };
	
	  // Constructor
	    public Adapter(Context c){
	        context = c;
	    }

		@Override
		public int getCount() {
		
			return imageId.length;
		}

		@Override
		public Object getItem(int position) {
			
			return imageId[position];
		}
		public static String getClass(int position)
		{
			return classes[position];
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			 View grid;
	            LayoutInflater inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	            if (convertView == null) {
	 
	                grid = new View(context);
	                grid = inflater.inflate(R.layout.gridsingle, null);
	                TextView textView = (TextView) grid.findViewById(R.id.grid_text);
	                ImageView imageview = (ImageView)grid.findViewById(R.id.grid_image);
	                textView.setText(classes[position]);
	                imageview.setImageResource(imageId[position]);
	            } else {
	                grid = (View) convertView;
	            }
	 
            return grid;
			
		}
	}
