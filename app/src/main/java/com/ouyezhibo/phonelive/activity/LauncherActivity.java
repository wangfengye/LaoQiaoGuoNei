package com.ouyezhibo.phonelive.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ouyezhibo.phonelive.AppContext;
import com.ouyezhibo.phonelive.R;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.LocationUtil;
import com.yunbao.common.utils.ProcessResultUtil;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

/**
 * Created by cxf on 2018/9/17.
 */

public class LauncherActivity extends AppCompatActivity {

    private Handler mHandler;
    protected Context mContext;

    private ProcessResultUtil mProcessResultUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //下面的代码是为了防止一个bug:
        // 收到极光通知后，点击通知，如果没有启动app,则启动app。然后切后台，再次点击桌面图标，app会重新启动，而不是回到前台。
        Intent intent = getIntent();
        if (!isTaskRoot()
                && intent != null
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
        setStatusBar();
        setContentView(R.layout.activity_launcher);
        mContext = this;
        ImageView imageView = findViewById(R.id.img);
        ImgLoader.display(R.mipmap.screen, imageView);
        mHandler = new Handler();
        mProcessResultUtil = new ProcessResultUtil(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    /**
     * 获取所在位置
     */
    private void getLocation() {
        mProcessResultUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new Runnable() {
            @Override
            public void run() {
                LocationUtil.getInstance().startLocation();
                LocationUtil.getInstance().setLocationListner(new LocationUtil.LocationListner() {
                    @Override
                    public void compelete() {
                        if(mHandler ==null){
                            Log.i("tag", "compelete: handler null");return;
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getConfig();
                            }
                        }, 300);
                    }
                });
            }
        });
    }

    /**
     * 获取Config信息
     */
    private void getConfig() {
        CommonHttpUtil.getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                if (bean != null) {
                    AppContext.sInstance.initBeautySdk(bean.getBeautyKey());
                    forwardMainActivity();
                }else {
                }
            }
        });
    }


    /**
     * 跳转到首页
     */
    private void forwardMainActivity() {
        Act_AdvertisementPage.forward(mContext, false);
        finish();
    }


    @Override
    protected void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.GET_BASE_INFO);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_CONFIG);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mProcessResultUtil != null) {
            mProcessResultUtil.release();
        }
        super.onDestroy();
    }

    /**
     * 设置透明状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }


}
