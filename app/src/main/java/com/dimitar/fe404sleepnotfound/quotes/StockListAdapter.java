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

/**
 * Adapter for the Recycler view in StockList
 *
 * @Author: Saad Khan
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockAdapterViewHolder> {

    private static final String TAG = "StockListAdapter";
    private final Context mContext;
    public List<String> mDataset;
    private LayoutInflater mInflater;

    /**
     * base consturctor to set the data set and the context of the stock list
     *
     * @param myDataset
     * @param context
     */
    public StockListAdapter(ArrayList<String> myDataset, Context context) {
        this.mDataset = myDataset;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    /**
     * inflates the row of from the xml layout defined
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public StockAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_stock_layout, viewGroup, false);
        return new StockAdapterViewHolder(view);

    }

    /**
     * binds the data to the textview in each row
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(StockAdapterViewHolder holder, int position) {
        String ticker = mDataset.get(position);
        holder.tickerName.setText(ticker);
    }


    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * add item to dataset, notify dataset has changed and add to shared preferences
     *
     * @param lastSearch
     */
    public void add(String lastSearch) {
        mDataset.add(lastSearch);
        this.notifyDataSetChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", new HashSet<String>(mDataset));
        editor.commit();
    }

    /**
     * remove item to dataset, notify dataset has changed and add to shared preferences
     *
     * @param ticker
     */
    public void remove(String ticker) {
        mDataset.remove(ticker);
        this.notifyDataSetChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", new HashSet<String>(mDataset));
        editor.commit();
    }

    /**
     * check if ticker is already in the list or not
     *
     * @param lastSearch
     * @return true if it is in the dataset and false if it is not
     */
    public boolean contains(String lastSearch) {
        return mDataset.contains(lastSearch);
    }

    /**
     * store and recycles he views as the screen moves
     */
    public class StockAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tickerName;
        Button removeBtn;

        public StockAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tickerName = itemView.findViewById(R.id.tickerName);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            itemView.setOnClickListener(this);
        }

        public String getTickerName() {
            return tickerName.getText().toString();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, StockInfo.class);

            String ticker = getTickerName();
            Log.i(TAG, "here we go lets see if this works" + ticker);
            intent.putExtra("ticker", ticker);
            mContext.startActivity(intent);
        }
    }

}