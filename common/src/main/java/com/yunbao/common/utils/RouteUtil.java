package com.yunbao.common.utils;

import android.content.Context;
import android.content.Intent;

import com.yunbao.common.CommonAppContext;
import com.yunbao.common.Constants;

/**
 * Created by cxf on 2019/2/25.
 */

public class RouteUtil {
    //Intent隐式启动 action
    public static final String INTENT_ACTION_LAUNCHER = "com.ouyezhibo.phonelive.activity.LauncherActivity";
    public static final String INTENT_ACTION_LOGIN_INVALID = "com.yunbao.main.activity.LoginInvalidActivity";
    public static final String INTENT_ACTION_USER_HOME = "com.yunbao.main.activity.UserHomeActivity";
    public static final String INTENT_ACTION_COIN = "com.yunbao.main.activity.MyCoinActivity";

    /**
     * 启动页
     */
    public static void forwardLauncher(Context context) {
        Intent intent = new Intent(INTENT_ACTION_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 登录过期
     */
    public static void forwardLoginInvalid(String tip) {
        Intent intent = new Intent(INTENT_ACTION_LOGIN_INVALID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TIP, tip);
        CommonAppContext.sInstance.startActivity(intent);
    }

    /**
     * 跳转到个人主页
     */
    public static void forwardUserHome(Context context, String toUid) {
        Intent intent = new Intent(INTENT_ACTION_USER_HOME);
        intent.putExtra(Constants.TO_UID, toUid);
        context.startActivity(intent);
    }

    /**
     * 跳转到充值页面
     */
    public static void forwardMyCoin(Context context) {
        context.startActivity(new Intent(INTENT_ACTION_COIN));
    }


}
