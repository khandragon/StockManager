package com.dimitar.fe404sleepnotfound;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dimitar.fe404sleepnotfound.data.CircularArray;
import com.dimitar.fe404sleepnotfound.persistence.HintDAO;
import com.dimitar.fe404sleepnotfound.persistence.HintDAOFirebase;

public final class HintActivity extends Activity {
    private ImageView hintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        // HintDAOFirebase.auth()
        final HintDAO dao = HintDAOFirebase.getInstance();

        // Fetching data from firebase
        Hint[] readHints = dao.readAllHints();
        shuffleArray(readHints);
        final CircularArray<Hint> hints = new CircularArray<>(readHints);

        // Manage ImageView with the hint
        hints.onCurrentChange(this::changeHintView);
        hintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHintSource(hints.getCurrent());
            }
        });

        // Manage left/right buttons
        // ImageButton rightArrow
        rightArrow.setOnClickListener(hints::next);
        // ImageButton leftArrow
        leftArrow.setOnClickListener(hints::prev);
    }

    /**
     * Changes which Hint the hintView ImageView is showing
     *
     * @param newCurrentHint the Hint hintView will now be showing
     */
    private void changeHintView(Hint newCurrentHint) {

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

    }
}
