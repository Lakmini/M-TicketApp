package com.m_ticket.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	
	 private static Pattern pattern;
	    private static Matcher matcher;
	    //Email Pattern
	    private static final String EMAIL_PATTERN = 
	            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    private static final String PHONE_PATTERN="\\d{10}";
	    
	      //return true for Valid Email and false for Invalid Email
	     
	    public static boolean validate(String email) {
	        pattern = Pattern.compile(EMAIL_PATTERN);
	        matcher = pattern.matcher(email);
	        return matcher.matches();
	 
	    }
	    
	    public static boolean validatePhone(String phone)
	    {
	    	pattern=Pattern.compile(PHONE_PATTERN);
	    	matcher=pattern.matcher(phone);
	    	return matcher.matches();
	    }
	   // true for not null and false for null String object
	     
	    public static boolean isNotNull(String txt){
	        return txt!=null && txt.trim().length()>0 ? true: false;
	    }
	
	

}
