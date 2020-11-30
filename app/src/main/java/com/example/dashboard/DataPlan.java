package com.example.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class DataPlan extends AppCompatActivity {

    AnyChartView anyChartView;

    String[] data = {"Excess data", "Planned Today", "Total Planned"};
    int[] plan = {800, 500, 1600};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_plan);

        anyChartView = findViewById(R.id.any_chart_view);

        setupPieChart();
    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i=0; i<3; i++){
            dataEntries.add(new ValueDataEntry(data[i], plan[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}