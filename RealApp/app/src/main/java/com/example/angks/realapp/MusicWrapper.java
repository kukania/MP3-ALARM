package com.example.angks.realapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by angks on 2016-03-26.
 */
public class MusicWrapper implements Parcelable{
    public String title="";
    public String artist="";
    public MusicWrapper(){}
    public MusicWrapper(Parcel src){
        title=src.readString();
        artist=src.readString();
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MusicWrapper createFromParcel(Parcel in) {
            return new MusicWrapper(in);
        }

        public MusicWrapper[] newArray(int size) {
            return new MusicWrapper[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
    }
}
