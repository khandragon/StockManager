package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RetreveCurrencies extends AsyncTask {

    private static final String TAG = "RetreveCurrenciesAsync";
    private static final int NETIOBUFFER = 1024;


    private String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
    private String currencyListString;

    @Override
    protected String doInBackground(Object[] objects) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
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
        }
        }catch (MalformedURLException e){
            Log.wtf(TAG,"Wrong URL");
        }catch (IOException e){
            Log.wtf(TAG,"IO Exception");
        }

        return currencyListString;
    }

    public String readInputStream(InputStream stream)  throws IOException{
        int bytesRead, totalRead=0;
        byte[] buffer = new byte[NETIOBUFFER];
        BufferedInputStream bufferedInStream = new BufferedInputStream(stream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(byteArrayOutputStream);
        while ((bytesRead = bufferedInStream.read(buffer)) != -1) {
            writer.write(buffer);
            totalRead += bytesRead;
        }
        writer.flush();
        Log.d(TAG, "Bytes read: " + totalRead + "(-1 means end of reader so max of)");
        return new String(byteArrayOutputStream.toString());
    }
}

