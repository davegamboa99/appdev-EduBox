package com.elearneur.edubox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.gson.*;

import java.io.IOException;

public class EventAdd extends AppCompatActivity {
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        cal = (Calendar) getIntent().getSerializableExtra("calendar");

        Toolbar toolbar;
        EditText title, date, time, type, duration, note;
        Button add;
        TextView msg;

        toolbar = findViewById(R.id.toolbar_addevent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.event_input_title);
        date = findViewById(R.id.event_input_date);
        time = findViewById(R.id.event_input_time);
        type = findViewById(R.id.event_input_type);
        duration = findViewById(R.id.event_input_duration);
        note = findViewById(R.id.event_input_note);
        add = findViewById(R.id.event_btn_add);
        msg = findViewById(R.id.event_add_msg);
        String calName;
        if (cal instanceof PCalendar) calName = "Personal Calendar";
        else calName = ((GCalendar) cal).getGroupName();
        calName = "This event will be added under\n" + calName + ".";
        msg.setText(calName);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int calId = getIntent().getIntExtra("calendar", -1);
                PCalendar pcal = null;
                GCalendar gcal;
                try {
                    pcal = PCalendar.loadCalendar(getApplicationContext());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
                }
                CalEvent evt;
                String evt_title, evt_date, evt_time, evt_type, evt_note;
                Float evt_duration;
                evt_title = title.getText().toString();
                evt_date = date.getText().toString();
                evt_time = time.getText().toString();
                evt_type = type.getText().toString();
                String evt_duration_string = duration.getText().toString();
                if ("".equals(evt_duration_string)) evt_duration_string = "0";
                evt_duration = Float.parseFloat(evt_duration_string);
                evt_note = note.getText().toString();
                evt = new CalEvent(evt_title, evt_date, evt_time, evt_type, evt_duration, evt_note);
                if (cal instanceof PCalendar){
                    pcal.addEvent(evt);
                    startActivity(new Intent(getApplicationContext(), PersonalCalendar.class));
                } else {
                    gcal = pcal.getGroup((GCalendar) cal);
                    gcal.addEvent(evt);
                    startActivity(new Intent(getApplicationContext(), Groups.class));
                }
                try {
//                    JSONParser.postEvent(evt); // to be posted
                    pcal.saveCalendar(getApplicationContext());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getApplicationContext(), pcal.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}