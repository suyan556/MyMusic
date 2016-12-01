package com.suyan.mymusic.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.suyan.mymusic.R;
import com.suyan.mymusic.impl.IMusic;
import com.suyan.mymusic.util.MyApp;
import com.suyan.mymusic.util.ShowMusicHandler;
import com.suyan.mymusic.util.Utility;

import java.io.IOException;
import java.util.HashMap;

import static com.suyan.mymusic.activity.Home_Activity.musicList;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    public static MediaPlayer mMediaPlayer;
    public ShowMusicHandler showMusicHandler;
    public MyApp myApp;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mMediaPlayer = new MediaPlayer();//创建一个音频播放器
        myApp = new MyApp();
        showMusicHandler = myApp.getShowMusicHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return new MyBinder();
    }

    /**
     * 如果不是第一次安装打开就按照存储下来的信息来设置初始值
     * @param ps
     * @param duration
     */
    public void defaultPlay(final int ps, final int duration) {
        Log.d(TAG, "调用了defaultPlay方法");
        try {
            HashMap<String, String> defaultList = musicList.get(ps);
            String defaultMusicPath = defaultList.get("url");
            final String defaultMusicTitle = defaultList.get("title");
            final String defaultMusicArtist = defaultList.get("artist");
            myApp.setMusicTitle(defaultMusicTitle);
            myApp.setMusicArtist(defaultMusicArtist);


            mMediaPlayer.reset();//准备
            mMediaPlayer.setDataSource(defaultMusicPath);//设置音频路径
            mMediaPlayer.prepareAsync();//异步准备音频资源
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "开始播放");
                    mMediaPlayer.seekTo(duration);
                    myApp.setMusicPosition(ps);
                    if (myApp.getISShow() == true){
                        showMusicHandler.sendEmptyMessage(2);
                    } else {
                        showMusicHandler.sendEmptyMessage(1);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放方法
     * @param
     */
    public void Play(final ImageView imgPause) {
        try {
            HashMap<String, String> list = Utility.MyMusicList().get(myApp.getMusicPosition());
            String path = list.get("url");
            final String title = list.get("title");
            final String artist = list.get("artist");

            myApp.setMusicTitle(title);
            myApp.setMusicArtist(artist);

            mMediaPlayer.reset();//准备
            mMediaPlayer.setDataSource(path);//设置音频路径
            mMediaPlayer.prepareAsync();//异步准备音频资源
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();//开始播放
                    Log.d(TAG, "开始播放");
                    if (myApp.getISShow() == true){
                        showMusicHandler.sendEmptyMessage(2);
                    } else {
                        showMusicHandler.sendEmptyMessage(1);
                    }
                    if (imgPause.getId() == R.id.img_pause) {
                        imgPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play_pressed));
                    } else if (imgPause.getId() == R.id.img_show_pause){
                        imgPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.a7r));
                    }

                }
            });
            //设置歌曲播放完毕后的逻辑
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "调用OnCompletionListener");
                    int nextPosition = myApp.getMusicPosition() + 1;
                    int size = musicList.size();
                    Log.d(TAG, "size:" + size);
                    if (myApp.getMusicPosition() == size - 1) {
                        nextPosition = 0;
                    }
                    myApp.setMusicPosition(nextPosition);
                    Play(imgPause);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();//暂停播放
        }
    }

    /**
     * 继续播放
     */
    public void continuePlay() {
        mMediaPlayer.start();
    }

    class MyBinder extends Binder implements IMusic {



        //供外部使用的播放方法
        public void doPlay(final ImageView imgPause) {
            Play(imgPause);
        }

        //暂停
        public void doPause() {
            pause();
        }

        //继续播放
        public void duContinuePlay() {
            continuePlay();
        }

        //设置开始默认路径
        public void doDefaultPlay(int ps, final int duration) {
            defaultPlay(ps, duration);
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mMediaPlayer.release();
    }
}
