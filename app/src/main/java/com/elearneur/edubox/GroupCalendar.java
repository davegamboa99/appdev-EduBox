package com.elearneur.edubox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.Locale;
import java.util.TreeSet;

public class GroupCalendar extends AppCompatActivity {
    private GCalendar gcal;
    private Dialog editEvent;
    private String date;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        Dates2 dates = new Dates2();
        ImageView nextMonth = findViewById(R.id.nextMonth);
        ImageView prevMonth = findViewById(R.id.prevMonth);
        TextView monthYear = findViewById(R.id.month_year);
        TextView dayWeek = findViewById(R.id.dayWeek);

        //daypicker
        NumberPicker dayPicker = findViewById(R.id.day_picker);
        // Set value
        date = dates.getDateString();
        initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
        dayPicker.setFadingEdgeEnabled(true);
        dayPicker.setScrollerEnabled(true);
        dayPicker.setWrapSelectorWheel(true);

        // OnClickListener
        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("as", "Click on current value");
            }
        });

        // OnValueChangeListener
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("asda", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                dates.setCurrentDate(newVal);
                date = dates.getDateString();
                dayWeek.setText(dates.getCurrentDayOfWeek());
                loadEvents(); // reload events
            }
        });

        monthYear.setText(dates.getCurrentMonthYear());
        dayWeek.setText(dates.getCurrentDayOfWeek());
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.moveNextMonth();
                monthYear.setText(dates.getCurrentMonthYear());
                dayWeek.setText(dates.getCurrentDayOfWeek());
                initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
            }
        });
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.movePrevMonth();
                monthYear.setText(dates.getCurrentMonthYear());
                dayWeek.setText(dates.getCurrentDayOfWeek());
                initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
            }
        });

        gcal = (GCalendar) getIntent().getSerializableExtra("calendar");

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar_group_calendar);
        toolbar.setTitle(gcal.getGroupName().toUpperCase());
        setSupportActionBar(toolbar);

        editEvent = new Dialog(this);

        FloatingActionButton addgroupevent;

        addgroupevent = findViewById(R.id.addgroupevent);

        addgroupevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdd.class);
                intent.putExtra("calendar", gcal);
                intent.putExtra("activity_type", 0);
                startActivity(intent);
            }
        });

        color = (String) getIntent().getStringExtra("color");
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
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("invitation_code", "" + gcal.getInvitationCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Invitation code copied!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leave){
            PCalendar pcal = deserializePCal();
            final Account acc = pcal.getAccount();
            if (acc != null){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONParser.removeMember(gcal, acc.toMemberData());
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
                pcal.removeGroup(gcal);
                reserializePCal(pcal);
            }

            finish();
            Toast.makeText(getApplicationContext(), "You left the group!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.edit_name){
            Intent intent = new Intent(GroupCalendar.this, GroupCreate.class);
            intent.putExtra("calendar", gcal);
            intent.putExtra("activity_type", 1); // 0 means create; 1 for edit
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        PCalendar pcal = deserializePCal();
        gcal = pcal.getGroup(gcal); //update
        loadEvents();
    }

    private void initDayPicker(NumberPicker picker, int min, int max, int current){
        picker.setMaxValue(max);
        picker.setMinValue(min);
        picker.setValue(current);
    }

    private void loadEvents(){
        LinearLayout events_container = findViewById(R.id.events_container);
        events_container.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();

        TreeSet<CalEvent> evts = gcal.getEvents();
        if (evts != null){
            for (CalEvent evt : evts){
                if (!date.equals(evt.getDate())) continue;

                LinearLayout ll_time = (LinearLayout) inflater.inflate(R.layout.events_item_time, null);
                TextView evt_time = ll_time.findViewById(R.id.label_time);
                evt_time.setText(evt.getTime());
                if ("".equals(evt_time.getText())) evt_time.setText("All Day");

                LinearLayout ll_event = (LinearLayout) inflater.inflate(R.layout.events_item_event, null);

                ll_event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.event_edit, null);

                        TextView event_title, event_date, event_time, event_type, event_speed, event_data, event_note;
                        event_title = rl.findViewById(R.id.event_title);
                        event_date = rl.findViewById(R.id.event_date);
                        event_time = rl.findViewById(R.id.event_time);
                        event_type = rl.findViewById(R.id.event_type);
                        event_speed = rl.findViewById(R.id.event_speed);
                        event_data = rl.findViewById(R.id.event_data);
                        event_note = rl.findViewById(R.id.event_note);
                        Button edit = rl.findViewById(R.id.edit);
                        Button back = rl.findViewById(R.id.back);
                        ImageView delete = rl.findViewById(R.id.delete);

                        event_title.setText("Title: " + evt.getTitle());
                        event_date.setText("Date: " + evt.getDate());
                        event_time.setText("Time: " + evt.getTime());
                        event_type.setText("Type: " + evt.getContentType());
                        event_speed.setText("Min. Speed Requirement: TBC"); //to be calculated
                        event_data.setText("Min. Data Consumption: TBC");
                        event_note.setText("Note: " + evt.getNote());

                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("EVENT = " + evt);
                                Intent intent = new Intent(GroupCalendar.this, EventAdd.class);
                                intent.putExtra("calendar", gcal);
                                intent.putExtra("event", evt);
                                intent.putExtra("activity_type", 1); // 0 for add, 1 for edit
                                startActivity(intent);
                                editEvent.dismiss();
                            }
                        });

                        back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editEvent.cancel();
                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONParser.putDeleteEvent(evt);
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
                                PCalendar pcal = deserializePCal();
                                GCalendar gCalendar = pcal.getGroup(gcal);
                                gCalendar.getEvents().remove(evt);
                                reserializePCal(pcal);
                                editEvent.dismiss();
                                onResume();
                            }
                        });

                        editEvent.setContentView(rl);
                        editEvent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        editEvent.show();
                    }
                });
                CardView group_color = ll_event.findViewById(R.id.left_color);
                group_color.setCardBackgroundColor(Color.parseColor(color));
                TextView evt_title = ll_event.findViewById(R.id.label_title);
                TextView evt_calName = ll_event.findViewById(R.id.label_calName);
                evt_title.setText(evt.getTitle());
                evt_calName.setText(gcal.getGroupName());

                events_container.addView(ll_time);
                events_container.addView(ll_event);
            }
        }
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

    private void reserializePCal(PCalendar pcal){
        try {
            pcal.saveCalendar(GroupCalendar.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}