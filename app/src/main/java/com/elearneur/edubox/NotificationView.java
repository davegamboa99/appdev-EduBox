package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationView extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        //Appbar
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView textToSpeech = findViewById(R.id.speech);
        ImageView completed = findViewById(R.id.completed);

        leftIcon.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        textToSpeech.setVisibility(View.INVISIBLE);
        completed.setVisibility(View.INVISIBLE);

        //Content
        TextView evenTitle = findViewById(R.id.eventTitleView);
        TextView eventDate = findViewById(R.id.eventDateView);
        TextView eventTime = findViewById(R.id.eventTimeView);
        TextView eventContent = findViewById(R.id.eventContentView);
        TextView eventInfo = findViewById(R.id.eventInfoView);
        Button eventButton = findViewById(R.id.eventButton);

        evenTitle.setText(getIntent().getStringExtra("eventTitle"));
        eventDate.setText(getIntent().getStringExtra("eventDate"));
        eventTime.setText(getIntent().getStringExtra("eventTime"));
        eventContent.setText(getIntent().getStringExtra("eventContent"));
        eventInfo.setText(getIntent().getStringExtra("eventInfo"));

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationView.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}