package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    private JSONObject currencyRates;

    private View fragmentView;
    private Context context;

    private EditText amountInput;
    private TextView fromView;
    private TextView toView;
    private TextView totalView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_options, container, false);
        this.context = container.getContext();
        //To Stop the input Keyboard from showing on launch
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        amountInput = fragmentView.findViewById(R.id.amountInput);
        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calTotal();
            }
        });
        fromView = fragmentView.findViewById(R.id.fromCur);
        toView = fragmentView.findViewById(R.id.toCur);
        totalView = fragmentView.findViewById(R.id.amountTotal);

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
        String[] updateText = newCurrency.split(",");
        String type = updateText[1];
        switch (type){
            case "To":
                toView.setText(updateText[0]);
                calTotal();
                break;
            case "From":
                fromView.setText(updateText[0]);
                calTotal();
                break;
            default:

                break;
        }
    }

    private void calTotal(){
        if(!amountInput.getText().toString().matches("")) {
            if(!fromView.getText().toString().matches("")) {
                if (!toView.getText().toString().matches("")) {
                    try {
                        Double val = Double.parseDouble(amountInput.getText().toString());
                        //Base will always be USD
                        //Double base = Double.parseDouble(currencyRates.get("USD").toString());
                        Double from = Double.parseDouble(currencyRates.get(fromView.getText().toString()).toString());
                        Double to = Double.parseDouble(currencyRates.get(toView.getText().toString()).toString());

                        Double temp = val/from;
                        Log.wtf("this", String.valueOf(temp*to));
                        totalView.setText(String.valueOf(temp*to));
                    } catch (Exception e) {
                        Log.wtf("test", e.toString());
                    }
                }
            }
        }
    }

}
