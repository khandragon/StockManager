package com.dimitar.fe404sleepnotfound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.menu.AboutActivity;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;
import com.dimitar.fe404sleepnotfound.notes.NotesActivity;
import com.dimitar.fe404sleepnotfound.quotes.StockActivity;

/**
 * Shows the user the application logo, a Menu, a list of launcher buttons for
 * each functionality Activities and the current user's name. From here, the user can launch the
 * Activity related to the function described by the buttons.
 */
public class MainActivity extends MenuActivity {

    /**
     * Custom implementation of the onCreate lifecycle method. It sets the view's contents and gets
     * the username of the current user set in Settings.
     *
     * @param savedInstanceState
     */
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

    /**
     * Implementation of the onClick for the logo ImageView that opens the About Activity.
     *
     * @param v
     */
    public void openAbout(View v) {
        Intent openAbout = new Intent(this, AboutActivity.class);
        startActivity(openAbout);
    }

    /**
     * Implementation of the onClick for the Notes btn that opens the NotesActivity
     *
     * @param view
     */
    public void openNotes(View view) {
        Intent openNotes = new Intent(this, NotesActivity.class);
        startActivity(openNotes);
    }

    /**
     * Implementation of the onClick for the Quotes btn that opens the Stock activity
     *
     * @param view
     */
    public void openQuotes(View view) {
        Intent openQuotes = new Intent(this, StockActivity.class);
        startActivity(openQuotes);
    }
}
