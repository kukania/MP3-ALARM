package com.example.angks.realapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmWakeUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar d=Calendar.getInstance();
        Bundle b=intent.getExtras();
        int code=b.getInt("code");
        Log.d("intent",""+code);
        Intent intent1=new Intent();
        intent1.setClassName("com.example.angks.realapp","com.example.angks.realapp.AlarmShow");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
