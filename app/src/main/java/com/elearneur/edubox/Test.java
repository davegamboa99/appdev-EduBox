package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Test extends AppCompatActivity {
    private  List<Event> events;
    private List<TemporaryItem> list;

    RecyclerView calendarItemRecyclerView;
    RecyclerView.LayoutManager calendarItemLayoutManager;
    RecyclerView.Adapter calendarItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        events = new ArrayList<Event>();
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false));

        Log.d("size", "onBindViewHolder: "+events.size());
        list = new ArrayList<TemporaryItem>();
        list.add(new TemporaryItem("7:35 AM",events,events.size()));
        list.add(new TemporaryItem("8:35 AM",events,events.size()));
        list.add(new TemporaryItem("9:35 AM",events,events.size()));
        list.add(new TemporaryItem("10:35 AM",events,events.size()));

        calendarItemRecyclerView = findViewById(R.id.eventCalendarList);
        calendarItemRecyclerView.setHasFixedSize(true);

        calendarItemLayoutManager = new LinearLayoutManager(this);
        calendarItemRecyclerView.setLayoutManager(calendarItemLayoutManager);

        calendarItemAdapter = new CalendarItemAdapter(this,list);
        calendarItemRecyclerView.setAdapter(calendarItemAdapter);
    }
}