package com.yunbao.common.expand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.views.AbsViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public abstract class ButterKnifeAbsViewHolder extends AbsViewHolder {
    private boolean isFirst=true;

    private List<ButterKnifeAbsViewHolder>childArray;
    private Intent intent;


    public ButterKnifeAbsViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
        ButterKnife.bind(this,mContentView);
        try {
            if(context!=null&&context instanceof Activity){
                Activity absActivity= (Activity)context;
                intent= absActivity.getIntent();
            }
            initView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void initView(){

    }
    @Override
    public void addToParent() {
        super.addToParent();
    }
    public void addToParent(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder) {
        if(butterKnifeAbsViewHolder!=null){
            butterKnifeAbsViewHolder.addViewHolder(this);
        }
        super.addToParent();

    }


    @Deprecated
    @Override
    public void init() {
    }
    public void viewVisibily(boolean isVisibily){
        if(isVisibily&&isFirst){
            isFirst=false;
            onFirstVisibiby();
        }
    }
    public void addViewHolder(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder){
        if(childArray==null){
            childArray=new ArrayList<>();
        }
        childArray.add(butterKnifeAbsViewHolder);
    }
    public void addViewHolderArray(List<ButterKnifeAbsViewHolder> childArray){
        this.childArray=childArray;
    }

    public void  onFirstVisibiby(){

    }
    public void onCreate(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onCreate();
            }
        }
    }

    public void onStart(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onStart();
            }
        }
    }

    public void onPause(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onPause();
            }
        }
    }

    public void onResume(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onResume();
            }
        }
    }
    public void onStop(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onStop();
            }
        }
    }


    public void onDestroy(){
        if(childArray!=null){
            for(ButterKnifeAbsViewHolder butterKnifeAbsViewHolder:childArray){
                butterKnifeAbsViewHolder.onDestroy();
            }
        }
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void initData(){

    }
}
