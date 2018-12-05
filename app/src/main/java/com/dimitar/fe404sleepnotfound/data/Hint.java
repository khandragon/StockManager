package com.dimitar.fe404sleepnotfound.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * Visual Hint, the hint is written in the image,
 * there also is a source supporting the hint
 */
public final class Hint implements Parcelable {
    /**
     * Image data
     */
    public Bitmap img;
    /**
     * Path pointing to the image file
     */
    public String imgName;
    /**
     * Points to a website which is the source of this Hint
     */
    public String url;

    public Hint() {
    }

    protected Hint(Parcel in) {
        img = in.readParcelable(Bitmap.class.getClassLoader());
        url = in.readString();
        imgName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        img.writeToParcel(dest, flags);
        dest.writeString(url);
        dest.writeString(imgName);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint = (Hint) o;
        return Objects.equals(imgName, hint.imgName) &&
                Objects.equals(url, hint.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(imgName, url);
    }
}
