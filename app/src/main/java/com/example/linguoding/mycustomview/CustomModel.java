package com.example.linguoding.mycustomview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wihaohoa.PageGridView;

public class CustomModel extends PageGridView.ItemModel {

    public String title;

    public CustomModel(String title) {
        this.title = title;
    }

    @Override
    protected String getItemName() {
        return null;
    }

    @Override
    protected void setIcon(ImageView imageView) {

    }

    @Override
    protected void setItemView(View itemView) {
        TextView textView= (TextView) itemView;
        textView.setText(title);
    }
}
