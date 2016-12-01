package com.suyan.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.model.Music;
import com.suyan.mymusic.util.MyApp;
import com.suyan.mymusic.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;

import static android.content.ContentValues.TAG;

public class SDCardFileExplorer_Activity extends Activity {

    private TextView cdPath;
    private ListView seekListView;
    private Button btnParent, btnSeek, btnQuery_back;

    private MusicDB mMusicDB;

    private MyApp mApp = null;
    private Home_Activity.MyHandler mHandler = null;


    File currentParent;//记录当前的父文件夹

    File[] currentFiles;//记录当前路径下的所以文件夹的文件数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sdcard_file_explorer_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mMusicDB = MusicDB.getInstance(this);

        mApp = (MyApp) getApplication();
        mHandler = mApp.getHandler();

        cdPath = (TextView) findViewById(R.id.sdcard_path);
        seekListView = (ListView) findViewById(R.id.custom_seek_listview);

        btnQuery_back = (Button) findViewById(R.id.query_back);
        btnQuery_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        File root = new File("/mnt/sdcard/");//获取系统的SDCard目录
        if (root.exists()) {
            //如果SD卡存在的话
            currentParent = root;
            currentFiles = root.listFiles();

            inflateListView(currentFiles);
        }
        //设置条目点击进入下一层目录事件
        seekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取用户点击的文件夹下的所以文件
                File[] tem = currentFiles[position].listFiles();
                if (tem == null || tem.length == 0){
                    Toast.makeText(getApplicationContext(), "当前路径不可访问或者该路径下没有文件", Toast.LENGTH_SHORT).show();
                }else {
                    currentParent = currentFiles[position];//获取用户点击的条目项对应的文件夹，并设置为父文件夹
                    currentFiles = tem;
                    inflateListView(currentFiles);
                }
            }
        });

        //设置点击返回上一层文件夹
        btnParent = (Button) findViewById(R.id.custom_seek_Parent);
        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!currentParent.getCanonicalPath().equals("/mnt/sdcard")){
                        //获取到上一级的目录
                        currentParent = currentParent.getParentFile();
                        //列出当前目录下的所以文件夹
                        currentFiles = currentParent.listFiles();
                        //再次更新ListView
                        inflateListView(currentFiles);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //搜索目录
        btnSeek = (Button) findViewById(R.id.sdcard_button_seek);
        btnSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            //动态请求权限
                            PermissionManager.with(SDCardFileExplorer_Activity.this)
                                    .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                                    .askagain(true)
                                    .askagainCallback(new AskagainCallback() {
                                        @Override
                                        public void showRequestPermission(UserResponse response) {
                                            Toast.makeText(SDCardFileExplorer_Activity.this, "请求权限", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .callback(new FullCallback() {
                                        @Override
                                        public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Utility.showProgressDialog(SDCardFileExplorer_Activity.this);
                                                }
                                            });
                                            mHandler.sendEmptyMessage(9);
                                            final String path = cdPath.getText().toString();
                                            Log.d(TAG, "选择的路径是:"+path);
                                            List<Music> musics =  Utility.getMusics(getApplicationContext());
                                            int count = 0;
                                            for (Music music : musics){
                                                String musicPath = music.getUrl();
                                                int ps = musicPath.lastIndexOf("/");
                                                String myMusicPath = musicPath.substring(0,ps);
                                                Log.d(TAG, "截取后的音乐路径:" + myMusicPath);
                                                if (path.equals(myMusicPath)){
                                                    Log.d(TAG, "匹配成功：");
                                                    mMusicDB.saveMusic(music);
                                                    Log.d(TAG, String.valueOf(music));
                                                    count++;
                                                }
                                            }
                                            Log.d(TAG, "共搜索到"+count+"条音乐");
                                            Log.d(TAG, "初始化ListView");
                                            mHandler.sendEmptyMessage(1);
                                            //mHandler.sendEmptyMessage(11);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Utility.closeProgressDialog();
                                                }
                                            });
                                            Intent intent = new Intent(SDCardFileExplorer_Activity.this, Seek_Ok_Activity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("count", count);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }).ask();
                    }
                }).start();
            }
        });

    }

    /**
     * 根据文件夹填充ListView
     *
     * @param files
     */
    private void inflateListView(File[] files) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {

                Map<String, Object> listItem = new HashMap<String, Object>();

            if (files[i].isDirectory()) {
                //如果是文件夹就显示的图片为文件夹的图片
                listItem.put("icon", R.drawable.aer);
            } else {
                listItem.put("icon", R.drawable.settings);
            }
                //添加一个文件的名称
                listItem.put("filename", files[i].getName());
                File myFile = new File(files[i].getName());
                listItems.add(listItem);

            SimpleAdapter mAdapter = new SimpleAdapter(SDCardFileExplorer_Activity.this, listItems, R.layout.sdcard_tiem, new String[]{"filename", "icon"},
                    new int[]{R.id.sdcard_name, R.id.sdcard_image});
            seekListView.setAdapter(mAdapter);

            try {
                cdPath.setText(currentParent.getCanonicalPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
