package com.example.linguoding.mycustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pagegridviewlibrary.PageGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MyIconModel> mList;
    List<CustomModel> mList2;

    private PageGridView<MyIconModel> mPageGridView;
    private PageGridView<CustomModel> mPageGridView2;
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

        //自定义item
        mPageGridView2=findViewById(R.id.p_grid_view2);
        mPageGridView2.setData(mList2);


    }

    private void initData() {
        mList=new ArrayList<>();
        mList2=new ArrayList<>();
        for(int i=0;i<30;i++){
            mList.add(new MyIconModel("测试"+i,R.mipmap.ic_launcher));
            mList2.add(new CustomModel("标题"+i));
        }
    }
}
