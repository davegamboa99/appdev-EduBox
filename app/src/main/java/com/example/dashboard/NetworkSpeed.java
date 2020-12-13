package com.example.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
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

public class NetworkSpeed extends AppCompatActivity {
    double temp = 0;
    SharedPreferences sharedpreferences;
    String toStore;
    TextView dlSpeed;
    public static final String Data = "dataKey";
    public static final String mypreference = "mypref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_speed);

        //Graph
        GraphView graphView = (GraphView) findViewById(R.id.graphview);
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


        Button btn = (Button) findViewById(R.id.chkSpeed);
        //upSpeed = (TextView)findViewById(R.id.upSpeed);
        dlSpeed = (TextView) findViewById(R.id.dlSpeed);
        //TextView test = (TextView)findViewById(R.id.test);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Data)) {
            dlSpeed.setText(sharedpreferences.getString(Data, ""));
        }
        TextView dateToday = (TextView) findViewById(R.id.dateToday);
        String date_n = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());
        dateToday.setText("Today, " + date_n);


        InternetSpeedBuilder builder = new InternetSpeedBuilder(NetworkSpeed.this);
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


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.start("ftp://speedtest.tele2.net/1MB.zip", 1); //speedtest dl server : downloads 1mb file to determine speed
            }
        });


        AnyChartView anyChartView;

        String[] data = {"Excess data", "Planned Today", "Total Planned"};
        int[] plan = {800, 500, 1600};

            anyChartView = findViewById(R.id.chartAV);
            Pie pie = AnyChart.pie();
            List<DataEntry> dataEntries = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                dataEntries.add(new ValueDataEntry(data[i], plan[i]));
            }

            pie.data(dataEntries);
            anyChartView.setChart(pie);


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
