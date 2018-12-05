package com.dimitar.fe404sleepnotfound.data;


import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonWriter;
import android.util.Log;

import com.google.android.gms.common.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Array which supports index wrapping, meaning if traversed too far to the right/left,
 * brings you back to the start/end
 *
 * @param <T> Type of elements contained
 */
public class CircularArray<T extends Parcelable> implements Parcelable {
    private final static String TAG = "CircularArray";

    /**
     * Used for state bundle loading
     *
     * @param in
     */
    protected CircularArray(Parcel in) {
        backingArray = (T[]) in.readArray(Parcelable.class.getClassLoader());
        currIndex = in.readInt();
    }

    /**
     * Used for state bundle loading
     */
    public static final Creator<CircularArray> CREATOR = new Creator<CircularArray>() {
        @Override
        public CircularArray createFromParcel(Parcel in) {
            return new CircularArray(in);
        }

        @Override
        public CircularArray[] newArray(int size) {
            return new CircularArray[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Used for state bundle saving
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currIndex);
        dest.writeArray(backingArray);
    }

    private final T[] backingArray;
    private int currIndex;

    /**
     * Used when 'currIndex' changes
     */
    private OnChangeCurrentEventListener<T> changeCurrentListener;
    private void fireChangeCurrentEvent() {
        if(changeCurrentListener != null) {
            changeCurrentListener.consumeCurrent(backingArray[currIndex]);
        }
    }
    public void onCurrentChange(OnChangeCurrentEventListener<T> changeCurrentListener) {
        this.changeCurrentListener = changeCurrentListener;
    }

    @FunctionalInterface
    public interface OnChangeCurrentEventListener<T> {
        void consumeCurrent(T newCurrent);
    }

    /**
     * Constructs a new CircularArray from an array. the array is deep-copied
     *
     * @param backingArray contained elements
     */
    public CircularArray(T... backingArray) {
        T[] copy = (T[]) new Parcelable[backingArray.length];
        System.arraycopy(backingArray, 0, copy, 0, backingArray.length);

        this.backingArray = copy;
        currIndex = 0;
    }

    /**
     * Iterate to the right (increases the index)
     * @param context to match method signature of event listener
     */
    public void next(Object context) {
        currIndex = (currIndex + 1) % backingArray.length;

        fireChangeCurrentEvent();
    }

    /**
     * Iterate to the left (decreases the index)
     * @param context to match method signature of event listener
     */
    public void prev(Object context) {
        currIndex--;
        if(currIndex < 0) {
            currIndex = backingArray.length - 1;
        }

        fireChangeCurrentEvent();
    }

    public T getCurrent() {
        return backingArray[currIndex];
    }
}