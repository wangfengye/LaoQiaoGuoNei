package com.yunbao.video.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.ImChatFacePagerAdapter;
import com.yunbao.common.interfaces.OnFaceClickListener;
import com.yunbao.video.bean.VideoCommentBean;
import com.yunbao.video.dialog.VideoInputDialogFragment;

/**
 * 实现该接口的activity可以被video模块调用评论相关函数
 */
public interface ICommentViewListener {

   void openCommentInputWindow(boolean openFace, String videoId, String videoUid, VideoCommentBean bean);

    /**
     * 显示评论
     */
     void openCommentWindow(String videoId, String videoUid);

    /**
     * 隐藏评论
     */
    void hideCommentWindow() ;
}
