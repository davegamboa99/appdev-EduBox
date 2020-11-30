package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.TreeSet;

public class GroupCalendar extends AppCompatActivity {
    private GCalendar gcal;
    private Dialog editEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        gcal = (GCalendar) getIntent().getSerializableExtra("calendar");
        if (gcal == null) {
            Toast.makeText(getApplicationContext(), "GCAL is NULL!", Toast.LENGTH_LONG).show();
            finish();
        }

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar_group_calendar);
        setSupportActionBar(toolbar);
        if (gcal!=null) toolbar.setTitle(gcal.getGroupName().toUpperCase());

        editEvent = new Dialog(this);

        FloatingActionButton addgroupevent;

        addgroupevent = findViewById(R.id.addgroupevent);

        addgroupevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                intent.putExtra("calendar", gcal);
                startActivity(intent);
            }
        });

        loadEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share){
            Toast.makeText(getApplicationContext(), "Invitation code copied!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leave){
            try {
                PCalendar pcal = null;
                pcal = PCalendar.loadCalendar(getApplicationContext());
                pcal.removeGroup(gcal);
                pcal.saveCalendar(getApplicationContext());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
            } catch (ClassNotFoundException e) {
                Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
            }
            finish();
            startActivity(new Intent(getApplicationContext(), Groups.class));
            Toast.makeText(getApplicationContext(), "You leave the group!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void loadEvents(){
        LinearLayout events_container = findViewById(R.id.events_container);
        events_container.removeAllViews();

        TreeSet<CalEvent> evts = gcal.getEvents();
        for (CalEvent evt : evts){
            LinearLayout ll_time = (LinearLayout) this.getLayoutInflater().inflate(R.layout.events_item_time, null);
            TextView evt_time = ll_time.findViewById(R.id.label_time);
            evt_time.setText(evt.getTime());
            if ("".equals(evt_time.getText())) evt_time.setText("All Day");

            LinearLayout ll_event = (LinearLayout) this.getLayoutInflater().inflate(R.layout.events_item_event, null);
            ll_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEvent.setContentView(R.layout.event_edit);
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