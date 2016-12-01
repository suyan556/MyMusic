package com.suyan.mymusic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yan on 2016/9/12.
 */
public class MyMusicOpenHelper extends SQLiteOpenHelper {


    //创建音乐表
    private static final String CREATE_MUSIC =
            "CREATE TABLE music(id INTEGER PRIMARY KEY,title TEXT, album TEXT, artist TEXT, duration LONG, size LONG, url TEXT, musicId LONG)";

    public MyMusicOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
