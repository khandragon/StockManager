package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.R;

import java.util.ArrayList;

/**
 * Custom RecyclerView Adapter for the currency fragments
 * will fill the recyclerView with the data
 * will set up a onClick listener using a custom interface
 *
 * @author Jamroa
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String TAG = "RecycleAdapter";

    private ArrayList<String> currencys;
    private Context mContext;
    private RecyclerAdapterListener mListener;

    /**
     *Custom listener for a click on a the RecyclerView
     */
    public interface RecyclerAdapterListener{
        void onItemClick(String item);
    }

    /**
     * Sets the listener for the recyclerView adapter
     * @param listener
     */
    public void setRecyclerAdapterListener(RecyclerAdapterListener listener){
        mListener = listener;
    }

    /**
     * Creates the recyclerView adapter
     *
     * @param currencys a array list of string that contain the currencies and ticker symbol
     * @param mContext
     */
    public RecyclerAdapter(ArrayList<String> currencys, Context mContext) {
        this.currencys = currencys;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_currency_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG,"viewHolderCalled");
        holder.currencyTicker.setText(currencys.get(position).split(",")[0] + ":");
        holder.currencyName.setText(currencys.get(position).split(",")[1]);

        holder.exchangeRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(currencys.get(position).split(":")[0]);
                }
                Toast.makeText(mContext, currencys.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Used to find the number of holderView that should be created in the recyclerView
     *
     * @return int with the number of holder objects
     */
    @Override
    public int getItemCount() {
        int i = currencys.size();
        Log.d(TAG, String.valueOf(i));
        return i;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView currencyTicker;
        TextView currencyName;
        RelativeLayout exchangeRelative;

        public ViewHolder(View itemView){
            super(itemView);
            currencyTicker = itemView.findViewById(R.id.currencyTicker);
            currencyName = itemView.findViewById(R.id.currencyName);
            exchangeRelative = itemView.findViewById(R.id.exchangeRelative);
        }
    }
}
