package com.dimitar.fe404sleepnotfound;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.data.CircularArray;
import com.dimitar.fe404sleepnotfound.data.Hint;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;
import com.dimitar.fe404sleepnotfound.persistence.HintDAOFirebase;
import com.google.firebase.auth.AuthResult;

import java.util.Arrays;
import java.util.Random;

public final class HintActivity extends MenuActivity {
    private final static String TAG = "HintActivity";

    private AnimationDrawable downloadAnimation;

    private ImageView hintView;
    private ImageButton right;
    private ImageButton left;

    private CircularArray<Hint> hints = null;

    private final static String HINT_BUNDLE_KEY = "hint_array";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        initViews();

        startDownloadAnimation();

        HintDAOFirebase.login()
                .addOnSuccessListener(this, this::onLoginSuccess)
                .addOnFailureListener(this::onLoginFailed);
    }

    /**
     * Initializes the class-level variables with views from the layout
     */
    private void initViews() {
        hintView = findViewById(R.id.hint);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
    }

    /**
     * Alerts the user that application login failed
     *
     * @param exc details of the exception failure
     */
    private void onLoginFailed(Exception exc) {
        Toast.makeText(getApplicationContext(), "Login Error : Try again later", Toast.LENGTH_LONG).show();
    }

    /**
     * Display hints from the state bundle or from the firebase database
     *
     * @param result the result from the authentication
     */
    private void onLoginSuccess(AuthResult result) {
        Log.d(TAG, "Successful Login!!");
        if(hints != null) {
            Log.d(TAG, "onLoginSuccess : get hints from the bundle");
            setupHintCircularArray();
        }
        else {
            Log.d(TAG, "onLoginSuccess : download hints from firebase");
            HintDAOFirebase.getInstance().readAllHints(this::initHints);
        }
    }

    /**
     * Initializes the 'hints' CircularArray with an array of hints
     *
     * @param hintArr hints downloaded from firebase
     */
    private void initHints(Hint[] hintArr) {
        // init the hint circular array
        hintArr = shuffleArray(hintArr);
        Log.d(TAG, "Received Hints : " + Arrays.toString(hintArr));
        hints = new CircularArray<>(hintArr);

        setupHintCircularArray();
    }

    /**
     * Attaches observers to the 'hints' CircularArray,
     * enables the navigation of the CircularArray with ui input
     */
    private void setupHintCircularArray() {
        right.setOnClickListener(hints::next);
        left.setOnClickListener(hints::prev);

        // Manage ImageView with the hint
        stopDownloadAnimation();

        changeHintView(hints.getCurrent());
        hints.onCurrentChange(this::changeHintView);
        hintView.setOnClickListener(e -> openHintSource(hints.getCurrent()));
    }

    /**
     * Saves the 'hints' CircularArray
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(hints != null) {
            outState.putParcelable(HINT_BUNDLE_KEY, hints);
        }
    }

    /**
     * Loads the 'hint' CircularArray
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        hints = savedInstanceState.getParcelable(HINT_BUNDLE_KEY);
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
     * Shuffles the array using the Fisher-Yates algorithm
     *
     * @param array array to shuffle
     * @param <T> type of elements in the array
     *
     * @return the shuffled version of the input array
     */
    private <T> T[] shuffleArray(T[] array) {
        T[] outArray = (T[]) new Object[array.length];

        int[] scratch = range(0, array.length);
        int limit = array.length - 1;
        Random rand = new Random();

        int toPick;
        for(int i = 0; i < outArray.length; i++, limit--) {
            toPick = rand.nextInt(limit + 1);
            outArray[i] = array[scratch[toPick]];

            permute(scratch, toPick, limit);
        }

        return outArray;
    }

    /**
     * Generates an int array for a given range
     *
     * @param from inclusive starting number
     * @param to exclusive last number
     *
     * @return the array filled with numbers of the range described by 'from' and 'to'
     */
    private int[] range(int from, int to) {
        int[] array = new int[to - from];

        for(int i = 0; i < array.length; i++) {
            array[i] = from++;
        }

        return array;
    }

    /**
     * Inter-changes what is at indexA with what is at indexB
     *
     * @param array in which to permute
     * @param indexA
     * @param indexB
     */
    private void permute(int[] array, int indexA, int indexB) {
        int temp = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = temp;
    }

    /**
     * Opens the source of hint in the default browser
     *
     * @param hint the hint providing the url
     */
    private void openHintSource(Hint hint) {
        Intent httpIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hint.url));
        startActivity(httpIntent);
    }

    /**
     * Initiates the download animation for the hint view
     */
    private void startDownloadAnimation() {
        hintView.setImageResource(R.drawable.download_animation);
        downloadAnimation = (AnimationDrawable) hintView.getDrawable();
        downloadAnimation.start();
    }

    /**
     * Stops the animation, the last frame of the animmation is left in the hint view
     */
    private void stopDownloadAnimation() {
        downloadAnimation.stop();
    }
}
