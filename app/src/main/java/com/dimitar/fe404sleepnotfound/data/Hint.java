package com.dimitar.fe404sleepnotfound.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Visual Hint, the hint is written in the image,
 * there also is a source supporting the hint
 */
public final class Hint implements Parcelable{
    /**
     * Path pointing to the image file
     */
    public Bitmap img;
    /**
     * Points to a website which is the source of this Hint
     */
    public String url;

    public Hint() {
    }

    protected Hint(Parcel in) {
        img = in.readParcelable(Bitmap.class.getClassLoader());
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        img.writeToParcel(dest, flags);
        dest.writeString(url);
    }

    public static final Creator<Hint> CREATOR = new Creator<Hint>() {
        @Override
        public Hint createFromParcel(Parcel in) {
            return new Hint(in);
        }

        @Override
        public Hint[] newArray(int size) {
            return new Hint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
