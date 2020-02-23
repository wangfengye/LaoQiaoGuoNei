package com.yunbao.common.expand.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ListUtil;
import java.util.List;

public class ViewLifeViewPager extends ViewPager {
    private PageChangeListener pageChangeListener;
    private boolean isScroll = false;

    public ViewLifeViewPager(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ViewLifeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(pageChangeListener!=null){
                        pageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                    }
            }

            @Override
            public void onPageSelected(int position) {
                if(pageChangeListener!=null){
                    pageChangeListener.onPageSelected(position);
                }
                judegeViewVisible(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(pageChangeListener!=null){
                    pageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }



    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter!=null&& adapter instanceof ViewLifePagerAdapter)
        {
            ViewLifePagerAdapter viewLifePagerAdapter= (ViewLifePagerAdapter) adapter;
            viewLifePagerAdapter.setViewLifeViewPager(this);
        }
    }

    public void firstAdd(){
        judegeViewVisible(0);
        L.e("firstAdd==");
    }

    private void judegeViewVisible(int position) {
        PagerAdapter pagerAdapter= getAdapter();
        if(pagerAdapter!=null&& pagerAdapter instanceof ViewLifePagerAdapter){
            ViewLifePagerAdapter viewLifePagerAdapter= (ViewLifePagerAdapter) pagerAdapter;
            List<ButterKnifeAbsViewHolder> array=viewLifePagerAdapter.getmViewList();
            if(ListUtil.haveData(array)){
                int size= array.size();
                for(int i=0;i<size;i++){
                    ButterKnifeAbsViewHolder holder=array.get(i);
                    View contentView=holder.getContentView();
                    if(contentView!=null&&indexOfChild(contentView)!=-1){
                        if(i==position){
                            holder.viewVisibily(true);
                        }else{
                            holder.viewVisibily(false);
                        }
                    }

                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            return true;// 可行,消费,拦截事件
        }
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public PageChangeListener getPageChangeListener() {
        return pageChangeListener;
    }

    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    interface PageChangeListener extends ViewPager.OnPageChangeListener {
    }

}
