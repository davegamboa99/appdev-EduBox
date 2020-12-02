package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.elearneur.edubox.Adapter.CalendarItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonalCalendar extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton addevent;
    private TextView event001;
    private Dialog editevent;

    private List<Event> events;
    private List<TemporaryItem> list;

    RecyclerView calendarItemRecyclerView;
    RecyclerView.LayoutManager calendarItemLayoutManager;
    RecyclerView.Adapter calendarItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_calendar);

        toolbar = findViewById(R.id.toolbar_calendar);
        setSupportActionBar(toolbar);
        addevent = findViewById(R.id.addevent);
        //event001 = findViewById(R.id.event001);


        //daypicker
        NumberPicker dayPicker = findViewById(R.id.day_picker);
        // Set value
        dayPicker.setMaxValue(59);
        dayPicker.setMinValue(0);
        dayPicker.setValue(3);
        dayPicker.setFadingEdgeEnabled(true);
        dayPicker.setScrollerEnabled(true);
        dayPicker.setWrapSelectorWheel(true);

// OnClickListener
        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("as", "Click on current value");
            }
        });

// OnValueChangeListener
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("asda", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        //dummy data for eventlist
        events = new ArrayList<Event>();
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true,true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false,true));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false,true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false,true));

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


        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.groups){
            Intent intent = new Intent(getApplicationContext(), Groups.class);
            startActivity(intent);
        }
        return true;
    }


}