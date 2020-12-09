package com.elearneur.edubox.detailedanalysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elearneur.edubox.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Calendar calendarToday;
    Calendar calendarSunday;
    Calendar calendarSaturday;
    Date dateToday;
    Date dateSunday;
    Date dateSaturday;
    String sundayToday;
    String saturdayToday;
    String today;
    TextView textViewDate;
    String[] horizontalLabels;
    ImageButton rightButton;
    ImageButton leftButton;
    int offsetCounter;
    GraphView graph;

    public WeekFragment() {
        // Required empty public constructor
        calendarToday = Calendar.getInstance();
        calendarSunday = Calendar.getInstance();
        calendarSaturday = Calendar.getInstance();

        calendarSunday.add(Calendar.DATE, -calendarToday.get(Calendar.DAY_OF_WEEK)+1);
        calendarSaturday.add(Calendar.DATE, 7-calendarToday.get(Calendar.DAY_OF_WEEK));
        dateToday = calendarToday.getTime();
        dateSunday = calendarSunday.getTime();
        dateSaturday = calendarSaturday.getTime();
        sundayToday = new SimpleDateFormat("yyyy-MM-dd").format(dateSunday);
        saturdayToday = new SimpleDateFormat("yyyy-MM-dd").format(dateSaturday);
        today = new SimpleDateFormat("yyyy-MM-dd").format(dateToday);

        horizontalLabels = new String[7];
        changeHorizontalLabels();

        offsetCounter = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
        View view = inflater.inflate(R.layout.detailed_analysis_fragment_week, container, false);
        textViewDate = view.findViewById(R.id.textViewDateWeek);
        leftButton = view.findViewById(R.id.imageButtonWeekLeft);
        rightButton = view.findViewById(R.id.imageButtonWeekRight);

        changeWeekLabel();
        rightButton.setEnabled(false);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToPreviousWeek();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToNextWeek();
            }
        });

        graph = view.findViewById((R.id.graphWeek));
        drawGraph();

        return view;
    }

    private void  changeWeekLabel(){
        String sunday = new SimpleDateFormat("MMMM d").format(dateSunday);
        String saturday = new SimpleDateFormat("MMMM d").format(dateSaturday);
        textViewDate.setText(sunday+" - "+saturday);
    }

    private void changeDateToPreviousWeek(){
        calendarSunday.add(Calendar.DATE,-7);
        dateSunday = calendarSunday.getTime();
        calendarSaturday.add(Calendar.DATE,-7);
        dateSaturday = calendarSaturday.getTime();
        changeWeekLabel();
        rightButton.setEnabled(true);
        offsetCounter--;
        changeHorizontalLabels();
        drawGraph();
    }

    private void changeDateToNextWeek() {
        calendarSunday.add(Calendar.DATE,+7);
        dateSunday = calendarSunday.getTime();
        calendarSaturday.add(Calendar.DATE,+7);
        dateSaturday = calendarSaturday.getTime();
        changeWeekLabel();
        offsetCounter++;
        if (offsetCounter==0) {
            rightButton.setEnabled(false);
        }
        changeHorizontalLabels();
        drawGraph();
    }

    private void changeHorizontalLabels(){
        Calendar temp = (Calendar)calendarSunday.clone();
        for(int i=0, x=0;i<7;i++){
            temp.add(Calendar.DATE,x);
            horizontalLabels[i] = new SimpleDateFormat("d").format(temp.getTime());
            x=1;
        }
    }

    private void drawGraph(){
        final double SCALE = 250.0;
        final int mBlue = getResources().getColor(R.color.blue_500);
        final int mGrey = getResources().getColor(R.color.grey_650);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0,380/SCALE),
                new DataPoint(1,240/SCALE),
                new DataPoint(2,350/SCALE),
                new DataPoint(3,565/SCALE),
                new DataPoint(4,520/SCALE),
                new DataPoint(5,400/SCALE)
        });
        //series.setDrawDataPoints(true);   //show DataPoints
        series.setColor(mBlue);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(horizontalLabels);
        staticLabelsFormatter.setVerticalLabels(new String[] {"0 KB", "250 KB", "500 KB","750 KB", "> 1 MB"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setXAxisBoundsManual(true); //enable custom x-axis items
        graph.getViewport().setYAxisBoundsManual(true); //enable custom y-axis items

        graph.getViewport().setMinX(0); //x-axis number of items start
        graph.getViewport().setMaxX(6);    //x-axis number of items end
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

        //graph.getViewport().setScrollable(true); // enables horizontal scrolling
    }
}