package com.suyan.mymusic.util;

import android.app.Application;

import com.suyan.mymusic.activity.Home_Activity;

/**
 * Created by Yan on 2016/10/14.
 */

public class MyApp extends Application {
    //共享变量
    public static Home_Activity.MyHandler mHandler;
    public static ShowMusicHandler mShowMusicHandler;

    public static String musicTitle, musicArtist;//全局使用的正在播放的歌曲名和歌手

    public static boolean isShow;

    public static int musicPosition;//记录播放音乐的位置

    /**
     * 设置播放音乐的位置
     * @param ps
     */
    public void setMusicPosition(int ps) {
        this.musicPosition = ps;
    }

    /**
     * 获取播放音乐的位置
     * @return
     */
    public static int getMusicPosition(){
        return musicPosition;
    }

    /**
     * 获取全局的歌手信息
     */

    /**
     * 设置全局使用的歌曲名
     * @param title
     */
    public void setMusicTitle(String title){
        this.musicTitle = title;
    }
    /**
     * 获取全局的歌手信息
     */
    public static String getMusicTitle(){
        return musicTitle;
    }

    /**
     * 设置全局使用的歌手信息
     * @param artist
     */
    public void setMusicArtist(String artist){
        this.musicArtist = artist;
    }
    /**
     * 获取全局的歌手信息
     */
    public static String getMusicArtist(){
        return musicArtist;
    }

    //set方法
    public void setHandler(Home_Activity.MyHandler handler){
        this.mHandler = handler;
    }
    //get方法
    public static Home_Activity.MyHandler getHandler(){
        return mHandler;
    }

    /**
     * 设置全局使用的ShowMusicHandler
     * @return
     */
    public void setShowMusicHandler(ShowMusicHandler showMusicHandler){
        this.mShowMusicHandler = showMusicHandler;
    }

    /**
     * 获取全局使用的ShowMusicHandler
     * @return
     */
    public static ShowMusicHandler getShowMusicHandler(){
        return mShowMusicHandler;
    }



    /**
     * 用于判断是否打开过播放界面
     */
    public void setIsShow(){
        this.isShow = true;
    }

    /**
     * 获取是否打开过播放界面，如果打开过则为true,否则为false
     * @return
     */
    public static boolean getISShow(){
        return isShow;
    }

    /*@Override
    public void onTerminate() {
        super.onTerminate();
        if (mMediaPlayer != null){
            mEditor.putInt("ps", musicPosition);
            mEditor.putInt("musicDuration", mMediaPlayer.getCurrentPosition());
            mEditor.putBoolean("sp", true);
            mEditor.commit();
        }
    }*/
}
