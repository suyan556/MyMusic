package com.suyan.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.suyan.mymusic.R;

public class Seek_Ok_Activity extends Activity {

    private TextView myMusicCount;
    private TextView backIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek__ok_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Bundle bundle = this.getIntent().getExtras();
        int musicCount = bundle.getInt("count");

        myMusicCount = (TextView) findViewById(R.id.musicCount);
        myMusicCount.setText(musicCount+"");

        backIndex = (TextView) findViewById(R.id.seek_ok_backIndex);
        backIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIndexIntent = new Intent(Seek_Ok_Activity.this, Home_Activity.class);
                startActivity(backIndexIntent);

            }
        });

    }
}
