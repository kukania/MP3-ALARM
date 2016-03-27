package com.example.angks.realapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MediaListAcitivity extends Activity {
    ListView lv;
    ArrayList<MusicWrapper>items;
    Context myContetxt=this;
    ImageView albermCover;
    TextView playingTitle;
    TextView playingArtist;
    boolean flag=false;
    int mPosition=-1;
    final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
    class ViewHolder{
        public LinearLayout parent;
        public TextView title;
        public TextView artist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list_acitivity);
        init();
        showList();
    }

    public void init(){
        albermCover=(ImageView)findViewById(R.id.albermCover);
        playingTitle=(TextView)findViewById(R.id.playTitle);
        playingArtist=(TextView)findViewById(R.id.playArtist);
        ImageButton backBtn=(ImageButton)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        items=new ArrayList<MusicWrapper>();

        for(int i = 0; i<mySongs.size(); i++){
            String []temp = (mySongs.get(i).getName().toString()).split("-");
            MusicWrapper tempW=new MusicWrapper();
            tempW.artist=temp[0];
            for(int j=1; j<temp.length; j++){
                if(temp[j]==null)
                    continue;
                tempW.title+=temp[j];
            }
            items.add(tempW);
        }
    }
    public void showList(){
        lv=(ListView)findViewById(R.id.listView);
        final BaseAdapter adapter=new BaseAdapter() {
            Context mContext=myContetxt;
            ArrayList<MusicWrapper> list=items;
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
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if(convertView==null){
                    holder=new ViewHolder();
                    LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=inflater.inflate(R.layout.one_list,null);
                    holder.parent=(LinearLayout)convertView;
                    holder.title=(TextView)convertView.findViewById(R.id.title);
                    holder.artist=(TextView)convertView.findViewById(R.id.artist);
                    convertView.setTag(holder);
                }
                else{
                    holder=(ViewHolder)convertView.getTag();
                }
                if(mPosition==position)
                    holder.parent.setBackgroundColor(Color.parseColor("#9d7087"));
                else
                    holder.parent.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.title.setText(list.get(position).title);
                holder.artist.setText(list.get(position).artist);
                return convertView;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playingTitle.setText(items.get(position).title);
                playingArtist.setText(items.get(position).artist);
                Intent intent=new Intent(getApplicationContext(),playActivity.class);
                mPosition=position;
                adapter.notifyDataSetChanged();
                intent.putExtra("pos",position);
                intent.putExtra("songList",mySongs);
                intent.putExtra("title",items.get(position).title);
                intent.putExtra("artist",items.get(position).artist);
                intent.putExtra("MusicWrapper",items);
                if(flag){
                    SharedPreferences pref=getSharedPreferences("MUSIC",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putInt("pos",position);
                    editor.putString("title",items.get(position).title);
                    editor.putString("artist",items.get(position).artist);
                    editor.commit();
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
                else{
                    flag=true;
                }
                startActivity(intent);
            }
        });
    }
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }
    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences pref=getSharedPreferences("text",MODE_PRIVATE);
        String tt=pref.getString("title", "");
        String att=pref.getString("artist","");
        playingTitle.setText(tt);
        playingArtist.setText(att);
    }
}
