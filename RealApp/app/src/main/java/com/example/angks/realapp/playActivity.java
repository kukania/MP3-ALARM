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
import android.view.KeyEvent;
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
    boolean isNew = true;
    Uri u;
    boolean flag = true;

    public synchronized MediaPlayer getMp(int n) {
        if (n == 0)
            mp = null;
        return mp;
    }

    public class BackGround extends AsyncTask<Void, Integer, Integer> {
        int totalDuration = 0;
        int currentPosition = 0;

        @Override
        protected Integer doInBackground(Void... params) {
            if (getMp(-1) != null) {
                totalDuration = getMp(-1).getDuration();
            }
            while (currentPosition < totalDuration && flag) {
                if (getMp(-1) != null) {
                    try {
                        if (getMp(-1).isPlaying()) {
                            totalDuration = getMp(-1).getDuration();
                            currentPosition = getMp(-1).getCurrentPosition();
                        }
                    } catch (IllegalStateException e) {
                        break;
                    }
                }
                publishProgress(currentPosition, totalDuration);
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... input) {
            try {
                if (getMp(-1).isPlaying())
                    seekBar.setProgress(input[0]);
            } catch (Exception e) {
                seekBar.setProgress(input[0]);
            }
        }
    }

    ;
    BackGround bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        visualInit();

        if (getMp(-1) != null) {
            if (getMp(-1).isPlaying()) {
                getMp(-1).stop();
            }
            getMp(-1).release();
        }
        musicSetting(-1);
        bg = new BackGround();
        bg.execute();
        getMp(-1).start();
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
                Log.d("test", sb.getProgress() + "/" + sb.getMax());
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

        SharedPreferences pref = getSharedPreferences("text", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("title", item.get(position).title);
        editor.putString("artist", item.get(position).artist);
        editor.commit();

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (bg.getStatus() == AsyncTask.Status.RUNNING)
                    bg.cancel(true);

                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    position = (position + 1) % mySongs.size();
                    musicSetting(position);
                    getMp(-1).start();
                    bg = new BackGround();
                    bg.execute();
                }
            }
        });
        seekBar.setProgress(0);
        seekBar.setMax(mp.getDuration());
        flag = true;
    }

    @Override
    public void onActivityResult(int request, int response, Intent intent) {
        if (bg.getStatus() == AsyncTask.Status.RUNNING)
            bg.cancel(true);

        if (getMp(-1) != null) {
            if (getMp(-1).isPlaying()) {
                getMp(-1).stop();
            }
            getMp(-1).release();
            getMp(0);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.listBtn:
                intent = new Intent(this, MediaListAcitivity.class);
                intent.putExtra("songList", mySongs);
                startActivityForResult(intent, 0);
                break;
            case R.id.leftBtn:
                if (!flag)
                    break;
                else {
                    flag = false;
                }
                if (bg.getStatus() == AsyncTask.Status.RUNNING)
                    bg.cancel(true);

                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    musicSetting(position);
                    position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                    getMp(-1).start();
                    bg = new BackGround();
                    bg.execute();
                }
                flag = true;
                break;
            case R.id.playBtn:
                if (getMp(-1).isPlaying()) {
                    playBtn.setImageResource(R.drawable.pause);
                    getMp(-1).pause();
                } else {
                    playBtn.setImageResource(R.drawable.start);
                    getMp(-1).start();
                }
                break;
            case R.id.rightBtn:
                if (!flag)
                    break;
                else
                    flag = false;
                if (bg.getStatus() == AsyncTask.Status.RUNNING)
                    bg.cancel(true);
                if (getMp(-1) != null) {
                    if (getMp(-1).isPlaying()) {
                        getMp(-1).stop();
                    }
                    getMp(-1).release();
                    getMp(0);
                    position = (position + 1) % mySongs.size();
                    musicSetting(position);
                    getMp(-1).start();
                    bg = new BackGround();
                    bg.execute();
                }
                flag = true;
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                intent = new Intent(this, MediaListAcitivity.class);
                intent.putExtra("songList", mySongs);
                startActivityForResult(intent, 0);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

        @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, MediaListAcitivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
