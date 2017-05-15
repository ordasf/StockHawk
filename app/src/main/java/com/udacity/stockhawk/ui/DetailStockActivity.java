package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.model.StockQuote;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        if (extras != null && extras.containsKey(MainActivity.DETAIL_STOCK_QUOTE)) {

            StockQuote quote = extras.getParcelable(MainActivity.DETAIL_STOCK_QUOTE);
            String history = quote.getHistory();

            CSVReader reader = new CSVReader(new CharArrayReader(history.toCharArray()));
            List<String[]> quotesOverTime = null;
            try {
                quotesOverTime = reader.readAll();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (quotesOverTime == null) {
                return;
            }

            Collections.reverse(quotesOverTime);

            List<Entry> entries = new ArrayList<>();
            final String[] dateLabels = new String[quotesOverTime.size()];
            for (int i = 0; i < quotesOverTime.size(); i++) {
                String[] historicQuote = quotesOverTime.get(i);
                dateLabels[i] = DateFormat.format("dd/MM/yyyy", Long.parseLong(historicQuote[0])).toString();
                entries.add(new Entry(i, Float.parseFloat(historicQuote[1])));
            }
            LineDataSet dataSet = new LineDataSet(entries, "caca");
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.setBackgroundColor(Color.WHITE);

            // Redraw
            lineChart.invalidate();

            // Put the correct labels in the x axis
            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return dateLabels[(int) value];
                }
            };

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

        }

    }
}
