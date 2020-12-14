package com.elearneur.edubox.notifications.helper;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.elearneur.edubox.R;
import com.elearneur.edubox.notifications.NotificationView;


public class ReminderNotification extends BroadcastReceiver {
    private String eventTitle, eventContent, eventInfo, eventTime, eventDate, eventSpeed, eventData;


    @Override
    public void onReceive(Context context, Intent intent) {
        eventTitle = intent.getStringExtra("eventTitle");
        eventContent = intent.getStringExtra("eventContent");
        eventInfo = intent.getStringExtra("eventInfo");
        eventTime = intent.getStringExtra("eventTime");
        eventDate = intent.getStringExtra("eventDate");
        eventSpeed = " ";
        eventData = " ";

        Intent voiceNotification = new Intent(context.getApplicationContext(), VoiceNotification.class);
        String eventDetails = "New event " + eventTitle + " at " + eventTime + "Today! It is a" + eventInfo;
        voiceNotification.putExtra("eventDetails",eventDetails);
        //context.startService(voiceNotification);



        Intent viewNotification = new Intent(context.getApplicationContext(), NotificationView.class);
        viewNotification.putExtra("eventTitle",eventTitle);
        viewNotification.putExtra("eventContent",eventContent);
        viewNotification.putExtra("eventInfo",eventInfo);
        viewNotification.putExtra("eventTime",eventTime);
        viewNotification.putExtra("eventDate",eventDate);

        PendingIntent viewNotificationPending = PendingIntent.getActivity(context.getApplicationContext(),1,viewNotification, PendingIntent.FLAG_ONE_SHOT);

        //Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notify")
                .setSmallIcon(R.drawable.edubox_icon)
                .setContentTitle(intent.getStringExtra("eventTitle"))
                .setContentText(intent.getStringExtra("eventContent"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra("eventInfo")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
        //open notification
        builder.setContentIntent(viewNotificationPending);
        builder.addAction(R.drawable.ic_baseline_open,"View",viewNotificationPending);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());

        //AlertDialog box
        final WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams alertDialogParams = new WindowManager.LayoutParams();
        alertDialogParams.gravity = Gravity.CENTER;
        alertDialogParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        alertDialogParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialogParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialogParams.packageName = context.getPackageName();

        final View alertDialogView = View.inflate(context.getApplicationContext(), R.layout.notifications_event_dialog_fragment,null);
        Button btnFinish = alertDialogView.findViewById(R.id.btnDialogFinish);
        Button btnView = alertDialogView.findViewById(R.id.btnDialogView);
        setDialogValues(alertDialogView);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.removeView(alertDialogView);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    notificationManagerCompat.cancel(200);
                    manager.removeView(alertDialogView);
                    viewNotificationPending.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });
        manager.addView(alertDialogView,alertDialogParams);

    }

    private void setDialogValues(View dialog) {
        TextView dialogEventTitle = dialog.findViewById(R.id.dialogEventTitle);
        TextView dialogEventContent = dialog.findViewById(R.id.dialogEventType);
        TextView dialogEventInfo = dialog.findViewById(R.id.dialogEventInfo);
        TextView dialogEventTime = dialog.findViewById(R.id.dialogEventTime);
        TextView dialogEventDate = dialog.findViewById(R.id.dialogEventDate);
        TextView dialogEventSpeed = dialog.findViewById(R.id.dialogEventSpeed);
        TextView dialogEventData = dialog.findViewById(R.id.dialogEventData);

        dialogEventTitle.setText(eventTitle);
        dialogEventContent.setText("Type: " + eventContent);
        dialogEventInfo.setText("Note: " + eventInfo);
        dialogEventTime.setText("Time: " + eventTime);
        dialogEventDate.setText("Date: " + eventDate);
        dialogEventSpeed.setText("Min. Speed Requirement: " + eventSpeed);
        dialogEventData.setText("Min. Data Consumption: " + eventData + "mb");
    }
}



//public class Test extends AppCompatActivity {
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//        createNotificationChannel();
//
//        Button btnTest = findViewById(R.id.btnTest);
//
//
//        //getting the timepicker object
//        TimePicker timePicker = findViewById(R.id.timePicker);
//
//        //attaching clicklistener on button
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //We need a calendar object to get the specified time in millis
//                //as the alarm manager method takes time in millis to setup the alarm
//                Calendar calendar = Calendar.getInstance();
//                if (android.os.Build.VERSION.SDK_INT >= 23) {
//                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//                            timePicker.getHour(), timePicker.getMinute(), 0);
//                } else {
//                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
//                }
//
//
//                setAlarm(calendar.getTimeInMillis());
//            }
//        });
//
//
//    }
//
//    private void setAlarm(long timeInMillis) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(this, ReminderNotification.class);
//        intent.putExtra("eventTitle","Application Development");
//        intent.putExtra("eventContent","Zoom Meeting");
//        intent.putExtra("eventInfo","Zoom Meeting starting soon at 9:30, 13 November 2020.");
//        intent.putExtra("eventTime","9:15 AM");
//        intent.putExtra("eventDate","November 13, 2020");
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
//        Toast.makeText(Test.this, "Reminder Set!", Toast.LENGTH_SHORT).show();
//    }
//
//    private  void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "Notification";
//            String description = "test";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("notify", name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//}

