package com.android.greenimainmissionlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class seedingActivity extends AppCompatActivity {
    ImageView seedimg;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeding);

        seedimg = (ImageView)findViewById(R.id.imageView2);
        Glide.with(this).load(R.drawable.seeding).into(seedimg);
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.seeding);
        seedimg.setImageBitmap(bitmap);
    }
}
