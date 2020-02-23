package com.yunbao.main.newview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.activity.LiveAudienceActivity;
import com.yunbao.live.views.AbsLiveViewHolder;

public class LiveAudienceViewHolder2 extends AbsLiveViewHolder {

    private String mLiveUid;
    private String mStream;
    private LiveAudienceViewHolder mHolder;

    public LiveAudienceViewHolder2(Context context, ViewGroup parentView,LiveAudienceViewHolder holder) {
        super(context, parentView);
        mHolder=holder;
    }


    @Override
    protected int getLayoutId() {
        return com.yunbao.live.R.layout.view_live_audience;
    }

    @Override
    public void init() {
        super.init();
        findViewById(com.yunbao.live.R.id.btn_close).setOnClickListener(this);
        findViewById(com.yunbao.live.R.id.btn_share).setOnClickListener(this);
        findViewById(com.yunbao.live.R.id.btn_red_pack).setOnClickListener(this);
        findViewById(com.yunbao.live.R.id.btn_gift).setOnClickListener(this);
    }

    public void setLiveInfo(String liveUid, String stream) {
        mLiveUid = liveUid;
        mStream = stream;
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        int i = v.getId();
        if (i == com.yunbao.live.R.id.btn_msg) {
            mHolder.openChatListWindow();

        } else if (i == com.yunbao.live.R.id.btn_chat) {
            mHolder.openChatWindow();

        }

        if (i == com.yunbao.live.R.id.btn_close) {
            close();

        } else if (i == com.yunbao.live.R.id.btn_share) {
            openShareWindow();

        } else if (i == com.yunbao.live.R.id.btn_red_pack) {
            mHolder.openRedPackSendWindow();

        } else if (i == com.yunbao.live.R.id.btn_gift) {
            openGiftWindow();

        }
    }

    /**
     * 退出直播间
     */
    private void close() {
        mHolder.onBackPressed();
    }


    /**
     * 打开礼物窗口
     */
    private void openGiftWindow() {
       mHolder.openGiftWindow();
    }

    /**
     * 打开分享窗口
     */
    private void openShareWindow() {
       mHolder.openShareWindow();
    }

}
