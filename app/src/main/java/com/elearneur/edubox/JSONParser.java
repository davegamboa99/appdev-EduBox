package com.elearneur.edubox;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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
        conn.disconnect();
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
        int responsecode = conn.getResponseCode();
        if (responsecode != 201) throw new RuntimeException("HttpResponseCode: " + responsecode);
        conn.disconnect();

        return responsecode==201;
    }

    public static CalEvent[] getEvents(int calendarId) throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/?calendar=";
        urlString += calendarId;
        urlString += "&format=json";
        String json = getJSON(urlString);
        // System.out.println("JSON = " + json);
        CalEvent[] events = gson.fromJson(json, CalEvent[].class);
        return events;
    }

    public static boolean postEvent(CalEvent event) throws IOException {
        String json = gson.toJson(event.new JSONPostData());
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/?format=json";
        return postJSON(json, urlString);
    }

    public static GCalendar[] getGCalendars(int accountId) throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/?user=";
        urlString += accountId;
        urlString += "&format=json";
        String json = getJSON(urlString);
        // System.out.println(json);
        GCalendar[] cals = gson.fromJson(json, GCalendar[].class);
        return cals;
    }

    public static boolean postCalendar(GCalendar cal) throws IOException {
        String json = gson.toJson(cal.new JSONPostData());
        // System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/?format=json";
        return postJSON(json, urlString);
    }

    public static Member[] getMembers(int calendarId) throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/membership/?calendar=";
        urlString += calendarId;
        urlString += "&format=json";
        String json = getJSON(urlString);
        // System.out.println(json);
        Member[] members = gson.fromJson(json, Member[].class);
        return members;
    }

    public static boolean addMember(GCalendar cal, Member member) throws IOException {  //post
        Member.JSONPostData data = member.new JSONPostData();
        data.setCalendar(cal.getInvitationCode());
        String json = gson.toJson(data);
        // System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/membership/?format=json";
        return postJSON(json, urlString);
    }
 }