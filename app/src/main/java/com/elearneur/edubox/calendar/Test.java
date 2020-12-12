package com.elearneur.edubox.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.elearneur.edubox.R;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private  List<Event> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        createNotificationChannel();

        events = new ArrayList<Event>();
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true,true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false,true));
        events.add(new Event("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false,true));
        events.add(new Event("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false,true));

        Button btnTest = findViewById(R.id.btnTest);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test.this, "Reminder Set!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Test.this, ReminderBroadcast.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(Test.this,0,intent,0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long tenSecondsMillis = 1000 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsMillis, pendingIntent);

            }
        });

    }

    private  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "EduBox";
            String descriptions = "Edubox";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("EduBox", name, importance);
            channel.setDescription(descriptions);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}