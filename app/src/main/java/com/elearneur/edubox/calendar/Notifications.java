package com.elearneur.edubox.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.elearneur.edubox.Adapter.NotificationAdaptor;
import com.elearneur.edubox.R;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    private static ArrayList<Event> events, completedEvents;
    RecyclerView notificationRecyclerView, completedNotificationRecyclerView;
    RecyclerView.LayoutManager notificationLayoutManager, completedNotificationLayoutManager;
    RecyclerView.Adapter notificationAdapter, completedNotificationAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        events = new ArrayList<Event>();
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));

        //finished dummy data
        completedEvents = new ArrayList<Event>();
        completedEvents.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));

        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView textToSpeech = findViewById(R.id.speech);
        ImageView completed = findViewById(R.id.completed);

        //event list
        notificationRecyclerView = findViewById(R.id.eventList);
        notificationRecyclerView.setHasFixedSize(true);

        notificationLayoutManager = new LinearLayoutManager(this);
        notificationRecyclerView.setLayoutManager(notificationLayoutManager);

        notificationAdapter = new NotificationAdaptor(Notifications.events);
        notificationRecyclerView.setAdapter(notificationAdapter);

        //completed event list
        completedNotificationRecyclerView = findViewById(R.id.eventListCompleted);
        completedNotificationRecyclerView.setHasFixedSize(true);

        completedNotificationLayoutManager = new LinearLayoutManager(this);
        completedNotificationRecyclerView.setLayoutManager(completedNotificationLayoutManager);

        completedNotificationAdapter = new NotificationAdaptor(Notifications.completedEvents);
        completedNotificationRecyclerView.setAdapter(completedNotificationAdapter);


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
                    completedNotificationRecyclerView.setVisibility(View.GONE);
                }
                else{
                    completed.setImageResource(R.drawable.ic_baseline_completed);
                    Toast.makeText(Notifications.this, "Completed task is showed", Toast.LENGTH_SHORT).show();
                    completed.setTag("completedOn");
                    completedNotificationRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}