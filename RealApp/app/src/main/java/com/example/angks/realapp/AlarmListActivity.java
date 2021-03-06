package com.example.angks.realapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmListActivity extends Activity implements View.OnClickListener {
    ListView listView;
    ArrayList<AlarmItem> items;
    Context myContetxt;
    boolean flag = false;
    AlarmItem newItem;
    boolean isNew = false;

    public class myAdapter extends BaseAdapter {
        public class ViewHolder {
            TextView AMPM;
            TextView Time;
            ImageView[] weekDay = new ImageView[7];
            ImageView check;
        }

        Context mContext = myContetxt;
        ArrayList<AlarmItem> list;

        public myAdapter(Context m, ArrayList<AlarmItem> input) {
            myContetxt = m;
            list = input;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.alarm_item, null);
                holder.AMPM = (TextView) convertView.findViewById(R.id.AM_PM);
                holder.Time = (TextView) convertView.findViewById(R.id.time);
                holder.weekDay[0] = (ImageView) convertView.findViewById(R.id.img1);
                holder.weekDay[1] = (ImageView) convertView.findViewById(R.id.img2);
                holder.weekDay[2] = (ImageView) convertView.findViewById(R.id.img3);
                holder.weekDay[3] = (ImageView) convertView.findViewById(R.id.img4);
                holder.weekDay[4] = (ImageView) convertView.findViewById(R.id.img5);
                holder.weekDay[5] = (ImageView) convertView.findViewById(R.id.img6);
                holder.weekDay[6] = (ImageView) convertView.findViewById(R.id.img7);
                holder.check = (ImageView) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AlarmItem temp = list.get(position);
            if (temp.isAm) holder.AMPM.setText("AM");
            else holder.AMPM.setText("PM");
            String s = "";
            if (temp.hour < 10) s += "0" + temp.hour;
            else s += temp.hour;
            s += ":";
            if (temp.minute < 10) s += "0" + temp.minute;
            else s += temp.minute;
            holder.Time.setText(s);
            if (temp.weekday[0] == 0) holder.weekDay[0].setImageResource(R.drawable.su);
            else holder.weekDay[0].setImageResource(R.drawable.su_c);
            if (temp.weekday[1] == 0) holder.weekDay[1].setImageResource(R.drawable.mo);
            else holder.weekDay[1].setImageResource(R.drawable.mo_c);
            if (temp.weekday[2] == 0) holder.weekDay[2].setImageResource(R.drawable.tu);
            else holder.weekDay[2].setImageResource(R.drawable.tu_c);
            if (temp.weekday[3] == 0) holder.weekDay[3].setImageResource(R.drawable.we);
            else holder.weekDay[3].setImageResource(R.drawable.we_c);
            if (temp.weekday[4] == 0) holder.weekDay[4].setImageResource(R.drawable.th);
            else holder.weekDay[4].setImageResource(R.drawable.th_c);
            if (temp.weekday[5] == 0) holder.weekDay[5].setImageResource(R.drawable.fr);
            else holder.weekDay[5].setImageResource(R.drawable.fr_c);
            if (temp.weekday[6] == 0) holder.weekDay[6].setImageResource(R.drawable.sa);
            else holder.weekDay[6].setImageResource(R.drawable.sa_c);

            if (temp.check) holder.check.setImageResource(R.drawable.do_alarm);
            else holder.check.setImageResource(R.drawable.no_alarm);

            holder.check.setOnClickListener(new View.OnClickListener() {
                int pos = position;
                boolean check = false;
                @Override
                public void onClick(View v) {
                    if (check) {
                        check = false;
                        list.get(pos).check = false;
                        holder.check.setImageResource(R.drawable.no_alarm);
                        releaseAlarm(list.get(pos));
                        writeAlarm();
                    } else {
                        check = true;
                        list.get(pos).check = true;
                        holder.check.setImageResource(R.drawable.do_alarm);
                        setAlarm(list.get(pos));
                        writeAlarm();
                    }
                    writeAlarm();
                }
            });
            return convertView;
        }

        public void addItem(AlarmItem input) {
            list.add(input);
        }

        public void remove(int pos) {
            releaseAlarm(list.get(pos));
            list.remove(pos);
            dataChange();
            writeAlarm();
        }

        public void dataChange() {
            this.notifyDataSetChanged();
        }
    }

    ;
    myAdapter adapter;
    int getSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        init();
        readAlarm();
        flag = true;
    }

    public void releaseAlarm(AlarmItem input) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmWakeUpReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, input.code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
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
        intent.putExtra("code",input.code+"");
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
            items.add(tempAlarm);
        }
    }

    public void writeAlarm() {
        SharedPreferences pref = getSharedPreferences("alarmList", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("size", items.size());
        String s;
        Gson gson = new Gson();
        for (int i = 0; i < items.size(); i++) {
            s = gson.toJson(items.get(i));
            editor.putString("alarm" + i, s);
        }
        editor.commit();
    }

    public void init() {
        myContetxt = this;
        items = new ArrayList<AlarmItem>();
        AlarmItem temp = new AlarmItem();
        listView = (ListView) findViewById(R.id.list_item);
        adapter = new myAdapter(myContetxt, items);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alt = new AlertDialog.Builder(myContetxt);
                alt.setMessage("삭제 하시겠습니까?").setCancelable(false).setPositiveButton("넹", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(position);
                    }
                }).setNegativeButton("아뇨", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog altt = alt.create();
                altt.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        Log.d("str", "result");
        if (result == 1) {
            Bundle b = data.getExtras();
            AlarmItem a = b.getParcelable("add");
            adapter.addItem(a);
            adapter.notifyDataSetChanged();
            writeAlarm();
            isNew = true;
            getSize++;
            setAlarm(a);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.addBtn:
                intent = new Intent(this, AlarmAddActivity.class);
                intent.putExtra("size", getSize);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
