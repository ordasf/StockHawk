package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailStockActivity extends AppCompatActivity {

    @BindView(R.id.tv_detail_stock)
    TextView detailStockTextView;

    @BindView(R.id.tv_history_detail_stock)
    TextView historyStockTextView;

    @BindView(R.id.chart_detail_stock)
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_stock);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(MainActivity.DETAIL_STOCK_SYMBOL)) {
                String symbol = extras.getString(MainActivity.DETAIL_STOCK_SYMBOL);
                detailStockTextView.setText(symbol);
                detailStockTextView.setContentDescription(symbol);
            }
            if (extras.containsKey(MainActivity.HISTORY_STOCK_SYMBOL)) {
                String history = extras.getString(MainActivity.HISTORY_STOCK_SYMBOL);
                //historyStockTextView.setText(history);
                //historyStockTextView.setContentDescription(history);
                CSVReader reader = new CSVReader(new CharArrayReader(history.toCharArray()));
                try {
                    List<String[]> quotesOverTime = reader.readAll();
                    System.out.println(quotesOverTime);
                    List<Entry> entries = new ArrayList<>();
                    for (String[] historicQuote : quotesOverTime) {
                        long millis = Long.parseLong(historicQuote[0]);
                        float price = Float.parseFloat(historicQuote[1]);
                        entries.add(new Entry(millis, price));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "caca");
                    dataSet.setColor(Color.CYAN);
                    dataSet.setValueTextColor(Color.GREEN);
                    dataSet.setLineWidth(10.0F);
                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);
                    lineChart.invalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // TODO Extract the historic crap and paint it in the activity
        // TODO We are getting the symbol, but it might be better to get the historic data
        // TODO find the library to paint charts
        // TODO Paint it, I don't mind about RTL and shit like that at this moment
    }
}
