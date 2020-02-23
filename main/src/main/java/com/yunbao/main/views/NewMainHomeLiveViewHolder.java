package com.yunbao.main.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.LiveClassBean;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import com.yunbao.common.expand.viewpager.ViewLifePagerAdapter;
import com.yunbao.common.expand.viewpager.ViewLifeViewPager;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.main.R;
import com.yunbao.main.R2;
import com.yunbao.main.activity.LiveClassActivity;
import com.yunbao.main.adapter.MainHomeLiveClassAdapter;
import com.yunbao.main.bean.BannerBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public abstract class NewMainHomeLiveViewHolder extends AbsMainHomeChildViewHolder {
    @BindView(R2.id.indicator)
    public MagicIndicator indicator;
    @BindView(R2.id.classRecyclerView_top)
    RecyclerView classRecyclerViewTop;
    @BindView(R2.id.viewPager)
    ViewLifeViewPager viewPager;
    @BindView(R2.id.line)
    View line;
    private RecyclerView mClassRecyclerViewDialog;


    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;
    private View mShadow;


    private View mBtnDismiss;

    private boolean hideClass;
    private boolean showBanner;

    public NewMainHomeLiveViewHolder(Context context, ViewGroup parentView, boolean shouBanner1) {
        super(context, parentView);
        ButterKnife.bind(this, mContentView);
        showBanner = shouBanner1;
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_new_home_live;
    }

    @Override
    public void init() {

    }

    Banner myBanner;

    public void initView() {

        myBanner = (Banner) findViewById(R.id.myBanner);
        mShadow = findViewById(R.id.shadow);
        mBtnDismiss = findViewById(R.id.btn_dismiss);
        mBtnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClick()) {
                    if (mShowAnimator != null) {
                        mShowAnimator.cancel();
                    }
                    if (mHideAnimator != null) {
                        mHideAnimator.start();
                    }
                }
            }
        });
        if(showBanner){
            indicator.setVisibility(View.VISIBLE);
            myBanner.setVisibility(View.VISIBLE);
        }else {
            indicator.setVisibility(View.GONE);
            myBanner.setVisibility(View.GONE);
        }
        mShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClick()) {
                    if (mShowAnimator != null) {
                        mShowAnimator.cancel();
                    }
                    if (mHideAnimator != null) {
                        mHideAnimator.start();
                    }
                }
            }
        });

        classRecyclerViewTop.setHasFixedSize(true);
        classRecyclerViewTop.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        List<ButterKnifeAbsViewHolder> butterKnifeAbsViewHolders = new ArrayList<>();
        LiveReclyViewHolder reclyViewHolder1 = new LiveReclyViewHolder(mContext, viewPager) {
            @Override
            public Observable<List<LiveBean>> requestData(int p) {
                return getHot(p);
            }
        };
        LiveReclyViewHolder reclyViewHolder2 = new LiveReclyViewHolder(mContext, viewPager) {
            @Override
            public Observable<List<LiveBean>> requestData(int p) {
                return getPopular(p);
            }
        };
        LiveReclyViewHolder reclyViewHolder3 = new LiveReclyViewHolder(mContext, viewPager) {
            @Override
            public Observable<List<LiveBean>> requestData(int p) {
                return getNew(p);
            }
        };
        butterKnifeAbsViewHolders.add(reclyViewHolder1);
        butterKnifeAbsViewHolders.add(reclyViewHolder2);
        butterKnifeAbsViewHolders.add(reclyViewHolder3);
        viewPager.setAdapter(new ViewLifePagerAdapter(butterKnifeAbsViewHolders));
        final String[] titles = {"热门", "人气", "最新"};
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray1));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewPager != null) {
                            viewPager.setCurrentItem(index);
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setXOffset(DpUtil.dp2px(5));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));
                return linePagerIndicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);
        mClassRecyclerViewDialog = (RecyclerView) findViewById(R.id.classRecyclerView_dialog);
        mClassRecyclerViewDialog.setHasFixedSize(true);
        mClassRecyclerViewDialog.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean != null) {
            List<LiveClassBean> list = configBean.getLiveClass();
            if (list != null && list.size() > 0) {
                List<LiveClassBean> targetList = new ArrayList<>();
                if (list.size() < 6) {
                    targetList.addAll(list);
                } else {
                    targetList.addAll(list.subList(0, 5));
                    LiveClassBean bean = new LiveClassBean();
                    bean.setAll(true);
                    bean.setName(WordUtil.getString(R.string.all));
                    targetList.add(bean);
                }
                MainHomeLiveClassAdapter topAdapter = new MainHomeLiveClassAdapter(mContext, targetList, false);
                topAdapter.setOnItemClickListener(new OnItemClickListener<LiveClassBean>() {
                    @Override
                    public void onItemClick(LiveClassBean bean, int position) {
                        if (!canClick()) {
                            return;
                        }
                        if (bean.isAll()) {//全部分类
                            showClassListDialog();
                        } else {
                            LiveClassActivity.forward(mContext, bean.getId(), bean.getName());
                        }
                    }
                });
                if (classRecyclerViewTop != null) {
                    classRecyclerViewTop.setAdapter(topAdapter);
                }
                MainHomeLiveClassAdapter dialogAdapter = new MainHomeLiveClassAdapter(mContext, list, true);
                dialogAdapter.setOnItemClickListener(new OnItemClickListener<LiveClassBean>() {
                    @Override
                    public void onItemClick(LiveClassBean bean, int position) {
                        if (!canClick()) {
                            return;
                        }
                        LiveClassActivity.forward(mContext, bean.getId(), bean.getName());
                    }
                });
                mClassRecyclerViewDialog.setAdapter(dialogAdapter);
                mClassRecyclerViewDialog.post(new Runnable() {
                    @Override
                    public void run() {
                        initAnim();
                    }
                });
            }
        }
        SlideGetSlideList();
    }

    private void showClassListDialog() {
        if (mBtnDismiss != null && mBtnDismiss.getVisibility() != View.VISIBLE) {
            mBtnDismiss.setVisibility(View.VISIBLE);
        }
        if (mShowAnimator != null) {
            mShowAnimator.start();
        }
    }


    private void initAnim() {
        final int height = mClassRecyclerViewDialog.getHeight();
        mClassRecyclerViewDialog.setTranslationY(-height);
        mShowAnimator = ObjectAnimator.ofFloat(mClassRecyclerViewDialog, "translationY", 0);
        mShowAnimator.setDuration(200);
        mHideAnimator = ObjectAnimator.ofFloat(mClassRecyclerViewDialog, "translationY", -height);
        mHideAnimator.setDuration(200);
        TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mShowAnimator.setInterpolator(interpolator);
        mHideAnimator.setInterpolator(interpolator);
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rate = 1 + ((float) animation.getAnimatedValue() / height);
                mShadow.setAlpha(rate);
            }
        };
        mShowAnimator.addUpdateListener(updateListener);
        mHideAnimator.addUpdateListener(updateListener);
        mHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mBtnDismiss != null && mBtnDismiss.getVisibility() == View.VISIBLE) {
                    mBtnDismiss.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    public abstract Observable<List<LiveBean>> getHot(int p);

    public abstract Observable<List<LiveBean>> getPopular(int p);

    public abstract Observable<List<LiveBean>> getNew(int p);

    public boolean isHideClass() {
        return hideClass;
    }

    public void setHideClass(boolean hideClass) {
        this.hideClass = hideClass;
        if (hideClass) {
            classRecyclerViewTop.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取banner图片
     */
    private List<String> bannerList;
    public void SlideGetSlideList() {
        bannerList = new ArrayList<>();
        LiveHttpUtil.SlideGetSlideList("2", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    Gson gson = new Gson();
                    BannerBean bannerBean = gson.fromJson(info[0], BannerBean.class);
                    for (int i = 0; i < bannerBean.getList().size(); i++) {
                        if (bannerBean.getList().get(i).getSlide_cid().equals("2")) {
                            bannerList.add(bannerBean.getList().get(i).getSlide_pic());
                        }
                    }
                    //设置图片加载器
                    myBanner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    myBanner.setImages(bannerList);
                    myBanner.start();
                }

            }
        });

    }
}
