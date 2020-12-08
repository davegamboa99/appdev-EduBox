package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.TreeSet;

public class GroupCreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        EditText group_create_input;
        Button group_create_btn;
        TextView cancel_create;

        group_create_input = findViewById(R.id.group_create_input);
        group_create_btn = findViewById(R.id.group_create_btn);
        cancel_create = findViewById(R.id.cancel_create);

        int activityType = (int) getIntent().getIntExtra("activity_type", 0);
        if (activityType==1){ //if (1) edit
            GCalendar gcal = (GCalendar) getIntent().getSerializableExtra("calendar");
            group_create_input.setText(gcal.getGroupName());
        }

        group_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gName = group_create_input.getText().toString();
                if ("".equals(gName)) {
                    Toast.makeText(getApplicationContext(), "Input GroupName", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (activityType == 0){ // create
                    GCalendar gcal = new GCalendar(gName);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PCalendar pcal = null;
                            try {
                                pcal = PCalendar.loadCalendar(getApplicationContext());
                                JSONParser.postCalendar(gcal, pcal.getAccount());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                    startActivity(new Intent(getApplicationContext(), Groups.class));
                } else { // (1) edit
                    GCalendar gcal = (GCalendar) getIntent().getSerializableExtra("calendar");
                    if (gcal != null){
                        gcal.setGroupName(gName);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONParser.putCalendar(gcal);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(GroupCreate.this, "Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), Groups.class));
                }
            }
        });

        cancel_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}