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

        group_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gName = group_create_input.getText().toString();
                if ("".equals(gName)) {
                    Toast.makeText(getApplicationContext(), "Input GroupName", Toast.LENGTH_SHORT).show();
                    return;
                }
                PCalendar cal = null;
                try {
                    cal = PCalendar.loadCalendar(getApplicationContext());
                } catch (IOException e) {
                    cal = new PCalendar();
                } catch (ClassNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
                }
                GCalendar gcal = new GCalendar(gName);
                cal.addGroup(gcal);
                try {
                    cal.saveCalendar(getApplicationContext());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), cal.toStringGroups(), Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Groups.class));
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