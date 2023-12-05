package com.android.greenimainmissionlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class MissionAdapter extends BaseAdapter {
    ArrayList <String> missionTextArray = new ArrayList<String>(); // 미션 텍스트를 표시하기 위한 배열 생성
    ArrayList <String> missionSubTextArray = new ArrayList<String>(); // 미션 텍스트 아래 설명을 표시하기 위한 배열 생성

    ArrayList <String> bufferedmissionTextArray = new ArrayList<String >();
    ArrayList <String> bufferedmissionSubTextArray = new ArrayList<String >();

    public MissionAdapter(String[] missionText, String[] missionSubText) {
        for(int i = 0 ; i <missionText.length; i++){
            this.missionTextArray.add(missionText[i]);
        }
        for(int i = 0 ; i <missionText.length; i++){
            this.missionSubTextArray.add(missionSubText[i]);
        }

        this.bufferedmissionTextArray.addAll(this.missionTextArray);
        this.bufferedmissionSubTextArray.addAll(this.missionSubTextArray);
    }

    @Override
    public int getCount() {
        return missionTextArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.mission_list,viewGroup, false);

        TextView missionTitle = view.findViewById(R.id.missionText);
        TextView missionSubTitle = view.findViewById(R.id.missionSubText);

        missionTitle.setText(missionTextArray.get(position));
        missionSubTitle.setText(missionSubTextArray.get(position));

        return view;
    }
}
