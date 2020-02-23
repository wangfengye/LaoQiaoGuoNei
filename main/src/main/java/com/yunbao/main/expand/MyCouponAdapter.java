package com.yunbao.main.expand;

import com.yunbao.common.expand.adapter.BaseBindingAdapter;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.common.bean.CouponEntity;
import com.yunbao.main.databinding.ItemReclyCouponBinding;
import java.util.List;

public class MyCouponAdapter extends BaseBindingAdapter<ItemReclyCouponBinding,CouponEntity> {
    private int type;
    public MyCouponAdapter(List<CouponEntity> data) {
        super(data);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void bindData(ItemReclyCouponBinding itemReclyCouponBinding, CouponEntity item) {
        itemReclyCouponBinding.setCoupon(item);
        itemReclyCouponBinding.setAdaper(this);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_coupon;
    }

    public String getTypeString(){
        if(type==1){
            return "可使用";
        }else if(type==2){
            return "已使用";
        }else{
            return "已过期";
        }
    }

    public int getTypeColor(){
        if(type==1){
            return mContext.getResources().getColor(R.color.red_coupon);
        }else {
            return mContext.getResources().getColor(R.color.gray_coupon);
        }
    }

}
