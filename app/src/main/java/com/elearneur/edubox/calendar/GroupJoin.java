package com.elearneur.edubox.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elearneur.edubox.R;

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
                Context here = getApplicationContext();
                Toast toast = Toast.makeText(here ,"Code does not exist!", Toast.LENGTH_SHORT);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PCalendar pcal = deserializePCal();
                        GCalendar gcal = new GCalendar("");
                        gcal.setId(code_int);
                        try {
                            String response = JSONParser.addMember(gcal, pcal.getAccount().toMemberData());
                            if (response.length() == 0) {
                                toast.show();
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cancel_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private PCalendar deserializePCal(){
        PCalendar pcal = null;
        try {
            pcal = PCalendar.loadCalendar(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pcal;
    }
}