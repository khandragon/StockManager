package com.dimitar.fe404sleepnotfound.stockPortfolio;

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

public class stockRecyclerAdapter extends RecyclerView.Adapter<stockRecyclerAdapter.ViewHolderStock>{

    private ArrayList<stockObject> stocks;
    private Context mContext;

    public stockRecyclerAdapter(ArrayList<stockObject> stocks, Context mContext) {
        this.stocks = stocks;
        this.mContext = mContext;
    }

    @Override
    public stockRecyclerAdapter.ViewHolderStock onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_stock, parent, false);
        ViewHolderStock holder = new ViewHolderStock(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderStock holder, int position) {
        holder.nameView.setText(stocks.get(position).getName());
        holder.amountView.setText(stocks.get(position).getAmount());
        holder.price_openView.setText(stocks.get(position).getPrice_open());
        holder.symbolView.setText(stocks.get(position).getSymbol());

        holder.stockViewLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.wtf("test", String.valueOf(stocks.size()));
        return stocks.size();
    }

    public class ViewHolderStock extends RecyclerView.ViewHolder{

        TextView nameView;
        TextView symbolView;
        TextView price_openView;
        TextView amountView;
        RelativeLayout stockViewLayout;

        public ViewHolderStock(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            symbolView = itemView.findViewById(R.id.symbolView);
            price_openView = itemView.findViewById(R.id.price_openView);
            amountView = itemView.findViewById(R.id.amountView);
            stockViewLayout = itemView.findViewById(R.id.stockViewLayout);
        }
    }
}
