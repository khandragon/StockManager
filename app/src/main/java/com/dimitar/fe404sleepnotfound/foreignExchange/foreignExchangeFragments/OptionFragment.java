package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeAsync.RetreveRates;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

public class OptionFragment extends Fragment {

    private static final String TAG = "OptionFragment";

    JSONObject currencyRates;

    View fragmentView;
    Context context;

    EditText amountInput;
    TextView fromView;
    TextView toView;
    TextView totalView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_options, container, false);
        this.context = container.getContext();

        amountInput = fragmentView.findViewById(R.id.amountInput);
        fromView = fragmentView.findViewById(R.id.fromCur);
        toView = fragmentView.findViewById(R.id.toCur);
        totalView = fromView.findViewById(R.id.amountTotal);

        getRatesList();

        return fragmentView;
    }

    private void getRatesList(){
        try{
            RetreveRates retreveRates = new RetreveRates();
            String currencyListString = retreveRates.execute().get().toString();
            this.currencyRates = new JSONObject(currencyListString);
        }catch (ConcurrentModificationException e){
            Log.wtf(TAG, "ConcurrentModificationException");
        }catch (InterruptedException e){
            Log.wtf(TAG, "InterruptedException");
        }catch (ExecutionException e){
            Log.wtf(TAG, "ExecutionException");
        }catch (JSONException e){
            Log.wtf(TAG, "JSONException");
        }
    }

    public void updateText(String newCurrency){
        Log.wtf(TAG, newCurrency);
    }
}
