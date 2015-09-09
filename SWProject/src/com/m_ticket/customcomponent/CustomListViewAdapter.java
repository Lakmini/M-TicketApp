package com.m_ticket.customcomponent;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.m_ticket.R;
import com.m_ticket.bean.Train;
public class CustomListViewAdapter extends ArrayAdapter<Train>  {
	 Context context;
	 public  CustomListViewAdapter(Context context, int resourceId,List<Train> trains) {
	        super(context, resourceId, trains);
	        this.context = context;
	    }
	     
	    /*private view holder class*/
	    private class ViewHolder {
	        
	        TextView train_name_txt;
	        TextView type_txt;
	        TextView arrival_time_txt;
	        TextView nameTxt;
	    }
	     
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        Train trains = getItem(position);
	         
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.row_single, null);
	            holder = new ViewHolder();
	            holder.train_name_txt = (TextView) convertView.findViewById(R.id.train_name);
	            holder.type_txt = (TextView) convertView.findViewById(R.id.type);
	            holder.arrival_time_txt = (TextView) convertView.findViewById(R.id.arrival_time);
	            convertView.setTag(holder);
	        } else
	            holder = (ViewHolder) convertView.getTag();        
	        holder.train_name_txt.setText("Name:"+trains.getName());
	        holder.type_txt.setText("Type: "+trains.getType());
	        holder.arrival_time_txt.setText("Arrive at:"+trains.getArrival_time());
	         
	        return convertView;
	    }
}
