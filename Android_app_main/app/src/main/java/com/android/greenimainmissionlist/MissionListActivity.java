package com.android.greenimainmissionlist;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

public class MissionListActivity extends AppCompatActivity { // 미션 리스트를 보여주는 액티비티

    ListView missionListView;
    MissionAdapter missionAdapter;
    ImageView todolist;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missionlist);

        todolist = (ImageView)findViewById(R.id.imageView4);
        Glide.with(this).load(R.drawable.todolist).into(todolist);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.todolist);
        todolist.setImageBitmap(bitmap);

        String[] missionListText = getResources().getStringArray(R.array.missionText);
        String[] missionSubListText = getResources().getStringArray(R.array.missionSubText);

        missionListView= (ListView)findViewById(R.id.missionListView); // 미션 리스트뷰 생성
        missionAdapter = new MissionAdapter(missionListText,missionSubListText); // 미션 어댑터를 불러온다
        missionListView.setAdapter(missionAdapter); // 리스트뷰에 미션 어댑터를 적용시킨다.

        // 미션 리스트를 클릭했었을 때 미션에 관한 설명이 보이도록 한, 버튼을 이벤트 처리한 부분
        missionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
                else if (i ==1) {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
                else if (i == 2) {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
                else if (i == 3) {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
                else if (i == 4) {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MissionListActivity.this,seedingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
