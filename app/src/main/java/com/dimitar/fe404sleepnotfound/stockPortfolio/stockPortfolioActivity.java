package com.dimitar.fe404sleepnotfound.stockPortfolio;

import android.app.Activity;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.RetreiveData;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class stockPortfolioActivity extends Activity {
    private static final String TAG = "stockPortfolioActivity";

    private stockRecyclerAdapter adapter;
    private ArrayList<stockObject> userStock;
    private TextView cashView;
    private String cash;
    private SharedPreferences settings;

    private String URL = "http://fe404sleepnotfound.herokuapp.com/api/";
    private String URLParams;
    private String WTURL = " https://www.worldtradingdata.com/api/v1/stock?symbol=";
    private String WTkey = "&api_token=ASX8iHsw80j4fFEXKXRVGhOV1VHV7RmzWASOZj6sjtFxz8de6iYNW40Uw4HQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_portfolio);

        settings = getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);
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
            URLParams = "api/cash";
            RetreiveData retreiveData = new RetreiveData(URL, URLParams, "GET", settings.getString("JWToken", "none"));
            String retreiveDataString = retreiveData.execute().get().toString();
            JSONObject retreiveDataObject = new JSONObject(retreiveDataString);
            cash = retreiveDataObject.getString("cashleft");
            Log.wtf(TAG, cash);
            cashView.setText(cash);
        }catch (Exception e ){
            Log.d(TAG, "error");
        }

        try {
            URLParams = "api/allstocks";
            RetreiveData retreiveData = new RetreiveData(URL, URLParams, "GET", settings.getString("JWToken", "none"));
            String retreiveDataString = retreiveData.execute().get().toString();
            JSONArray retreiveDataObject = new JSONArray(retreiveDataString);
            userStock = new ArrayList<>();
            for(int i = 0; i < retreiveDataObject.length(); i ++){
                JSONObject temoObj = retreiveDataObject.getJSONObject(i);
                RetreiveData retreiveDataWT = new RetreiveData(WTURL, temoObj.getString("stockTicker")+WTkey, "GET","");
                String retreiveDataStringWT = retreiveDataWT.execute().get().toString();
                JSONObject retreiveDataObjectWT = new JSONObject(retreiveDataStringWT);
                Log.wtf(TAG, retreiveDataObjectWT.get("data").toString());
                JSONArray tempWTArray = new JSONArray(retreiveDataObjectWT.get("data").toString());
                JSONObject tempWTObj = new JSONObject(tempWTArray.getJSONObject(0).toString());
                Log.wtf(TAG, tempWTObj.getString("name"));
                stockObject temp = new stockObject(tempWTObj.getString("name"),temoObj.getString("stockTicker"), temoObj.getString("buyPrice"), temoObj.getString("amount"));
                userStock.add(temp);
            }
            displayStocks();
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
