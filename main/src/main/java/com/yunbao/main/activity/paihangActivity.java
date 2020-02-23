package com.yunbao.main.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.yunbao.main.R;
import com.yunbao.main.views.MainListViewHolder;

/**
 * 单独的排行view
 */
public class paihangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paihang);
        FrameLayout layout=findViewById(R.id.frame);
        MainListViewHolder holder=new MainListViewHolder(this,layout);
        holder.addToParent();
        holder.subscribeActivityLifeCycle();
        holder.loadData();
    }
}
