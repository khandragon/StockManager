package com.dimitar.fe404sleepnotfound.quotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockAdapterViewHolder> {

    private List<String> mDataset;
    private Context context;

    public StockListAdapter(Set<String> myDataset, Context context) {
        this.mDataset = new ArrayList<>(myDataset);
        this.context = context;
    }

    @NonNull
    @Override
    public StockListAdapter.StockAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layout = LayoutInflater.from(viewGroup.getContext());
        View view = layout.inflate(R.layout.adapter_stock_layout, viewGroup, false);
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

    public void changeList(Set<String> newList) {
        mDataset = new ArrayList<>(newList);
    }

    public class StockAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tickerName;
        Button removeBtn;

        public StockAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tickerName = itemView.findViewById(R.id.tickerName);
            removeBtn = itemView.findViewById(R.id.removeBtn);
//            removeBtn.setOnClickListener(this);
        }

        public TextView getTickerName() {
            return tickerName;
        }

        @Override
        public void onClick(View view) {

        }
    }

}