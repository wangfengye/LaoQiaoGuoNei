package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.common.Constants;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import com.yunbao.common.expand.RefreshView;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.main.R;
import com.yunbao.main.R2;
import com.yunbao.main.activity.MainActivity;
import com.yunbao.main.adapter.MainHomeLiveAdapter;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;

public abstract class LiveReclyViewHolder extends ButterKnifeAbsViewHolder implements OnItemClickListener<LiveBean> {
    private MainHomeLiveAdapter adapter;
    @BindView(R2.id.refreshView)
    RefreshView refreshView;

    public LiveReclyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_recly_view;
    }

    @Override
    public void initView() {
        super.initView();
        adapter=new MainHomeLiveAdapter(mContext);
        adapter.setOnItemClickListener(this);
        refreshView.setAdapter(adapter);
        refreshView.defaultGridSetting(2);

        refreshView.setDataListner(new RefreshView.DataListner<LiveBean>() {
            @Override
            public Observable<List<LiveBean>> loadData(int p) {
                return requestData(p);
            }
            @Override
            public void compelete(List data) {
            }
            @Override
            public void error(Throwable e) {
            }
        });
    }
    @Override
    public void onItemClick(LiveBean bean, int position) {
        ((MainActivity) mContext).watchLive(bean, Constants.LIVE_HOME, position);
    }

    @Override
    public void onFirstVisibiby() {
        super.onFirstVisibiby();
        refreshView.initData();
    }

    public abstract Observable<List<LiveBean>> requestData(int p);
}
