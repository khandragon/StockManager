package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.content.Context;
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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String TAG = "RecycleAdapter";

    private ArrayList<String> currencys;
    private Context mContext;

    private RecyclerAdapterListener mListener;

    public interface RecyclerAdapterListener{
        void onItemClick(String item);
    }

    public void setRecyclerAdapterListener(RecyclerAdapterListener listener){
        mListener = listener;
    }

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
        Log.wtf(TAG,"viewHolderCalled");
        holder.currencyTicker.setText(currencys.get(position).split(",")[0] + ":");
        holder.currencyName.setText(currencys.get(position).split(",")[1]);

        holder.exchangeRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(currencys.get(position).split(",")[0]);
                }
                Toast.makeText(mContext, currencys.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        int i = currencys.size();
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
