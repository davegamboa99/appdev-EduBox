package com.elearneur.edubox.detailedanalysis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elearneur.edubox.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Calendar calendar;
    private Date date;
    private String today;
    private TextView textViewDate;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private int offsetCounter;
    private GraphView graph;
    private ImageButton hourLeftButton;
    private ImageButton hourRightButton;
    private TextView textViewHourLabel;
    private TextView textViewHour1;
    private TextView textViewHour2;
    private TextView textViewHour3;
    private TextView textViewHour4;
    private TextView textViewHour5;
    private TextView textViewHour6;
    int hourState;
    private CardView cardViewHour;
    private Handler handler;
    private Runnable runnable;
    private TextView textView3;
    private long mStartRX;
    private long rxBytes;
    private double currentRxBytes;


    public DayFragment() {
        // Required empty public constructor
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        offsetCounter = 0;
        hourState = 1;
        handler = new Handler();
        mStartRX = TrafficStats.getTotalRxBytes();
        runnable = new Runnable() {
            @Override
            public void run() {
                rxBytes = TrafficStats.getTotalRxBytes();
                currentRxBytes = (rxBytes - mStartRX)/1000.0;
                mStartRX = rxBytes;
                textView3.setText(currentRxBytes + " kbps");

                handler.postDelayed(this,1000);
            }
        };
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.detailed_analysis_fragment_day, container, false);
        textViewDate = view.findViewById(R.id.textViewDateDay);
        leftButton = view.findViewById(R.id.imageButtonDayLeft);
        rightButton = view.findViewById(R.id.imageButtonDayRight);
        hourLeftButton = view.findViewById(R.id.imageButtonHourLeft);
        hourRightButton = view.findViewById(R.id.imageButtonHourRight);
        textViewHourLabel = view.findViewById(R.id.textViewHourLabel);
        textViewHour1 = view.findViewById(R.id.textViewHour1);
        textViewHour2 = view.findViewById(R.id.textViewHour2);
        textViewHour3 = view.findViewById(R.id.textViewHour3);
        textViewHour4 = view.findViewById(R.id.textViewHour4);
        textViewHour5 = view.findViewById(R.id.textViewHour5);
        textViewHour6 = view.findViewById(R.id.textViewHour6);
        cardViewHour = view.findViewById(R.id.cardViewHour);
        textView3 = view.findViewById(R.id.textView3);

        changeDateLabel();
        rightButton.setEnabled(false);
        hourLeftButton.setEnabled(false);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToPrevious();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToNext();
            }
        });


        graph = view.findViewById((R.id.graph));
        drawGraph();

        hourLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHourToPrevious();
            }
        });

        hourRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHourToNext();
            }
        });

        handler.postDelayed(runnable,1000);

        return view;
    }

    private void  changeDateLabel(){
        String today = new SimpleDateFormat("MMMM d").format(date);
        textViewDate.setText(today);
    }

    private void changeDateToPrevious(){
        calendar.add(Calendar.DATE,-1);
        date = calendar.getTime();
        changeDateLabel();
        rightButton.setEnabled(true);
        cardViewHour.setEnabled(false);
        cardViewHour.setVisibility(View.GONE);
        offsetCounter--;
    }

    private void changeDateToNext(){
        calendar.add(Calendar.DATE,1);
        date = calendar.getTime();
        changeDateLabel();
        offsetCounter++;
        if(offsetCounter==0){
            rightButton.setEnabled(false);
            cardViewHour.setEnabled(true);
            cardViewHour.setVisibility(View.VISIBLE);
        }
    }

    private void drawGraph(){
        final double SCALE = 250.0;
        final int mBlue = getResources().getColor(R.color.blue_500);
        final int mGrey = getResources().getColor(R.color.grey_650);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0.5,0),
                new DataPoint(1.5,0),
                new DataPoint(2.5,0/SCALE),
                new DataPoint(3.5,300/SCALE)
        });

        series.setDrawDataPoints(true);   //show DataPoints
        series.setDataPointsRadius(7);
        series.setColor(mBlue);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0","2", "4", "6","8", "10","12", "14", "16","18", "20","22","24"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"0 KB", "250 KB", "500 KB","750 KB", "> 1 MB"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setXAxisBoundsManual(true); //enable custom x-axis items
        graph.getViewport().setYAxisBoundsManual(true); //enable custom y-axis items

        graph.getViewport().setMinX(0); //x-axis number of items start
        graph.getViewport().setMaxX(12);    //x-axis number of items end
        graph.getViewport().setMinY(0); //y-axis number of items start
        graph.getViewport().setMaxY(4); //y-axis number of items end

        graph.getGridLabelRenderer().setHumanRounding(false);   //don't skip x-values labels

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);    //no grid lines
        //graph.getViewport().setDrawBorder(true);    //show X-Y axis
        //graph.getViewport().setBorderColor(mGrey);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);    //X-Y axis thin lines

        graph.getGridLabelRenderer().setVerticalLabelsColor(mGrey);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(mGrey);
        graph.getGridLabelRenderer().setTextSize(20f);  //labels text size
        graph.getGridLabelRenderer().reloadStyles();

        //graph.refreshDrawableState();

        //graph.getViewport().setScrollable(true); // enables horizontal scrolling
    }

    private void changeHourToNext(){
        switch(hourState){
            case 1:
                textViewHourLabel.setText("6:00 AM - 12:00 NN");
                textViewHour1.setText("6:00 AM - 7:00 AM");
                textViewHour2.setText("7:00 AM - 8:00 AM");
                textViewHour3.setText("8:00 AM - 9:00 AM");
                textViewHour4.setText("9:00 AM - 10:00 AM");
                textViewHour5.setText("10:00 AM - 11:00 AM");
                textViewHour6.setText("11:00 AM - 12:00 NN");
                hourState = 2;
                hourLeftButton.setEnabled(true);
                break;
            case 2:
                textViewHourLabel.setText("12:00 NN - 6:00 PM");
                textViewHour1.setText("12:00 NN - 1:00 PM");
                textViewHour2.setText("1:00 PM - 2:00 PM");
                textViewHour3.setText("2:00 PM - 3:00 PM");
                textViewHour4.setText("3:00 PM - 4:00 PM");
                textViewHour5.setText("4:00 PM - 5:00 PM");
                textViewHour6.setText("5:00 PM - 6:00 PM");
                hourState = 3;
                break;
            case 3:
                textViewHourLabel.setText("6:00 PM - 12:00 MN");
                textViewHour1.setText("6:00 PM - 7:00 PM");
                textViewHour2.setText("7:00 PM - 8:00 PM");
                textViewHour3.setText("8:00 PM - 9:00 PM");
                textViewHour4.setText("9:00 PM - 10:00 PM");
                textViewHour5.setText("10:00 PM - 11:00 PM");
                textViewHour6.setText("11:00 PM - 12:00 MN");
                hourState = 4;
                hourRightButton.setEnabled(false);
                break;
            case 4:
                break;
        }
    }

    private void changeHourToPrevious(){
        switch (hourState){
            case 1:
                break;
            case 2:
                textViewHourLabel.setText("12:00 MN - 6:00 AM");
                textViewHour1.setText("12:00 MN - 1:00 AM");
                textViewHour2.setText("1:00 AM - 2:00 AM");
                textViewHour3.setText("2:00 AM - 3:00 AM");
                textViewHour4.setText("3:00 AM - 4:00 AM");
                textViewHour5.setText("4:00 AM - 5:00 AM");
                textViewHour6.setText("5:00 AM - 6:00 AM");
                hourState = 1;
                hourLeftButton.setEnabled(false);
                break;
            case 3:
                textViewHourLabel.setText("6:00 AM - 12:00 NN");
                textViewHour1.setText("6:00 AM - 7:00 AM");
                textViewHour2.setText("7:00 AM - 8:00 AM");
                textViewHour3.setText("8:00 AM - 9:00 AM");
                textViewHour4.setText("9:00 AM - 10:00 AM");
                textViewHour5.setText("10:00 AM - 11:00 AM");
                textViewHour6.setText("11:00 AM - 12:00 NN");
                hourState = 2;
                break;
            case 4:
                textViewHourLabel.setText("12:00 NN - 6:00 PM");
                textViewHour1.setText("12:00 NN - 1:00 PM");
                textViewHour2.setText("1:00 PM - 2:00 PM");
                textViewHour3.setText("2:00 PM - 3:00 PM");
                textViewHour4.setText("3:00 PM - 4:00 PM");
                textViewHour5.setText("4:00 PM - 5:00 PM");
                textViewHour6.setText("5:00 PM - 6:00 PM");
                hourState = 3;
                hourRightButton.setEnabled(true);
                break;
        }
    }
}