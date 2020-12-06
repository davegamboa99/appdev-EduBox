package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.TreeSet;
import java.util.zip.Inflater;

public class Groups extends AppCompatActivity {
    PCalendar pcal;
    private boolean optionsAreVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        FloatingActionButton creategroup, joingroup, addgroup;

        creategroup = findViewById(R.id.creategroup);
        joingroup = findViewById(R.id.joingroup);
        addgroup = findViewById(R.id.addgroup);

        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupCreate.class);
                startActivity(intent);
                finish();
            }
        });

        joingroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupJoin.class);
                startActivity(intent);
            }
        });

        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsAreVisible){
                    creategroup.setVisibility(View.INVISIBLE);
                    joingroup.setVisibility(View.INVISIBLE);
                    optionsAreVisible = false;
                } else {
                    creategroup.setVisibility(View.VISIBLE);
                    joingroup.setVisibility(View.VISIBLE);
                    optionsAreVisible = true;
                }
            }
        });

        initPcal();
        loadGroups();
        savePcal();
    }

    private void initPcal(){
        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
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

    public void getGroupsFromServer(){
        if (pcal != null){
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int userId = getIntent().getIntExtra("userId", 0);
                        GCalendar[] gcals = JSONParser.getGCalendars(userId);
                        if (gcals != null){
                            for (GCalendar group : gcals){
                                pcal.addGroup(group);
                            }
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getEventsFromServer(){
        TreeSet<GCalendar> groups = pcal.getGroups();
        if (groups != null){
            for (GCalendar group : groups){
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CalEvent[] evts = null;
                        try {
                            evts = JSONParser.getEvents(group.getId());
                        } catch (IOException e){
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if (evts != null){
                            for (CalEvent evt : evts){
                                group.addEvent(evt);
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
        }
    }

    private void loadGroups(){
        LinearLayout groups_container = findViewById(R.id.groups_container);
        groups_container.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();

        getGroupsFromServer();
        getEventsFromServer();

        if (pcal != null){
            TreeSet<GCalendar> groups = pcal.getGroups();
            if (groups != null){
                //load group items ui
                for (GCalendar group : groups){
                    LinearLayout newView = (LinearLayout) this.getLayoutInflater().inflate(R.layout.groups_item, null);
                    Button item = newView.findViewById(R.id.groups_item_item);
                    item.setText(group.getGroupName());
                    // set item background tint
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
                            intent.putExtra("calendar", group);
                            startActivity(intent);
                            finish();
                        }
                    });
                    groups_container.addView(newView);
                }
            }
        }
    }
}