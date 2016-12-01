package com.suyan.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.model.Music;
import com.suyan.mymusic.util.Utility;

import java.util.ArrayList;
import java.util.List;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;

import static android.content.ContentValues.TAG;

public class Seek_Activity extends Activity implements View.OnClickListener {

    private Button seekBack;
    private TextView allSearch, customSearch;

    public MusicDB mMusicDB;//数据库操作对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mMusicDB = MusicDB.getInstance(Seek_Activity.this);

        seekBack = (Button) findViewById(R.id.seek_back);
        allSearch = (TextView) findViewById(R.id.allSearch_textView);
        customSearch = (TextView) findViewById(R.id.customSearch_textView);

        seekBack.setOnClickListener(this);
        allSearch.setOnClickListener(this);
        customSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seek_back:
                finish();
                break;
            //全盘扫描
            case R.id.allSearch_textView:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PermissionManager.with(Seek_Activity.this)
                                .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                                .askagain(true)
                                .askagainCallback(new AskagainCallback() {
                                    @Override
                                    public void showRequestPermission(UserResponse response) {
                                        Toast.makeText(Seek_Activity.this, "请求权限", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .callback(new FullCallback() {
                                    @Override
                                    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utility.showProgressDialog(Seek_Activity.this);
                                            }
                                        });
                                        int musicCount = 0;
                                        List<Music> musics =  Utility.getMusics(getApplicationContext());
                                        for (Music music : musics){
                                            mMusicDB.saveMusic(music);
                                            Log.d(TAG, String.valueOf(music));
                                            musicCount++;
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utility.closeProgressDialog();
                                            }
                                        });
                                        Intent intent = new Intent(Seek_Activity.this, Seek_Ok_Activity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("count", musicCount);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }).ask();
                    }
                }).start();
                break;
            //自定义扫描
            case  R.id.customSearch_textView:
                Intent customSeekIntent = new Intent(Seek_Activity.this, SDCardFileExplorer_Activity.class);
                startActivity(customSeekIntent);
                break;
        }
    }
}
