package com.ouyezhibo.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ouyezhibo.phonelive.R;
import com.ouyezhibo.phonelive.activity.bean.GuanGaoBean;
import com.ouyezhibo.phonelive.tool.ACache;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.http.HttpClient;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.main.activity.LoginActivity;
import com.yunbao.main.activity.MainActivity;
import com.yunbao.main.http.MainHttpUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 广告页或轮播页
 */
public class Act_AdvertisementPage extends AppCompatActivity implements View.OnTouchListener {
    protected Context mContext;
    ACache aCache;
    private ViewPager myViewPager;
    private RelativeLayout myGuanggao;

    public static void forward(Context context, boolean showInvite) {
        Intent intent = new Intent(context, Act_AdvertisementPage.class);
        intent.putExtra(Constants.SHOW_INVITE, showInvite);
        context.startActivity(intent);
    }

    private int sss;
    private TextView tiaoguo;
    private ImageView guanggaoImg;
    boolean onclcick = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.act_advertisementpage);
        mContext = this;
        myViewPager = findViewById(R.id.viewpager);
        myGuanggao = findViewById(R.id.myGuangao);
        tiaoguo = findViewById(R.id.AdvertisementPage_tiaoguo);
        guanggaoImg = findViewById(R.id.AdvertisementPage_Image);
        tiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUidAndToken();
                countDownTimer.cancel();
            }
        });
        initData();
    }

    CountDownTimer countDownTimer;

    public void initData() {
        aCache = ACache.get(this);
        if (aCache.getAsString("islogin") == null) {//执行欢迎页
            myViewPager.setVisibility(View.VISIBLE);
            myGuanggao.setVisibility(View.GONE);
            MyAdapter adapter = new MyAdapter();
            myViewPager.setOnTouchListener(this);
            myViewPager.setAdapter(adapter);
            myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    sss = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {//执行广告页
            myViewPager.setVisibility(View.GONE);
            myGuanggao.setVisibility(View.VISIBLE);
            SlideGetSlideList();
        }
    }

    int i = 10;

    public void setBannder() {
        countDownTimer = new CountDownTimer(11000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i--;
                tiaoguo.setText("跳过 " + i + " s");
                if (i == 0) {
                    checkUidAndToken();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
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

    /**
     * 跳转到首页
     */
    private void forwardMainActivity() {
        MainActivity.forward(mContext);
        finish();
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (x1 - x2 > 50 && sss == 2) {
                aCache.put("islogin", "yes");
                checkUidAndToken();
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 自定义类实现PagerAdapter，填充显示数据
     */
    class MyAdapter extends PagerAdapter {

        // 显示多少个页面
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化显示的条目对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(Act_AdvertisementPage.this).inflate(R.layout.item_welcome, null);
            ImageView iv = (ImageView) view.findViewById(R.id.welcome_iv);
            switch (position) {
                case 0:
                    iv.setImageResource(R.mipmap.icon_w1);
                    break;
                case 1:
                    iv.setImageResource(R.mipmap.icon_w2);
                    break;
                case 2:
                    iv.setImageResource(R.mipmap.icon_w3);
                    break;
            }
            container.addView(view);

            return view;
        }

        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    /**
     * 检查uid和token是否存在
     */
    private void checkUidAndToken() {
        String[] uidAndToken = SpUtil.getInstance().getMultiStringValue(
                new String[]{SpUtil.UID, SpUtil.TOKEN});
        final String uid = uidAndToken[0];
        final String token = uidAndToken[1];
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) {
            MainHttpUtil.getBaseInfo(uid, token, new CommonCallback<UserBean>() {
                @Override
                public void callback(UserBean bean) {
                    if (bean != null) {
                        CommonAppConfig.getInstance().setLoginInfo(uid, token, false);
                        forwardMainActivity();
                    }
                }
            });
        } else {
            LoginActivity.forward();
        }
    }

    /**
     * 获取广告页面
     */
    boolean guanggao = false;//用于判断广告页是否检测到值

    public void SlideGetSlideList() {
        Log.e("aa", "----------获取广告页面===");
        OkHttpUtils.post().url(new HttpClient().mUrl + "Slide.GetSlideList")
                .addParams("slide_cid", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                checkUidAndToken();
                Log.e("aa", "----------onError===" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                GuanGaoBean bean = gson.fromJson(response, GuanGaoBean.class);
                if (bean.getRet() == 200) {
                    for (int i = 0; i < bean.getData().getInfo().getList().size(); i++) {
                        if (bean.getData().getInfo().getList().get(i).getSlide_cid().equals("1")) {
                            Log.e("aa", "----------获取广告页面==="+response);
                            Glide.with(getApplicationContext()).load(bean.getData().getInfo().getList().get(i).getSlide_pic()).into(guanggaoImg);
                            guanggao = true;
                        }
                    }
                    if (guanggao) {
                        setBannder();
                    } else {
                        checkUidAndToken();
                    }
                } else {
                    checkUidAndToken();
                }
            }
        });
    }

    private static long mLastClickTime;// 用户判断多次点击的时间

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - mLastClickTime) > 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }
}
