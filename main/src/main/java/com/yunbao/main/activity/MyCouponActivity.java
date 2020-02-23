package com.yunbao.main.activity;

import android.widget.CompoundButton;
import android.widget.RadioButton;
import com.yunbao.common.expand.BaseActivity;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import com.yunbao.common.expand.viewpager.ViewLifePagerAdapter;
import com.yunbao.common.expand.viewpager.ViewLifeViewPager;
import com.yunbao.main.R;
import com.yunbao.main.R2;
import com.yunbao.main.expand.MyCouponViewHolder;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class MyCouponActivity extends BaseActivity {

    @BindView(R2.id.rb_wait_use)
    RadioButton rbWaitUse;
    @BindView(R2.id.rb_has_use)
    RadioButton rbHasUse;
    @BindView(R2.id.rb_overdue)
    RadioButton rbOverdue;
    @BindView(R2.id.viewPager)
    ViewLifeViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_coupon;
    }
    @Override
    public void init() {
        super.init();
        setTabTitle("优惠券中心");
        MyCouponViewHolder waitUserviewHolder=new MyCouponViewHolder(this,viewPager);
        waitUserviewHolder.setType(1);




        waitUserviewHolder.setCouponLisnter(new MyCouponViewHolder.CouponLisnter() {
            @Override
            public void coupon(int size1, int size2, int size3) {
                rbWaitUse.setText("待使用("+size1+")");
                rbHasUse.setText("已使用("+size2+")");
                rbOverdue.setText("已过期("+size3+")");
            }
        });


        MyCouponViewHolder usedViewHolder=new MyCouponViewHolder(this,viewPager);
        usedViewHolder.setType(2);



        MyCouponViewHolder outViewHolder=new MyCouponViewHolder(this,viewPager);
        outViewHolder.setType(3);




        List<ButterKnifeAbsViewHolder>viewArray=new ArrayList<>();
        viewArray.add(waitUserviewHolder);
        viewArray.add(usedViewHolder);
        viewArray.add(outViewHolder);

        ViewLifePagerAdapter adapter=new ViewLifePagerAdapter(viewArray);
        viewPager.setAdapter(adapter);
        viewPager.setScroll(false);
    }


    @OnCheckedChanged({R2.id.rb_wait_use,R2.id.rb_has_use,R2.id.rb_overdue})
    public void selectPage(CompoundButton compoundButton, boolean isChecked){
        try {
            if(isChecked){
                String tag= (String) compoundButton.getTag();
                if(tag!=null){
                    viewPager.setCurrentItem(Integer.parseInt(tag));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
