package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.model.StockQuote;
import com.udacity.stockhawk.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ListWidgetRemoteViewsServices extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() { }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[] {}),
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                long id = data.getLong(Contract.Quote.POSITION_ID);
                String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);

                DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                DecimalFormat dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus.setPositivePrefix("+$");

                float price = data.getFloat(Contract.Quote.POSITION_PRICE);
                float absoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                String history = data.getString(Contract.Quote.POSITION_HISTORY);

                StockQuote quote = new StockQuote(id, symbol, price, absoluteChange, percentageChange, history);

                String priceFormatted = dollarFormat.format(price);
                String changeFormatted = dollarFormatWithPlus.format(absoluteChange);

                views.setTextViewText(R.id.widget_list_symbol, symbol);
                views.setContentDescription(R.id.widget_list_symbol, symbol);

                views.setTextViewText(R.id.widget_list_price, priceFormatted);
                views.setContentDescription(R.id.widget_list_price, priceFormatted);

                views.setTextViewText(R.id.widget_list_change, changeFormatted);
                views.setContentDescription(R.id.widget_list_change, changeFormatted);

                if (absoluteChange > 0) {
                    views.setInt(R.id.widget_list_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.widget_list_change, "setBackgroundResource",R.drawable.percent_change_pill_red);
                }

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(MainActivity.DETAIL_STOCK_QUOTE, quote);

                views.setOnClickFillInIntent(R.id.widget_list_item_main, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(Contract.Quote.POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
