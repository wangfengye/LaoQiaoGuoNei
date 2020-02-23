package com.yunbao.main.newview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunbao.common.interfaces.LifeCycleListener;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.main.views.AbsMainHomeChildViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLiveViewHolder extends AbsMainHomeChildViewHolder {
    public AbsLiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public AbsLiveViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context,parentView,args);
    }

    @Override
    public void init() {
        mTag = this.getClass().getSimpleName();

        mLifeCycleListeners = new ArrayList<>();
        main();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onCreate();
            }
        }
    }
    protected String mTag;
    protected List<LifeCycleListener> mLifeCycleListeners;



    protected void main() {

    }

    protected boolean isStatusBarWhite() {
        return false;
    }

    protected void setTitle(String title) {
        TextView titleView = (TextView) findViewById(com.yunbao.common.R.id.titleView);
        if (titleView != null) {
            titleView.setText(title);
        }
    }




    protected boolean canClick() {
        return ClickUtil.canClick();
    }



    @Override
    public void onDestroy() {
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onDestroy();
            }
            mLifeCycleListeners.clear();
            mLifeCycleListeners = null;
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onStart();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onResume();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onPause();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onStop();
            }
        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (mLifeCycleListeners != null && listener != null) {
            mLifeCycleListeners.add(listener);
        }
    }

    public void addAllLifeCycleListener(List<LifeCycleListener> listeners) {
        if (mLifeCycleListeners != null && listeners != null) {
            mLifeCycleListeners.addAll(listeners);
        }
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        if (mLifeCycleListeners != null) {
            mLifeCycleListeners.remove(listener);
        }
    }
}
