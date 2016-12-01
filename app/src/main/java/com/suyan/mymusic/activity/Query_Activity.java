package com.suyan.mymusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.model.Music;
import com.suyan.mymusic.util.MyAdapter;
import com.suyan.mymusic.util.MyApp;
import com.suyan.mymusic.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.suyan.mymusic.activity.Home_Activity.img_pause;
import static com.suyan.mymusic.activity.Home_Activity.musicList;

public class Query_Activity extends Activity {

    private ListView queryListView;//显示音乐列表的ListVew
    private EditText queryEditText;//查询输入文本框

    private MusicDB mMusicDB;//数据库操作对象
    private SimpleAdapter mAdapter;//ListView适配器

    private MyAdapter mMyAdapter;

    private MyApp mMyApp;


    public static List<HashMap<String, String >> queryMusicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.query_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mMyApp = new MyApp();

        mMusicDB = MusicDB.getInstance(this);//获取数据库处理对象

        Button query_back = (Button) findViewById(R.id.query_back);
        query_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        queryListView = (ListView) findViewById(R.id.query_listView);
        queryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int homePosition = 0;
                HashMap<String, String> list =queryMusicList.get(position);
                final String title = list.get("title");
                Log.d(TAG, "选择了歌曲："+title);
                String musicId = list.get("musicId");
                Log.d(TAG, "选择歌曲ID为："+musicId);
                Log.d(TAG, "总歌曲数目："+ musicList.size());
                int size = musicList.size();
                for (int i = 0; i < size; i++){
                    HashMap<String, String> homeMusicList = musicList.get(i);
                    String homeMusicId = homeMusicList.get("musicId");
                    Log.d(TAG, "循环的歌曲ID为："+homeMusicId);
                    if (musicId.equals(homeMusicId)){
                        homePosition = i;
                        Log.d(TAG, "选择第"+homePosition+"行");
                        break;
                    }
                }
                mMyApp.setMusicPosition(homePosition);
                Home_Activity.binder.doPlay(img_pause);

            }
        });


        queryEditText = (EditText) findViewById(R.id.query_editText);
        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //当文本框里的内容发生变化时候调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                Log.d(TAG, "搜索框发生了变化："+text);
                queryMusicList = QueryLoadMusicList(text);

                mMyAdapter = new MyAdapter(Query_Activity.this, queryMusicList);

                //mAdapter = new SimpleAdapter(getApplicationContext(), queryMusicList, R.layout.list_item, new String[]{"title", "artist", "duration"}, new int[]{R.id.listview_music_title, R.id.listview_music_artist});
                queryListView.setAdapter(mMyAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //获取查询返回的信息
    public List<HashMap<String, String >> QueryLoadMusicList(String name){
        List<Music> musics = mMusicDB.loadMusicByName(name);
        List<HashMap<String, String >> musicList =  new ArrayList<HashMap<String, String>>();
        for(Iterator<Music> iterator = musics.iterator(); iterator.hasNext();){
            Music music = iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", music.getLrcTitle());
            map.put("album", music.getAlbum());
            map.put("duration", Utility.Format(music.getDuration()));
            map.put("size", String.valueOf(music.getSize()));
            map.put("url", music.getUrl());
            map.put("musicId", String.valueOf(music.getMusicId()));
            map.put("artist", music.getArtist());
            musicList.add(map);
        }
        return musicList;
    }

}
