package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeAsync;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RetreveRates extends AsyncTask {

    private static final String TAG = "RetreveRatesAsync";

    private static final int NETIOBUFFER = 1024;

    private String currencyListUrl = "https://openexchangerates.org/api/latest.json?app_id=";
    private String openExchangeratesKey = "7b0dc9c3d41b40989a3bafcea4683d5f";
    private String currencyListString;

    /**
     *Does a http request on the openexchangerates api to get a list of all currencies rate
     * based off of USD
     * Must have a api key to retreive the data
     *
     * @param objects for the override of doInBachground for a async task
     * @return a string of all currencies ticker and rates relative to 1 USD
     */
    @Override
    protected Object doInBackground(Object[] objects) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(currencyListUrl + openExchangeratesKey);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                currencyListString = readInputStream(is);
                //Create json object to only get a array of the rates values
                JSONObject temp = new JSONObject(currencyListString);
                currencyListString = temp.get("rates").toString();
            }
        }catch (MalformedURLException e){
            Log.e(TAG,"Wrong URL");
        }catch (IOException e){
            Log.e(TAG,"IO Exception");
        }catch (JSONException e) {
            Log.e(TAG,"JSONException");
        }

        return currencyListString;
    }

    /**
     * Convers the Input stream into a String
     *
     * @param stream The passed inputStream
     * @return A string of what was in the inputStream
     * @throws IOException if there is a invalid inputStream
     */
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
