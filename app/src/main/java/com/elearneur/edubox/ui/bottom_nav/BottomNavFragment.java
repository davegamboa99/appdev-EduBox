package com.elearneur.edubox.ui.bottom_nav;

import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.elearneur.edubox.R;
import com.elearneur.edubox.ui.home.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavFragment extends Fragment {

    private BottomNavViewModel bottomNavViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bottomNavViewModel =
                new ViewModelProvider(this).get(BottomNavViewModel.class);

        View root = inflater.inflate(R.layout.fragment_bottom_nav, container, false);

        BottomNavigationView bottomnav = root.findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setSelectedItemId(R.id.page_Calendar);    //set calendar button as active
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calendar");    //set appbar title
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs_fragment_container,
                new TabCalendarFragment()).commit();    //set calendar as default page

//        final TextView textView = root.findViewById(R.id.text_bottom_nav);
//        bottomNavViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.page_Dashboard:
                            fragment = new TabDashboardFragment();
                            break;
                        case R.id.page_Calendar:
                            fragment = new TabCalendarFragment();
                            break;
                        case R.id.page_Notifications:
                            fragment = new TabNotificationsFragment();
                            break;
                    }

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs_fragment_container,
                            fragment).commit();
                    return true;
                }
            };
}