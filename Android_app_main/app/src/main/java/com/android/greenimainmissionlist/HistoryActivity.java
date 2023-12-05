package com.android.greenimainmissionlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HistoryActivity extends AppCompatActivity {
    ImageView iv; //이미지 뷰 생성
    Bitmap bitmap;

    static String MQTTHOST ="tcp://113.198.79.118:1883";
    private static final String IMG_TOPIC = "IMG"; // 이미지 Topic

    MqttAndroidClient client; // Mqtt서버를 불러오기 위한 과정
    MqttConnectOptions options; // Mqtt서버를 불러오기 위한 과정

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        iv = (ImageView)findViewById(R.id.image);

        String clientId = MqttClient.generateClientId(); // Mqtt클라이언트 Id값 받아오기
        options = new MqttConnectOptions(); // Mqtt커넥트 옵션 객체 생성

        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId); // MqttClient객체 생성

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(HistoryActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        // Mqtt연결 및 확인

    }

    // 카메라 이미지가 있는 Url 불러오기
    public void show(View v) {
        Thread t = new Thread() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                try{
                    URL url = new URL("http://113.198.79.119:8080/image.jpg"); // 이미지가 저장되어 있는 ip주소 입력
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        t.start();

        try {
            t.join();

            iv.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Mqtt로 부터 이미지 토픽 받아오기
    public void pub(View v) {
        String topic = IMG_TOPIC;
        String message = "the payload";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // 이미지 토픽 subscribe
    private void setSubscription() {
        try {
            client.subscribe(IMG_TOPIC,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Mqtt서버 연결여부 확인
    public void conn(View v) {
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(HistoryActivity.this,"connect",Toast.LENGTH_SHORT).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(HistoryActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
