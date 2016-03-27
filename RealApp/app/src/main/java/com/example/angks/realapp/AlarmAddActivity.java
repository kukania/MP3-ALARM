package com.example.angks.realapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class AlarmAddActivity extends Activity implements View.OnClickListener{
    AlarmItem temp;
    EditText hour;
    EditText minute;
    TextView ampm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        temp=new AlarmItem();
        hour=(EditText)findViewById(R.id.hour);
        minute=(EditText)findViewById(R.id.minute);
        ampm=(TextView)findViewById(R.id.AM_PM);
        ampm.setText("AM");
        hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int hourtime=0;
                try{hourtime=Integer.parseInt(hour.getText().toString());}
                catch (Exception e){

                }
                if(hourtime>=0 && hourtime<12){
                    ampm.setText("AM");
                }
                else{
                    ampm.setText("PM");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int hourtime=0;
                try{hourtime=Integer.parseInt(hour.getText().toString());}
                catch (Exception e){}
                if(hourtime>=0 && hourtime<12){
                    ampm.setText("AM");
                }
                else{
                    ampm.setText("PM");
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        ImageView img;
        switch (v.getId()){
            case R.id.img1:
                if(temp.weekday[0]==1){
                    temp.weekday[0]=0;
                    img=(ImageView)findViewById(R.id.img1);
                    img.setImageResource(R.drawable.su);
                }
                else{
                    temp.weekday[0]=1;
                    img=(ImageView)findViewById(R.id.img1);
                    img.setImageResource(R.drawable.su_c);
                }
                break;
            case R.id.img2:

                if(temp.weekday[1]==1){
                    temp.weekday[1]=0;
                    img=(ImageView)findViewById(R.id.img2);
                    img.setImageResource(R.drawable.mo);
                }
                else{
                    temp.weekday[1]=1;
                    img=(ImageView)findViewById(R.id.img2);
                    img.setImageResource(R.drawable.mo_c);
                }
                break;
            case R.id.img3:
                if(temp.weekday[2]==1){
                    temp.weekday[2]=0;
                    img=(ImageView)findViewById(R.id.img3);
                    img.setImageResource(R.drawable.tu);
                }
                else{
                    temp.weekday[2]=1;
                    img=(ImageView)findViewById(R.id.img3);
                    img.setImageResource(R.drawable.tu_c);
                }
                break;
            case R.id.img4:
                if(temp.weekday[3]==1){
                    temp.weekday[3]=0;
                    img=(ImageView)findViewById(R.id.img4);
                    img.setImageResource(R.drawable.we);
                }
                else{
                    temp.weekday[3]=1;
                    img=(ImageView)findViewById(R.id.img4);
                    img.setImageResource(R.drawable.we_c);
                }
                break;
            case R.id.img5:
                if(temp.weekday[4]==1){
                    temp.weekday[4]=0;
                    img=(ImageView)findViewById(R.id.img5);
                    img.setImageResource(R.drawable.th);
                }
                else{
                    temp.weekday[4]=1;
                    img=(ImageView)findViewById(R.id.img5);
                    img.setImageResource(R.drawable.th_c);
                }
                break;
            case R.id.img6:
                if(temp.weekday[5]==1){
                    temp.weekday[5]=0;
                    img=(ImageView)findViewById(R.id.img6);
                    img.setImageResource(R.drawable.fr);
                }
                else{
                    temp.weekday[5]=1;
                    img=(ImageView)findViewById(R.id.img6);
                    img.setImageResource(R.drawable.fr_c);
                }
                break;
            case R.id.img7:
                if(temp.weekday[6]==1){
                    temp.weekday[6]=0;
                    img=(ImageView)findViewById(R.id.img7);
                    img.setImageResource(R.drawable.sa);
                }
                else{
                    temp.weekday[6]=1;
                    img=(ImageView)findViewById(R.id.img7);
                    img.setImageResource(R.drawable.sa_c);
                }
                break;
            case R.id.cancelBtn:
            case R.id.backBtn:
                finish();
                break;
            case R.id.checkBtn:
                temp.hour=0;
                temp.minute=0;
                try{
                    temp.hour=Integer.parseInt(hour.getText().toString());
                    temp.minute=Integer.parseInt(minute.getText().toString());
                }
                catch (Exception e){}
                if(temp.hour>=0 && temp.hour<12){
                    temp.isAm=true;
                }
                else{
                    temp.isAm=false;
                }

                Intent intent=new Intent();
                intent.putExtra("add",temp);
                setResult(1, intent);
                finish();
                break;
        }
    }
}
