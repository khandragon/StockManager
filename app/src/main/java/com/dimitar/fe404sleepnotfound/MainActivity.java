package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the username from SharedPreferences and display in bottom TextView
        SharedPreferences settings = getSharedPreferences("com.dimitar.fe404sleepnotfound", MODE_PRIVATE);
        String username = settings.getString("username", getString(R.string.noUser));
        TextView userTxtView = findViewById(R.id.username);
        userTxtView.setText(username);
    }

    public void openAbout(View v){
        Intent openAbout = new Intent(this, AboutActivity.class);
        startActivity(openAbout);
    }
}
