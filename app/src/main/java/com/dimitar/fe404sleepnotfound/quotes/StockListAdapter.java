package com.dimitar.fe404sleepnotfound.quotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockAdapterViewHolder> {

    private static final String TAG = "StockListAdapter";
    private final Context myContext;
    public List<String> mDataset;
    private LayoutInflater mInflater;

    public StockListAdapter(ArrayList<String> myDataset, Context context) {
        this.mDataset = myDataset;
        this.myContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public StockAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_stock_layout, viewGroup, false);
        return new StockAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(StockAdapterViewHolder holder, int position) {
        String ticker = mDataset.get(position);
        holder.tickerName.setText(ticker);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(String lastSearch) {
        mDataset.add(lastSearch);
        this.notifyDataSetChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", new HashSet<String>(mDataset));
        editor.commit();
    }

    public void remove(String ticker) {
        mDataset.remove(ticker);
        this.notifyDataSetChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", new HashSet<String>(mDataset));
        editor.commit();
    }

    public boolean contains(String lastSearch) {
        return mDataset.contains(lastSearch);
    }

    public class StockAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tickerName;
        Button removeBtn;

        public StockAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tickerName = itemView.findViewById(R.id.tickerName);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            itemView.setOnClickListener(this);
//            removeBtn.setOnClickListener(this);
        }

        public String getTickerName() {
            return tickerName.getText().toString();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(myContext, StockInfo.class);

            String ticker = getTickerName();
            Log.i(TAG, "here we go lets see if this works" + ticker);
            intent.putExtra("ticker", ticker);
            myContext.startActivity(intent);

            Log.i(TAG, getTickerName());
        }
    }

}