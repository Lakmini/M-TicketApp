package com.m_ticket.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Train implements Serializable{

	private String name;
	private String type;
	private int available_seats_first_class;
	private int available_seats_second_class;
	private String arrival_time;
	private String depature_time;
	
	

	public String getName() {
		return name;
	}

	public String getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	public String getDepature_time() {
		return depature_time;
	}

	public void setDepature_time(String depature_time) {
		this.depature_time = depature_time;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAvailable_seats_first_class() {
		return available_seats_first_class;
	}

	public void setAvailable_seats_first_class(int available_seats_first_class) {
		this.available_seats_first_class = available_seats_first_class;
	}

	public int getAvailable_seats_second_class() {
		return available_seats_second_class;
	}

	public void setAvailable_seats_second_class(int available_seats_second_class) {
		this.available_seats_second_class = available_seats_second_class;
	}

	
	
	
	
}
