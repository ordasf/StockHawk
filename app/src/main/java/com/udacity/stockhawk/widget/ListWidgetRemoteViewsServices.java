package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.lang.annotation.Target;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

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
                        Contract.Quote._ID + " ASC");
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

                String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);

                DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                DecimalFormat dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus.setPositivePrefix("+$");

                float price = data.getFloat(Contract.Quote.POSITION_PRICE);
                float change = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);

                String priceFormatted = dollarFormat.format(price);
                String changeFormatted = dollarFormatWithPlus.format(change);

                views.setTextViewText(R.id.widget_list_symbol, symbol);
                views.setContentDescription(R.id.widget_list_symbol, symbol);

                views.setTextViewText(R.id.widget_list_price, priceFormatted);
                views.setContentDescription(R.id.widget_list_price, priceFormatted);

                views.setTextViewText(R.id.widget_list_change, changeFormatted);
                views.setContentDescription(R.id.widget_list_change, changeFormatted);

                final Intent fillInIntent = new Intent();
                Uri quoteUri = Contract.Quote.makeUriForStock(symbol);
                fillInIntent.setData(quoteUri);
                views.setOnClickFillInIntent(R.id.widget_list_main, fillInIntent);
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
