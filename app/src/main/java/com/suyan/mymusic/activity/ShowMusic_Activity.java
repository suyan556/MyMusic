package com.suyan.mymusic.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.suyan.mymusic.R;
import com.suyan.mymusic.util.MyApp;
import com.suyan.mymusic.util.Utility;

import static com.suyan.mymusic.activity.Home_Activity.binder;
import static com.suyan.mymusic.activity.Home_Activity.mEditor;
import static com.suyan.mymusic.activity.Home_Activity.musicList;
import static com.suyan.mymusic.server.MusicService.mMediaPlayer;

public class ShowMusic_Activity extends Activity implements View.OnClickListener {

    private static final String TAG = "ShowMusic_Activity";
    private MyApp mMyApp;
    private int mp = mMyApp.getMusicPosition();

    public static TextView musicTitle, musicArtist, nowTime, totalTime;

    private ImageView showMusicUp, showMusicPause, showMusicNext, showMusicDemo, showMusicOther;
    private Button showMusicBack;

    public static SeekBar showMusicSeekBar;
    public static ObjectAnimator recordOA;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_music_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        mMyApp = new MyApp();

        mMyApp.setIsShow();


        showMusicDelayThread delayThread = new showMusicDelayThread(500);
        delayThread.start();
        Log.d(TAG, "调用了更新界面的子线程");

        musicTitle = (TextView) findViewById(R.id.showMusic_title);
        musicArtist = (TextView) findViewById(R.id.showMusic_artist);
        musicTitle.setText(mMyApp.getMusicTitle());
        musicArtist.setText(mMyApp.getMusicArtist());

        showMusicUp = (ImageView) findViewById(R.id.img_show_up);
        showMusicPause = (ImageView) findViewById(R.id.img_show_pause);
        showMusicNext = (ImageView) findViewById(R.id.img_show_next);
        showMusicBack = (Button) findViewById(R.id.showMusic_back);
        nowTime = (TextView) findViewById(R.id.showMusic_nowTime);
        showMusicDemo = (ImageView) findViewById(R.id.img_show_demo);
        showMusicOther = (ImageView) findViewById(R.id.img_show_other);

        totalTime = (TextView) findViewById(R.id.showMusic_totalTime);
        totalTime.setText(Utility.Format(mMediaPlayer.getDuration()));


        showMusicUp.setOnClickListener(this);
        showMusicPause.setOnClickListener(this);
        showMusicNext.setOnClickListener(this);
        showMusicBack.setOnClickListener(this);
        showMusicDemo.setOnClickListener(this);
        showMusicOther.setOnClickListener(this);

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            showMusicPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.a7r));
        } else {
            showMusicPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.a7t));
        }

        showMusicSeekBar = (SeekBar) findViewById(R.id.showmusic_progressBar);
        showMusicSeekBar.setMax(mMediaPlayer.getDuration());
        showMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        ImageView showMusicRecord = (ImageView) findViewById(R.id.showMusic_record);
        recordOA = ObjectAnimator.ofFloat(showMusicRecord, "rotation", 0f, 360f);
        recordOA.setDuration(6000);
        recordOA.setInterpolator(new LinearInterpolator());
        recordOA.setRepeatCount(Animation.INFINITE);
        recordOA.setRepeatMode(ObjectAnimator.RESTART);
        if (mMediaPlayer.isPlaying()){
            recordOA.start();
        } else {
            recordOA.start();
            recordOA.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_show_up:
                if (mMediaPlayer != null){
                    int upPosition = mMyApp.getMusicPosition() - 1;
                    int size = musicList.size();
                    Log.d(TAG, "size:"+size);
                    if (mMyApp.getMusicPosition() == 0){
                        upPosition = size-1;
                    }
                    mMyApp.setMusicPosition(upPosition);
                    binder.doPlay(showMusicPause);

                }
                break;
            case R.id.img_show_pause:
                if (mMediaPlayer.isPlaying()){
                    binder.doPause();
                    showMusicPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.a7t));
                    recordOA.pause();
                } else {
                    binder.duContinuePlay();
                    recordOA.resume();
                    showMusicPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.a7r));
                }
                break;
            case R.id.img_show_next:
                Log.d(TAG, "点击了下一首");
                if (mMediaPlayer != null){
                    int nextPosition = mMyApp.getMusicPosition()+1;
                    int size = musicList.size();
                    Log.d(TAG, "size:"+size);
                    if (mMyApp.getMusicPosition() == size-1){
                        nextPosition = 0;
                    }
                    mMyApp.setMusicPosition(nextPosition);
                    binder.doPlay(showMusicPause);
                }
                break;
            case R.id.showMusic_back:
                finish();
                break;
            case R.id.img_show_demo:
                Toast.makeText(this, "该功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_show_other:
                Toast.makeText(this, "该功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    public class showMusicDelayThread extends Thread{
        int milliseconds;
        public showMusicDelayThread(int i){
            milliseconds = i;
        }

        @Override
        public void run() {
            while (true){
                try {
                    sleep(milliseconds);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMusicSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                            nowTime.setText(Utility.Format(mMediaPlayer.getCurrentPosition()));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null){
            mEditor.putInt("ps", mMyApp.getMusicPosition());
            mEditor.putInt("musicDuration", mMediaPlayer.getCurrentPosition());
            mEditor.putBoolean("sp", true);
            mEditor.commit();
        }
        super.onDestroy();
    }
}
