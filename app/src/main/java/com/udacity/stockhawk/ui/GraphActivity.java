package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.DbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    LineChart chart;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TextView historyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        chart = (LineChart) findViewById(R.id.chart);
        String history = getIntent().getStringExtra("history");
        List<String> historyListByInterval = Arrays.asList(history.split("\n"));

        List<Entry> entries = new ArrayList<>();
        for (String x : historyListByInterval) {
            String[] values = x.split(",");
            entries.add(new Entry(Float.valueOf(values[0]), Float.valueOf(values[1])));
        }

//        YourData[] dataObjects = ...;
//
//        List<Entry> entries = new ArrayList<Entry>();
//
//        for (YourData data : dataObjects) {
//
//            // turn your data into Entry objects
//            entries.add(new Entry(data.getValueX(), data.getValueY()));
//        }
        LineDataSet dataSet = new LineDataSet(entries, "Stock");
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setValueTextColor(R.color.colorAccent);

        LineData lineData = new LineData(dataSet);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(200000000000f);
        xAxis.setAxisMinimum(100000000000f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart.setData(lineData);
        chart.setFitsSystemWindows(true);
        chart.getAxisLeft().setEnabled(false);
        chart.invalidate();



        System.out.print(entries);
////        Toast.makeText(this,"The Item Clicked is: " + stock,Toast.LENGTH_SHORT).show();
//        historyText = (TextView) findViewById(R.id.history_text);
//        historyText.setText(historySplit);
    }

}
