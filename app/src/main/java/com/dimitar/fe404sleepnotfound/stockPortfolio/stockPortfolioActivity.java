package com.dimitar.fe404sleepnotfound.stockPortfolio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.RecyclerAdapter;

import java.util.ArrayList;

public class stockPortfolioActivity extends Activity {

    private stockRecyclerAdapter adapter;
    private ArrayList<stockObject> userStock;
    private TextView cashView;
    private int cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_portfolio);

        cashView = findViewById(R.id.cashView);
        getUserStock();
    }

    /**
     * Will query the api from php to get the user stock data
     */
    private void getUserStock(){
        //will be filled with api call
        //this is mock data
        userStock=new ArrayList<>();
        userStock.add(new stockObject("test", "aatt", "10.2", "2"));
        userStock.add(new stockObject("test2", "aatt", "10.2", "2"));

        cash = 10000;
        displayStocks();
    }

    private void displayStocks(){
        cashView.setText(String.valueOf(cash));

        RecyclerView recyclerView = findViewById(R.id.stocksView);
        adapter = new stockRecyclerAdapter(userStock, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
