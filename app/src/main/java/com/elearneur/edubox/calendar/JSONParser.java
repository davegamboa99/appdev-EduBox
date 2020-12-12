package com.elearneur.edubox.calendar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


import com.google.gson.*;

public class JSONParser {
    private static final String IP = "192.168.42.170"; //"192.168.254.101";
    private static final Gson gson = new Gson();

    private static String getJSON(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        String inline = "";
        if (responsecode == 200) {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()){
                inline += sc.nextLine();
            }
            sc.close();
        } else {
            System.out.println("HttpResponseCode: " + responsecode);
        }

        conn.disconnect();
        return inline;
    }
    private static String postJSON(String json, String urlString) throws IOException {
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
        String inline = "";
        if (responsecode == 201) {
            Scanner sc = new Scanner(conn.getInputStream());
            while (sc.hasNext()){
                inline += sc.nextLine();
            }
            sc.close();
        }

        conn.disconnect();

        return inline;
    }
    private static String putJSON(String json, String urlString) throws IOException {
        String urlParameters  = json;
        byte[] postData       = urlParameters.getBytes("UTF-8");
        int    postDataLength = postData.length;
        String request        = urlString;
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "PUT" );
        conn.setRequestProperty( "Content-Type", "application/json");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        }
        int responsecode = conn.getResponseCode();

        conn.disconnect();

        return "Response = " + responsecode;
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

    public static String postEvent(CalEvent event) throws IOException {
        String json = gson.toJson(event.new JSONPostPutData());
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/?format=json";
        return postJSON(json, urlString);
    }

    public static String putEvent(CalEvent event) throws IOException {
        String json = gson.toJson(event.new JSONPostPutData());
//        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/";
        urlString += event.getEventId();
        urlString += "/?format=json";
//        System.out.println("URL = " + urlString);
        return putJSON(json, urlString);
    }

    public static String putDeleteEvent(CalEvent event) throws IOException {
        CalEvent.JSONPutDeleteData data = event.new JSONPutDeleteData();
        data.setIsDeleted(true);
        String json = gson.toJson(data);
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/events/";
        urlString += event.getEventId();
        urlString += "/?format=json";
        System.out.println("URL = " + urlString);
        return putJSON(json, urlString);
    }

    public static GCalendar[] getGCalendars(int accountId) throws IOException {
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/?user=";
        urlString += accountId;
        urlString += "&format=json";
        // System.out.println("URL = " + urlString);
        String json = getJSON(urlString);
        // System.out.println(json);
        GCalendar[] cals = gson.fromJson(json, GCalendar[].class);
        return cals;
    }

    public static String postCalendar(GCalendar cal, Account account) throws IOException {
        String json = gson.toJson(cal.new JSONPostPutData());
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/?format=json";
        String response = postJSON(json, urlString);
        System.out.println("Response = " + response);
        GCalendar gcal = gson.fromJson(response, GCalendar.class);
        addMember(gcal, account.toMemberData());
        return response;
    }

    public static String putCalendar(GCalendar cal) throws IOException {
        GCalendar.JSONPostPutData data = cal.new JSONPostPutData();
        String json = gson.toJson(data);
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/calendars/";
        urlString += cal.getId();
        urlString += "/?format=json";
        return putJSON(json, urlString);
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

    public static String addMember(GCalendar cal, Member member) throws IOException {  //post
//        Member.JSONPostData data = member.new JSONPostData();
//        data.setCalendar(cal.getId());
//        String json = gson.toJson(data); //does not work; generates GCalendar.JSONPostData
        String json = "{\"calendar\": " + cal.getId() + ", \"account\": " + member.getId() + "}";
        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/membership/?format=json";
        return postJSON(json, urlString);
    }

    public static String removeMember(GCalendar cal, Member member) throws IOException { //to be edited
//        String json = "{\"calendar\": " + cal.getId() + ", \"account\": " + member.getId() + "}";
//        System.out.println("JSON = " + json);
        String urlString = "http://";
        urlString += IP;
        urlString += ":8000/membership/?calendar=";
        urlString += cal.getId();
        urlString += "&account=";
        urlString += member.getId();
        urlString += "&format=json";
        String json = getJSON(urlString);
        Member.JSONGetData[] data = gson.fromJson(json, Member.JSONGetData[].class);

        String s = "";
        if (data[0] != null){
            urlString = "http://";
            urlString += IP;
            urlString += ":8000/membership/";
            urlString += data[0].getId();
            urlString += "/?format=json";
            json = getJSON(urlString);
            Member.JSONGetData datum = gson.fromJson(json, Member.JSONGetData.class);
            if (datum != null){
                datum.setIsDeleted(true);
                json = gson.toJson(datum);
                s = putJSON(json, urlString);
            }
        }


        return s;
    }
 }