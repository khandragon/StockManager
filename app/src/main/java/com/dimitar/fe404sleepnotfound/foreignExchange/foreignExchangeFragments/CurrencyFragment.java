package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;
import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimitar.fe404sleepnotfound.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CurrencyFragment extends Fragment {

    private static final String TAG = "HttpURLConn";
    private static final int NETIOBUFFER = 1024;

    private View fragmentView;
    private Context context;

    private String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
    private String currencyListString;
    private JSONObject currencyJson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getCurrencyList();

        this.fragmentView = inflater.inflate(R.layout.fragment_currency, container, false);
        this.context = container.getContext();
        return fragmentView;
    }

    public CurrencyFragment() {
        // Required empty public constructor
    }

    private void getCurrencyList(){
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
                        currencyJson = new JSONObject(currencyListString);
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
