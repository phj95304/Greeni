package com.android.greenimainmissionlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }
}
