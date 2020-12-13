package com.elearneur.edubox.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elearneur.edubox.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.TreeSet;

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
                intent.putExtra("activity_type", 0); // 0 means create; 1 for edit
                startActivity(intent);
                creategroup.setVisibility(View.INVISIBLE);
                joingroup.setVisibility(View.INVISIBLE);
                optionsAreVisible = false;
            }
        });

        joingroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupJoin.class);
                startActivity(intent);
                creategroup.setVisibility(View.INVISIBLE);
                joingroup.setVisibility(View.INVISIBLE);
                optionsAreVisible = false;
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
    }

    @Override
    public void onResume(){
        super.onResume();
        initPcal();
        getGroupsFromServer();
        getEventsFromServer();
        loadGroups();
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
                            savePcal();
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    getEventsFromServer();
                }
            });
            thread1.start();
//            try {
//                thread1.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
                            savePcal();
                        }
                        if (group.equals(groups.last())){
                            //loadGroups(); // error
                            System.out.println("------------DONE----------");
                        }
                    }
                });
                thread2.start();
//                try {
//                    thread2.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private void loadGroups(){
        LinearLayout groups_container = findViewById(R.id.groups_container);
        groups_container.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();

//        getGroupsFromServer();
//        getEventsFromServer();

        if (pcal != null){
            TreeSet<GCalendar> groups = pcal.getGroups();
            if (groups != null){
                //load group items ui
                int index = 0;
                for (GCalendar group : groups){
                    String color = PCalendar.getColor(index++);
                    LinearLayout newView = (LinearLayout) this.getLayoutInflater().inflate(R.layout.groups_item, null);
                    CardView item = newView.findViewById(R.id.groups_item_item);
                    CardView itemColor = newView.findViewById(R.id.groups_item_color);
                    TextView itemText = newView.findViewById(R.id.groups_item_text);
                    itemText.setText(group.getGroupName());
                    itemColor.setCardBackgroundColor(Color.parseColor(color));
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
                            intent.putExtra("calendar", group);
                            intent.putExtra("color", color);
                            startActivity(intent);
                        }
                    });
                    groups_container.addView(newView);
                }
            }
        }
    }
}