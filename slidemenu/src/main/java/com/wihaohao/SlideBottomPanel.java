package com.wihaohao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slidemenu.R;

/**
 * 利用bottom_sheet_behavior实现底部划动菜单
 */
public class SlideBottomPanel extends CoordinatorLayout {

    private View mContentView;
    private View mBottomView;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View mBlackView;
    private boolean isFade;
    public static final float MAX_ALPHA = 0.6f;
    private int alpha = 0x00;

    public SlideBottomPanel(Context context) {
        this(context, null, 0);
    }

    public SlideBottomPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBottomPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        initViews(context);
    }

    /**
     * 初始化自定义属性，并实例化子View
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("ResourceType")
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        this.mLayoutInflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideBottomPanel);
        if (null != typedArray) {
            if (typedArray.hasValue(R.styleable.SlideBottomPanel_contentView)) {
                mContentView = mLayoutInflater.inflate(typedArray.getResourceId(R.styleable.SlideBottomPanel_contentView, 0), this, true);
            }
            if (typedArray.hasValue(R.styleable.SlideBottomPanel_bottomView)) {
                mBottomView = mLayoutInflater.inflate(typedArray.getResourceId(R.styleable.SlideBottomPanel_bottomView, 0), this, false);
            }
            isFade = typedArray.getBoolean(R.styleable.SlideBottomPanel_fade, false);
            typedArray.recycle();
        }

    }

    private void initViews(Context context) {
        this.mContext = context;
        if (isFade) {
            mBlackView = new View(mContext);
            mBlackView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mBlackView.setBackgroundColor(Color.parseColor("#000000"));
            mBlackView.setAlpha(alpha);
            addView(mBlackView);
        }
        CoordinatorLayout.LayoutParams bottomLayoutParams = (LayoutParams) mBottomView.getLayoutParams();
        bottomLayoutParams.setBehavior(mBottomSheetBehavior = new BottomSheetBehavior());
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变回调
                Log.e("状态", newState + "");
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    blackView.setBackgroundColor(Color.TRANSPARENT);
                    if (isFade) {
                        mBlackView.setVisibility(View.GONE);
                    }
                }

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:// 关闭Bottom Sheets,显示peekHeight的高度，默认是0
                        if (null != mOnSlideBottomPanelStateListener) {
                            mOnSlideBottomPanelStateListener.onSlidBottomPanelStateCollapsed();
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING://用户拖拽Bottom Sheets时的状态
                        break;
                    case BottomSheetBehavior.STATE_SETTLING://当Bottom Sheets view摆放时的状态。
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED://当Bottom Sheets 展开的状态
                        if (null != mOnSlideBottomPanelStateListener) {
                            mOnSlideBottomPanelStateListener.onSlideBottomPanelStateExpanded();
                        }
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN://当Bottom Sheets 隐藏的状态
                        if (null != mOnSlideBottomPanelStateListener) {
                            mOnSlideBottomPanelStateListener.onSlideBottomPanelStateHidden();
                        }
                        break;

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
                Log.e("slideOffset", slideOffset + "");

                if (isFade && slideOffset > 0) {
                    mBlackView.setVisibility(View.VISIBLE);

                    mBlackView.setAlpha(slideOffset*MAX_ALPHA);
                }
                if (null != mOnSlideChangedListener) {
                    mOnSlideChangedListener.onSlideChanged(slideOffset);
                }
            }
        });

        addView(mBottomView, bottomLayoutParams);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View displayView = findViewById(R.id.display_layout);
        if (null != displayView) {
            mBottomSheetBehavior.setPeekHeight(displayView.getMeasuredHeight());
        }
    }


    private OnSlideBottomPanelStateListener mOnSlideBottomPanelStateListener;

    public void setOnSlideBottomPanelStateListener(OnSlideBottomPanelStateListener onSlideBottomPanelStateListener) {
        mOnSlideBottomPanelStateListener = onSlideBottomPanelStateListener;
    }

    /**
     * 监听底部是否显示
     */
    public interface OnSlideBottomPanelStateListener {
        void onSlideBottomPanelStateHidden();

        void onSlideBottomPanelStateExpanded();

        void onSlidBottomPanelStateCollapsed();

    }


    public void hideSlideBottomPanel() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void expandSlideBottomPanel() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapseSlidBottomPanel() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    public OnSlideChangedListener mOnSlideChangedListener;

    public void setOnSlideChangedListener(OnSlideChangedListener onSlideChangedListener) {
        mOnSlideChangedListener = onSlideChangedListener;
    }

    public interface OnSlideChangedListener {
        void onSlideChanged(float slideOffset);
    }


}
