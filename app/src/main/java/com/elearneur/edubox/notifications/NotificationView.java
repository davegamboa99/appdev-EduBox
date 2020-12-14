package com.elearneur.edubox.notifications;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.elearneur.edubox.R;

public class NotificationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        Toolbar toolbar = findViewById(R.id.notificationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView evenTitle = findViewById(R.id.eventTitleView);
        TextView eventDate = findViewById(R.id.eventDateView);
        TextView eventTime = findViewById(R.id.eventTimeView);
        TextView eventContent = findViewById(R.id.eventContentView);
        TextView eventInfo = findViewById(R.id.eventInfoView);

        evenTitle.setText(getIntent().getStringExtra("eventTitle"));
        eventDate.setText(getIntent().getStringExtra("eventDate"));
        eventTime.setText(getIntent().getStringExtra("eventTime"));
        eventContent.setText(getIntent().getStringExtra("eventContent"));
        eventInfo.setText(getIntent().getStringExtra("eventInfo"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}