package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class GroupCalendar extends AppCompatActivity {
    private GCalendar gcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        gcal = (GCalendar) getIntent().getSerializableExtra("calendar");

        Toolbar toolbar;
        FloatingActionButton addgroupevent;

        toolbar = findViewById(R.id.toolbar_group_calendar);
        setSupportActionBar(toolbar);
        if (gcal!=null) toolbar.setTitle(gcal.getGroupName());

        addgroupevent = findViewById(R.id.addgroupevent);

        addgroupevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                intent.putExtra("calendar", gcal);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share){
            Toast.makeText(getApplicationContext(), "Invitation code copied!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leave){
            try {
                PCalendar pcal = null;
                pcal = PCalendar.loadCalendar(getApplicationContext());
                pcal.removeGroup(gcal);
                pcal.saveCalendar(getApplicationContext());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
            } catch (ClassNotFoundException e) {
                Toast.makeText(getApplicationContext(), "ClassNotFoundException", Toast.LENGTH_SHORT).show();
            }
            finish();
            startActivity(new Intent(getApplicationContext(), Groups.class));
            Toast.makeText(getApplicationContext(), "You leave the group!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}