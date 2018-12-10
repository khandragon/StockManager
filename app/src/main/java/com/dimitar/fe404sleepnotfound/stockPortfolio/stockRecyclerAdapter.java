package com.dimitar.fe404sleepnotfound.stockPortfolio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.RetreiveData;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class stockRecyclerAdapter extends RecyclerView.Adapter<stockRecyclerAdapter.ViewHolderStock>{

    private ArrayList<stockObject> stocks;
    private Context mContext;
    private String URL = "http://fe404sleepnotfound.herokuapp.com/api/";
    private String URLParams = "api/sell?";

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
                 Toast.makeText(mContext,"Long Click",Toast.LENGTH_SHORT).show();
                 AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                 builder.setTitle(R.string.sellStock);
                 builder.setMessage( mContext.getResources().getString(R.string.sellStockQuestion1)+ " " + stocks.get(position).getName() + " " + mContext.getResources().getString(R.string.sellStockQuestion2));

                 //makes a editText of type number so there cannot be a error for string input
                 EditText input = new EditText(mContext);
                 input.setInputType(InputType.TYPE_CLASS_NUMBER);
                 builder.setView(input);
                 builder.setPositiveButton(R.string.sellStock, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         //this will be replaced with selling the stock or a error
                         Toast.makeText(mContext,input.getText().toString(),Toast.LENGTH_SHORT).show();
                         //Append the ticker and amount to URL params
                         String urlArgs = "quantity="+stocks.get(position).getAmount()+"&ticker="+stocks.get(position).getSymbol();
                         URLParams += urlArgs;
                         //Get the JWT token
                         SharedPreferences settings = mContext.getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);
                         String token = settings.getString("JWToken", "none");
                         RetreiveData retreiveData = new RetreiveData(URL, URLParams, "POST", token);
                         retreiveData.execute();

                     }
                 });

                 AlertDialog ad = builder.create();
                 ad.show();
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
