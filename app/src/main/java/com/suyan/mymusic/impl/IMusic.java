package com.suyan.mymusic.impl;

import android.widget.ImageView;

/**
 * Created by Yan on 2016/9/19.
 */
public interface IMusic {

   void doPlay(ImageView imgPause);

   void doPause();

   void doDefaultPlay(int ps,  int duration);

   void duContinuePlay();
}
