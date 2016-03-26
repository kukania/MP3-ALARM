package com.example.angks.realapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playActivity extends Activity implements View.OnClickListener {
    static MediaPlayer mp;
    TextView title;
    TextView artist;
    SeekBar seekBar;
    ArrayList<File> mySongs;
    ArrayList<MusicWrapper> item;
    int position;
    ImageView playBtn;
    Uri u;
    boolean flag = true;
    public synchronized MediaPlayer getMp(int n){
        if(n==0)
            mp=null;
        return mp;
    }
    AsyncTask<Void, Integer, Integer> bg = new AsyncTask<Void, Integer, Integer>() {
        int totalDuration = 0;
        int currentPosition = 0;

        @Override
        protected Integer doInBackground(Void... params) {
            while (true) {
                if (getMp(-1) != null) {
                    totalDuration = getMp(-1).getDuration();
                }
                while (currentPosition <= totalDuration && flag) {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getMp(-1) != null)
                        if (getMp(-1).isPlaying()) {
                            totalDuration = getMp(-1).getDuration();
                            currentPosition = getMp(-1).getCurrentPosition();
                        }
                    publishProgress(currentPosition, totalDuration);
                }
            }

        }

        @Override
        protected void onProgressUpdate(Integer... input) {
            if (getMp(-1).isPlaying())
                seekBar.setProgress(input[0]);
            if (input[0] > input[1] - 1000) {
                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    position = (position + 1) % mySongs.size();
                    musicSetting(position);
                    totalDuration = getMp(-1).getDuration();
                    getMp(-1).start();
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        visualInit();
        musicSetting(-1);
        bg.execute();
        getMp(-1).start();
        getMp(-1).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mpl) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int temp = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                if (seekBar.getProgress() > seekBar.getMax() - 2000)
                    getMp(-1).seekTo(seekBar.getProgress() - 2000);
                else
                    getMp(-1).seekTo(seekBar.getProgress());
                Log.d("test",sb.getProgress()+"/"+sb.getMax());
            }
        });
    }

    public void visualInit() {
        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.playTitle);
        artist = (TextView) findViewById(R.id.playArtist);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playBtn = (ImageView) findViewById(R.id.playBtn);

        Bundle b = intent.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songList");
        item = (ArrayList) b.getParcelableArrayList("MusicWrapper");
        position = b.getInt("pos", 0);
    }

    public void musicSetting(int inputP) {
        flag = false;
        if (inputP != -1) {
            position = inputP;
        }
        title.setText(item.get(position).title);
        artist.setText(item.get(position).artist);
        SharedPreferences pref=getSharedPreferences("text",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        editor.putString("title",item.get(position).title);
        editor.putString("artist",item.get(position).artist);
        editor.commit();
        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        seekBar.setProgress(0);
        seekBar.setMax(mp.getDuration());
        flag = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listBtn:
                finish();
                break;
            case R.id.leftBtn:
                if (!flag)
                    break;
                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    musicSetting(position);
                    position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                    getMp(-1).start();
                }

                break;
            case R.id.playBtn:
                if (getMp(-1).isPlaying()) {
                    //일시정지 이미지 붙이기
                    playBtn.setImageResource(R.drawable.play_btn);
                    getMp(-1).pause();
                } else {
                    playBtn.setImageResource(R.drawable.play_btn);
                    getMp(-1).start();
                }
                break;
            case R.id.rightBtn:
                if (!flag)
                    break;
                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    position = (position + 1) % mySongs.size();
                    musicSetting(position);
                    getMp(-1).start();
                }
                break;
        }
    }
}
