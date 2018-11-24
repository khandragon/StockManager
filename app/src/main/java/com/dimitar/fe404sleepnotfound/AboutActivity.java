package com.dimitar.fe404sleepnotfound;

import android.os.Bundle;

/**
 * Shows the user information about the application, its developers and their pictures.
 */
public class AboutActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
