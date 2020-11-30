package com.example.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class NetworkSpeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_speed);

        GraphView graphView = (GraphView)findViewById(R.id.graphview);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
            new DataPoint(0,1),
            new DataPoint(4,5),
            new DataPoint(7,6),

        });

        graphView.addSeries(series);
    }
}