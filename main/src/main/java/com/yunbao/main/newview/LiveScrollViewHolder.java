package com.yunbao.main.newview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.adapter.LiveRoomScrollAdapter;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.utils.LiveStorge;
import com.yunbao.live.views.LivePlayTxViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.video.adapter.VideoScrollAdapter;
import com.yunbao.video.bean.VideoBean;
import com.yunbao.video.custom.VideoLoadingBar;
import com.yunbao.video.event.VideoScrollPageEvent;
import com.yunbao.video.utils.VideoStorge;
import com.yunbao.video.views.VideoPlayViewHolder;
import com.yunbao.video.views.VideoPlayWrapViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiveScrollViewHolder extends AbsViewHolder implements SwipeRefreshLayout.OnRefreshListener, LiveRoomScrollAdapter.ActionListener, View.OnClickListener {
    private String mLiveKey;
    private LivePlayTxViewHolder mLivePlayViewHolder;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int mPosition;
    private LiveRoomScrollAdapter mLiveAdapter;
    private VideoLoadingBar mVideoLoadingBar;
    private HttpCallback mRefreshCallback;
    private HttpCallback mLoadMoreCallback;
    private int mPage;
    private String mLiveUid;

    public LiveScrollViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public LiveScrollViewHolder(Context mContext, ViewGroup viewById, int i, String videoHome, int i1) {
        super(mContext, viewById, i, videoHome, i1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_scroll;
    }
    @Override
    protected void processArguments(Object... args) {
        mPosition = (int) args[0];
        mLiveKey = (String) args[1];
        mPage = (int) args[2];
    }
    @Override
    public void init() {
        List<LiveBean> list = LiveStorge.getInstance().get(Constants.LIVE_HOME);
        Log.i("LIve", "init: "+list.size());
        mLivePlayViewHolder =new LivePlayTxViewHolder(mContext,null);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(com.yunbao.video.R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(com.yunbao.video.R.color.global);
        mRefreshLayout.setEnabled(false);//产品不让使用刷新
        mRecyclerView = (RecyclerView) findViewById(com.yunbao.video.R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mLiveAdapter = new LiveRoomScrollAdapter(mContext, list, mPosition);
        mLiveAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mLiveAdapter);
        mVideoLoadingBar = (VideoLoadingBar) findViewById(com.yunbao.video.R.id.video_loading);
        findViewById(com.yunbao.video.R.id.input_tip).setOnClickListener(this);
        findViewById(com.yunbao.video.R.id.btn_face).setOnClickListener(this);
       // EventBus.getDefault().register(this);
        mRefreshCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<LiveBean> list = JSON.parseArray(Arrays.toString(info), LiveBean.class);
                    if (mLiveAdapter != null) {
                        mLiveAdapter.setList(list);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        };
        mLoadMoreCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<LiveBean> list = JSON.parseArray(Arrays.toString(info), LiveBean.class);
                    if (list.size() > 0) {
                        if (mLiveAdapter != null) {
                            mLiveAdapter.insertList(list);
                        }
                        EventBus.getDefault().post(new VideoScrollPageEvent(mLiveKey, mPage));
                    } else {
                        ToastUtil.show(com.yunbao.video.R.string.video_no_more_video);
                        mPage--;
                    }
                } else {
                    mPage--;
                }
            }
        };

    }


    @Override
    public void onRefresh() {
        mPage=1;
        MainHttpUtil.getHot(mPage,mRefreshCallback);
    }
    public void onPageSelected(LiveBean liveBean, ViewGroup container, boolean first) {
        View mMainContentView = LayoutInflater.from(mContext).inflate(com.yunbao.live.R.layout.activity_live_audience, null, false);

        if (mMainContentView != null && container != null) {
            ViewParent parent = mMainContentView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (viewGroup != container) {
                    viewGroup.removeView(mMainContentView);
                    container.addView(mMainContentView);
                }
            } else {
                container.addView(mMainContentView);
            }
        }
        if (!first) {
            //checkLive(liveBean);
        }
    }

    @Override
    public void onPageOutWindow(String liveUid) {
        if (TextUtils.isEmpty(mLiveUid) || mLiveUid.equals(liveUid)) {
            LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
            LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM);
            LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE);
           // clearRoomData();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
