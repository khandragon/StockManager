package com.dimitar.fe404sleepnotfound.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.MainActivity;
import com.dimitar.fe404sleepnotfound.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Lets the user set application settings used by other Activities in SharedPreferences.
 */
public class SettingsActivity extends MenuActivity {
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private EditText username;
    private EditText email;
    private EditText password;
    private Spinner prefCurrency;
    private Spinner prefExchange;
    private TextView lastUpdated;
    private String usernameTxt;
    private String emailTxt;
    private String passwordTxt;
    private String prefCurrencyTxt;
    private String prefExchangeTxt;
    private String lastUpdatedTxt;

    /**
     * Custom implementation of the onCreate lifecycle method that sets references to the necessary
     * views, SharedPreferences and check if any settings have been previously set.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);
        settingsEditor = settings.edit();

        //Acquire reference to all views
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        prefCurrency = findViewById(R.id.prefCurrency);
        prefExchange = findViewById(R.id.prefExchange);
        lastUpdated = findViewById(R.id.lastUpdated);

        //Set Spinner adapters for preferred currency & stock exchange
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefCurrency.setAdapter(currencyAdapter);

        ArrayAdapter<CharSequence> exchangeAdapter = ArrayAdapter.createFromResource(this, R.array.exchanges, android.R.layout.simple_spinner_item);
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefExchange.setAdapter(exchangeAdapter);

        //Check if settings have been previously set
        if(!settings.getString("username", "none").equals("none")){
            //Get String values from SharedPreferences
            usernameTxt = settings.getString("username", "none");
            emailTxt = settings.getString("email", "none");
            passwordTxt = settings.getString("password", "none");
            prefCurrencyTxt = settings.getString("prefCurrency", "none");
            prefExchangeTxt = settings.getString("prefExchange", "none");
            lastUpdatedTxt = settings.getString("lastUpdated", "none");
            //If settings have been previously set, display them as hints & display appropriate items in Spinners
            username.setHint(usernameTxt);
            email.setHint(emailTxt);
            password.setText(passwordTxt);
            switch(prefCurrencyTxt){
                case "CAD":
                    prefCurrency.setSelection(0);
                    break;
                case "USD":
                    prefCurrency.setSelection(1);
                    break;
                case "BTC":
                    prefCurrency.setSelection(2);
                    break;
            }
            switch(prefExchangeTxt){
                case "TSX":
                    prefExchange.setSelection(0);
                    break;
                case "NYSE":
                    prefExchange.setSelection(1);
                    break;
                case "NASDAQ":
                    prefExchange.setSelection(2);
                    break;
            }
            lastUpdated.setText(lastUpdatedTxt);
        }

    }

    /**
     * Click implementation for the Save Changes button. It saves all changes made and displays a
     * message regarding save status.
     * @param v
     */
    public void onClick(View v){
        //Save all changes to strings values
        usernameTxt = username.getText().toString();
        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();
        prefExchangeTxt = prefExchange.getSelectedItem().toString();
        prefCurrencyTxt = prefCurrency.getSelectedItem().toString();
        if(checkIfChangesMade()){
            //Get current date timestamp and save changes in SharedPreferences
            lastUpdatedTxt = Calendar.getInstance().getTime().toString();
            if(usernameTxt.isEmpty() && !username.getHint().toString().isEmpty()){
                settingsEditor.putString("username", username.getHint().toString());
            }
            else {
                settingsEditor.putString("username", usernameTxt);
            }
            if(emailTxt.isEmpty() && !email.getHint().toString().isEmpty()){
                settingsEditor.putString("email",email.getHint().toString());
            }
            else {
                settingsEditor.putString("email", emailTxt);
            }
            settingsEditor.putString("password", passwordTxt);
            settingsEditor.putString("prefCurrency", prefCurrencyTxt);
            settingsEditor.putString("prefExchange", prefExchangeTxt);
            settingsEditor.putString("lastUpdated", lastUpdatedTxt);
            //Get bearer token and save it to SharedPreferences
            String url = "http://ass3.test/api/auth/login?email="+emailTxt+"&password="+passwordTxt+"123456";
            new GetBearerTokenResponse().execute(url);
            //Save all changes
            settingsEditor.commit();
            //Notify user that settings have been saved
            Toast.makeText(this, getString(R.string.changesSaved), Toast.LENGTH_LONG).show();
            //Go back to the main activity
            Intent openMainActivity = new Intent(this, MainActivity.class);
            startActivity(openMainActivity);
        }
        else{
            //Notify the user that no changes have been made
            Toast.makeText(this, getString(R.string.noChanges), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Click implementation for the Android back button. If there are any unsaved changes, it prompts
     * the user to confirm that they want to leave the Activity without saving their changes.
     * @param keyCode
     * @param event
     * @return event status
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        super(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && checkIfChangesMade()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.exitWithoutSaving))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Go back to the main activity
                            Intent openMainActivity = new Intent(SettingsActivity.this, MainActivity.class);
                            startActivity(openMainActivity);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Stay in the settings activity
                            return;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Utility method that checks if any changes have been made.
     * @return true if changes have been made to settings
     */
    private boolean checkIfChangesMade(){
        //If no settings have been set previously
        if(settings.getString("username", "none").equals("none") || settings.getString("email", "none").equals("none") || settings.getString("password", "none").equals("none")){
            return true;
        }
        //USER NAME
        //If settings have already been set and the textview is not modified, check with the hint
        if(username.getText().toString().isEmpty() && !username.getHint().toString().isEmpty()){
            if(!settings.getString("username", "none").equals(username.getHint().toString())){
                return true;
            }
        }
        else if(!settings.getString("username", "none").equals(username.getText().toString())){
            return true;
        }
        //EMAIL
        //If settings have already been set and the textview is not modified, check with the hint
        if(email.getText().toString().isEmpty() && !email.getHint().toString().isEmpty()){
            if(!settings.getString("email", "none").equals(email.getHint().toString())){
                return true;
            }
        }
        else if(!settings.getString("email", "none").equals(email.getText().toString())){
            return true;
        }
        //PASSWORD
        if(!settings.getString("password", "none").equals(password.getText().toString())){
            return true;
        }
        //PREF CURRENCY
        if(!settings.getString("prefCurrency", "none").equals(prefCurrency.getSelectedItem().toString())){
            return true;
        }
        //PREF EXCHANGE
        if(!settings.getString("prefExchange", "none").equals(prefExchange.getSelectedItem().toString())){
            return true;
        }
        return false;
    }

    /**
     * Menu functionality in SettingsActivity. It doesn't allow for settings to be opened again and
     * opening About finishes the actvity.
     * @param item
     * @return true if an option was successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Open Dawson Computer Science web page
        if(item.getItemId() == R.id.dawson){
            Intent openDawsonPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
            startActivity(openDawsonPage);
            return true;
        }
        //Open AboutActivity and finish SettingsActivity
        else if(item.getItemId() == R.id.about){
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            finish();
            return true;
        }
        else{
            return false;
        }
    }

    private class GetBearerTokenResponse extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                BufferedReader reader = null;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");

                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }
                connection.disconnect();
                reader.close();
                return buffer.toString();
            }
            catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject json = new JSONObject(result);
                String token = json.getString("access_token");
                settingsEditor.putString("token", token);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
