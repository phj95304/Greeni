package com.android.greenimainmissionlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PlantListActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    String name;
    String plant;
    int image_num;
    int image;
    ListView listView;
    PlantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantlist);
//식물종류 선택

        listView = (ListView) findViewById(R.id.listView);

        adapter = new PlantAdapter();

        adapter.addItem(new PlantItem("상추",R.drawable.item1 ));
        adapter.addItem(new PlantItem("로메인",R.drawable.item3 ));
        adapter.addItem(new PlantItem("적근대",R.drawable.item2 ));
        adapter.addItem(new PlantItem("청경채",R.drawable.item4 ));


        listView.setAdapter(adapter);//리스트뷰에 어댑터 객체 설정

        editText=(EditText)findViewById(R.id.editText);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트뷰에서 식물선택
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {//아이템클릭했을 때
                PlantItem item = (PlantItem) adapter.getItem(position);
                image=item.getImage();
                plant=item.getPlant();
                name = editText.getText().toString();

                Intent intent = new Intent();
                if ((name.isEmpty() && plant.isEmpty()) == false) {//이름과 식물을 입력받았을 때
                    editText.getText().toString();
                    intent.putExtra("name", name);//이름인텐트전달
                    intent.putExtra("plant", plant);//작물종류인텐트전달
                    intent.putExtra("image",image);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                else {//입력한 이름이 공백일 때
                    if (editText.length() < 0) {
                        editText.getText().clear();
                    }
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }

        });
    }

    class PlantAdapter extends BaseAdapter {//Base Adapter 를 이용해서 PlantAdapter 클래스 정의
        ArrayList<PlantItem> items = new ArrayList<PlantItem>();//PlantItem 객체를 저장할 ArrayList 객체생성

        @Override
        public int getCount() {//PlantAdapter 어댑터에서 관리하는 아이템의 개수를 리턴
            return items.size();
        }

        public void addItem(PlantItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {//
            PlantItemView view = new PlantItemView(getApplicationContext());

            PlantItem item = items.get(position);
            view.setPlant(item.getPlant());
            view.setImage(item.getImage());

            return view;

        }

    }
}
