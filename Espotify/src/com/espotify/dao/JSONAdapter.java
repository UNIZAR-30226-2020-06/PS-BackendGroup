package com.espotify.dao;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class JSONAdapter {
	public static JSONObject parsarJSON(HttpServletRequest request) {
		JSONObject peticionJSON = null;
		
	     try {
	    	 StringBuilder sb = new StringBuilder();
		     String s;
		     
			while ((s = request.getReader().readLine()) != null) {
				 sb.append(s);
			 }
			
			 String parseJSON = sb.toString();
			 peticionJSON = new JSONObject(parseJSON);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return peticionJSON;
	}
	
	public static String obtenerParametros(HttpServletRequest request) {
		
        return "";
	}
	
}
