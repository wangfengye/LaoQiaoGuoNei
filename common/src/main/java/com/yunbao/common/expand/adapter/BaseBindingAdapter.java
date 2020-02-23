package com.yunbao.common.expand.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.yunbao.common.expand.RefreshView;
import java.util.List;

public abstract class BaseBindingAdapter <E extends ViewDataBinding,T> extends BaseRecyclerAdapter<T, BaseBindingAdapter.BaseBindingReclyViewHolder> implements RefreshView.DataAdapter<T> {

    public BaseBindingAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected void convert(BaseBindingAdapter.BaseBindingReclyViewHolder helper, T item) {
        E e= (E) helper.register();
        bindData(e,item);
        e.executePendingBindings();
    }
    protected abstract void bindData(E e,T item);


    public static class BaseBindingReclyViewHolder<T extends ViewDataBinding> extends BaseReclyViewHolder {
        private T dataBinding;
        public BaseBindingReclyViewHolder(View view) {
            super(view);
        }

        public <T extends ViewDataBinding >ViewDataBinding register(){
            if(dataBinding==null){
                dataBinding= DataBindingUtil.bind(itemView);
            }
            return dataBinding;
        }
        public ViewDataBinding getDataBinding() {
            return dataBinding;
        }
    }
    @Override
    public RecyclerView.Adapter returnRecyclerAdapter() {
        return this;
    }
    @Override
    public void notifyReclyDataChange() {
        notifyDataSetChanged();
    }
}
