package com.suyan.mymusic.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.suyan.mymusic.activity.Home_Activity;
import com.suyan.mymusic.db.MusicDB;
import com.suyan.mymusic.model.Music;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.suyan.mymusic.activity.Home_Activity.mListView;

/**
 * Created by Yan on 2016/9/12.
 */

/**
 * 把一个long单位转换成00:00
 */
public class Utility {

    private static MyApp mMyApp = new MyApp();

    public static MusicDB mMusicDB;

    public static ProgressDialog progressDialog;

    public static String Format(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        String hmsString = format.format(time);
        return hmsString;
    }

    //Home_Activity的ListView自动跳到正在播放的条目
    public static void toMusicPosition(){
        mListView.smoothScrollBy(10, 1000);
        mListView.smoothScrollByOffset(mMyApp.getMusicPosition());
        mListView.smoothScrollToPosition(mMyApp.getMusicPosition());
    }

    /**
     * 获取本地列表数据库信息
     * @return
     */
    public static List<HashMap<String, String >> MyMusicList(){
        mMusicDB = Home_Activity.mMusicDB;
        List<Music> musics = mMusicDB.loadMusic();
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

    /**
     * 关闭进度对话框
     */
    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框
     */
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("扫描中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 获取本地所有的音乐列表
     * @return
     */
    public static List<Music> getMusics(Context context){
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<Music> musics = new ArrayList<Music>();
        for (int i = 0; i < cursor.getCount() ; i++){
            Music music = new Music();
            cursor.moveToNext();
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));//音乐标题
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));//文件大小
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//文件路径
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            long musicId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));//音乐ID
            if(isMusic != 0){
                //只把音乐添加到集合里
                music.setTitle(title);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setSize(size);
                music.setUrl(url);
                music.setMusicId(musicId);
                musics.add(music);
            }
        }
        return musics;
    }

    /**
     * 把字节单位转换成兆单位
     */
    public static int byteToM(String bt){
        int size = Integer.parseInt(bt);
        int musicSize = size/1024/1024;
        return musicSize;
    }

}
