package com.example.angks.realapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

public class AlarmShow extends Activity implements View.OnClickListener{
    MediaPlayer audio_play;
    int getSize;
    AlarmItem item;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
        Intent intent=getIntent();
        code=intent.getIntExtra("code",0);
        readAlarm();

        Log.d("AlarmShow", item.code + "");
        if(item.check)
            setAlarm(item);
        try {
            AssetFileDescriptor afd = getAssets().openFd("audio/signus.mp3");
            audio_play = new MediaPlayer();
            audio_play.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            audio_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            audio_play.prepareAsync();
            Log.d("test", "test");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test", "test");
        }

    }
    public void setAlarm(AlarmItem input) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmWakeUpReceiver.class);
        intent.putExtra("code",input.code);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, input.code, intent, Intent.FILL_IN_DATA);
        //PendingIntent pIntent=PendingIntent.getBroadcast(this, input.code, intent, 0);
        Calendar c = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        int cnt = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.set(Calendar.HOUR_OF_DAY, input.hour);
        c.set(Calendar.MINUTE, input.minute);
        c.set(Calendar.SECOND, 0);
        intent.putExtra("code", input.code + "");
        for (int i = 0; i < 7; i++) {
            if (input.weekday[(cnt + i) % 7] == 1) {
                c.add(Calendar.DATE, i);
                if (!(c.getTimeInMillis() < b.getTimeInMillis())) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pIntent);
                    Log.d("1", c.getTime().toString());
                } else {
                    c.add(Calendar.DATE, 7);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pIntent);
                    Log.d("1", c.getTime().toString());
                    c.add(Calendar.DATE, (-1) * 7);
                }
                break;
            }
        }
    }
    public void readAlarm() {
        SharedPreferences pref = getSharedPreferences("alarmList", MODE_PRIVATE);
        getSize = pref.getInt("size", 0);
        Gson gson = new Gson();
        String s;
        for (int i = 0; i < getSize; i++) {
            s = pref.getString("alarm" + i, "");
            AlarmItem tempAlarm = gson.fromJson(s, AlarmItem.class);
            if(tempAlarm.code==code)
                item=tempAlarm;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checking:
                if (audio_play!=null) {
                    if (audio_play.isPlaying()) {
                        audio_play.stop();
                    }
                    audio_play.release();
                    audio_play=null;
                }
                setAlarm(item);
                finish();
                break;
        }
    }
}
