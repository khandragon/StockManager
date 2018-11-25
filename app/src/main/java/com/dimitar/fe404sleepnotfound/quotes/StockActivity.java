package com.dimitar.fe404sleepnotfound.quotes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class StockActivity extends MenuActivity {
    private static final String TAG = "HttpURLConn";

    private String apiToken = "gHYVnybcdk2fooQ9INz7b11s23qqG57Xxn4197VPboOUYmO2hB7ra6bmKwoF";
    private EditText urlText;
    private TextView textView;
    private ScrollView scrollv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        urlText = (EditText) findViewById(R.id.myTicker);
        textView = (TextView) findViewById(R.id.uriMessage);
        scrollv = (ScrollView) findViewById(R.id.scrollv);
        scrollv.setVisibility(View.GONE);
    }

    public void getTickerQuote(View view) {
        InputStream stream = null;
        HttpsURLConnection conn = null;
        String ticker = urlText.getText().toString();
        scrollv.setVisibility(View.VISIBLE);
        if (ticker.isEmpty()) {
            textView.setText("Gimmey some URLz pleaze.");

        } else {
            try {
                String tickerURL = "https://www.worldtradingdata.com/api/v1/stock?symbol=" + ticker + "&api_token=" + apiToken;
                Log.d(TAG, "url: " + tickerURL);
                // check to see if we can get on the network
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                URL url = new URL(tickerURL);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                Log.i(TAG,"welp");
                conn.connect();
                Log.i(TAG,"ayy");
                int response = conn.getResponseCode();
                Log.d(TAG, "Server returned: " + response + " aborting read.");
                if (response != HttpURLConnection.HTTP_OK) {
                    textView.setText("Server returned: " + response + " aborting read.");
                }
                stream = conn.getInputStream();
                readReturn(stream);

            } catch (IOException e) {
                textView.setText("sowwy didnt work");
            }finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                    if (conn != null)
                        try {
                            conn.disconnect();
                        } catch (IllegalStateException ignore ) {
                        }
                }
            }
        }
    }

    private void readReturn(InputStream stream) throws IOException {
        int bytesRead, totalRead=0;
        byte[] buffer = new byte[1024];

        // for data from the server
        BufferedInputStream bufferedInStream = new BufferedInputStream(stream);
        // to collect data in our output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(byteArrayOutputStream);

        // read the stream until end
        while ((bytesRead = bufferedInStream.read(buffer)) != -1) {
            writer.write(buffer);
            totalRead += bytesRead;
        }
        writer.flush();
        Log.d(TAG, "Bytes read: " + totalRead
                + "(-1 means end of reader so max of)");

        textView.setText(new String(byteArrayOutputStream.toString()));
    }
}
