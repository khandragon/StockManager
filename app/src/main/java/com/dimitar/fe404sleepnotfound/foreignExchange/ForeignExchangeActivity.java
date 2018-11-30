package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

public class ForeignExchangeActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_exchange);

        setFragments();
    }

    private void setFragments(){
        /*
        optionsFragment = new OptionsFragment();
        FragmentTransaction ftO = getFragmentManager().beginTransaction();
        ftO.replace(R.id.optionsLayout, optionsFragment);
        ftO.commit();

        currencyFragmentFrom = new CurrencyFragment();
        FragmentTransaction ftF = getFragmentManager().beginTransaction();
        ftF.replace(R.id.fromCurrency, currencyFragmentFrom);
        ftF.commit();

        currencyFragmentTo = new CurrencyFragment();
        FragmentTransaction ftT = getFragmentManager().beginTransaction();
        ftT.replace(R.id.toCurrency, currencyFragmentTo);
        ftT.commit();
        */
    }

}
