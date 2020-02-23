package com.yunbao.common.expand.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import java.util.List;

public class ViewLifePagerAdapter extends PagerAdapter {
    private List<ButterKnifeAbsViewHolder> mViewList;
    private ViewLifeViewPager viewLifeViewPager;
    private ButterKnifeAbsViewHolder contextViewHolder;


    public ViewLifePagerAdapter(ButterKnifeAbsViewHolder contextViewHolder,List<ButterKnifeAbsViewHolder> list) {
        this.contextViewHolder=contextViewHolder;
        if(contextViewHolder!=null){
            contextViewHolder.addViewHolderArray(mViewList);
        }
        mViewList = list;
    }

    public ViewLifePagerAdapter(List<ButterKnifeAbsViewHolder> list) {
        mViewList = list;
    }


    public ViewLifePagerAdapter(ButterKnifeAbsViewHolder contextViewHolder) {
        this.contextViewHolder=contextViewHolder;
        if(contextViewHolder!=null){
            contextViewHolder.addViewHolderArray(mViewList);
        }
    }





    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViewList.get(position).getContentView();
        container.addView(view);

        if(viewLifeViewPager!=null){
            viewLifeViewPager.firstAdd();
            viewLifeViewPager=null;
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewList.get(position).getContentView());
    }


    public List<ButterKnifeAbsViewHolder> getmViewList() {
        return mViewList;
    }

    public void setmViewList(List<ButterKnifeAbsViewHolder> mViewList) {
        this.mViewList = mViewList;
    }


    public void setViewLifeViewPager(ViewLifeViewPager viewLifeViewPager) {
        this.viewLifeViewPager = viewLifeViewPager;
    }
}
