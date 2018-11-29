package com.dimitar.fe404sleepnotfound.menu;

import android.os.Bundle;

import com.dimitar.fe404sleepnotfound.R;

/**
 * Shows the user information about the application, its developers and their pictures.
 */
public class AboutActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

//    @Override
//    protected void onPause() {
////        super.onPause();
////        finish();
//    }
}
