package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeAsync.RetreiveCurrencyData;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

/**
 * Fragment to display a recyclerView with all the currencies from the openExchange api
 *
 * @author Jamroa
 */
public class CurrencyFragment extends Fragment {

    private static final String TAG = "CurrencyFragment";
    private String currencyListUrl = "https://openexchangerates.org/api/currencies.json";

    private View fragmentView;
    private Context context;
    private RecyclerAdapter adapter;
    private currencyListner mListner;
    private String type;

    public interface currencyListner{
        void onCurrencySent(CurrencySelect selected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_currency, container, false);
        this.context = container.getContext();
        mListner = (currencyListner) context;
        getCurrencyList();
        return fragmentView;
    }

    /**
     *Calls the Async task to get the string of currencies from the api
     *Will call createCurrenciesList if task returns a string to convert the sting into a arrayList
     */
    private void getCurrencyList(){
        try{
            RetreiveCurrencyData retreveCurrencies = new RetreiveCurrencyData(currencyListUrl, "");
            String currencyListString = retreveCurrencies.execute().get().toString();
            Log.d(TAG, currencyListString);
            createCurrenciesList(currencyListString);
        }catch (ConcurrentModificationException e){
            Log.e(TAG, "ConcurrentModificationException");
        }catch (InterruptedException e){
            Log.e(TAG, "InterruptedException");
        }catch (ExecutionException e){
            Log.e(TAG, "ExecutionException");
        }
    }

    /**
     * Takes the String of currencys and splits it into a arrayList
     * Takes a string and not a json object because to dispay it it needs to be a string
     *
     * @param currencyListString String of all currencis that is returned from the api call
     */
    private void createCurrenciesList(String currencyListString) {
        ArrayList<String> currencies = new ArrayList<>();
        //Each row of the string is split
        String[] rows = currencyListString.split(",");
        for(String row : rows){
            //Splits each row into there Ticker symbol and there name
            String[] brokenRow = row.split("\"");
            String cur = brokenRow[1] + "," + brokenRow[3];
            currencies.add(cur);
        }
        displayCurrencies(currencies);
    }

    /**
     * Displays the Sets the Recycler view adapter onto the Recycler view in the Fragment
     *
     * @param currencies A array List of all the currencies and there ticker symbol for the adapter
     */
    private void displayCurrencies(ArrayList<String> currencies){
        RecyclerView recyclerView = fragmentView.findViewById(R.id.currencyView);
        adapter = new RecyclerAdapter(currencies, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        adapter.setRecyclerAdapterListener(new RecyclerAdapter.RecyclerAdapterListener() {
            @Override
            public void onItemClick(String item) {
                if(mListner != null){
                    mListner.onCurrencySent(new CurrencySelect(item, type));
                }else{
                    //If mListner was not set for any reason
                    mListner = (currencyListner) context;
                    Log.d(TAG, "listner was null");
                    mListner.onCurrencySent(new CurrencySelect(item, type));
                }
            }
        });
    }

    /**
     * Attaches the listner for the click lister in the recycler view
     *
     * @param context currency Listner
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof currencyListner){
            mListner = (currencyListner) context;
        }
    }

    /**
     * Detaches the listener of the click listener
     */
    @Override
    public void onDetach(){
        super.onDetach();
        mListner = null;
    }

    /**
     * Allows you to set the type of the currency fragment to be able to identify the fragment object
     *
     * @param type String of what type you want to set the fragment to
     */
    public void setType(String type){
        this.type = type;
    }
}
