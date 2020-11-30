package com.elearneur.edubox;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


import com.google.gson.*;

public class JSONParser {
    private static final String IP = "192.168.254.101";
    private static final Gson gson = new Gson();

    private static String getJSON(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        String inline = "";
        if (responsecode != 200) throw new RuntimeException("HttpResponseCode: " + responsecode);
        else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()){
                inline += sc.nextLine();
            }
            sc.close();
        }
        return inline;
    }
    private static boolean postJSON(String json, String urlString) throws IOException {
        String urlParameters  = json;
        byte[] postData       = urlParameters.getBytes("UTF-8");
        int    postDataLength = postData.length;
        String request        = urlString;
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/json"); 
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        }
        int response = conn.getResponseCode();
        // System.out.println("response = " + response);
        conn.disconnect();

        return response==201;
    }

    public static CalEvent[] getEvents() throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/?format=json";
        String json = getJSON(urlString);
        // System.out.println("JSON = " + json);
        CalEvent[] events = gson.fromJson(json, CalEvent[].class);
        return events;
    }

    public static boolean postEvent(CalEvent event) throws IOException {
        String json = gson.toJson(event);
        // System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/?format=json";
        return postJSON(json, urlString);
    }

    public static GCalendar[] getGCalendars() throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/?format=json";
        String json = getJSON(urlString);
        GCalendar[] cals = gson.fromJson(json, GCalendar[].class);
        return cals;
    }
 }