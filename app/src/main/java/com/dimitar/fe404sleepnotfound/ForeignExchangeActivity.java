package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class ForeignExchangeActivity extends Activity {

    String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
    Handler currencyHandles;

    //URL for the open exchange api with the api key attached;
    //Unable to change the base type with a free api key. Will be USD
    String openExchangeUrl = "https://openexchangerates.org/api/latest.json?app_id=7b0dc9c3d41b40989a3bafcea4683d5f";

    TextView text;
    String textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreignexchange);

        text = findViewById(R.id.textView);


        OkHttpClient client = new OkHttpClient();
        currencyHandles = new Handler(Looper.getMainLooper());
        Request currencyListRequest = new Request.Builder()
                .url(currencyListUrl)
                .build();

        client.newCall(currencyListRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Fuck fuck fuck fuck fuck");
                textContent = "Fick adfsadfasdasd";
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                currencyHandles.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(response.body().toString());
                    }
                });
            }
        });

    }
}
