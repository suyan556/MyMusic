package com.suyan.mymusic.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suyan.mymusic.R;
import com.suyan.mymusic.activity.SelectPicPopupWindow;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yan on 2016/10/19.
 */

public class MyAdapter extends BaseAdapter {

    private List<HashMap<String, String >> musicList;
    private Context context;
    private LayoutInflater mLayoutInflater;//视图容器
    //自定义控件集合
    public final class ListItemView{
        public TextView musicName;
        public TextView musicArtist;
        //public ImageView imageViewMore;
        public LinearLayout mLayoutMore;
    }

    public MyAdapter(Context context, List<HashMap<String, String >> listItems){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);//创建视图容器并设置上下文
        this.musicList = listItems;
    }


    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = mLayoutInflater.inflate(R.layout.list_item, null);
            //获取控件对象
            listItemView.musicName = (TextView) convertView.findViewById(R.id.listview_music_title);
            listItemView.musicArtist = (TextView) convertView.findViewById(R.id.listview_music_artist);
            //listItemView.imageViewMore = (ImageView) convertView.findViewById(R.id.listview_btn_imageview);
            listItemView.mLayoutMore = (LinearLayout) convertView.findViewById(R.id.listview_btn_layout);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        //设置文字和图片
        listItemView.musicName.setText(musicList.get(position).get("title"));
        listItemView.musicArtist.setText(musicList.get(position).get("artist"));

        listItemView.mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPicPopupWindow.class);
                Bundle bundle = new Bundle();
                bundle.putInt("selectID", selectID);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
