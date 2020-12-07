package com.elearneur.edubox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class GroupJoin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);

        EditText group_join_input;
        Button group_join_btn;
        TextView cancel_join;

        group_join_input = findViewById(R.id.group_join_input);
        group_join_btn = findViewById(R.id.group_join_btn);
        cancel_join = findViewById(R.id.cancel_join);

        group_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = group_join_input.getText().toString();
                if ("".equals(code)) {
                    Toast.makeText(getApplicationContext(), "Input Invitation Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                int code_int = Integer.parseInt(code);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PCalendar pcal = null;
                        GCalendar gcal = new GCalendar("");
                        gcal.setId(code_int);
                        try {
                            pcal = PCalendar.loadCalendar(getApplicationContext());
                            String response = JSONParser.addMember(gcal, pcal.getAccount().toMemberData());
                            if (response.length() == 0) Toast.makeText(getApplicationContext(),"Code does not exist!", Toast.LENGTH_SHORT);
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
            }
        });

        cancel_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}