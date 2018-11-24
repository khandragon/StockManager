package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends MenuActivity {
    private boolean settingsUpdated;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsUpdated = false;
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

    public void onClick(View v){
        //Save all changes to strings values
        usernameTxt = username.getText().toString();
        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();
        prefExchangeTxt = prefExchange.getSelectedItem().toString();
        prefCurrencyTxt = prefCurrency.getSelectedItem().toString();
        if(checkIfChangesMade()){
            //Get current date timestamp and save changes in SharedPreferences
            settingsUpdated = true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

    private boolean checkIfChangesMade(){
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
}