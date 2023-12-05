package com.android.greenimainmissionlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    public static final int FLAG_PLANT_NUMBER_0 = 000; //화분개수 0
    public static final int FLAG_PLANT_NUMBER_1 = 111; //화분개수 1
    public static final int FLAG_PLANT_NUMBER_2 = 222;//화분개수 2
    SharedPreferences pref;
    public int mode = FLAG_PLANT_NUMBER_0 ;

    public static final String PREF_ID = "pref";

    int image1;
    int image2;
    ImageButton imagebutton1 ;
    ImageButton imagebutton2;
    String name1;
    String plant1;
    String name2;
    String plant2;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imagebutton1 = (ImageButton)findViewById(R.id.myplantbutton1);
        imagebutton2 =(ImageButton)findViewById(R.id.myplantbutton2);
        textView1= (TextView)findViewById(R.id.text1);
        textView2= (TextView)findViewById(R.id.text2);
        textView3= (TextView)findViewById(R.id.text3);
        textView4=  (TextView)findViewById(R.id.text4);
        restoreState();


    }

    public void myplant01Clicked(View v) {//설정버튼누르면 add 액티비티로 이동 @Override
        saveState();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void myplant02Clicked(View v) {//설정버튼누르면 add 액티비티로 이동 @Override
        saveState();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    public void plus1Clicked(View v) {//+추가+버튼누르면 add 액티비티로 이동
        saveState();
        Intent intent = new Intent(getApplicationContext(), PlantListActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, mode);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//plantlist액티비티 실행후 호출되는 메소드
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_PLANT_NUMBER_0) {//화분0개인 상태

            if (resultCode == RESULT_OK ) {//이름과 화분을 입력받았을 때
                name1 = data.getExtras().getString("name");
                plant1 = data.getExtras().getString("plant");
                image1 = data.getExtras().getInt("image");
                imagebutton1.setImageResource(image1);
                textView1.setText("화분이름 : " + name1 ) ;
                textView2.setText("식물 : " + plant1);
                mode = FLAG_PLANT_NUMBER_1;

                saveState();
            }

            else if(resultCode == RESULT_CANCELED) {//제대로 입력되지 않은 경우
                restoreState();
                Toast.makeText(getApplicationContext(), "다시입력하세요 ", Toast.LENGTH_SHORT).show();
            }


        }

        else if (requestCode == FLAG_PLANT_NUMBER_1) {//화분 1개 등록일때

            if (resultCode == RESULT_OK ) {//이름과 화분을 입력받았을 때
                name2 = data.getExtras().getString("name");
                plant2 = data.getExtras().getString("plant");
                image2 = data.getExtras().getInt("image");
                imagebutton2.setImageResource(image2);
                textView3.setText("화분이름 : " + name2) ;
                textView4.setText("식물 : " + plant2);
                mode = FLAG_PLANT_NUMBER_2;

                saveState();

            }

            else if(resultCode == RESULT_CANCELED) {//제대로 입력되지 않은 경우
                restoreState();
                Toast.makeText(getApplicationContext(), "다시입력하세요 ", Toast.LENGTH_SHORT).show();
            }
        }

        else if ((requestCode == FLAG_PLANT_NUMBER_2) || (requestCode ==(RESULT_CANCELED))) {//화분 2개입력후 추가버튼 눌렀을 때
            restoreState();
            Toast.makeText(getApplicationContext(),"화분을 모두 사용중입니다!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
    }


    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
    }


    protected void restoreState() {
        pref = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("mode"))) {
            int resotredmode = pref.getInt("mode", FLAG_PLANT_NUMBER_0);
            mode = resotredmode;
            //Toast.makeText(this, "restoreMode 실행됨 ", Toast.LENGTH_SHORT).show();

            if ((pref != null) && (pref.contains("name1"))&&(pref.contains("plant1"))&&(pref.contains("image1"))  ) {
                String name1 = pref.getString("name1", "");
                String plant1 = pref.getString("plant1", "");
                image1 = pref.getInt("image1", R.drawable.tmp_pot_image);
                textView1.setText(name1);
                textView2.setText(plant1);
                imagebutton1.setImageResource(image1);

                if ((pref != null) && (pref.contains("name2")) && (pref.contains("plant2")) && (pref.contains("image2")) ) {
                    String name2 = pref.getString("name2", "");
                    String plant2 = pref.getString("plant2", "");
                    image2 = pref.getInt("image2", R.drawable.tmp_pot_image);
                    textView3.setText(name2);
                    textView4.setText(plant2);
                    imagebutton2.setImageResource(image2);
                }
            }
        }
    }



    protected void saveState() {
        pref = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name1", textView1.getText().toString());
        editor.putString("plant1", textView2.getText().toString());
        editor.putInt("image1", image1);
        editor.putString("name2", textView3.getText().toString());
        editor.putString("plant2", textView4.getText().toString());
        editor.putInt("image2", image2);
        editor.putInt("mode",mode);
        // Toast.makeText(this,"saveState 실행됨 ",Toast.LENGTH_SHORT).show();

        editor.commit();
    }



    protected void clearMyPrefs() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}