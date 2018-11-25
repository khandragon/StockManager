package com.dimitar.fe404sleepnotfound.quotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TabHost;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class StockActivity extends MenuActivity {
    private static final String TAG = "HttpURLConn";

    private String apiToken = "gHYVnybcdk2fooQ9INz7b11s23qqG57Xxn4197VPboOUYmO2hB7ra6bmKwoF";
    private EditText urlText;
    private TextView textView;
    private Button addBtn;
    private Set<String> saved = null;
    private String lastSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        urlText = (EditText) findViewById(R.id.myTicker);
        textView = (TextView) findViewById(R.id.uriMessage);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setEnabled(false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        saved = prefs.getStringSet("savedList", new HashSet<String>());
        if (saved != null) {
            //populateList()
        }

    }

    public void addTicker(View view) {
        Log.i(TAG, lastSearch);
        saved.add(lastSearch);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", saved);
        editor.commit();
        populateList();
    }
    public void removeTicker(String ticker) {
        Log.i(TAG, ticker);
        saved.remove(ticker);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", saved);
        editor.commit();
        populateList();
    }


    public void getTickerQuote(View view) {
        addBtn.setEnabled(false);
        InputStream stream = null;
        HttpsURLConnection conn = null;
        String ticker = urlText.getText().toString();
        if (ticker.isEmpty()) {
            textView.setText("Gimmey some ticker pleaze.");
        } else {
            String tickerURL = "https://www.worldtradingdata.com/api/v1/stock?symbol=" + ticker + "&api_token=" + apiToken;
            Log.d(TAG, "url: " + tickerURL);
            // check to see if we can get on the network
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new makeRequest().execute(tickerURL);
            } else {
                textView.setText("No network connection available.");
            }
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


    private void populateList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        saved = prefs.getStringSet("savedList", new HashSet<String>());
        Log.i(TAG, "this is from savedlist" + saved.toString());
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
                textView.setText("sowwy didnt work");
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
            try {
                Log.i(TAG, result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("Message")) {
                    textView.setText("Unknown Ticker");
                } else {
                    addBtn.setEnabled(true);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        companyName = object.getString("name");
                        currentPrice = object.getString("price");
                        currency = object.getString("currency");
                        lastSearch = object.getString("symbol");
                    }
                    textView.setText(" company: " + companyName + " curreny: " + currency + " price: " + currentPrice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
