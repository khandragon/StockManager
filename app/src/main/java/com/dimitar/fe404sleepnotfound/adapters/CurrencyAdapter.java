package com.dimitar.fe404sleepnotfound.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<String> {

    String TAG = "Currencys";
    Context mContext;
    int mResource;

    public CurrencyAdapter(Context context, int resource, ArrayList<String> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView name = convertView.findViewById(R.id.CurrencyName);
        name.setText(getItem(position));

        return convertView;


    }
}
