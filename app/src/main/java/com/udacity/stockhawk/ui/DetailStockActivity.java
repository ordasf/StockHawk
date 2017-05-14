package com.udacity.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;

public class DetailStockActivity extends AppCompatActivity {

    @BindView(R.id.tv_detail_stock)
    TextView detailStockTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stock);

        if (savedInstanceState != null && savedInstanceState.containsKey(MainActivity.DETAIL_STOCK_SYMBOL)) {
            String symbol = savedInstanceState.getString(MainActivity.DETAIL_STOCK_SYMBOL);
            detailStockTextView.setText(symbol);
            detailStockTextView.setContentDescription(symbol);
            // TODO Extract the historic crap and paint it in the activity
            // TODO We are getting the symbol, but it might be better to get the historic data
            // TODO find the library to paint charts
            // TODO Paint it, I don't mind about RTL and shit like that at this moment
        }
    }
}
