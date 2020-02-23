package com.yunbao.common.utils;


import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.yunbao.common.glide.ImgLoader;

public class BindAdapter {
    @BindingAdapter("imageUrl")
    public static void src(ImageView view, String resId){
        ImgLoader.display(resId,view);
    }


    @BindingAdapter("rating")
    public static void ratting(RatingBar ratingBar, float rating ){
        if(rating>0){
            ratingBar.setRating(rating);
        }
    }


}
