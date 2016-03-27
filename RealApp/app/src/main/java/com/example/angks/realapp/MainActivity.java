package com.example.angks.realapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.MediaPlayerBtn:
                intent=new Intent(MainActivity.this,MediaListAcitivity.class);
                startActivity(intent);
                break;
            case R.id.AlarmBtn:
                intent=new Intent(MainActivity.this,AlarmListActivity.class);
                startActivity(intent);
                break;
            case R.id.LightBtn:
                break;
        }
    }
}
