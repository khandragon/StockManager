package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.data.CircularArray;
import com.dimitar.fe404sleepnotfound.data.Hint;
import com.dimitar.fe404sleepnotfound.persistence.HintDAOFirebase;
import com.google.firebase.auth.AuthResult;

import java.util.Arrays;

public final class HintActivity extends Activity {
    private final static String TAG = "HintActivity";
    private ImageView hintView;
    private ImageButton right;
    private ImageButton left;

    private CircularArray<Hint> hints;

    private Bundle appBundle;
    private final static String HINT_BUNDLE_KEY = "hint_array";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBundle = savedInstanceState;
        setContentView(R.layout.activity_hint);

        initViews();

        HintDAOFirebase.login()
                .addOnSuccessListener(this, this::onLoginSuccess)
                .addOnFailureListener(this::onLoginFailed);
    }

    /**
     *
     * @param exc
     */
    private void onLoginFailed(Exception exc) {
        Toast.makeText(getApplicationContext(), "Login Error : Try again later", Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param result
     */
    private void onLoginSuccess(AuthResult result) {
        if(appBundle != null && appBundle.containsKey(HINT_BUNDLE_KEY)) {
            hints = appBundle.getParcelable(HINT_BUNDLE_KEY);

            setupHintCircularArray();
        }
        else {
            HintDAOFirebase.getInstance().readAllHints(this::initHints);
        }
    }

    /**
     *
     */
    private void initViews() {
        hintView = findViewById(R.id.hint);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
    }

    /**
     *
     * @param hintArr
     */
    private void initHints(Hint[] hintArr) {
        // init the hint circular array
        shuffleArray(hintArr);
        Log.d(TAG, Arrays.toString(hintArr));
        hints = new CircularArray<>(hintArr);

        setupHintCircularArray();
    }

    private void setupHintCircularArray() {
        right.setOnClickListener(hints::next);
        left.setOnClickListener(hints::prev);

        // Manage ImageView with the hint
        changeHintView(hints.getCurrent());
        hints.onCurrentChange(this::changeHintView);
        hintView.setOnClickListener(e -> openHintSource(hints.getCurrent()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save my counter
        outState.putParcelable(HINT_BUNDLE_KEY, hints);
    }

    /**
     * Changes which Hint the hintView ImageView is showing
     *
     * @param newCurrentHint the Hint hintView will now be showing
     */
    private void changeHintView(Hint newCurrentHint) {
        hintView.setImageBitmap(newCurrentHint.img);
    }

    /**
     * Shuffles the array on itself
     *
     * @param array array to shuffle
     * @param <T> type of elements in the array
     */
    private <T> void shuffleArray(T[] array) {
    }

    /**
     * Opens the source of hint in the default browser
     *
     * @param hint the hint providing the url
     */
    private void openHintSource(Hint hint) {
        Toast.makeText(getApplicationContext(), hint.url, Toast.LENGTH_SHORT).show();
    }
}
