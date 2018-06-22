package com.example.linguoding.mycustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pagegridviewlibrary.PageGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MyIconModel> mList;

    private PageGridView<MyIconModel> mPageGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPageGridView =findViewById(R.id.vp_grid_view);
        initData();
        mPageGridView.setData(mList);
        mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initData() {
        mList=new ArrayList<>();
        for(int i=0;i<5;i++){
            mList.add(new MyIconModel("测试"+i,R.mipmap.ic_launcher));
        }
    }
}
