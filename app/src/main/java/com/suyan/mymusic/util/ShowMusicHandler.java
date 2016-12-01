package com.suyan.mymusic.util;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.suyan.mymusic.activity.Home_Activity;
import com.suyan.mymusic.activity.ShowMusic_Activity;
import com.suyan.mymusic.server.MusicService;

import static com.suyan.mymusic.server.MusicService.mMediaPlayer;

/**
 * Created by Yan on 2016/11/5.
 */

public class ShowMusicHandler extends Handler {
    private MyApp mMyApp;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handleMessage(Message msg) {
        mMyApp = new MyApp();
        switch (msg.what) {
            case 1:
                Home_Activity.showTitle.setText(mMyApp.getMusicTitle());
                Home_Activity.showArtist.setText(mMyApp.getMusicArtist());
                break;
            case 2:
                Home_Activity.showTitle.setText(mMyApp.getMusicTitle());
                Home_Activity.showArtist.setText(mMyApp.getMusicArtist());

                ShowMusic_Activity.musicTitle.setText(mMyApp.getMusicTitle());
                ShowMusic_Activity.musicArtist.setText(mMyApp.getMusicArtist());
                ShowMusic_Activity.showMusicSeekBar.setMax(mMediaPlayer.getDuration());
                ShowMusic_Activity.totalTime.setText(Utility.Format(MusicService.mMediaPlayer.getDuration()));
                ShowMusic_Activity.recordOA.resume();
                break;
            default:
                break;
        }
    }
}
