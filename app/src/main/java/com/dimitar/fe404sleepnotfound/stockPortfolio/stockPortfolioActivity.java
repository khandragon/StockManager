package com.dimitar.fe404sleepnotfound.stockPortfolio;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.RetreiveData;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.RecyclerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class stockPortfolioActivity extends Activity {
    private static final String TAG = "stockPortfolioActivity";

    private stockRecyclerAdapter adapter;
    private ArrayList<stockObject> userStock;
    private TextView cashView;
    private String cash;
    private SharedPreferences settings;

    private String URL = "http://ass3.test/api/";
    private String URLParams = "api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_portfolio);

        settings = getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);

        cashView = findViewById(R.id.cashView);
        getUserStock();
    }

    /**
     * Will query the api from php to get the user stock data
     */
    private void getUserStock(){
        //get API Token
        try {
            URLParams = URLParams + "cash";
            RetreiveData RetreiveData = new RetreiveData(URL, URLParams, "POST", settings.getString("JWToken", "none"));
            //String retreveCash = retreveCurrencies.execute().get().toString();
            String retreveCashString = "[{\"cash\":\"7843.76\"}]";
            JSONObject retreiveDataObject = new JSONObject(retreveCashString);
            cash = retreiveDataObject.getString("cash");
        }catch (Exception e ){
            Log.d(TAG, "error");
        }

        try {
            URLParams = URLParams + "allstocks";
            RetreiveData RetreiveData = new RetreiveData(URL, URLParams, "POST", settings.getString("JWToken", "none"));
            //String retreveCash = retreveCurrencies.execute().get().toString();
            String retreveCashString = "[{\"cash\":\"7843.76\"}]";
            JSONObject retreiveDataObject = new JSONObject(retreveCashString);
        }catch (Exception e ){
            Log.d(TAG, "error");
        }

    }

    private void displayStocks(){
        cashView.setText(String.valueOf(cash));

        RecyclerView recyclerView = findViewById(R.id.stocksView);
        adapter = new stockRecyclerAdapter(userStock, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
