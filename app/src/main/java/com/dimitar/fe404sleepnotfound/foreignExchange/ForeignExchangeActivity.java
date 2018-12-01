package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.CurrencyFragment;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments.OptionFragment;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

public class ForeignExchangeActivity extends MenuActivity implements CurrencyFragment.currencyListner{

    OptionFragment optionsFragment;
    CurrencyFragment currencyFragmentFrom;
    CurrencyFragment currencyFragmentTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_exchange);

        setFragments();
    }

    /**
     * Will set the fragments of the activity to there correct postion
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

    @Override
    public void onCurrencySent(String selected) {
        Log.wtf("TestFrag", selected);
    }
}
