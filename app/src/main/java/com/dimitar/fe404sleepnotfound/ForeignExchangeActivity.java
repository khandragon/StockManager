package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class ForeignExchangeActivity extends Activity {

    String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
    String currencyListString;
    JSONObject currencyJson;
    //URL for the open exchange api with the api key attached;
    //Unable to change the base type with a free api key. Will be USD
    String openExchangeUrl = "https://openexchangerates.org/api/latest.json?app_id=7b0dc9c3d41b40989a3bafcea4683d5f";
    String openExchangeString;
    JSONObject openExchangeJson;

    CurrencyFragment currencyFragmentFrom;
    CurrencyFragment currencyFragmentTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreignexchange);

        currencyFragmentFrom = new CurrencyFragment();
        FragmentTransaction ftF = getFragmentManager().beginTransaction();
        ftF.replace(R.id.fromCurrency, currencyFragmentFrom);
        ftF.commit();

        currencyFragmentTo = new CurrencyFragment();
        FragmentTransaction ftT = getFragmentManager().beginTransaction();
        ftT.replace(R.id.toCurrency, currencyFragmentTo);
        ftT.commit();

        currencyListGet();
        //openExchangeGet();
    }

    private void currencyListGet(){
        OkHttpClient client = new OkHttpClient();
        Request currencyListRequest = new Request.Builder()
                .url(currencyListUrl)
                .build();

        client.newCall(currencyListRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Fuck fuck fuck fuck fuck");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        currencyListString = response.body().string();
                        currencyJson = new JSONObject(currencyListString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    currencyFragmentFrom.setFragmentJson(currencyJson);
                                    currencyFragmentTo.setFragmentJson(currencyJson);
                                    //text.setText(currencyJson.toString());
                                }catch (Exception e){
                                }
                            }
                        });
                    }catch (Exception e){
                    }
                }else {
                }
            }
        });
    }

    private void openExchangeGet(){
        OkHttpClient client = new OkHttpClient();
        Request currencyListRequest = new Request.Builder()
                .url(currencyListUrl)
                .build();

        client.newCall(currencyListRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Fuck fuck fuck fuck fuck");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if(response.isSuccessful()){
                    currencyListString = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //text.setText(currencyListString);
                        }
                    });
                }else {
                    //text.setText("Faliled to get currency list");
                }
            }
        });
    }
}