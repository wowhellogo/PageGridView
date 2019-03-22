package com.example.linguoding.mycustomview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wihaohao.PageGridView;

public class CustomModel implements PageGridView.ItemModel {

    public String title;

    public CustomModel(String title) {
        this.title = title;
    }

    @Override
    public String getItemName() {
        return null;
    }

    @Override
    public void setIcon(ImageView imageView) {

    }

    @Override
    public void setItemView(View itemView) {
        TextView textView= (TextView) itemView;
        textView.setText(title);
    }
}
