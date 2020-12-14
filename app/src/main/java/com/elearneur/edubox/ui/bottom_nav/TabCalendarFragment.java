package com.elearneur.edubox.ui.bottom_nav;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elearneur.edubox.R;
import com.elearneur.edubox.calendar.Account;
import com.elearneur.edubox.calendar.CalEvent;
import com.elearneur.edubox.calendar.Dates2;
import com.elearneur.edubox.calendar.EventAdd;
import com.elearneur.edubox.calendar.GCalendar;
import com.elearneur.edubox.calendar.Groups;
import com.elearneur.edubox.calendar.JSONParser;
import com.elearneur.edubox.calendar.PCalendar;
import com.elearneur.edubox.calendar.PersonalCalendar;
import com.elearneur.edubox.detailedanalysis.DetailedAnalysisActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.Locale;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabCalendarFragment extends Fragment {
    public static PCalendar pcal;
    private Dialog editEvent;
    private String date;
    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabCalendarFragment newInstance(String param1, String param2) {
        TabCalendarFragment fragment = new TabCalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);    //enable appbar menus
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_personal_calendar, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calendar");

        initPcal();
        if (pcal.getAccount() == null){
            Account acc = (Account) getActivity().getIntent().getSerializableExtra("account");
            pcal.setAccount(acc);
            try {
                pcal.saveCalendar(getContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Dates2 dates = new Dates2();
        ImageView nextMonth = view.findViewById(R.id.nextMonth);
        ImageView prevMonth = view.findViewById(R.id.prevMonth);
        TextView monthYear = view.findViewById(R.id.month_year);
        TextView dayWeek = view.findViewById(R.id.dayWeek);

        //daypicker
        NumberPicker dayPicker = view.findViewById(R.id.day_picker);
        // Set value
        date = dates.getDateString();
        initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
        dayPicker.setFadingEdgeEnabled(true);
        dayPicker.setScrollerEnabled(true);
        dayPicker.setWrapSelectorWheel(true);

        // OnClickListener
        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("as", "Click on current value");
            }
        });

        // OnValueChangeListener
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Log.d("asda", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                dates.setCurrentDate(newVal);
                date = dates.getDateString();
                dayWeek.setText(dates.getCurrentDayOfWeek());
                loadEvents(view); // reload events
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
                date = dates.getDateString();
                loadEvents(view);
            }
        });
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.movePrevMonth();
                monthYear.setText(dates.getCurrentMonthYear());
                dayWeek.setText(dates.getCurrentDayOfWeek());
                initDayPicker(dayPicker, dates.getMinDay(), dates.getMaxDay(), dates.getCurrentDay());
                date = dates.getDateString();
                loadEvents(view);
            }
        });

        FloatingActionButton addevent;
        addevent = view.findViewById(R.id.addevent);

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventAdd.class);
                intent.putExtra("calendar", pcal);
                intent.putExtra("activity_type", 0);
                startActivity(intent);
            }
        });

        editEvent = new Dialog(getContext());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.toolbar_groups:
                if (pcal.getAccount() == null){
                    Toast.makeText(getContext(), "You need to login before using this feature.", Toast.LENGTH_LONG).show();
                    break;
                }
                intent = new Intent(getContext(), Groups.class);
                intent.putExtra("userId", pcal.getAccount().getId());
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPcal();
        loadEvents(view);
    }

    private void initDayPicker(NumberPicker picker, int min, int max, int current){
        picker.setMaxValue(max);
        picker.setMinValue(min);
        picker.setValue(current);
    }

    private void initPcal(){
        try {
            pcal = PCalendar.loadCalendar(getContext());
        } catch (IOException e) {
            pcal = new PCalendar();
            try {
                pcal.saveCalendar(getContext());
            } catch (IOException ioException) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            pcal = new PCalendar();
            try {
                pcal.saveCalendar(getContext());
            } catch (IOException ioException) {
                e.printStackTrace();
            }
        }
    }

    private void savePcal(){
        if (pcal != null){
            try {
                pcal.saveCalendar(getContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPcalEvents(LinearLayout events_container, LayoutInflater inflater){
        TreeSet<CalEvent> evts = pcal.getEvents();
        if (evts != null) {
            for (CalEvent evt : evts){
                if (!date.equals(evt.getDate())) continue;

                LinearLayout ll_time = (LinearLayout) inflater.inflate(R.layout.events_item_time, null);
                TextView evt_time = ll_time.findViewById(R.id.label_time);
                evt_time.setText(evt.getTime());
                if ("".equals(evt_time.getText())) evt_time.setText("All Day");

                LinearLayout ll_event = (LinearLayout) this.getLayoutInflater().inflate(R.layout.events_item_event, null);
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
                                Intent intent = new Intent(getContext(), EventAdd.class);
                                intent.putExtra("calendar", pcal);
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
                                pcal.getEvents().remove(evt);
                                savePcal();
                                editEvent.dismiss();
                                onResume();
                            }
                        });

                        editEvent.setContentView(rl);
                        editEvent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        editEvent.show();
                    }
                });
                TextView evt_title = ll_event.findViewById(R.id.label_title);
                TextView evt_calName = ll_event.findViewById(R.id.label_calName);
                evt_title.setText(evt.getTitle());
                evt_calName.setText("Personal");

                events_container.addView(ll_time);
                events_container.addView(ll_event);
            }
        }
    }

    private void loadGcalEvents(LinearLayout events_container, LayoutInflater inflater){
        TreeSet<GCalendar> gcals = pcal.getGroups();
        TreeSet<CalEvent> evts;
        if (gcals != null){
            int index = 0;
            for (GCalendar gcal : gcals){
                evts = gcal.getEvents();
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
                                        Intent intent = new Intent(getContext(), EventAdd.class);
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
                                        GCalendar gCalendar = pcal.getGroup(gcal);
                                        gCalendar.getEvents().remove(evt);
                                        savePcal();
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
                        group_color.setCardBackgroundColor(Color.parseColor(PCalendar.getColor(index)));
                        TextView evt_title = ll_event.findViewById(R.id.label_title);
                        TextView evt_calName = ll_event.findViewById(R.id.label_calName);
                        evt_title.setText(evt.getTitle());
                        evt_calName.setText(gcal.getGroupName());

                        events_container.addView(ll_time);
                        events_container.addView(ll_event);
                    }
                }
                index++;
            }
        }
    }

    private void loadEvents(View view){
        LinearLayout events_container = view.findViewById(R.id.events_container);
        events_container.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();

        loadPcalEvents(events_container, inflater);
        loadGcalEvents(events_container, inflater);
    }
}