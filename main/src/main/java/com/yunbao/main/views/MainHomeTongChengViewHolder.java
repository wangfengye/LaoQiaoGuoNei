package com.yunbao.main.views;


import android.content.Context;
import android.view.ViewGroup;

public class MainHomeTongChengViewHolder extends AbsMainHomeParentViewHolder {
    public MainHomeTongChengViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }
    @Override
    protected void loadPageData(int position) {

    }
    @Override
    protected int getPageCount() {
        return 0;
    }
    @Override
    protected String[] getTitles() {
        return new String[0];
    }
    @Override
    protected int getLayoutId() {
        return 0;
    }
}
