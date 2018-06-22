package com.example.linguoding.mycustomview;

import android.widget.ImageView;

import com.vpgridviewlibrary.VpGridView;

public class MyIconModel extends VpGridView.ItemModel {
    private String name;


    private int iconId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public MyIconModel(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    @Override
    protected String getItemName() {
        return name;
    }

    @Override
    protected void setIcon(ImageView imageView) {
        imageView.setImageResource(iconId);
    }
}
