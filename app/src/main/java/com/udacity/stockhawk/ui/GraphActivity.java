package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.DbHelper;

public class GraphActivity extends AppCompatActivity {

    LineChart chart;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        chart = (LineChart) findViewById(R.id.chart);
        String stock = getIntent().getStringExtra("id");
//        Toast.makeText(this,"The Item Clicked is: " + stock,Toast.LENGTH_SHORT).show();
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        String filter = "_ID=" + stock;
        String[] columns = {
                Contract.Quote._ID
        };
        cursor = db.query(Contract.Quote.TABLE_NAME,
                columns, filter, null, null, null, null);
        if (cursor.moveToFirst()) {
            Toast.makeText(this,"The Item Clicked is: " + cursor,Toast.LENGTH_SHORT).show();

            cursor.close();
            db.close();
        }


    }

}
