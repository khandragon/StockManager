package com.dimitar.fe404sleepnotfound.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dimitar.fe404sleepnotfound.data.Hint;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class HintDAOFirebase implements HintDAO {
    private final static int KIBI = 1024;
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

    private StorageReference img() {
        return FirebaseStorage.getInstance().getReference();
    }

    public void readAllHints(Consumer<Hint[]> consumer) {
        db().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Hint[] hints;
                GenericTypeIndicator<List<Map<String, String>>> listType =
                        new GenericTypeIndicator<List<Map<String, String>>>(){};

                List<Map<String, String>> hintMaps = dataSnapshot.getValue(listType);
                hints = new Hint[hintMaps.size()];

                Task<byte[]> previousTask = null;
                for(int i = 0; i < hints.length; i++) {
                    hints[i] = new Hint();
                    Map<String, String> hintMap = hintMaps.get(i);

                    hints[i].url = hintMap.get("url");
                    hints[i].imgName = hintMap.get("img");

                    if(previousTask == null) {
                        previousTask = getBitmapTask(hints[i].imgName, hints[i]);
                    }
                    else {
                        Task newTask = getBitmapTask(hints[i].imgName, hints[i]);

                        previousTask.continueWithTask(task -> newTask);
                        previousTask = newTask;
                    }

                    if((i + 1) >= hints.length) {
                        previousTask
                                .addOnSuccessListener(bytes -> consumer.consume(hints))
                                .addOnFailureListener(exc -> {
                                    Log.d(TAG, exc.getMessage());
                                    consumer.consume(new Hint[0]);
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getDetails());
                consumer.consume(new Hint[0]);
            }
        });
    }

    private Task<byte[]> getBitmapTask(String imageName, Hint hint) {
        return img().child(imageName)
                .getBytes(250 * KIBI)
                .addOnSuccessListener(bytes ->
                        hint.img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}
