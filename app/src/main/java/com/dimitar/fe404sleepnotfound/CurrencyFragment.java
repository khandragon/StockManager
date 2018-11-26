package com.dimitar.fe404sleepnotfound;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragment extends Fragment {

    JSONObject currencyJson;
    List<String> currencies;

    public CurrencyFragment() {
        // Required empty public constructor
    }

    public void setFragmentJson(CurrencyFragment currencyFragment){
        this.currencyJson = currencyJson;

        currencies = new ArrayList<>();
        try{
            Iterator currencyIretator = currencyJson.keys();
            while (currencyIretator.hasNext()){
                currencies.add(currencyIretator.next().toString() + currencyJson.get(currencyIretator.next().toString()));
            }
        }catch (Exception e){

        }

        setFragmentContents();
    }

    private void setFragmentContents(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }

}
