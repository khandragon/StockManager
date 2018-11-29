package com.dimitar.fe404sleepnotfound.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Open SettingsActivity
        if(item.getItemId() == R.id.settings){

            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            finish();
            return true;
        }
        //Open Dawson Computer Science web page
        else if(item.getItemId() == R.id.dawson){
            Intent openDawsonPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
            startActivity(openDawsonPage);
            return true;
        }
        else{
            return false;
        }
    }
}
