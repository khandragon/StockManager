package com.dimitar.fe404sleepnotfound;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dimitar.fe404sleepnotfound.adapters.CurrencyAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragment extends Fragment {

    JSONObject currencyJson;
    ArrayList<String> currencies;

    Context context;
    View fragmentView;

    ListView currencyView;

    public CurrencyFragment() {
        // Required empty public constructor
    }

    public void setFragmentJson(JSONObject currencyJson){
        this.currencyJson = currencyJson;

        currencies = new ArrayList<>();
        try{
            Iterator currencyIretator = currencyJson.keys();
            while (currencyIretator.hasNext()){
                currencies.add(currencyIretator.next().toString() + ":" + currencyJson.get(currencyIretator.next().toString()));
            }
        }catch (Exception e){

        }
        setFragmentContents();
    }

    private void setFragmentContents(){
        CurrencyAdapter adapter = new CurrencyAdapter(this.context, R.layout.currencylistitem, currencies);
        ListView listData = (ListView) fragmentView.findViewById(R.id.currencyView);
        listData.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_currency, container, false);
        this.context = container.getContext();
        return fragmentView;
    }

}
