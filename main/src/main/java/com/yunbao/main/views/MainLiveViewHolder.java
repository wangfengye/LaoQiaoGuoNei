package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.main.R;

/**
 * @author  maple
 *
 * 直播流
 */
public class MainLiveViewHolder extends AbsMainHomeParentViewHolder{
    public MainLiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home;
    }

    @Override
    protected void loadPageData(int position) {
        if (mViewHolders == null) {
            return;

        }
        position=1;
        AbsMainHomeChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }

                 vh= new MainHomeLiveViewHolder(mContext, parent);


                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData();
        }
    }

    @Override
    protected int getPageCount() {
        //return TextUtils.isEmpty(CommonAppConfig.getInstance().getCity())?3:4;
        return 2;
    }

    @Override
    protected String[] getTitles() {
      return new String[]{};

    }
}
