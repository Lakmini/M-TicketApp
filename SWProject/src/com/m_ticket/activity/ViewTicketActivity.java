package com.m_ticket.activity;

import java.util.ArrayList;

import com.example.m_ticket.R;
import com.m_ticket.bean.Ticket;
import com.m_ticket.customcomponent.TicketListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewTicketActivity extends Activity {
	private ArrayList<Ticket> ticketlist=new ArrayList<Ticket> ();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewticket);
		ticketlist=(ArrayList<Ticket>) getIntent().getSerializableExtra("ticketList");
		ListView  listView = (ListView) findViewById(R.id.list);
		TicketListAdapter adapter=new TicketListAdapter(this, R.layout.ticket_single_row, ticketlist);
		listView.setAdapter(adapter);
		 
		 
	}

	
	
}
