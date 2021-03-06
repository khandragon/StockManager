package com.dimitar.fe404sleepnotfound;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Class responsible for retreving the list of currencys
 * Will return a String of
 */
public class RetreiveData extends AsyncTask {

    private static final String TAG = "RetreiveData";
    private static final int NETIOBUFFER = 1024;

    //url for the list of Currencies
    private String currencyListUrl;
    private String currencyKey;
    private String currencyListString;
    private String type;
    private String jwtKey;

    public RetreiveData(String Url, String key, String type, String jwtKey){
        this.currencyListUrl = Url;
        this.currencyKey = key;
        this.type = type;
        this.jwtKey= jwtKey;
    }

    /**
     *Does a http request on the openexchangerates api to get a list of all currencies
     *
     *
     * @param objects for the override of doInBachground for a async task
     * @return a string of all currencies ticker and name
     */
    @Override
    protected String doInBackground(Object[] objects) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(currencyListUrl + currencyKey);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", jwtKey);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(type);
            conn.connect();
            Log.d(TAG, "conn connected");
            int response = conn.getResponseCode();
            Log.d(TAG, String.valueOf(response));
            if (response == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                currencyListString = readInputStream(is);
                Log.d(TAG, currencyListString);
            }else{
                Log.d(TAG, "invalid response");
            }
        }catch (MalformedURLException e){
            Log.d(TAG,"Wrong URL");
        }catch (IOException e){
            Log.d(TAG, e.toString());
            Log.d(TAG,"IO Exception");
        }
        return currencyListString;
    }

    /**
     * Convers the Input stream into a String
     * source from https://gitlab.com/Android518-2018/topic12-02-http-get/blob/master/app/src/main/java/ca/campbell/httpexample/HttpExample.java
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
        //Log.wtf(TAG, byteArrayOutputStream.toString());
        return new String(byteArrayOutputStream.toString());
    }
}

