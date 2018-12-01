package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
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
import com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeAsync.RetrieveRates;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

/**
 * Fragment to display the view that allows you to convert a currency
 * Displays a numberEditText to get the amount you want to convert
 * displays the to and from currencys that were selected or the defaults
 * along with the total of the conversion
 *
 * @author Jamroa
 */
public class OptionFragment extends Fragment {

    private static final String TAG = "OptionFragment";
    private SharedPreferences settings;
    private JSONObject currencyRates;

    private View fragmentView;
    private Context context;

    private EditText amountInput;
    private TextView fromView;
    private TextView toView;
    private TextView totalView;

    /**
     * default on create of a fragment
     * disables the openSoftKeyboard to on the launch of the fragment
     * has a onChangListener to the editText to remove the need of a submit button
     * sets up the Views with the default values
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_options, container, false);
        this.context = container.getContext();
        //To Stop the input Keyboard from showing on launch
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        amountInput = fragmentView.findViewById(R.id.amountInput);
        //Sets the Default value of the amount
        amountInput.setText("1");
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
        settings = context.getSharedPreferences("com.dimitar.fe404sleepnotfound",  Context.MODE_PRIVATE);
        if(!settings.getString("prefCurrency", "none").equals("none")){
            fromView.setText(settings.getString("prefCurrency", "none"));
        }
        toView.setText("Please Select");
        getRatesList();
        calTotal();
        return fragmentView;
    }

    /**
     * Gets the Rates JSONobject from the RetrieveRates async task and set it to currencyRates
     */
    private void getRatesList(){
        try{
            RetrieveRates retreveRates = new RetrieveRates();
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

    /**
     * Will update the textViews on the options fragment and calls the calculate total method
     *
     * @param newCurrency new CurrencySelect object that is send from the RecyclerView
     */
    public void updateText(CurrencySelect newCurrency){
        String type = newCurrency.getType();
        switch (type){
            case "To":
                toView.setText(newCurrency.getCurrency());
                calTotal();
                break;
            case "From":
                fromView.setText(newCurrency.getCurrency());
                calTotal();
                break;
            default:
                Log.e(TAG, "Wrong currency type");
                break;
        }
    }

    /**
     * Makes Sure that there are no empty View and then converts the value in the editText from the
     * currency of the fromValue to the toValue and displays the reuslt;
     */
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
