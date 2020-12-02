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

        loadGroups();

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
    }

    private void loadGroups(){
        LinearLayout groups_container;
        LayoutInflater inflater;
//        Color[] colors = new Color[3];

//        colors[0] = Color.valueOf(Color.BLUE);
//        colors[1] = Color.valueOf(Color.GREEN);
//        colors[2] = Color.valueOf(Color.YELLOW);

        groups_container = findViewById(R.id.groups_container);
        groups_container.removeAllViews();

        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
        } catch (IOException e) {
            System.out.println("MyException1: " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("MyException2: " + e);
        }

        inflater = this.getLayoutInflater();
        if (pcal != null){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GCalendar[] gcals = JSONParser.getGCalendars();
                        for (GCalendar group : gcals){
                            pcal.addGroup(group);
//                            LinearLayout newView = (LinearLayout) inflater.inflate(R.layout.groups_item, null);
//                            Button item = newView.findViewById(R.id.groups_item_item);
//                            item.setText(group.getGroupName());
//                            // set item background tint
//                            item.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
//                                    intent.putExtra("calendar", group); // not working
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                            groups_container.addView(newView);
                        }
                    } catch (IOException e){
                        System.out.println("MyException1: " + e);
                    } catch (Exception e){
                        System.out.println("MyException2: " + e);
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("MyException3: " + e);
            }

            TreeSet<GCalendar> groups = pcal.getGroups();
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