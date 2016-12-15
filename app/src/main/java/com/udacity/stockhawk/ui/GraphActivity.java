package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.collect.Lists;
import com.udacity.stockhawk.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {

    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        chart = (LineChart) findViewById(R.id.chart);
        String history = getIntent().getStringExtra("history");
        String symbol = getIntent().getStringExtra("symbol");

        TextView stock_name = (TextView) findViewById(R.id.stock_graph_name);
        stock_name.setText(symbol);
        List<String> historyListByInterval = Lists.reverse(Arrays.asList(history.split("\n")));
        List<Entry> entries = new ArrayList<>();
        int y = 1;
        for (String x : historyListByInterval) {
            String[] values = x.split(",");
            entries.add(new Entry((float) y, Float.valueOf(values[1].trim())));
            y++;
        }

        String endDate = historyListByInterval.get(0).split(",")[0];
        String beginningDate = historyListByInterval.get(historyListByInterval.size() - 1).split(",")[0];
        Date end = new Date(Long.parseLong(endDate));
        Date begin = new Date(Long.parseLong(beginningDate));
        DateFormat df = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());


        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.currency_symbol));
        dataSet.setColor(getColor(R.color.colorAccent));
        dataSet.setValueTextColor(getColor(R.color.colorAccent));

        LineData lineData = new LineData(dataSet);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false);
        Description description = new Description();
        String historyDates = df.format(begin) + " - " + df.format(end);
        description.setText(historyDates);
        chart.setContentDescription(symbol + " " + getString(R.string.chart_content_description) + " " + historyDates);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        chart.setData(lineData);
        chart.setFitsSystemWindows(true);
        chart.getAxisLeft().setEnabled(false);
        chart.setDescription(description);
        chart.setBackgroundColor(getColor(R.color.colorPrimary));
        chart.invalidate();
    }

}
