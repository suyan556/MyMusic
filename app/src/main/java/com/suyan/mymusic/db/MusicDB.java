package com.suyan.mymusic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suyan.mymusic.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan on 2016/9/12.
 */
public class MusicDB {
    public static final int version = 3;//版本号
    public static final String DB_NAME = "My_Music";//数据库名字
    private static final String TAG = "MusicDB";
    private static MusicDB mMusicDB;//单例对象
    private SQLiteDatabase mSQLiteDatabase;//数据库处理对象





    private MusicDB(Context context) {
        MyMusicOpenHelper myMusicOpenHelper = new MyMusicOpenHelper(context, DB_NAME, null, version);
        mSQLiteDatabase = myMusicOpenHelper.getWritableDatabase();
    }

    /**
     * 创建全局唯一的数据库对象
     * @param context
     * @return
     */
    public static MusicDB getInstance(Context context){
        if(mMusicDB == null){
            mMusicDB = new MusicDB(context);
        }
        return mMusicDB;
    }

    /* private String title; // 歌曲名称
    private String album; // 专辑
    private long albumId;// 专辑ID
    private String displayName; // 显示名称
    private String artist; // 歌手名称
    private long duration; // 歌曲时长
    private long size; // 歌曲大小
    private String url; // 歌曲路径
    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小*/



    /**
     * 保存音乐对象到数据库
     */
    public void saveMusic(Music music){
        if(music != null){
            ContentValues values = new ContentValues();
            values.put("title", music.getTitle());
            values.put("album", music.getAlbum());
            values.put("duration", music.getDuration());
            values.put("size", music.getSize());
            values.put("url", music.getUrl());
            values.put("musicId", music.getMusicId());
            values.put("artist", music.getArtist());
            long musicId = music.getMusicId();
            Cursor cursor =  mSQLiteDatabase.rawQuery("select musicId from music where musicId = ?", new String[]{String.valueOf(musicId)});
            if (cursor != null){
                int count = cursor.getCount();
                Log.d(TAG, "共："+count);
                if (count == 0) {
                    mSQLiteDatabase.insert("music", null, values);
                }
            }
            cursor.close();

        }
    }

    /**
     * 删除数据库数据
     */
    public void deleteMusic(String musicId){
        //mSQLiteDatabase.execSQL("delete from music where musicId='"+musicId+"'");
        mSQLiteDatabase.delete("music", "musicId=?", new String[]{musicId});
    }

    /**
     * 获取本地数据库里的所有的音乐
     */

    public List<Music> loadMusic(){
        List<Music> musics = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("music", null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Music music = new Music();
                music.setLrcTitle(cursor.getString(cursor.getColumnIndex("title")));
                music.setAlbum(cursor.getString(cursor.getColumnIndex("album")));
                music.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                music.setSize(cursor.getLong(cursor.getColumnIndex("size")));
                music.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                music.setMusicId(cursor.getLong(cursor.getColumnIndex("musicId")));
                music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
                musics.add(music);
            } while (cursor.moveToNext());
        }
        if (cursor == null){
            cursor.close();
        }
        return musics;
    }


    /**
     * 根据歌曲名字获取数据库里该歌曲的信息
     */

    public List<Music> loadMusicByName(String name){
        List<Music> musics = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("music", null, "title like ?", new String[]{"%" + name + "%"}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                Music music = new Music();
                music.setLrcTitle(cursor.getString(cursor.getColumnIndex("title")));
                music.setAlbum(cursor.getString(cursor.getColumnIndex("album")));
                music.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                music.setSize(cursor.getLong(cursor.getColumnIndex("size")));
                music.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                music.setMusicId(cursor.getLong(cursor.getColumnIndex("musicId")));
                music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
                musics.add(music);
            } while (cursor.moveToNext());
        }
        if (cursor == null){
            cursor.close();
        }
        return musics;
    }



}

