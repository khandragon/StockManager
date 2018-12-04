package com.dimitar.fe404sleepnotfound.quotes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class StockInfo extends MenuActivity {
    private static final String TAG = "StockInfo";
    private String apiToken = "gHYVnybcdk2fooQ9INz7b11s23qqG57Xxn4197VPboOUYmO2hB7ra6bmKwoF";

    private TextView tickerName;
    private TextView tickerCompany;
    private TextView tickerPrice;
    private TextView dayHeight;
    private TextView dayLowest;
    private TextView yesturdayClose;
    private TextView dayChange;
    private String ticker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        tickerName = (TextView) findViewById(R.id.tickerName);
        tickerCompany = (TextView) findViewById(R.id.companyName);
        tickerPrice = (TextView) findViewById(R.id.tickerPrice);
        dayHeight = (TextView) findViewById(R.id.dayHeight);
        dayLowest = (TextView) findViewById(R.id.dayLowest);
        yesturdayClose = (TextView) findViewById(R.id.yesturdayClose);
        dayChange = (TextView) findViewById(R.id.dayChange);
        Bundle extra = getIntent().getExtras();
        ticker = extra.getString("ticker");
        findTickerInfo(ticker);
    }

    private void findTickerInfo(String ticker) {
        InputStream stream = null;
        HttpsURLConnection conn = null;
        String tickerURL = "https://www.worldtradingdata.com/api/v1/stock?symbol=" + ticker + "&api_token=" + apiToken;
        Log.d(TAG, "url: " + tickerURL);
        // check to see if we can get on the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new makeRequest().execute(tickerURL);
        } else {
            tickerCompany.setText("No network connection available.");
        }
    }

    private class makeRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            InputStream stream = null;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(urls[0]);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                Log.i(TAG, "welp");
                conn.connect();
                Log.i(TAG, "ayy");
                int response = conn.getResponseCode();
                Log.d(TAG, "Server returned: " + response + " aborting read.");
                if (response != HttpURLConnection.HTTP_OK) {
                    return "Server returned: " + response + " aborting read.";
                }
                stream = conn.getInputStream();
                return readReturn(stream);

            } catch (IOException e) {
                tickerCompany.setText("sowwy didnt work");
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                    if (conn != null)
                        try {
                            conn.disconnect();
                        } catch (IllegalStateException ignore) {
                        }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            String companyName = null;
            String currentPrice = null;
            String currency = null;
            String ticker = null;
            String dayHigh = null;
            String dayLow = null;
            String yesturday = null;
            String change = null;
            try {
                Log.i(TAG, result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    companyName = object.getString("name");
                    currentPrice = object.getString("price");
                    currency = object.getString("currency");
                    ticker = object.getString("symbol");
                    dayHigh = object.getString("day_high");
                    dayLow = object.getString("day_low");
                    yesturday = object.getString("close_yesterday");
                    change = object.getString("day_change");
                }
                tickerName.setText(ticker);
                tickerCompany.setText(companyName);
                tickerPrice.setText("$" + currentPrice + ", " + currency);
                dayHeight.setText(dayHeight.getText() + ": $" + dayHigh);
                dayLowest.setText(dayLowest.getText() + ": $" + dayLow);
                yesturdayClose.setText(yesturdayClose.getText() + ": $" + yesturday);
                dayChange.setText(dayChange.getText() + ": $" + change);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String readReturn(InputStream stream) throws IOException {
            int bytesRead, totalRead = 0;
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

            return new String(byteArrayOutputStream.toString());
        }

    }
}
