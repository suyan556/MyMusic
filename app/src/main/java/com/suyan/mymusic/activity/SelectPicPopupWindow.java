package com.suyan.mymusic.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.suyan.mymusic.activity.Home_Activity.musicList;

public class SelectPicPopupWindow extends Activity {

    private SimpleAdapter mSimpleAdapter;

    private ListView alertListView;

    private LinearLayout mLayout;

    private TextView dialogMusicName;
    private MusicDB mMusicDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mMusicDB = MusicDB.getInstance(SelectPicPopupWindow.this);

        Bundle bundle = this.getIntent().getExtras();
        final int selectID = bundle.getInt("selectID");

        HashMap<String, String> list = musicList.get(selectID);
        final String musictitle = list.get("title");
        final String musicId = list.get("musicId");

        dialogMusicName = (TextView) findViewById(R.id.dialog_music_name);
        dialogMusicName.setText(musictitle);

        mLayout = (LinearLayout) findViewById(R.id.pop_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectPicPopupWindow.this, "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
            }
        });

        //设置ListView的标题集合
        final String[] title = new String[]{"删除", "查看歌曲信息"};

        //设置ListView的图片资源
        int[] imageId = new int[]{R.drawable.ss, R.drawable.su};

        final List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < title.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", title[i]);
            map.put("image", imageId[i]);
            lists.add(map);
        }

        mSimpleAdapter = new SimpleAdapter(SelectPicPopupWindow.this, lists, R.layout.dialog_tiem, new String[]{"title", "image"}, new int[]{R.id.alert_title, R.id.alert_image});

        alertListView = (ListView) findViewById(R.id.dialog_list_view);
        alertListView.setAdapter(mSimpleAdapter);
        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> contentLists = lists.get(position);
                String contentTitle = (String) contentLists.get("title");
                switch (contentTitle){
                    case "删除":
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SelectPicPopupWindow.this);
                        builder.setMessage("确定将所选音乐从本地列表中移除？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMusicDB.deleteMusic(musicId);
                                finish();
                                Toast.makeText(SelectPicPopupWindow.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.create();
                            }
                        });
                        builder.show();
                        break;
                    case "查看歌曲信息":
                        Intent intent = new Intent(SelectPicPopupWindow.this, Details_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("selectID", selectID);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                }


            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
