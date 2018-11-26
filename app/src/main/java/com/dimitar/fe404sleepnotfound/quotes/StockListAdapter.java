package com.dimitar.fe404sleepnotfound.quotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StockListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StockListAdapter";
    private Context mContext;
    private int mResource;

    public StockListAdapter(Context context, int resource, ArrayList<String> strings) {
        super(context,resource,strings);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String ticker = getItem(position);
        Log.i(TAG, "got here somehow "+ticker);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.tickerName);
        textView.setText(ticker);
        return convertView;
    }
}
