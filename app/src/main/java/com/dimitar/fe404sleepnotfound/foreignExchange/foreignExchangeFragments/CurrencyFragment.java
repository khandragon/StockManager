package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;
import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.RecyclerAdapter;
import com.dimitar.fe404sleepnotfound.foreignExchange.RetreveCurrencies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class CurrencyFragment extends Fragment {

    private static final String TAG = "CurrencyFragment";

    private View fragmentView;
    private Context context;

    private String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
    //private String currencyListString;
    //private JSONObject currencyJson;
    //private ArrayList<String> currencies;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_currency, container, false);
        this.context = container.getContext();

        getCurrencyList();

        return fragmentView;
    }

    public CurrencyFragment() {
        // Required empty public constructor
    }

    private void getCurrencyList(){
        try{
            RetreveCurrencies retreveCurrencies = new RetreveCurrencies();
            String currencyListString = retreveCurrencies.execute().get().toString();
            //Log.wtf(TAG, currencyListString);
            createCurrenciesList(currencyListString);
        }catch (ConcurrentModificationException e){
            Log.wtf(TAG, "ConcurrentModificationException");
        }catch (InterruptedException e){
            Log.wtf(TAG, "InterruptedException");
        }catch (ExecutionException e){
            Log.wtf(TAG, "ExecutionException");
        }


        /*
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    HttpURLConnection conn = null;
                    URL url = new URL(currencyListUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int response = conn.getResponseCode();
                    if (response != HttpURLConnection.HTTP_OK){

                    }else {
                        is = conn.getInputStream();
                        currencyListString = readInputStream(is);
                        //Log.wtf(TAG, currencyListString);
                        //currencyJson = new JSONObject(currencyListString);
                        createCurrenciesList();
                    }
                }catch (MalformedURLException e){
                    Log.wtf(TAG,"Wrong URL");
                }catch (IOException e){
                    Log.wtf(TAG,"IO Exception");
                }catch (JSONException e){
                    Log.wtf(TAG,"JSON failed to parse");
                }
            }
        });
        thread.start();
        */
    }

    private void createCurrenciesList(String currencyListString) {
        ArrayList<String> currencies = new ArrayList<>();
        String[] temp = currencyListString.split(",");
        for(String row : temp){
            String[] brokenRow = row.split("\"");
            String cur = brokenRow[1] + "," + brokenRow[3];
            currencies.add(cur);
        }
        displayCurrencies(currencies);
    }

    private void displayCurrencies(ArrayList<String> currencies){
        RecyclerView recyclerView = fragmentView.findViewById(R.id.currencyView);
        RecyclerAdapter adapter = new RecyclerAdapter(currencies, this.context);

        for(String cur : currencies){
            Log.wtf(TAG, cur);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
    }
}
