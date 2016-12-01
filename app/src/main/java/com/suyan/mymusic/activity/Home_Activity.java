package com.suyan.mymusic.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.suyan.mymusic.R;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.impl.IMusic;
import com.suyan.mymusic.server.MusicService;
import com.suyan.mymusic.util.MyAdapter;
import com.suyan.mymusic.util.MyApp;
import com.suyan.mymusic.util.ShowMusicHandler;
import com.suyan.mymusic.util.Utility;

import java.util.HashMap;
import java.util.List;

import rebus.permissionutils.PermissionManager;

import static com.suyan.mymusic.server.MusicService.mMediaPlayer;

public class Home_Activity extends Activity implements View.OnClickListener {

    //private List<Map<String, String>> musicList = new ArrayList<Map<String, String>>();
    //private List<Music> musicList;
    public static List<HashMap<String, String >> musicList;

    private static final String TAG = "Home_Activity";
    public static   MusicDB mMusicDB;//数据库操作对象
    public static ListView mListView;//音乐ListView
    private Button mButton;
    private Button query_btn;
    public static IMusic binder;

    public static TextView showTitle;//显示的歌曲名
    public static TextView showArtist;//显示的歌手名

    //public static SeekBar mSeekBar;//进度条

    private ServiceConnection connection;

    private ImageView img_up;//上一首
    public static ImageView img_pause;//播放暂停
    private ImageView img_next;//下一首

    private MyHandler mHandler = null;

    private MyApp mApp = null;

    private MyAdapter myAdapter;
    private LinearLayout mLayoutPlay;

    public static DelayThread delaythread;

    public ShowMusicHandler showMusicHandler;

    public SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mMusicDB = MusicDB.getInstance(this);//获取数据库处理对象

        mApp = (MyApp) getApplication();
        mHandler = new MyHandler();

        showMusicHandler = new ShowMusicHandler();
        mApp.setShowMusicHandler(showMusicHandler);

        final Intent intentService = new Intent(this, MusicService.class);
        startService(intentService);

        mSharedPreferences = getSharedPreferences("musicInfo", Activity.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        query_btn = (Button) findViewById(R.id.query_btn);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了搜索按钮");
                Intent intentQuery = new Intent(Home_Activity.this, Query_Activity.class);
                startActivity(intentQuery);
            }
        });

        showTitle = (TextView) findViewById(R.id.music_show_name);
        showArtist = (TextView) findViewById(R.id.music_show_artist);

        img_pause = (ImageView) findViewById(R.id.img_pause);
        img_next = (ImageView) findViewById(R.id.img_next);
        img_up = (ImageView) findViewById(R.id.img_up);
        mLayoutPlay = (LinearLayout) findViewById(R.id.home_play);

        img_pause.setOnClickListener(this);
        img_next.setOnClickListener(this);
        img_up.setOnClickListener(this);
        mLayoutPlay.setOnClickListener(this);


        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                binder = (IMusic) service;
                delaythread=new DelayThread(500);
                delaythread.start();
                mApp.setHandler(mHandler);
                Log.d(TAG, "调用了内部类DelayThread");

                /**
                 * 判断是不是第一次安装
                 */
                if (mSharedPreferences.getBoolean("sp", false) == true){
                    //如果不是则读取里面的内容
                    Log.d(TAG, "上一次播放的音乐位置是：" + mSharedPreferences.getInt("ps", 0));
                    Log.d(TAG, "上一次播放的音乐的进度条是：" + mSharedPreferences.getInt("musicDuration", 0));
                    int ps = mSharedPreferences.getInt("ps", 0);
                    int md = mSharedPreferences.getInt("musicDuration", 0);
                    binder.doDefaultPlay(ps, md);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");

            }
        };
        bindService(intentService, connection, Context.BIND_ABOVE_CLIENT);

        mListView = (ListView) findViewById(R.id.music_listview);
        MyAdapter();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mApp.setMusicPosition(position);
                binder.doPlay(img_pause);
            }
        });

        mButton = (Button) findViewById(R.id.bt);
        mButton.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMediaPlayer != null) {
            showTitle.setText(mApp.getMusicTitle());
            showArtist.setText(mApp.getMusicArtist());
            if (mMediaPlayer.isPlaying()) {
                img_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play_pressed));
                Utility.toMusicPosition();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMediaPlayer != null) {
            showTitle.setText(mApp.getMusicTitle());
            showArtist.setText(mApp.getMusicArtist());
            if (mMediaPlayer.isPlaying()) {
                img_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play_pressed));

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt:
                showPopupMenu(mButton);
                break;
            case R.id.img_up:
                if (mMediaPlayer != null){
                    int upPosition = mApp.getMusicPosition() - 1;
                    int size = musicList.size();
                    Log.d(TAG, "size:"+size);
                    if (mApp.getMusicPosition() == 0){
                        upPosition = size-1;
                    }
                    mApp.setMusicPosition(upPosition);
                    binder.doPlay(img_pause);

                }
                break;
            case R.id.img_pause:
                if (mMediaPlayer.isPlaying()){
                    binder.doPause();
                    img_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_pause_pressed));
                } else {
                    binder.duContinuePlay();
                    img_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play_pressed));
                }
                break;
            case R.id.img_next:
                if (mMediaPlayer != null){
                    int nextPosition = mApp.getMusicPosition()+1;
                    int size = musicList.size();
                    Log.d(TAG, "size:"+size);
                    if (mApp.getMusicPosition() == size-1){
                        nextPosition = 0;
                    }
                    mApp.setMusicPosition(nextPosition);
                    binder.doPlay(img_pause);
                }
                break;
            case R.id.home_play:
                Intent toShowMusicIntent = new Intent(this, ShowMusic_Activity.class);
                startActivity(toShowMusicIntent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            String mFilePath  = Uri.decode(data.getDataString());
            mFilePath = mFilePath.substring(7, mFilePath.length());
            Log.d(TAG, "路径是："+mFilePath);
        }
    }

    /**
     * 显示popupMenu
     * @param view
     */
    private void showPopupMenu(View view) {
        //View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        //menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = (String) item.getTitle();
                switch (title){
                    case "扫描歌曲":
                        Intent seekIntent = new Intent(Home_Activity.this, Seek_Activity.class);
                        startActivity(seekIntent);
                        break;
                }
                return false;
            }
        });
        //PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }

    //初始化ListView
    public void MyAdapter(){
        musicList = Utility.MyMusicList();
        myAdapter = new MyAdapter(this, musicList);
        mListView.setAdapter(myAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyAdapter();
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null){
            mEditor.putInt("ps", mApp.getMusicPosition());
            mEditor.putInt("musicDuration", mMediaPlayer.getCurrentPosition());
            mEditor.putBoolean("sp", true);
            mEditor.commit();
        }
        super.onDestroy();
        unbindService(connection);
    }



    public class DelayThread extends Thread{
        int milliseconds;
        public DelayThread(int i){
            milliseconds = i;
        }

        @Override
        public void run() {
            while (true){
                try {
                    sleep(milliseconds);
                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    break;
                case 1:
                    MyAdapter();//初始化ListView的数据
                    break;
                case 10:
                    //显示Home_Activity的进度条
                    Utility.showProgressDialog(Home_Activity.this);//显示进度条
                    break;
                case 11:
                    Utility.closeProgressDialog();//关闭进度条
                    break;
                default:
                    break;
            }
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //创建退出对话框
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确定退出？");
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create();
                }
            });
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            builder.show();
        }
        return false;
    }*/
}
