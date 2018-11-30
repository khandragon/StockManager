package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private 

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
