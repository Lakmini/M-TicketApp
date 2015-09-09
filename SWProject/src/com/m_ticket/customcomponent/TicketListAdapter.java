package com.m_ticket.customcomponent;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.m_ticket.bean.Ticket;
import com.example.m_ticket.R;




public class TicketListAdapter extends ArrayAdapter<Ticket> {
	Context context;
	 public  TicketListAdapter(Context context, int resourceId,List<Ticket> ticketList) {
		 super(context, resourceId, ticketList);
	        this.context = context;
	    }
	     
	    /*private view holder class*/
	    private class ViewHolder {
	        
	        TextView ticket_id_txt;
	        TextView date_txt;
	        TextView train_name_txt;
	        TextView source_txt;
	        TextView destination_txt;
	        TextView class_txt;
	        TextView fare_txt;
	        TextView arrival_time_txt;
	        
	    }
	     
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        Ticket ticket = getItem(position);
	         
	        LayoutInflater mInflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.ticket_single_row, null);
	            holder = new ViewHolder();
	            holder.ticket_id_txt=(TextView) convertView.findViewById(R.id.ticket_id_txt);
	            holder.date_txt=(TextView) convertView.findViewById(R.id.date_txt);
	            holder.train_name_txt = (TextView) convertView.findViewById(R.id.train_name_txt);
	            holder.source_txt=(TextView) convertView.findViewById(R.id.source_txt);
	            holder.destination_txt=(TextView) convertView.findViewById(R.id.destination_txt);
	            holder.class_txt=(TextView) convertView.findViewById(R.id.class_txt);
	            holder.fare_txt=(TextView) convertView.findViewById(R.id.fare_txt);
	            holder.arrival_time_txt = (TextView) convertView.findViewById(R.id.arrival_time_txt);
	            convertView.setTag(holder);
	        } else
	        {  holder = (ViewHolder) convertView.getTag();}
	           
	        holder.ticket_id_txt.setText("Ticket ID:"+ticket.getTicket_id());
	        holder.date_txt.setText("Date:  "+ticket.getDate());
	        holder.train_name_txt.setText("Train :"+ticket.getTrain_name());
	        holder.source_txt.setText("Source: "+ticket.getSource());
	        holder.destination_txt.setText("Destination: "+ticket.getDestination());
	        holder.class_txt.setText("Class: "+ticket.getClass_name());
	        holder.fare_txt.setText("Fare : "+ticket.getFare());
	        holder.arrival_time_txt.setText("Arrive at :"+ticket.getArrival_time());
	        
	      
	         
	        return convertView;
	    }
}
