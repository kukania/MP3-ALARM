package com.example.angks.realapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by angks on 2016-03-27.
 */
public class AlarmItem implements Parcelable{
    public int[] weekday=new int[7];
    public boolean isAm;
    public int hour;
    public int minute;
    public boolean check;
    public int code;
    public AlarmItem(Parcel in){
        for(int i=0; i<7; i++)
            weekday[i]=in.readInt();
        if(in.readInt()==1) isAm=true; else isAm=false;
        hour=in.readInt();
        minute=in.readInt();
        if(in.readInt()==1) check=true; else check=false;
        code=in.readInt();
    }
    public AlarmItem(){
        isAm=true;
        check=true;
        for(int i=0; i<7; i++)
            weekday[i]=0;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlarmItem createFromParcel(Parcel in) {
            return new AlarmItem(in);
        }

        public AlarmItem[] newArray(int size) {
            return new AlarmItem[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for(int i=0; i<7; i++)
            dest.writeInt(weekday[i]);
        if(isAm) dest.writeInt(1); else dest.writeInt(0);
        dest.writeInt(hour);
        dest.writeInt(minute);
        if(check) dest.writeInt(1); else dest.writeInt(0);
        dest.writeInt(code);
    }
}
