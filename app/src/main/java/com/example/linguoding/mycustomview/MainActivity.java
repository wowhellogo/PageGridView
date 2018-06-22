package com.example.linguoding.mycustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vpgridviewlibrary.VpGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MyIconModel> mList;

    private VpGridView<MyIconModel> mVpGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpGridView=findViewById(R.id.vp_grid_view);
        initData();
        mVpGridView.setData(mList);
        mVpGridView.setOnItemClickListener(new VpGridView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });





        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.add(new MyIconModel("添加测试数据",R.mipmap.ic_launcher));
                mVpGridView.setData(mList);
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
