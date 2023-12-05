package com.android.greenimainmissionlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlantItemView extends LinearLayout {

    TextView textView;
    TextView textView2;
    ImageView imageView;


    public PlantItemView(Context context){
        super(context);
        init(context);
    }

    public PlantItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_plantitemview,this,true);//아이템모양 구성한 xml 레이아웃을 인플레이터로 객체화

        imageView = (ImageView)findViewById(R.id.imageView1);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);


    }

    public void setImage(int image) {
        imageView.setImageResource(image);
    }
    public void setPlant(String plant) {
        textView.setText(plant);
    }
    public void setPlantex(String plantex) {
        textView2.setText(plantex);
    }

}
