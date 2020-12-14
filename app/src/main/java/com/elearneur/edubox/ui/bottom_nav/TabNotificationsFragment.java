package com.elearneur.edubox.ui.bottom_nav;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elearneur.edubox.R;
import com.elearneur.edubox.calendar.CalEvent;
import com.elearneur.edubox.calendar.GCalendar;
import com.elearneur.edubox.calendar.PCalendar;
import com.elearneur.edubox.notifications.helper.EventTest;
import com.elearneur.edubox.notifications.helper.NotificationAdaptor;
import com.elearneur.edubox.notifications.helper.ReminderNotification;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabNotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabNotificationsFragment newInstance(String param1, String param2) {
        TabNotificationsFragment fragment = new TabNotificationsFragment();
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

    ArrayList<CalEvent> eventList, notCompletedEventList, completedEventList;
    RecyclerView notificationRecyclerView, completedNotificationRecyclerView;
    RecyclerView.LayoutManager notificationLayoutManager, completedNotificationLayoutManager;
    RecyclerView.Adapter notificationAdapter, completedNotificationAdapter;
    public static PCalendar pcal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_notifications_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");


        try{
            pcal = PCalendar.loadCalendar(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        TreeSet<CalEvent> personalEvents = pcal.getEvents();
        TreeSet<GCalendar> gcals = pcal.getGroups();
        TreeSet<CalEvent> evts;


        eventList = new ArrayList<>();
        notCompletedEventList = new ArrayList<>();
        completedEventList = new ArrayList<>();

        for(CalEvent event: personalEvents) {
            eventList.add(event);
        }

        for(GCalendar gcal : gcals){
            evts = gcal.getEvents();
            if (evts != null){
                for(CalEvent event: evts) {
                    eventList.add(event);
                }
            }
        }
        Collections.sort(eventList, new Comparator<CalEvent>() {
            DateFormat dateAndTime = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            @Override
            public int compare(CalEvent o1, CalEvent o2) {
                try{
                    return dateAndTime.parse(o1.getDate()+' '+ o1.getTime()).compareTo(dateAndTime.parse(o2.getDate()+' '+ o2.getTime()));
                } catch (ParseException e){
                    throw new IllegalArgumentException(e);
                }

            }
        });

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) +1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String current = Integer.toString(calendar.get(Calendar.YEAR)) +'-'+month+'-'+day;



        for(CalEvent evt: eventList){
            if(evt.getDate().compareTo(current) >= 0){
                notCompletedEventList.add(evt);

            }
            else{
                completedEventList.add(evt);
            }
        }


        //event list
        notificationRecyclerView = view.findViewById(R.id.eventList);
        notificationRecyclerView.setHasFixedSize(true);
        notificationLayoutManager = new LinearLayoutManager(view.getContext());
        notificationRecyclerView.setLayoutManager(notificationLayoutManager);
        notificationAdapter = new NotificationAdaptor(getContext(), pcal, notCompletedEventList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        //completed event list
        completedNotificationRecyclerView = view.findViewById(R.id.eventListCompleted);
        completedNotificationRecyclerView.setHasFixedSize(true);
        completedNotificationLayoutManager = new LinearLayoutManager(view.getContext());
        completedNotificationRecyclerView.setLayoutManager(completedNotificationLayoutManager);
        completedNotificationAdapter = new NotificationAdaptor(getContext(), pcal, completedEventList);
        completedNotificationRecyclerView.setAdapter(completedNotificationAdapter);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notifications, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_check:
                if(item.getTitle().equals("Completed Off")){
                    item.setTitle("Completed On");
                    item.setIcon(R.drawable.ic_check_circle_on);
                    completedNotificationRecyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    item.setTitle("Completed Off");
                    item.setIcon(R.drawable.ic_check_circle_off);
                    completedNotificationRecyclerView.setVisibility(View.GONE);
                }
                break;
            case R.id.action_sound:
                if(item.getTitle().equals("Voice Notification Off")){
                    item.setTitle("Voice Notification On");
                    item.setIcon(R.drawable.ic_voice_notification_on);
                }
                else{
                    item.setTitle("Voice Notification Off");
                    item.setIcon(R.drawable.ic_voice_notification_off);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

}