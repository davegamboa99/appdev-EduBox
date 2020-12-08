package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.Locale;
import java.util.TreeSet;

public class PersonalCalendar extends AppCompatActivity {
    public static PCalendar pcal;
    private Dialog editEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_calendar);

        Dates2 dates = new Dates2();
        ImageView nextMonth = findViewById(R.id.nextMonth);
        ImageView prevMonth = findViewById(R.id.prevMonth);
        TextView monthYear = findViewById(R.id.month_year);
        TextView dayWeek = findViewById(R.id.dayWeek);

        //daypicker
        NumberPicker dayPicker = findViewById(R.id.day_picker);
        // Set value
        initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
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
                dates.setCurrentDate(newVal);
                dayWeek.setText(dates.getCurrentDayOfWeek());
                System.out.println(dates);
            }
        });

        monthYear.setText(dates.getCurrentMonthYear());
        dayWeek.setText(dates.getCurrentDayOfWeek());
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.moveNextMonth();
                monthYear.setText(dates.getCurrentMonthYear());
                dayWeek.setText(dates.getCurrentDayOfWeek());
                initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
            }
        });
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.movePrevMonth();
                monthYear.setText(dates.getCurrentMonthYear());
                dayWeek.setText(dates.getCurrentDayOfWeek());
                initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
            }
        });

        Toolbar toolbar;
        FloatingActionButton addevent;

        toolbar = findViewById(R.id.toolbar_calendar);
        setSupportActionBar(toolbar);
        addevent = findViewById(R.id.addevent);

        initPcal();

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                intent.putExtra("calendar", pcal);
                intent.putExtra("activity_type", 0);
                startActivity(intent);
                finish();
            }
        });

        editEvent = new Dialog(this);

        loadEvents();
        savePcal();
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
            intent.putExtra("userId", pcal.getAccount().getId());
            startActivity(intent);
        }
        return true;
    }

    private void initDayPicker(NumberPicker picker, int min, int max, int current){
        picker.setMaxValue(max);
        picker.setMinValue(min);
        picker.setValue(current);
    }

    private void initPcal(){
        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
//            System.out.println(pcal);
        } catch (IOException e) {
            pcal = new PCalendar();
            Account acc = pcal.getAccount();
            acc.setId(1);
            acc.setUsername("Monching");
            try {
                pcal.saveCalendar(getApplicationContext());
            } catch (IOException ioException) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePcal(){
        if (pcal != null){
            try {
                pcal.saveCalendar(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPcalEvents(LinearLayout events_container, LayoutInflater inflater){
        TreeSet<CalEvent> evts = pcal.getEvents();
        if (evts != null) {
            for (CalEvent evt : evts){
                LinearLayout ll_time = (LinearLayout) inflater.inflate(R.layout.events_item_time, null);
                TextView evt_time = ll_time.findViewById(R.id.label_time);
                evt_time.setText(evt.getTime());
                if ("".equals(evt_time.getText())) evt_time.setText("All Day");

                LinearLayout ll_event = (LinearLayout) this.getLayoutInflater().inflate(R.layout.events_item_event, null);
                ll_event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.event_edit, null);

                        TextView event_title, event_date, event_time, event_type, event_speed, event_data, event_note;
                        event_title = rl.findViewById(R.id.event_title);
                        event_date = rl.findViewById(R.id.event_date);
                        event_time = rl.findViewById(R.id.event_time);
                        event_type = rl.findViewById(R.id.event_type);
                        event_speed = rl.findViewById(R.id.event_speed);
                        event_data = rl.findViewById(R.id.event_data);
                        event_note = rl.findViewById(R.id.event_note);
                        Button edit = rl.findViewById(R.id.edit);

                        event_title.setText("Title: " + evt.getTitle());
                        event_date.setText("Date: " + evt.getDate());
                        event_time.setText("Time: " + evt.getTime());
                        event_type.setText("Type: " + evt.getContentType());
                        event_speed.setText("Min. Speed Requirement: TBC"); //to be calculated
                        event_data.setText("Min. Data Consumption: TBC");
                        event_note.setText("Note: " + evt.getNote());

                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PersonalCalendar.this, EventAdd.class);
                                intent.putExtra("calendar", pcal);
                                intent.putExtra("event", evt);
                                intent.putExtra("activity_type", 1); // 0 for add, 1 for edit
                                startActivity(intent);
                                editEvent.dismiss();
                                finish();
                            }
                        });

                        editEvent.setContentView(rl);
                        editEvent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        editEvent.show();
                    }
                });
                TextView evt_title = ll_event.findViewById(R.id.label_title);
                TextView evt_calName = ll_event.findViewById(R.id.label_calName);
                evt_title.setText(evt.getTitle());
                evt_calName.setText("Personal");

                events_container.addView(ll_time);
                events_container.addView(ll_event);
            }
        }
    }

    private void loadGcalEvents(LinearLayout events_container, LayoutInflater inflater){
        TreeSet<GCalendar> gcals = pcal.getGroups();
        TreeSet<CalEvent> evts;
        if (gcals != null){
            for (GCalendar gcal : gcals){
                evts = gcal.getEvents();
                if (evts != null){
                    for (CalEvent evt : evts){
                        LinearLayout ll_time = (LinearLayout) inflater.inflate(R.layout.events_item_time, null);
                        TextView evt_time = ll_time.findViewById(R.id.label_time);
                        evt_time.setText(evt.getTime());
                        if ("".equals(evt_time.getText())) evt_time.setText("All Day");

                        LinearLayout ll_event = (LinearLayout) inflater.inflate(R.layout.events_item_event, null);
                        ll_event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.event_edit, null);

                                TextView event_title, event_date, event_time, event_type, event_speed, event_data, event_note;
                                event_title = rl.findViewById(R.id.event_title);
                                event_date = rl.findViewById(R.id.event_date);
                                event_time = rl.findViewById(R.id.event_time);
                                event_type = rl.findViewById(R.id.event_type);
                                event_speed = rl.findViewById(R.id.event_speed);
                                event_data = rl.findViewById(R.id.event_data);
                                event_note = rl.findViewById(R.id.event_note);
                                Button edit = rl.findViewById(R.id.edit);

                                event_title.setText("Title: " + evt.getTitle());
                                event_date.setText("Date: " + evt.getDate());
                                event_time.setText("Time: " + evt.getTime());
                                event_type.setText("Type: " + evt.getContentType());
                                event_speed.setText("Min. Speed Requirement: TBC"); //to be calculated
                                event_data.setText("Min. Data Consumption: TBC");
                                event_note.setText("Note: " + evt.getNote());

                                edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(PersonalCalendar.this, EventAdd.class);
                                        intent.putExtra("calendar", gcal);
                                        intent.putExtra("event", evt);
                                        intent.putExtra("activity_type", 1); // 0 for add, 1 for edit
                                        startActivity(intent);
                                        editEvent.dismiss();
                                        finish();
                                    }
                                });

                                editEvent.setContentView(rl);
                                editEvent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                editEvent.show();
                            }
                        });
                        TextView evt_title = ll_event.findViewById(R.id.label_title);
                        TextView evt_calName = ll_event.findViewById(R.id.label_calName);
                        evt_title.setText(evt.getTitle());
                        evt_calName.setText(gcal.getGroupName());

                        events_container.addView(ll_time);
                        events_container.addView(ll_event);
                    }
                }
            }
        }
    }

    private void loadEvents(){
        LinearLayout events_container = findViewById(R.id.events_container);
        events_container.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();

        loadPcalEvents(events_container, inflater);
        loadGcalEvents(events_container, inflater);
    }
}