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
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Calendar calendar;
    Date month;
    TextView textViewDate;
    String[] horizontalLabels;
    ImageButton rightButton;
    ImageButton leftButton;
    GraphView graph;
    int offsetCounter;

    public MonthFragment() {
        // Required empty public constructor
        calendar = Calendar.getInstance();
        month = calendar.getTime();
        offsetCounter = 0;
        horizontalLabels = new String[calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
        changeHorizontalLabels();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
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
        View view = inflater.inflate(R.layout.detailed_analysis_fragment_month, container, false);
        textViewDate = view.findViewById(R.id.textViewDateMonth);
        leftButton = view.findViewById(R.id.imageButtonMonthLeft);
        rightButton = view.findViewById(R.id.imageButtonMonthRight);

        changeMonthLabel();
        rightButton.setEnabled(false);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToPreviousMonth();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDateToNextMonth();
            }
        });

        graph = view.findViewById((R.id.graphMonth));
        drawGraph();


        return view;
    }

    private void  changeMonthLabel(){
        String monthYear = new SimpleDateFormat("MMMM yyyy").format(month);
        textViewDate.setText(monthYear);
    }

    private void changeDateToPreviousMonth(){
        calendar.add(Calendar.MONTH,-1);
        month = calendar.getTime();
        changeMonthLabel();
        rightButton.setEnabled(true);
        offsetCounter--;
        changeHorizontalLabels();
        drawGraph();
    }

    private void changeDateToNextMonth() {
        calendar.add(Calendar.MONTH,1);
        month = calendar.getTime();
        changeMonthLabel();
        offsetCounter++;
        if (offsetCounter==0) {
            rightButton.setEnabled(false);
        }
        changeHorizontalLabels();
        drawGraph();
    }

    private void changeHorizontalLabels(){
        Calendar temp = (Calendar)calendar.clone();
        for(int i=0, x=0;i<horizontalLabels.length;i++){
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
                new DataPoint(1,240/SCALE),
                new DataPoint(2,350/SCALE),
                new DataPoint(3,565/SCALE),
                new DataPoint(4,520/SCALE),
                new DataPoint(5,400/SCALE),
                new DataPoint(6,380/SCALE),
                new DataPoint(7,240/SCALE),
                new DataPoint(8,350/SCALE),
                new DataPoint(9,565/SCALE),
                new DataPoint(10,520/SCALE),
                new DataPoint(11,400/SCALE),
                new DataPoint(12,520/SCALE),
                new DataPoint(13,400/SCALE)
        });
        //series.setDrawDataPoints(true);   //show DataPoints
        series.setColor(mBlue);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {"1","2", "3", "4","5", "6","7","8","9","10","11","12","13","14","15","16","17","18", "19", "20","21", "22","23","24","25","26","27","28","29","30"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"0 KB", "250 KB", "500 KB","750 KB", "> 1 MB"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setXAxisBoundsManual(true); //enable custom x-axis items
        graph.getViewport().setYAxisBoundsManual(true); //enable custom y-axis items

        graph.getViewport().setMinX(1); //x-axis number of items start
        graph.getViewport().setMaxX(5);    //x-axis number of items end
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

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
    }
}