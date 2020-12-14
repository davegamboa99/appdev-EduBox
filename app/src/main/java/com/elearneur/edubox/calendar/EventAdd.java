package com.elearneur.edubox.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elearneur.edubox.R;
import com.elearneur.edubox.notifications.helper.ReminderNotification;

import java.util.Calendar;

//import com.google.gson.*;

import java.io.IOException;
import java.util.TreeSet;

public class EventAdd extends AppCompatActivity {
    private com.elearneur.edubox.calendar.Calendar cal;
    String evt_title, evt_date, evt_time, evt_type, evt_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        createNotificationChannel(); //for notifications

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
            toolbar.setTitle("Edit Event");
            add.setText("Edit");
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

        cal = (com.elearneur.edubox.calendar.Calendar) getIntent().getSerializableExtra("calendar");

        // set the message below the add/edit button
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

                Float evt_duration;
                evt_title = title.getText().toString();
                evt_date = date.getText().toString();
                evt_time = time.getText().toString();
                int type = type_spinner.getSelectedItemPosition();

                // check the required fields
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

                // gets the spinner string value
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

                //check if duration is empty
                String evt_duration_string = duration.getText().toString();
                if ("".equals(evt_duration_string)) evt_duration_string = "0";

                evt_duration = Float.parseFloat(evt_duration_string);
                evt_note = note.getText().toString();
                evt = new CalEvent(evt_title, evt_date, evt_time, evt_type, evt_duration, evt_note);

                //for notifications

                setAlarm(DateTimeConverter(evt_date,evt_time));
                Log.d("Alarm", "Alarm Set"+DateTimeConverter(evt_date,evt_time));


                PCalendar pcal = deserializePCal();
                if (cal instanceof PCalendar){
                    if (activityType == 0){ // create
                        evt.setEventId(((PCalendar) cal).generateId());
                    } else { // (1) update
                        CalEvent calEvent = (CalEvent) getIntent().getSerializableExtra("event");
                        evt.setEventId(calEvent.getEventId());
                    }
                    pcal.addEvent(evt);  // add == update (tree set collection)
                } else {
                    GCalendar gcal = (GCalendar) cal;
                    evt.setCalendar(gcal.getId());

                    if (activityType == 0){ //create
                        postEvent(evt); //uses HttpUrlConnection; creates event online
                        getEventsFromServer(pcal.getGroup(gcal));
                    } else { // (1) update
                        CalEvent calEvent = (CalEvent) getIntent().getSerializableExtra("event");
                        evt.setEventId(calEvent.getEventId()); // updates the event's id of the created event evt
                        putEvent(evt); //uses HttpUrlConnection; updates the event found online
                        pcal.getGroup(gcal).addEvent(evt); // updates the event found offline
                    }
                }
                reserializePCal(pcal);
                finish();
            }
        });
    }

    private PCalendar deserializePCal(){
        PCalendar pcal = null;
        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pcal;
    }

    private void reserializePCal(PCalendar pcal){
        try {
            pcal.saveCalendar(EventAdd.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putEvent(CalEvent evt){
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

    private void postEvent(CalEvent evt){
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
    }

    public void getEventsFromServer(GCalendar cal){
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                CalEvent[] evts = null;
                try {
                    evts = JSONParser.getEvents(cal.getId());
                } catch (IOException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (evts != null){
                    for (CalEvent evt : evts){
                        cal.addEvent(evt);
                    }
                }
            }
        });
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), ReminderNotification.class);
        intent.putExtra("eventTitle",evt_title);
        intent.putExtra("eventContent",evt_type);
        intent.putExtra("eventInfo",evt_note);
        intent.putExtra("eventTime",evt_time);
        intent.putExtra("eventDate",evt_date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
        }

    }

    private  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notification";
            String description = "test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private long DateTimeConverter(String date, String time){
        Calendar cal = Calendar.getInstance();

        //date "YYYY-MM-DD"
        String[] parts = date.split("-", 3);
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1;
        int dayOfMonth = Integer.parseInt(parts[2]);

        //time "HH:MM AA"
        parts = time.split(":", 2);
        int hour = Integer.parseInt(parts[0]);
        parts = parts[1].split(" ", 2);
        int minute = Integer.parseInt(parts[0]);
        if (parts[1].equals("PM")){
            if (hour != 12) hour += 12;
        } else {
            if (hour == 12) hour = 0;
        }

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        return cal.getTimeInMillis();
    }

}