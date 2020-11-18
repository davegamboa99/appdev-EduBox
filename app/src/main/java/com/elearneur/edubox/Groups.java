package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Groups extends AppCompatActivity {
    private FloatingActionButton creategroup, joingroup, addgroup;
    private boolean optionsAreVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        creategroup = findViewById(R.id.creategroup);
        joingroup = findViewById(R.id.joingroup);
        addgroup = findViewById(R.id.addgroup);

        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupCreate.class);
                startActivity(intent);
            }
        });

        joingroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupJoin.class);
                startActivity(intent);
            }
        });

        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsAreVisible){
                    creategroup.setVisibility(View.INVISIBLE);
                    joingroup.setVisibility(View.INVISIBLE);
                } else {
                    creategroup.setVisibility(View.VISIBLE);
                    joingroup.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}