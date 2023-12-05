package com.android.greenimainmissionlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.android.greenimainmissionlist.R.drawable.temp;
import static com.android.greenimainmissionlist.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private static final String MAINACTIVITY_TAG = "MAINACTIVITY";

    private static final String TEMP_TOPIC = "TEMPO";// 온도 Topic
    private static final String SOILHUMI_TOPIC = "HUMI";// 습도 Topic
    private static final String CDS_TOPIC = "LIGHT";// 조도 Topic

    private static final String TAG ="hi";

    private static final String SYNCHRONIZE_TOPIC = "Test/Synchronize";//동기화 Topic
    private static final String SYNCHRONIZE_MSG = "2";

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); //날짜가 출력 될 형식
    TextView mTextView;//날짜가 출력될 텍슽트뷰
    TextView countDate;//몇 일차가 출력될 텍스트 뷰;
    TextView nameText;//이름이 출력될 텍스트 뷰

    String plantName;

    ProgressBar humidprogressBar;
    ProgressBar sunprogressBar;
    ProgressBar tempprogressBar;

    TextView humidStat;
    TextView luxStat;
    TextView tempStat;

    Bitmap bitmap;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;

    ImageView humidityimg;
    ImageView luximg;
    ImageView temptimg;

    MqttService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 온도, 습도, 조도에 관한 프로그래스 바
        humidprogressBar = (ProgressBar)findViewById(R.id.humidprogressBar);
        sunprogressBar = (ProgressBar)findViewById(R.id.sunprogressBar);
        tempprogressBar = (ProgressBar)findViewById(R.id.tempprogressBar);

        // 날짜 정보를 표시
        mTextView = (TextView) findViewById(textView);
        nameText = (TextView) findViewById(R.id.textName);
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat.format(mDate);
        mTextView.setText(mFormat.format(mDate));

        // 온도, 습도, 조도 이미지 삽입
        humidityimg = (ImageView)findViewById(R.id.humiimage);
        Glide.with(this).load(R.drawable.humidity).into(humidityimg);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.humidity);
        humidityimg.setImageBitmap(bitmap);

        luximg = (ImageView)findViewById(R.id.luximage);
        Glide.with(this).load(R.drawable.sun).into(luximg);
        bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.sun);
        luximg.setImageBitmap(bitmap2);

        temptimg = (ImageView)findViewById(R.id.tmptimage);
        Glide.with(this).load(temp).into(temptimg);
        bitmap3 = BitmapFactory.decodeResource(getResources(), temp);
        temptimg.setImageBitmap(bitmap3);

        humidStat = (TextView)findViewById(R.id.humidStat);
        luxStat = (TextView)findViewById(R.id.luxStat);
        tempStat = (TextView)findViewById(R.id.tempStat);

        nameText = (TextView)findViewById(R.id.textName);
        Intent nameIntent = getIntent();
        plantName = nameIntent.getStringExtra("name");
        nameText.setText(plantName);

        //몇 일차인지 계산
        countDate = (TextView)findViewById(R.id.textView2);

        //http://mainia.tistory.com/2119 참고
        //안드로이드 d-day검색?


        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Mqtt서버 시작
        startMqtt();
    }

    private void startMqtt() {
        mqttService = new MqttService(getApplicationContext());
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            //Mqtt서버로 부터 값을 받아와 값을 출력
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                if (topic.equals("LIGHT")) luxStat.setText(mqttMessage.toString());
                if (topic.equals("HUMI")) humidStat.setText(mqttMessage.toString());
                if (topic.equals("TEMPO")) tempStat.setText(mqttMessage.toString());
                Log.w("Debug",mqttMessage.toString());
                Log.w("Debug",topic.toString());

            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        String clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://113.198.84.26:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 메뉴 아이템 선택시 발생하는 부분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//mission activity에서
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent historyIntent = new Intent(getApplicationContext(),HistoryActivity.class);
                historyIntent.setFlags(historyIntent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(historyIntent);
                break;

            case R.id.action_mission:
                Intent missionIntent = new Intent(getApplicationContext(),MissionListActivity.class);
                missionIntent.setFlags(missionIntent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(missionIntent);
                break;

            case android.R.id.home:
                Intent startIntent = new Intent(getApplicationContext(),StartActivity.class);
                startIntent.setFlags(startIntent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    온도상태에서 따른 프로그래스 바
    private String tempStatCheck(int temp) {
        String tempStat = null;
        if (temp < 16) {
            tempprogressBar.incrementProgressBy(33);
        } else if (temp>=16 && temp <= 19) {
            tempprogressBar.incrementProgressBy(66);
        } else if (temp > 19) {
            tempprogressBar.incrementProgressBy(100);
        } else tempStat = null;
        return tempStat;
    }
//    습도 프로그랴스바
    private String soilhumiStatCheck(int soilhumi) {
        String soilhumiStat = null;
        if (soilhumi <= 77) {
            humidprogressBar.incrementProgressBy(33);
        } else if (soilhumi>77 && soilhumi <= 78) {
            humidprogressBar.incrementProgressBy(66);
        } else if (soilhumi >= 78) {
            humidprogressBar.incrementProgressBy(100);
        } else soilhumiStat = null;
        return soilhumiStat;
    }
//    조도 프로그래스바
    private String cdsStatCheck(int cds) {
        String cdsStat = null;
        if (cds > 5) {
            sunprogressBar.incrementProgressBy(33);
        } else if (cds>1 && cds <= 5) {
            sunprogressBar.incrementProgressBy(66);
        } else if (cds <= 1) {
            sunprogressBar.incrementProgressBy(100);
        } else cdsStat = null;
        return cdsStat;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

