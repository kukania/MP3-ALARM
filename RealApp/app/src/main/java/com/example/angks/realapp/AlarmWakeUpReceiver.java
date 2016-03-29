package com.example.angks.realapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmWakeUpReceiver extends BroadcastReceiver {
    int code;
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar d=Calendar.getInstance();
        code=intent.getIntExtra("code",0);
        Log.d("code",""+code);
        Intent intent1=new Intent();
        intent1.putExtra("code",code);
        intent1.setClassName("com.example.angks.realapp","com.example.angks.realapp.AlarmShow");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
