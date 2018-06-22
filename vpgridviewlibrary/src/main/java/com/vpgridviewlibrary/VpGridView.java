package com.vpgridviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager+GridView实现左右滑动查看更多分类的控件
 */
public class VpGridView<T extends VpGridView.ItemModel> extends FrameLayout {
    public final static int PAGE_Size = 8;
    public final static int NUM_COUNT = 4;
    public final static boolean IS_ShOW_INDICATOR = true;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewPager mViewPager;
    private LinearLayout mLlDot;
    private List<T> mDatas;
    private List mPagerList;

    /**
     * 每一页显示的个数
     */
    private int pageSize;
    /**
     * 总的页数
     */
    private int pageCount;

    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    private int numColumns = 0;
    private boolean isShowIndicator;


    public VpGridView(Context context) {
        this(context, null, 0);
    }

    public VpGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public VpGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initViews(context);
    }


    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.VpGridView);
        pageSize = typedArray.getInteger(R.styleable.VpGridView_pageSize, PAGE_Size);
        numColumns = typedArray.getInteger(R.styleable.VpGridView_numColumns, NUM_COUNT);
        isShowIndicator = typedArray.getBoolean(R.styleable.VpGridView_isShowIndicator, IS_ShOW_INDICATOR);
        typedArray.getResourceId(R.styleable.VpGridView_selectedIndicator, 0);
        typedArray.getResourceId(R.styleable.VpGridView_unSelectedIndicator, 0);

        typedArray.recycle();
    }

    private void initViews(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.vp_gridview, this, true);
        mViewPager = mContentView.findViewById(R.id.view_pager);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        //动态设备ViewPager
        float rate= (float) pageSize / (float) numColumns;
        int rows= (int) Math.ceil(rate);
        layoutParams.height=rows*getDimensionPixelOffset(R.dimen.item_height);
        mViewPager.setLayoutParams(layoutParams);
        mLlDot = mContentView.findViewById(R.id.ll_dot);

    }


    public void setData(List<T> data){
        this.mDatas=data;
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = new GridView(mContext);
            gridView.setNumColumns(numColumns);
            gridView.setOverScrollMode(OVER_SCROLL_NEVER);
            gridView.setAdapter(new GridViewAdapter(mContext, mDatas, i, pageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(pos);
                    }


                }
            });
        }
        //设置适配器
        mViewPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        if (isShowIndicator && pageCount > 1) {
            setOvalLayout();
        }else{
            mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int position) {
                    curIndex = position;
                }
            });
        }


    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(mInflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        ImageView imageView = mLlDot.getChildAt(0).findViewById(R.id.v_dot);
        imageView.setImageResource(R.mipmap.ic_menu_dot_selected);

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                ImageView lastImageView = mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot);
                lastImageView.setImageResource(R.mipmap.ic_menu_dot_normal);
                // 圆点选中
                ImageView nextImageView = mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot);
                nextImageView.setImageResource(R.mipmap.ic_menu_dot_selected);
                curIndex = position;
            }
        });
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return (mViewList.get(position));
        }

        @Override
        public int getCount() {
            if (mViewList == null)
                return 0;
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    class GridViewAdapter<T extends ItemModel> extends BaseAdapter {
        private List<T> mDatas;
        private LayoutInflater inflater;
        /**
         * 页数下标,从0开始(当前是第几页)
         */
        private int curIndex;
        /**
         * 每一页显示的个数
         */
        private int pageSize;

        public GridViewAdapter(Context context, List<T> mDatas, int curIndex, int pageSize) {
            inflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
            this.curIndex = curIndex;
            this.pageSize = pageSize;
        }

        public void setData(List<T> data) {
            this.mDatas = data;
            notifyDataSetChanged();
        }
        /**
         * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
         */
        @Override
        public int getCount() {
            return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position + curIndex * pageSize);
        }

        @Override
        public long getItemId(int position) {
            return position + curIndex * pageSize;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_view, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv = convertView.findViewById(R.id.tv_item_name);
                viewHolder.iv = convertView.findViewById(R.id.im_item_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            /**
             * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
             */
            int pos = position + curIndex * pageSize;
            viewHolder.tv.setText(mDatas.get(pos).getItemName());
            mDatas.get(pos).setIcon(viewHolder.iv);
            return convertView;
        }


        class ViewHolder {
            public TextView tv;
            public ImageView iv;
        }
    }



    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public abstract static class ItemModel {
        protected abstract String getItemName();

        protected abstract void setIcon(ImageView imageView);
    }

    /**
     * dp转px
     */
    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }

    public  int getDimensionPixelOffset(@DimenRes int resId) {
        return mContext.getResources().getDimensionPixelOffset(resId);
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
