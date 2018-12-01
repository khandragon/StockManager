package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.CurrencyFragment;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.CurrencySelect;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.OptionFragment;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

/**
 * Class responsable for creating and controling the foreign exchange actiivity
 * sets the 2 recycler view fragmenst and a option menu fragment.
 * uses currency listner to pass information from the currency fragment to the option fragment
 *
 * @author Jamroa
 */
public class ForeignExchangeActivity extends MenuActivity implements CurrencyFragment.currencyListner{

    private String TAG = "ForeignExchangeActivity";

    private OptionFragment optionsFragment;
    private CurrencyFragment currencyFragmentFrom;
    private CurrencyFragment currencyFragmentTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_exchange);

        //Call method to set the fragments
        setFragments();
    }

    /**
     *Creates the fragment objects and places them on the foreign exchange activity
     */
    private void setFragments(){
        optionsFragment = new OptionFragment();
        FragmentTransaction ftO = getFragmentManager().beginTransaction();
        ftO.replace(R.id.optionsLayout, optionsFragment);
        ftO.commit();

        currencyFragmentFrom = new CurrencyFragment();
        currencyFragmentFrom.setType("From");
        FragmentTransaction ftF = getFragmentManager().beginTransaction();
        ftF.replace(R.id.fromCurrency, currencyFragmentFrom);
        ftF.commit();

        currencyFragmentTo = new CurrencyFragment();
        currencyFragmentTo.setType("To");
        FragmentTransaction ftT = getFragmentManager().beginTransaction();
        ftT.replace(R.id.toCurrency, currencyFragmentTo);
        ftT.commit();
    }

    /**
     * Custom event listener for the 2 currencyFragment objects
     * Passes the sent information to the options fragment
     *
     * @param selected a object of the type of currency fragment and currency selected
     */
    @Override
    public void onCurrencySent(CurrencySelect selected) {
        optionsFragment.updateText(selected);
    }
}
