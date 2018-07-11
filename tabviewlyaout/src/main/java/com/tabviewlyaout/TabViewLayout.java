package com.tabviewlyaout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 作者:林国定 邮件:lingguodingg@gmail.com
 * 创建时间:2018/6/26
 * 描述:ViewPager+RadioGroup管理组合控件
 */
public class TabViewLayout extends FrameLayout implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioGroup tabView;
    private ViewPager viewPager;
    private int currentTab = 0; // 当前Tab页面索引
    private OnTabChangedListener onTabChangedListener;
    private LayoutInflater mLayoutInflater;
    private View mContentView;

    public TabViewLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public TabViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }


    private void initViews(Context context, AttributeSet attrs) {
        this.mLayoutInflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabViewLayout);
        if (null != typedArray) {
            if (typedArray.hasValue(R.styleable.TabViewLayout_content_view)) {
                mContentView = mLayoutInflater.inflate(typedArray.getResourceId(R.styleable.TabViewLayout_content_view, 0), this, true);
            }
            typedArray.recycle();
        }
        this.tabView = findViewById(R.id.tab);
        this.viewPager = findViewById(R.id.viewpager);
        this.viewPager.addOnPageChangeListener(this);
        this.tabView.setOnCheckedChangeListener(this);
        ((RadioButton) this.tabView.getChildAt(currentTab)).setChecked(true);
    }

    /**
     * 设置Tab变化监听
     *
     * @param onTabChangedListener
     */
    public void setOnTabChangedListener(OnTabChangedListener onTabChangedListener) {
        this.onTabChangedListener = onTabChangedListener;
    }

    /**
     * 获得当前Tab页面索引
     *
     * @return
     */
    public int getCurrentTab() {
        return currentTab;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < tabView.getChildCount(); i++) {
            if (tabView.getChildAt(i).getId() == checkedId) {
                viewPager.setCurrentItem(i);
                setCurrentTab(i);
                break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.currentTab = position;
        ((RadioButton) tabView.getChildAt(position)).setChecked(true);
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        if (null != onTabChangedListener) {
            onTabChangedListener.onTabChanged(tabView, tabView.getCheckedRadioButtonId(), currentTab);
        }
    }


    public void setTabText(int position, String text) {
        ((RadioButton) tabView.getChildAt(position)).setText(text);
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * Tab变化监听接口
     */
    public interface OnTabChangedListener {
        /**
         * Tab变化监听回调函数
         *
         * @param group     RadioGroup
         * @param checkedId 选中的RadioButton的id
         * @param position  当前页面索引
         */
        void onTabChanged(RadioGroup group, int checkedId, int position);
    }


}
