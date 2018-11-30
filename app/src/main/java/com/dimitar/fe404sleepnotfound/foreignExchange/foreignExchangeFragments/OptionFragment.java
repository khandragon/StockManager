package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimitar.fe404sleepnotfound.R;

public class OptionFragment extends Fragment {

    View fragmentView;
    Context context;

    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_options, container, false);
        this.context = container.getContext();
        return fragmentView;
    }


}
