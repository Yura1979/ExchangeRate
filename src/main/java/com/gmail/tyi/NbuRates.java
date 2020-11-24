package com.gmail.tyi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gmail.tyi.dao.DbHelper;

public class NbuRates {
	
	private Map<String, BigDecimal> nbRates = new HashMap<>();
	private DbHelper dbHelper;
	
	private NbuRates() {
		dbHelper = DbHelper.getInstance();
	}
	
	public static NbuRates getInstance() {
		return new NbuRates();
	}
	
	public BigDecimal getRateByDate(String date) {
		return nbRates.get(date);
	}
	
	public void addRateByDays(Calendar cal, String urlString, int numOfDays) {
		
		for (int i = 0; i < numOfDays; i++) {
			String result = null;
			String date = cal.get(Calendar.DAY_OF_MONTH) + "." + cal.get(Calendar.MONTH) + "." + cal.get(Calendar.YEAR);
			String urlStringWithDate = urlString + date;
			
			URL url;
			try {
				url = new URL(urlStringWithDate);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
				result = br.readLine();
				
				br.close();
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JSONObject jsonObj = new JSONObject(result);
	    	JSONObject jsonUSD = null;
	    	
	    	JSONArray jsonArray = (JSONArray) jsonObj.get("exchangeRate");
	    	for (Object object : jsonArray) {
				if (object.toString().contains("USD")) {
					jsonUSD = (JSONObject) object;
					break;
				}
			}
	    	
	    	nbRates.put((String) jsonObj.get("date"), (BigDecimal) jsonUSD.get("purchaseRateNB"));
	    	dbHelper.addRate((String) jsonObj.get("date"), (BigDecimal) jsonUSD.get("purchaseRateNB"));
	    	cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}

	@Override
	public String toString() {
		return "NbuRates [nbRates=" + nbRates + "]";
	}

	public DbHelper getDbHelper() {
		// TODO Auto-generated method stub
		return dbHelper;
	}

	
	
}
