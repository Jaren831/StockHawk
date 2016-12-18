package com.udacity.stockhawk.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Jaren Lynch on 12/17/2016.
 */

public  class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private final ArrayList<ListItem> listItemList = new ArrayList<>();
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;
    private Context context = null;
    private Cursor cursor;
    private static final String[] STOCK_COLUMNS = {
            Contract.Quote.TABLE_NAME + "." + Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
            Contract.Quote.COLUMN_HISTORY
    };


    public ListProvider(Context context, Intent intent) {
        this.context = context;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        dollarFormatWithPlus.setPositivePrefix("+" + context.getString(R.string.currency_symbol));
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");

        populateListItem();
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private String getSymbolAtPosition(int position) {

        cursor.moveToPosition(position);
        return cursor.getString(Contract.Quote.POSITION_SYMBOL);
    }
    private String getPriceAtPosition(int position) {
        cursor.moveToPosition(position);
        return dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE));
    }
    private String getRawAbsoluteChangeAtPosition(int position) {
        cursor.moveToPosition(position);
        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        return dollarFormatWithPlus.format(rawAbsoluteChange);
    }
    private String getPercentageChangeAtPosition(int position) {
        cursor.moveToPosition(position);
        float rawPercentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
        return percentageFormat.format(rawPercentageChange / 100);
    }

    private void populateListItem() {
        for (int i = 0; i < getCount(); i++) {
            String symbol = getSymbolAtPosition(i);
            ListItem listItem = new ListItem();
            listItem.symbol = symbol;
            listItem.price = getPriceAtPosition(i);
            listItem.absoluteChange = getRawAbsoluteChangeAtPosition(i);
            listItem.percentChange = getPercentageChangeAtPosition(i);
            listItemList.add(listItem);
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.stock_widget_item);
        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.widget_symbol, listItem.symbol);
        remoteView.setTextViewText(R.id.widget_price, listItem.price);
        remoteView.setTextViewText(R.id.widget_absolute_change, listItem.absoluteChange);
        remoteView.setTextViewText(R.id.widget_percent_change, listItem.percentChange);

        if (Float.parseFloat(listItem.absoluteChange) > 0) {
            remoteView.setInt(R.id.widget_absolute_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            remoteView.setInt(R.id.widget_percent_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            remoteView.setInt(R.id.widget_absolute_change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            remoteView.setInt(R.id.widget_percent_change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
//        ContentProvider contentProvider = new StockProvider();
        Uri stockUri = Contract.Quote.URI;
        cursor = context.getContentResolver().query(stockUri,
                STOCK_COLUMNS,
                null,
                null,
                null);
        setCursor(cursor);
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }
}
