package com.example.angks.realapp;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class AlarmShow extends Activity implements View.OnClickListener{
    MediaPlayer audio_play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
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
                finish();
                break;
        }
    }
}
