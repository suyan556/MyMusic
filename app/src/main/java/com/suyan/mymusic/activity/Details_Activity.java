package com.suyan.mymusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.util.Utility;

import java.util.HashMap;

import static com.suyan.mymusic.activity.Home_Activity.musicList;

public class Details_Activity extends Activity {

    private TextView detailsMusicTitle, detailsMusicDuration, detailsMusicSize, detailsMusicUrl;
    private MusicDB mMusicDB;
    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        mMusicDB = MusicDB.getInstance(this);

        Bundle bundle = this.getIntent().getExtras();
        final int selectID = bundle.getInt("selectID");

        HashMap<String, String> list = musicList.get(selectID);
        final String musicTitle = list.get("title");
        final String musicDuration = list.get("duration");
        final int musicSize = Utility.byteToM(list.get("size"));
        final String musicUrl = list.get("url");

        mLayout = (LinearLayout) findViewById(R.id.details_activity);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Details_Activity.this, "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
            }
        });

        detailsMusicTitle = (TextView) findViewById(R.id.details_music_title);
        detailsMusicDuration = (TextView) findViewById(R.id.details_music_duration);
        detailsMusicSize = (TextView) findViewById(R.id.details_music_size);
        detailsMusicUrl = (TextView) findViewById(R.id.details_music_url);

        detailsMusicTitle.setText(musicTitle);
        detailsMusicDuration.setText(musicDuration);
        detailsMusicSize.setText(musicSize+"");
        detailsMusicUrl.setText(musicUrl);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
