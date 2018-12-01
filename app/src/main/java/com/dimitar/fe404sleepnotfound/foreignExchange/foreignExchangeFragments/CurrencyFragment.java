package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeAsync.RetreveCurrencies;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

public class CurrencyFragment extends Fragment {

    private static final String TAG = "CurrencyFragment";

    private View fragmentView;
    private Context context;
    private RecyclerAdapter adapter;
    private currencyListner mListner;
    private String type;

    public interface currencyListner{
        void onCurrencySent(String selected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_currency, container, false);
        this.context = container.getContext();
        getCurrencyList();
        return fragmentView;
    }

    private void getCurrencyList(){
        try{
            RetreveCurrencies retreveCurrencies = new RetreveCurrencies();
            String currencyListString = retreveCurrencies.execute().get().toString();
            //Log.wtf(TAG, currencyListString);
            createCurrenciesList(currencyListString);
        }catch (ConcurrentModificationException e){
            Log.wtf(TAG, "ConcurrentModificationException");
        }catch (InterruptedException e){
            Log.wtf(TAG, "InterruptedException");
        }catch (ExecutionException e){
            Log.wtf(TAG, "ExecutionException");
        }
    }

    private void createCurrenciesList(String currencyListString) {
        ArrayList<String> currencies = new ArrayList<>();
        String[] temp = currencyListString.split(",");
        for(String row : temp){
            String[] brokenRow = row.split("\"");
            String cur = brokenRow[1] + "," + brokenRow[3];
            currencies.add(cur);
        }
        displayCurrencies(currencies);
    }

    private void displayCurrencies(ArrayList<String> currencies){
        RecyclerView recyclerView = fragmentView.findViewById(R.id.currencyView);
        adapter = new RecyclerAdapter(currencies, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        adapter.setRecyclerAdapterListener(new RecyclerAdapter.RecyclerAdapterListener() {
            @Override
            public void onItemClick(String item) {
                mListner.onCurrencySent(item);
            }
        });
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof currencyListner){
            mListner = (currencyListner) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListner = null;
    }

    public void setType(String type){
        this.type = type;
    }
}
