package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

        //Check if settings have been previously set
        if(!settings.getString("username", "none").equals("none")){
            //Get String values from SharedPreferences
            usernameTxt = settings.getString("username", "none");
            emailTxt = settings.getString("email", "none");
            passwordTxt = settings.getString("password", "none");
            prefCurrencyTxt = settings.getString("prefCurrency", "none");
            prefExchangeTxt = settings.getString("prefExchange", "none");
            lastUpdatedTxt = settings.getString("lastUpdated", "none");
            //If settings have been previously set, display them as hints
            username.setHint(usernameTxt);
            email.setHint(emailTxt);
            password.setHint(passwordTxt);
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
            settingsEditor.putString("username", usernameTxt);
            settingsEditor.putString("email", emailTxt);
            settingsEditor.putString("password", passwordTxt);
            settingsEditor.putString("prefCurrency", prefCurrencyTxt);
            settingsEditor.putString("prefExchange", prefExchangeTxt);
            settingsEditor.putString("lastUpdated", lastUpdatedTxt);
        }
        else{
            Toast.makeText(this, getString(R.string.noChanges), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIfChangesMade(){
        if(!settings.getString("username", "none").equals(usernameTxt)){
            return true;
        }
        else if(!settings.getString("email", "none").equals(emailTxt)){
            return true;
        }
        else if(!settings.getString("password", "none").equals(passwordTxt)){
            return true;
        }
        else if(!settings.getString("prefCurrency", "none").equals(prefCurrencyTxt)){
            return true;
        }
        else if(!settings.getString("prefExchange", "none").equals(prefExchangeTxt)){
            return true;
        }
        else {
            return false;
        }
    }
}
