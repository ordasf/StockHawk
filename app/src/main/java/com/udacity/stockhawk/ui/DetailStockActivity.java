package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailStockActivity extends AppCompatActivity {

    @BindView(R.id.tv_symbol_detail_stock)
    TextView symbolTextView;

    @BindView(R.id.tv_price_detail_stock)
    TextView priceTextView;

    @BindView(R.id.tv_absolute_change_detail_stock)
    TextView absoluteChangeTextView;

    @BindView(R.id.tv_percentage_change_detail_stock)
    TextView percentageChangeTextView;

    @BindView(R.id.chart_detail_stock)
    LineChart lineChart;

    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    DetailStockActivity() {
        super();

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_stock);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(MainActivity.DETAIL_STOCK_QUOTE)) {

            StockQuote quote = extras.getParcelable(MainActivity.DETAIL_STOCK_QUOTE);
            String history = quote.getHistory();

            // Populate the data into the Text Views
            symbolTextView.setText(quote.getSymbol());
            symbolTextView.setContentDescription(quote.getSymbol());

            priceTextView.setText(dollarFormat.format(quote.getPrice()));
            priceTextView.setContentDescription(dollarFormat.format(quote.getPrice()));

            absoluteChangeTextView.setText(dollarFormat.format(quote.getAbsoluteChange()));
            absoluteChangeTextView.setContentDescription(dollarFormat.format(quote.getAbsoluteChange()));

            percentageChangeTextView.setText(percentageFormat.format(quote.getPercentageChange() / 100));
            percentageChangeTextView.setContentDescription(percentageFormat.format(quote.getPercentageChange() / 100));

            List<String[]> quotesOverTime = readHistoricData(history);

            if (quotesOverTime == null) {
                return;
            }

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

    private List<String[]> readHistoricData(String history) {

        CSVReader reader = new CSVReader(new CharArrayReader(history.toCharArray()));
        List<String[]> quotesOverTime = null;
        try {
            quotesOverTime = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Collections.reverse(quotesOverTime);

        return quotesOverTime;
    }

}
