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
    private String WTkey = "&api_token=UN57mJ2dsqDQxVTJIaJ7diF85Au3B1iU6ERGKlMgEn9pg6z5vOchKpbJCYDz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_portfolio);

        settings = getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);

        cashView = findViewById(R.id.cashView);
        getUserStock();
    }

    /**
     * Querrys the php api to get the stocks that the user has
     */
    public void getUserStock() {
        //get API Token
        try {
            URLParams = "api/cash";
            //all api calls use the RetreiveData object which is a async task
            RetreiveData retreiveData = new RetreiveData(URL, URLParams, "GET", settings.getString("JWToken", "none"));
            String retreiveDataString = retreiveData.execute().get().toString();
            JSONObject retreiveDataObject = new JSONObject(retreiveDataString);
            cash = retreiveDataObject.getString("cashleft");
            Log.d(TAG, cash);
            //This will set the account balance for the user to display on the stockPortfolio activity
            cashView.setText(cash);
        } catch (Exception e) {
            Log.d(TAG, "error");
        }

        //Will get all stocks that the user has from the php api and add it to a arrayList of stockObjects
        try {
            URLParams = "api/allstocks";
            RetreiveData retreiveData = new RetreiveData(URL, URLParams, "GET", settings.getString("JWToken", "none"));
            String retreiveDataString = retreiveData.execute().get().toString();
            JSONArray retreiveDataObject = new JSONArray(retreiveDataString);
            userStock = new ArrayList<>();
            //Goes through the Json object to add it to the arraylist
            for(int i = 0; i < retreiveDataObject.length(); i ++){
                JSONObject temoObj = retreiveDataObject.getJSONObject(i);
                //Querrys the WT api in order to get the name of the company because only the ticker is sent from the PHP api
                RetreiveData retreiveDataWT = new RetreiveData(WTURL, temoObj.getString("stockTicker")+WTkey, "GET","");
                String retreiveDataStringWT = retreiveDataWT.execute().get().toString();
                JSONObject retreiveDataObjectWT = new JSONObject(retreiveDataStringWT);
                Log.d(TAG, retreiveDataObjectWT.get("data").toString());
                JSONArray tempWTArray = new JSONArray(retreiveDataObjectWT.get("data").toString());
                JSONObject tempWTObj = new JSONObject(tempWTArray.getJSONObject(0).toString());
                Log.d(TAG, tempWTObj.getString("name"));
                //Will and a new stock object to the array list so it can later be added to the recycler view
                stockObject temp = new stockObject(tempWTObj.getString("name"),temoObj.getString("stockTicker"), temoObj.getString("buyPrice"), temoObj.getString("amount"));
                userStock.add(temp);
            }
            displayStocks();
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    /**
     * Takes the JsonArray of userStocks and fills the recyclerView with the data using a custom adapter
     */
    private void displayStocks(){
        RecyclerView recyclerView = findViewById(R.id.stocksView);
        adapter = new stockRecyclerAdapter(userStock, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
