package com.dimitar.fe404sleepnotfound.persistence;

import android.util.Log;

import com.dimitar.fe404sleepnotfound.data.Hint;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HintDAOFirebase implements HintDAO {
    private final static String TAG = "HintDAOFirebase";
    private static HintDAO instance;

    public static HintDAO getInstance() {
        if(instance == null) {
            instance = new HintDAOFirebase();
        }

        return instance;
    }

    /**
     * Shortcut method for authentication.
     * e.g.: HintDAOFirebase.login().addOnSuccessListener(...).addOnFailureListener(...)
     *
     * @return a Task on which to attach listeners
     */
    public static Task<AuthResult> login() {
        return FirebaseAuth.getInstance().signInAnonymously();
    }

    /**
     * Shortcut to get entire database reference
     *
     * @return a database reference
     */
    private DatabaseReference db() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public void readAllHints(Consumer<Hint[]> consumer) {
        db().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consumer.consume(dataSnapshot.getValue(Hint[].class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getDetails());
                consumer.consume(new Hint[0]);
            }
        });
    }
}
