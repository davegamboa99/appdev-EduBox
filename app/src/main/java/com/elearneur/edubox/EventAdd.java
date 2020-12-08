package com.elearneur.edubox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

//import com.google.gson.*;

import java.io.IOException;

public class EventAdd extends AppCompatActivity {
    private com.elearneur.edubox.Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        Toolbar toolbar;
        EditText title, date, time, duration, note;
        Spinner type_spinner = findViewById(R.id.event_input_type_spinner);
        Button add;
        TextView msg;
        DatePickerDialog.OnDateSetListener setDateListener;
        TimePickerDialog.OnTimeSetListener setTimeListener;
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        toolbar = findViewById(R.id.toolbar_addevent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.event_input_title);
        date = findViewById(R.id.event_input_date);
        time = findViewById(R.id.event_input_time);
        duration = findViewById(R.id.event_input_duration);
        note = findViewById(R.id.event_input_note);
        add = findViewById(R.id.event_btn_add);
        msg = findViewById(R.id.event_add_msg);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(EventAdd.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(typeAdapter);

        int activityType = (int) getIntent().getIntExtra("activity_type", 0);
        if (activityType==1){
            CalEvent evt = (CalEvent) getIntent().getSerializableExtra("event");
            if (evt != null){
                title.setText(evt.getTitle());
                date.setText(evt.getDate());
                time.setText(evt.getTime());
                duration.setText("" + evt.getDuration());
                switch(evt.getContentType()){
                    case "None":
                        type_spinner.setSelection(1);
                        break;
                    case "Text/Image":
                        type_spinner.setSelection(2);
                        break;
                    case "Video Stream":
                        type_spinner.setSelection(3);
                        break;
                    case "Video Call":
                        type_spinner.setSelection(4);
                        break;
                    case "Doc Collab":
                        type_spinner.setSelection(5);
                        break;
                    default:
                        type_spinner.setSelection(0);
                        break;
                }
                note.setText(evt.getNote());
            }
        }

        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                date.setText(String.format("%04d-%02d-%02d", year, month, dayOfMonth));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventAdd.this, setDateListener, year, month, dayOfMonth);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    datePickerDialog.show();
                } else {
                    datePickerDialog.hide();
                }
            }
        });

        setTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String amPm = "am";
                if (hourOfDay >= 12) amPm = "pm";
                if (hourOfDay >= 13){
                    hourOfDay -= 12;
                } else if (hourOfDay == 0){
                    hourOfDay = 12;
                }
                time.setText(String.format("%02d:%02d ", hourOfDay, minute) + amPm);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(EventAdd.this, setTimeListener, hourOfDay, minute, false);
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    timePickerDialog.show();
                } else {
                    timePickerDialog.hide();
                }
            }
        });

        cal = (com.elearneur.edubox.Calendar) getIntent().getSerializableExtra("calendar");

        String calName;
        if (cal instanceof PCalendar) calName = "Personal Calendar";
        else calName = ((GCalendar) cal).getGroupName();
        calName = "This event will be added under\n" + calName + ".";
        msg.setText(calName);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate an event
                CalEvent evt;
                String evt_title, evt_date, evt_time, evt_type, evt_note;
                Float evt_duration;
                evt_title = title.getText().toString();
                evt_date = date.getText().toString();
                evt_time = time.getText().toString();
                int type = type_spinner.getSelectedItemPosition();

                if ("".equals(evt_title)) {
                    Toast.makeText(EventAdd.this, "Title is required!", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(evt_date)){
                    Toast.makeText(EventAdd.this, "Date is required!", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(evt_time)){
                    Toast.makeText(EventAdd.this, "Time is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (type){
                    case 2:
                        evt_type = "Text/Image";
                        break;
                    case 3:
                        evt_type = "Video Stream";
                        break;
                    case 4:
                        evt_type = "Video Call";
                        break;
                    case 5:
                        evt_type = "Doc Collab";
                        break;
                    default:
                        evt_type = "None";
                        break;
                }

                String evt_duration_string = duration.getText().toString();
                if ("".equals(evt_duration_string)) evt_duration_string = "0";

                evt_duration = Float.parseFloat(evt_duration_string);
                evt_note = note.getText().toString();
                evt = new CalEvent(evt_title, evt_date, evt_time, evt_type, evt_duration, evt_note);

                if (cal instanceof PCalendar){
                    PCalendar pcal = null;
                    //load pcal ; to be moved outside this method
                    try {
                        pcal = PCalendar.loadCalendar(getApplicationContext());
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
                    } catch (ClassNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
                    }

                    if (activityType == 0){ // create
                        evt.setEventId(((PCalendar) cal).generateId());
                    } else {
                        CalEvent calEvent = (CalEvent) getIntent().getSerializableExtra("event");
                        if (calEvent!=null){
                            evt.setEventId(calEvent.getEventId());
                        }
                    }
                    System.out.println("-------------here-----------");
                    pcal.addEvent(evt);  // add == update (tree set collection)
                    System.out.println(evt);
                    System.out.println(cal);
                    try {
                        pcal.saveCalendar(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), PersonalCalendar.class));
                } else {
                    GCalendar gcal = (GCalendar) cal;
                    evt.setCalendar(gcal.getId());
                    if (activityType == 0){ //create
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONParser.postEvent(evt);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else { // (1) update
                        CalEvent calEvent = (CalEvent) getIntent().getSerializableExtra("event");
                        if (calEvent != null){
                            evt.setEventId(calEvent.getEventId());
                        }
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONParser.putEvent(evt);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                finish();
                startActivity(new Intent(getApplicationContext(), Groups.class));
            }
        });
    }
}