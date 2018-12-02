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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Shows a dialoge to open tickers and check stock prices, user is able to have up to 5
 * stock tickers, on click of ticker it will display
 *
 * @Author Saad Khan
 */
public class StockActivity extends MenuActivity {
    private static final String TAG = "HttpURLConn";

    //api password for stock api
    private String apiToken = "gHYVnybcdk2fooQ9INz7b11s23qqG57Xxn4197VPboOUYmO2hB7ra6bmKwoF";

    private EditText urlText;
    private TextView textView;
    private Button addBtn;
    private ListView listView;

    //list of saved tickers and last searched ticker
    private Set<String> saved = null;
    private String lastSearch = null;

    /**
     * Custom impelmentation fo the onCreate lifecycle method. disables the add btn by default
     * if no saved list add default else populate the list
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        urlText = (EditText) findViewById(R.id.myTicker);
        textView = (TextView) findViewById(R.id.uriMessage);
        addBtn = (Button) findViewById(R.id.addBtn);
        listView = (ListView) findViewById(R.id.tickerList);
        addBtn.setEnabled(false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        saved = prefs.getStringSet("savedList", new HashSet<String>());
        if (saved != null) {
            populateList();
        }

    }

    /**
     * Implemntation of the onClick for the add Button, add the searched ticker to the list
     *
     * @param view
     */
    public void addTicker(View view) {
        addBtn.setEnabled(false);
        //check if the save list is not greater than 5
        if (!check5tickers()) {
            Log.i(TAG, lastSearch);
            //add to list and shared preferences
            saved.add(lastSearch);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet("savedList", saved);
            editor.commit();
            // populate the list with the saved list
            populateList();
        }
    }

    /**
     * Implmentation of the onClick for the remove btn that removes the ticker from the saved list
     *
     * @param view
     */
    public void removeTicker(View view) {
        //get the ticker from the text field
        LinearLayout linearLayout = (LinearLayout) view.getParent();
        TextView textView = (TextView) linearLayout.getChildAt(0);
        String ticker = (String) textView.getText();
        Log.i(TAG, "removing " + ticker);
        //remove from the list
        saved.remove(ticker);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("savedList", saved);
        editor.commit();
        populateList();
        //check the list size
        if (!check5tickers()) {
            addBtn.setEnabled(true);
        }
    }

    /**
     * Implementation of the onclick of the load btn if proper ticker is inputed will search for the
     * will make a get request for the ticker if said ticker is valid
     *
     * @param view
     */
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
            // check network connection
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new makeRequest().execute(tickerURL);
            } else {
                textView.setText("No network connection available.");
            }
        }
    }

    /**
     * create a buffer to read the data that is incoming from the api
     *
     * @param stream
     * @return
     * @throws IOException
     */
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

    /**
     * take list of saved tickers and populate the list
     */
    private void populateList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        saved = prefs.getStringSet("savedList", new HashSet<String>());
        StockListAdapter adapter = new StockListAdapter(this, R.layout.adapter_stock_layout, new ArrayList<String>(saved));
        listView.setAdapter(adapter);
    }


    /**
     * private class to make an async request of the api
     */
    private class makeRequest extends AsyncTask<String, Void, String> {

        /**
         * create a connection object and receive input from the api
         *
         * @param urls
         * @return
         */
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

        /**
         * process the json response from the api
         *
         * @param result
         */
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
                    if (!check5tickers()) {
                        addBtn.setEnabled(true);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        companyName = object.getString("name");
                        currentPrice = object.getString("price");
                        currency = object.getString("currency");
                        lastSearch = object.getString("symbol");
                    }
                    textView.setText("Company: " + companyName + " Curreny: " + currency + " Price: " + currentPrice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * check if the list of saved tickers is greater than 5
     *
     * @return true if greater false if less
     */
    private boolean check5tickers() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        saved = prefs.getStringSet("savedList", new HashSet<String>());
        return saved.size() >= 5;
    }
}