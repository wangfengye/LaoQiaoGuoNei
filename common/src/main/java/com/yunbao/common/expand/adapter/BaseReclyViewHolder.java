package com.yunbao.common.expand.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunbao.common.glide.ImgLoader;

public  class BaseReclyViewHolder extends BaseViewHolder {

        public BaseReclyViewHolder(View view) {
            super(view);
        }
        public void setImageUrl(String url,int id){
            ImageView imageView=getView(id);
            if(imageView==null|| TextUtils.isEmpty(url)){
                return;
            }
            ImgLoader.display(url,imageView);
        }
    }