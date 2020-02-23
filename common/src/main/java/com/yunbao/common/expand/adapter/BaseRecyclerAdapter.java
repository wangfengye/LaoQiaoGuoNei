package com.yunbao.common.expand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunbao.common.expand.RefreshView;

import java.util.List;

public abstract class BaseRecyclerAdapter<T,E extends BaseReclyViewHolder> extends BaseQuickAdapter<T,E> implements RefreshView.DataAdapter<T> {
    public Context context;
    public BaseRecyclerAdapter(List<T> data) {
        super(data);
        setLayoutId();
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        E e=super.onCreateViewHolder(parent, viewType);
        bindContext(parent.getContext());
        return e;
    }
    public void bindContext(Context context) {
    this.context=context;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }
    public void setLayoutId() {
        mLayoutResId = getLayoutId();
    }

    public abstract int getLayoutId();

    @Override
    public void appendData(List<T> data) {
        if(data!=null){
            addData(data);
        }else{
            setData(data);
        }
    }



    @Override
    public void notifyReclyDataChange() {
        notifyDataSetChanged();
    }
    @Override
    public List<T> getArray() {
        return mData;
    }

    @Override
    public RecyclerView.Adapter returnRecyclerAdapter() {
        return this;
    }


}
