package com.yunbao.common.expand;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunbao.common.R;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.server.observer.DefaultObserver;
import java.util.List;
import io.reactivex.Observable;

public class RefreshView<T> extends FrameLayout implements View.OnClickListener {
    public static  final int STATE_NO_ATA=1;
    public static  final int STATE_ERROR=2;
    public static  final int STATE_HAVE_DATA=3;


    private Context mContext;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout mNoData;//没有数据
    private View mLoadFailure;//加载失败
    private View mLoading;
    private int mLayoutRes;
    private boolean mShowNoData;//是否显示没有数据
    private boolean mEnableRefresh;
    private boolean mEnableLoadMore;
    private boolean mShowLoading;
    private int mPage;
    private boolean mScrollEnable = true;

    private DataListner<T> dataListner;
    private DataAdapter<T>dataAdapter;

    public RefreshView(Context context) {
        this(context, null);
    }
    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonRefreshView);
        mShowNoData = ta.getBoolean(R.styleable.RefreshView_showNoData, true);
        mShowLoading = ta.getBoolean(R.styleable.RefreshView_showLoading, true);
        mEnableRefresh = ta.getBoolean(R.styleable.RefreshView_enableRefresh, true);
        mEnableLoadMore = ta.getBoolean(R.styleable.RefreshView_enableLoadMore, true);
        mLayoutRes = ta.getResourceId(R.styleable.RefreshView_layout, R.layout.view_refresh_group);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayoutRes, this, false);
        addView(view);
        mNoData = (RelativeLayout) view.findViewById(R.id.no_data);
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout.setScorllView(mRecyclerView);
        mLoadFailure = view.findViewById(R.id.load_failure);
        mLoading = view.findViewById(R.id.loading);
        if (!mShowLoading) {
            mLoading.setVisibility(INVISIBLE);
        }
        mRefreshLayout.setRefreshEnable(mEnableRefresh);
        mRefreshLayout.setLoadMoreEnable(mEnableLoadMore);
        View btnReload = view.findViewById(R.id.btn_reload);
        if (btnReload != null) {
            btnReload.setOnClickListener(this);
        }
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void showNoData() {
        if (mNoData != null && mNoData.getVisibility() != VISIBLE) {
            mNoData.setVisibility(VISIBLE);
        }
    }
    private void showLoading(){

    }

    public void hideNoData() {
        if (mNoData != null && mNoData.getVisibility() == VISIBLE) {
            mNoData.setVisibility(INVISIBLE);
        }
    }

    public void hideLoadFailure() {
        if (mLoadFailure != null && mLoadFailure.getVisibility() == VISIBLE) {
            mLoadFailure.setVisibility(INVISIBLE);
        }
    }

    public void showLoadFailure() {
        if (mLoadFailure != null && mLoadFailure.getVisibility() == VISIBLE) {
            mLoadFailure.setVisibility(VISIBLE);
        }
    }

    public void initData() {
        refresh();
    }


    public <T> void setDataHelper(CommonRefreshView.DataHelper<T> list) {

    }

    private void refresh() {
        mPage=1;
       requestData(new DefaultObserver<List<T>>() {
           @Override
           public void onNext(List<T> data) {
               checkReclyAdapter();
               mScrollEnable=true;
               dataAdapter.setData(data);
               mRefreshLayout.completeRefresh();
               dataListner.compelete(data);
               stateJudge(STATE_HAVE_DATA);
           }
          @Override
          public void onError(Throwable e) {
              super.onError(e);
              mScrollEnable=false;
              dataListner.error(e);
              stateJudge(STATE_ERROR);
          }
      })
      ;
    }

    private void checkReclyAdapter(){
        if(mRecyclerView.getAdapter()==null){
            mRecyclerView.setAdapter(dataAdapter.returnRecyclerAdapter());
        }
    }

    private void loadMore() {
        mPage++;
        requestData(new DefaultObserver<List<T>>() {
            @Override
            public void onNext(List<T> data) {
                mScrollEnable=true;
                mRefreshLayout.completeLoadMore();
                checkReclyAdapter();
                if(data!=null&&data.size()>0){
                    dataAdapter.appendData(data);
                    dataListner.compelete(data);
                }else{
                    mPage--;
                }
                stateJudge(STATE_HAVE_DATA);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                stateJudge(STATE_ERROR);
                dataListner.error(e);
                mScrollEnable=true;
                mPage--;
            }
        })
        ;
    }
    private void requestData(DefaultObserver<List<T>>observer){
        mScrollEnable = false;
        if(dataListner!=null){
            dataListner.loadData(mPage).subscribe(observer);
        }
    }

    public int getPage() {
        return mPage;
    }
    public void setPage(int page) {
        mPage = page;
    }

    public void setRefreshEnable(boolean enable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshEnable(enable);
        }
    }


    public void setNoDataLayoutId(int view_no_data_live_follow) {
        setNoDataLayout(view_no_data_live_follow);
    }

    public void setLoadMoreEnable(boolean enable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setLoadMoreEnable(enable);
        }
    }

    public void requestData(){

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reload) {
            refresh();
        }
    }


    //无数据的时候的布局
    public Holder setNoDataLayout(int noDataLayoutId) {
       Holder holder=null;
        if (mShowNoData && mNoData != null) {
            View v = LayoutInflater.from(getContext()).inflate(noDataLayoutId, mNoData, false);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
            v.setLayoutParams(params);
            mNoData.addView(v);
            holder=new Holder(v);
        }
        return holder;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mScrollEnable && super.dispatchTouchEvent(ev);
    }

    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    public void setAdapter(DataAdapter<T> adapter){
        dataAdapter=(DataAdapter<T>) adapter;
        mRecyclerView.setAdapter(dataAdapter.returnRecyclerAdapter());
    }

    public void setDataListner(DataListner dataListner) {
        this.dataListner = dataListner;
    }

    public interface DataListner<T>{
        public Observable<List<T>> loadData(int p);
        public void compelete(List<T> data);
        public void error(Throwable e);
    }

    public void stateJudge(int state){
       if(state==STATE_HAVE_DATA||state==STATE_NO_ATA){
           List<T>array=dataAdapter.getArray();
          if(array!=null&&array.size()>0){
              stateChange(STATE_HAVE_DATA);
          }else{
              stateChange(STATE_NO_ATA);
          }
       }else{
           stateChange(state);
       }
        if(mLoading.getVisibility()==View.VISIBLE){
            mLoading.setVisibility(View.INVISIBLE);
        }
    }

    public void stateChange(int state){
        if(state==STATE_NO_ATA){
            showNoData();
            hideLoadFailure();
        }else if(state==STATE_ERROR){
            hideNoData();
            showLoadFailure();
        }else if(state==STATE_HAVE_DATA){
            hideNoData();
            hideLoadFailure();
        }
    }


    /*无数据布局的hol derchuli*/
    public static class Holder{
        private View rootView;
        public Holder(View view) {
            this.rootView=view;
        }
        public<T extends View> T findViewById(int id){
            return rootView.findViewById(id);
        }

        public Holder setText(int resId,int viewId){
            TextView textView=findViewById(viewId);
            if(textView!=null)textView.setText(resId);
            return this;
        }
        public Holder setText(String resId,int viewId){
            TextView textView=findViewById(viewId);
            if(textView!=null) textView.setText(resId);
            return this;
        }
        public Holder setImage(int resId,int viewId){
            ImageView imageView=findViewById(viewId);
            if(imageView!=null)imageView.setImageResource(resId);
            return this;
        }
        public Holder setOnClickLisnter(int viewId,OnClickListener onClickListener){
            View view=findViewById(viewId);
            if(view!=null)view.setOnClickListener(onClickListener);
            return this;
        }
    }

    public void defaultSetting(){
        setReclyViewSetting(ReclyViewSetting.createLinearSetting(mContext));
    }
    public void defaultGridSetting(int spanCount){
       setReclyViewSetting(ReclyViewSetting.createGridSetting(mContext,spanCount));
    }



    public interface DataAdapter<E>{
        public void setData(List<E> data);
        public void appendData(List<E> data);
        public List<E> getArray();
        public RecyclerView.Adapter returnRecyclerAdapter();
        public void notifyReclyDataChange();
    }


     public void setReclyViewSetting(ReclyViewSetting reclyViewSetting){
         mRecyclerView.setLayoutManager(reclyViewSetting.layoutManager);
         if(mRecyclerView.getItemDecorationCount()==0&&reclyViewSetting.itemDecoration!=null){
            mRecyclerView.addItemDecoration(reclyViewSetting.itemDecoration);
         }
         mRecyclerView.setHasFixedSize(reclyViewSetting.hasFixedSize);
     }

    public static class ReclyViewSetting{
        private RecyclerView.LayoutManager layoutManager;
        private ItemDecoration itemDecoration;
        private  boolean hasFixedSize;

        public ReclyViewSetting(RecyclerView.LayoutManager layoutManager, ItemDecoration itemDecoration, boolean hasFixedSize) {
            this.layoutManager = layoutManager;
            this.itemDecoration = itemDecoration;
            this.hasFixedSize=hasFixedSize;
        }

        public static ReclyViewSetting createLinearSetting(Context context){
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            ItemDecoration decoration = new ItemDecoration(context, 0xffdd00, 5, 5);
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(linearLayoutManager,decoration,true);
            return reclyViewSetting;
        }

        public static ReclyViewSetting createGridSetting(Context context,int spanCount){
            GridLayoutManager gridLayoutManager= new GridLayoutManager(context,spanCount);
            ItemDecoration decoration = new ItemDecoration(context, 0xffdd00, 5, 5);
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(gridLayoutManager,decoration,true);
            return reclyViewSetting;
        }

        public void settingRecyclerView(RecyclerView recyclerView){
            recyclerView.setLayoutManager(layoutManager);
            if(recyclerView.getItemDecorationCount()==0&&itemDecoration!=null){
                recyclerView.addItemDecoration(itemDecoration);
            }
            recyclerView.setHasFixedSize(hasFixedSize);
        }

    }

}
