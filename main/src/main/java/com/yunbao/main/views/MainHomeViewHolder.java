package com.yunbao.main.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.api.API;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.main.R;
import com.yunbao.video.views.VideoPlayWrapViewHolder;
import com.yunbao.video.views.VideoScrollViewHolder;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by cxf on 2018/9/22.
 * MainActivity 首页
 */

public class MainHomeViewHolder extends AbsMainHomeParentViewHolder {

    private MainHomeFollowViewHolder mFollowViewHolder;
    private NewMainHomeLiveViewHolder mLiveViewHolder;
    private MainHomeVideoViewHolder mVideoViewHolder;
    private NewMainHomeLiveViewHolder tongchengViewHolder;
    private VideoPlayWrapViewHolder.OnClickPaihangListener mOnClickPaihangListener;

    public MainHomeViewHolder(Context context, ViewGroup parentView, VideoPlayWrapViewHolder.OnClickPaihangListener onClickPaihangListener) {
        super(context, parentView);
        mOnClickPaihangListener=onClickPaihangListener;
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
        AbsMainHomeChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mFollowViewHolder = new MainHomeFollowViewHolder(mContext, parent);
                    vh = mFollowViewHolder;
                } else if (position == 2) {
                    mLiveViewHolder = new NewMainHomeLiveViewHolder(mContext, parent,true) {
                        @Override
                        public Observable<List<LiveBean>> getHot(int p) {
                            return API.getHot(p);
                        }

                        @Override
                        public Observable<List<LiveBean>> getPopular(int p) {
                            return API.getPopular(p);
                        }

                        @Override
                        public Observable<List<LiveBean>> getNew(int p) {
                            return API.getNewlive(p);
                        }
                    };
                    vh = mLiveViewHolder;
                } else if (position == 1) {
                    mVideoViewHolder = new MainHomeVideoViewHolder(mContext, parent,mOnClickPaihangListener);
                    vh = mVideoViewHolder;

                }

                else if (position == 3) {
                    tongchengViewHolder = new NewMainHomeLiveViewHolder(mContext, parent,false) {
                        @Override
                        public Observable<List<LiveBean>> getHot(int p) {
                            return API.getCityHot(p);
                        }

                        @Override
                        public Observable<List<LiveBean>> getPopular(int p) {
                            return API.getCityPopular(p);
                        }

                        @Override
                        public Observable<List<LiveBean>> getNew(int p) {
                            return API.getCityNewlive(p);
                        }
                    };
                    tongchengViewHolder.setHideClass(true);
                    vh = tongchengViewHolder;
                }


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
        String city=CommonAppConfig.getInstance().getCity();
        if(TextUtils.isEmpty(city)){
            return new String[]{
                    WordUtil.getString(R.string.follow),
                    //WordUtil.getString(R.string.live),
                    WordUtil.getString(R.string.recommend)
            };
        }else{
            return new String[]{
                    WordUtil.getString(R.string.follow),
                    //WordUtil.getString(R.string.live),
                    WordUtil.getString(R.string.recommend),
                    //"同城"
            };
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mViewHolders==null)return;
        for (int i = 0; i < mViewHolders.length; i++) {
            if (mViewHolders[i]!=null) mViewHolders[i].onPause();
        }
    }
}
