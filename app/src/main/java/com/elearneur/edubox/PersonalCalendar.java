package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class PersonalCalendar extends AppCompatActivity {
    public static PCalendar pcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_calendar);

        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
        } catch (IOException e) {
            pcal = new PCalendar();
        } catch (ClassNotFoundException e) {
            Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar;
        FloatingActionButton addevent;
        TextView event001;
        Dialog editevent;

        toolbar = findViewById(R.id.toolbar_calendar);
        setSupportActionBar(toolbar);
        addevent = findViewById(R.id.addevent);
        event001 = findViewById(R.id.event001);
        editevent = new Dialog(this);

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                intent.putExtra("calendar", pcal);
                startActivity(intent);
            }
        });

        event001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editevent.setContentView(R.layout.event_edit);
                editevent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editevent.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.groups){
            Intent intent = new Intent(getApplicationContext(), Groups.class);
            startActivity(intent);
        }
        return true;
    }
}