package com.elearneur.edubox.ui.bottom_nav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.elearneur.edubox.R;
import com.elearneur.edubox.detailedanalysis.DetailedAnalysisActivity;
import com.example.internet_speed_testing.InternetSpeedBuilder;
import com.example.internet_speed_testing.ProgressionModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabDashboardFragment newInstance(String param1, String param2) {
        TabDashboardFragment fragment = new TabDashboardFragment();
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

    @Override   //transition behave swipe right on back key
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_analysis:
                intent = new Intent(getActivity(), DetailedAnalysisActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_analysis, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_dashboard_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");

        SharedPreferences sharedpreferences;
        TextView dlSpeed;
        final String Data = "dataKey";
        final String mypreference = "mypref";
        Button btn;

        GraphView graphView = (GraphView) view.findViewById(R.id.graphview);
        final double SCALE = 250.0;
        final int mBlue = getResources().getColor(R.color.blue_500);
        final int mGrey = getResources().getColor(R.color.grey_650);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 240 / SCALE),
                new DataPoint(2, 350 / SCALE),
                new DataPoint(3, 565 / SCALE),
                new DataPoint(4, 520 / SCALE),

        });
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(7);
        series.setColor(mBlue);

        graphView.addSeries(series);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"0 KB", "250 KB", "500 KB", "750 KB", "> 1 MB"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.getViewport().setXAxisBoundsManual(true); //enable custom x-axis items
        graphView.getViewport().setYAxisBoundsManual(true); //enable custom y-axis items

        graphView.getViewport().setMinX(1); //x-axis number of items start
        graphView.getViewport().setMaxX(6);    //x-axis number of items end
        graphView.getViewport().setMinY(0); //y-axis number of items start
        graphView.getViewport().setMaxY(4); //y-axis number of items end

        graphView.getGridLabelRenderer().setHumanRounding(false);   //don't skip x-values labels

        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.getGridLabelRenderer().setHighlightZeroLines(false);    //X-Y axis thin lines

        graphView.getGridLabelRenderer().setVerticalLabelsColor(mGrey);
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(mGrey);
        graphView.getGridLabelRenderer().setTextSize(20f);  //labels text size
        graphView.getGridLabelRenderer().reloadStyles();

         btn = (Button) view.findViewById(R.id.chkSpeed);
        dlSpeed = (TextView) view.findViewById(R.id.dlSpeed);

        sharedpreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Data)) {
            dlSpeed.setText(sharedpreferences.getString(Data, ""));
        }

        TextView dateToday = (TextView) view.findViewById(R.id.dateToday);
        String date_n = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());
        dateToday.setText("Today, " + date_n);





        InternetSpeedBuilder builder = new InternetSpeedBuilder(getActivity());
        builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
            @Override
            public void onDownloadProgress(int count, final ProgressionModel progressModel) {

                BigDecimal bigDecimal = new BigDecimal("" + progressModel.getDownloadSpeed());
                float finalDownload = (bigDecimal.longValue() / 1000000);

                BigDecimal bd = progressModel.getDownloadSpeed();
                final double d = bd.doubleValue();


                dlSpeed.setText("" + formatFileSize(d));
                //test.setText(""+formatFileSize(d));


                if (progressModel.getProgressDownload() == 100f) {
                    //test.setText(""+formatFileSize(d));
                    String n = dlSpeed.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Data, n);
                    editor.commit();
                    // toStore = dlSpeed.getText().toString();
                }


            }

            @Override
            public void onUploadProgress(int count, final ProgressionModel progressModel) {
            }

            @Override
            public void onTotalProgress(int count, final ProgressionModel progressModel) {
            }


        });

        btn.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.start("ftp://speedtest.tele2.net/1MB.zip", 1); //speedtest dl server : downloads 1mb file to determine speed
            }
        }));


        //Data Plan Chart
        AnyChartView anyChartView;

        String[] data = {"Excess data", "Planned Today", "Total Planned"};
        int[] plan = {800, 500, 1600};

        anyChartView = view.findViewById(R.id.chartAV);
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dataEntries.add(new ValueDataEntry(data[i], plan[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);









        return view;
    }

    public static String formatFileSize(double size) {

        String hrSize;
        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" ");
        } else if ( g>1 ) {
            hrSize = dec.format(g);
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" mb/s");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" kb/s");
        } else {
            hrSize = dec.format(b);
        }

        return hrSize;
    }

}