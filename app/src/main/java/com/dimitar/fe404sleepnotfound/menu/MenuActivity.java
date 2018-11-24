package com.dimitar.fe404sleepnotfound.menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import com.dimitar.fe404sleepnotfound.R;

/**
 * Contains Menu features. An Activity that wants to display the menu and make its functionality
 * accessible has to extends this class.
 */
public class MenuActivity extends Activity {

    /**
     * Inflates the Menu.
     * @param menu
     * @return true if the Menu was successfully inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Performs an Action depending on which MenuItem was selected.
     * @param item
     * @return true if an option was successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Open SettingsActivity
        if(item.getItemId() == R.id.settings){
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        }
        //Open Dawson Computer Science web page
        else if(item.getItemId() == R.id.dawson){
            Intent openDawsonPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
            startActivity(openDawsonPage);
            return true;
        }
        //Open AboutActivity
        else if(item.getItemId() == R.id.about){
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        }
        else{
            return false;
        }
    }
}
