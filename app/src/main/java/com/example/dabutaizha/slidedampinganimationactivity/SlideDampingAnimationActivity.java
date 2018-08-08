package com.example.dabutaizha.slidedampinganimationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slideDampongAnimationLayout.SlideDampingAnimationLayout;
import slideDampongAnimationLayout.SlideEventListener;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * @author dabutaizha
 * @date 2018/7/20 上午9:58
 */
public class SlideDampingAnimationActivity extends AppCompatActivity implements SlideEventListener {

    private SlideDampingAnimationLayout mSlideLayout;
    private ListView mListView;
    private List<Map<String, String>> mData;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        mSlideLayout = findViewById(R.id.slide_layout);
        mListView = findViewById(R.id.example_list);

        mSlideLayout.setSlideListener(this);

        initData();
        mAdapter = new SimpleAdapter(this,
                mData,
                android.R.layout.simple_list_item_1,
                new String[] {"content"},
                new int[] {android.R.id.text1});
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void initData() {
        mData = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("content", "这是第一个Activity");
            mData.add(map);
        }

    }

    @Override
    public void leftEvent() {
        Toast.makeText(this, "leftEvent", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void rightEvent() {
        Toast.makeText(this, "rightEvent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
