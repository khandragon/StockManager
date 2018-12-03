package com.dimitar.fe404sleepnotfound.foreignExchange;

import android.app.FragmentTransaction;
import android.nfc.Tag;
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

        if(savedInstanceState == null) {
            //Call method to set the fragments
            setFragments();
        }else{
            optionsFragment = (OptionFragment) getFragmentManager().getFragment(savedInstanceState, "optFrag");
            currencyFragmentFrom = (CurrencyFragment) getFragmentManager().getFragment(savedInstanceState, "fromFrag");
            currencyFragmentFrom.setType("From");
            currencyFragmentTo = (CurrencyFragment) getFragmentManager().getFragment(savedInstanceState, "toFrag");
            currencyFragmentTo.setType("To");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState,"optFrag", optionsFragment);
        getFragmentManager().putFragment(outState,"fromFrag", currencyFragmentFrom);
        getFragmentManager().putFragment(outState,"toFrag", currencyFragmentTo);

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
        Log.d(TAG, selected.getCurrency());
        Log.d(TAG, selected.getType());
        optionsFragment.updateText(selected);
    }
}
