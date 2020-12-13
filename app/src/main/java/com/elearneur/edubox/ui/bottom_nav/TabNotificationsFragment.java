package com.elearneur.edubox.ui.bottom_nav;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elearneur.edubox.R;
import com.elearneur.edubox.detailedanalysis.DetailedAnalysisActivity;
import com.elearneur.edubox.notifications.helper.EventTest;
import com.elearneur.edubox.notifications.helper.NotificationAdaptor;

import java.util.ArrayList;

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

    ArrayList<EventTest> events, completedEvents;
    RecyclerView notificationRecyclerView, completedNotificationRecyclerView;
    RecyclerView.LayoutManager notificationLayoutManager, completedNotificationLayoutManager;
    RecyclerView.Adapter notificationAdapter, completedNotificationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_notifications_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");



        events = new ArrayList<EventTest>();
        events.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));
        events.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", false, false));
        events.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", false, false));

        //finished dummy data
        completedEvents = new ArrayList<EventTest>();
        completedEvents.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));
        completedEvents.add(new EventTest("November 13, 2020", "9:15 AM", "Application Development", "Zoom Meeting starting soon at 9:30, 13 November 2020.", "Calendar", "2 hours", true, true));
        completedEvents.add(new EventTest("November 05, 2020", "11:36 AM", "Data Plan", "Your 200 MB data will expire tomorrow.", "Dashboard", "0 hours", true, true));

        //event list
        notificationRecyclerView = view.findViewById(R.id.eventList);
        notificationRecyclerView.setHasFixedSize(true);

        notificationLayoutManager = new LinearLayoutManager(view.getContext());
        notificationRecyclerView.setLayoutManager(notificationLayoutManager);

        notificationAdapter = new NotificationAdaptor(events);
        notificationRecyclerView.setAdapter(notificationAdapter);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //completed event list
        completedNotificationRecyclerView = view.findViewById(R.id.eventListCompleted);
        completedNotificationRecyclerView.setHasFixedSize(true);

        completedNotificationLayoutManager = new LinearLayoutManager(view.getContext());
        completedNotificationRecyclerView.setLayoutManager(completedNotificationLayoutManager);

        completedNotificationAdapter = new NotificationAdaptor(completedEvents);
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