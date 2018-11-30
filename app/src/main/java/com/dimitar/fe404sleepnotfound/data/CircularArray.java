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

public class CircularArray<T extends Parcelable> implements Parcelable {
    private final static String TAG = "CircularArray";
    protected CircularArray(Parcel in) {
        backingArray = (T[]) in.readArray(Parcelable.class.getClassLoader());
        currIndex = in.readInt();
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currIndex);
        dest.writeArray(backingArray);
    }

    private final T[] backingArray;
    private int currIndex;

    private OnChangeCurrentEventListener<T> changeCurrentListener;
    private void fireChangeCurrentEvent() {
        if(changeCurrentListener != null) {
            changeCurrentListener.consumeCurrent(backingArray[currIndex]);
        }
    }

    @FunctionalInterface
    public interface OnChangeCurrentEventListener<T> {
        void consumeCurrent(T newCurrent);
    }

    public CircularArray(T... backingArray) {
        T[] copy = (T[]) new Parcelable[backingArray.length];
        System.arraycopy(backingArray, 0, copy, 0, backingArray.length);

        this.backingArray = copy;
        currIndex = 0;
    }

    public void next(Object context) {
        currIndex = (currIndex + 1) % backingArray.length;

        fireChangeCurrentEvent();
    }

    public void prev(Object context) {
        currIndex--;
        if(currIndex < 0) {
            currIndex = backingArray.length - 1;
        }

        fireChangeCurrentEvent();
    }

    public void onCurrentChange(OnChangeCurrentEventListener<T> changeCurrentListener) {
        this.changeCurrentListener = changeCurrentListener;
    }

    public T getCurrent() {
        return backingArray[currIndex];
    }
}