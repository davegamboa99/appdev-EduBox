package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity implements EventAdaptor.ItemClicked{
    public static ArrayList<Event> events;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        events = new ArrayList<Event>();
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false));



        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView textToSpeech = findViewById(R.id.speech);
        ImageView completed = findViewById(R.id.completed);

        recyclerView = findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new EventAdaptor(this, Notifications.events);
        recyclerView.setAdapter(myAdapter);



        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Notifications.this, "Sidebar!", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textToSpeech.getTag().equals("speechOn")) {
                    textToSpeech.setImageResource(R.drawable.ic_baseline_notify_not_voice);
                    Toast.makeText(Notifications.this, "Notification Speech Off", Toast.LENGTH_SHORT).show();
                    textToSpeech.setTag("speechOff");
                }
                else{
                    textToSpeech.setImageResource(R.drawable.ic_baseline_notify_with_speech);
                    Toast.makeText(Notifications.this, "Notification Speech On", Toast.LENGTH_SHORT).show();
                    textToSpeech.setTag("speechOn");
                }

            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completed.getTag().equals("completedOn")) {
                    completed.setImageResource(R.drawable.ic_baseline_not_completed);
                    Toast.makeText(Notifications.this, "Completed task is hidden", Toast.LENGTH_SHORT).show();
                    completed.setTag("completedOff");
                }
                else{
                    completed.setImageResource(R.drawable.ic_baseline_completed);
                    Toast.makeText(Notifications.this, "Completed task is showed", Toast.LENGTH_SHORT).show();
                    completed.setTag("completedOn");
                }
            }
        });




    }

    @Override
    public void onItemClicked( int index) {
    }
}