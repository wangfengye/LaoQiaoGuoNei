package com.yunbao.common.adapter;

import android.view.View;
import com.yunbao.common.R;
import com.yunbao.common.bean.CouponEntity;
import com.yunbao.common.bean.ShopResponse;
import com.yunbao.common.databinding.ItemDialogCouponBinding;
import com.yunbao.common.expand.adapter.BaseBindingAdapter;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.ShopCallBack;
import com.yunbao.common.utils.ListUtil;
import com.yunbao.common.utils.ToastUtil;

import java.util.List;

public class CouponAdapter extends BaseBindingAdapter<ItemDialogCouponBinding, CouponEntity> {
   private boolean shouldReceiver;


    public CouponAdapter(List<CouponEntity> data) {
        super(data);
    }

    @Override
    protected void bindData(ItemDialogCouponBinding itemDialogCouponBinding, CouponEntity item) {
        itemDialogCouponBinding.setCoupon(item);
    }

    @Override
    protected void convert(final BaseBindingReclyViewHolder helper, final CouponEntity item) {
        super.convert(helper, item);
        if(!shouldReceiver){
            return;
        }
        helper.setOnClickListener(R.id.tv_state,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             final  int positon= helper.getLayoutPosition();
               if(!item.isNew()){
                   return;
               }
                CommonHttpUtil.receiveGetCoupon(new ShopCallBack() {
                    @Override
                    public void success(ShopResponse shopResponse) {
                        ToastUtil.show(shopResponse.getMsg());
                        if(shopResponse.getStatus()==1){
                            item.setNew(false);
                            notifyItemChanged(positon);
                        }
                    }
                }, item.getCouponid());
            }
        });
    }

    public void receiverAll(ShopCallBack shopCallBack){
        if(!ListUtil.haveData(mData)){
            return;
        }
        int size=mData.size();
        String[]couponIdArray=new String[size];
        for(int i=0;i<size;i++){
            couponIdArray[i]=mData.get(i).getCouponid();
        }
        CommonHttpUtil.receiveGetCoupon(shopCallBack,couponIdArray);
    }


    private void allSetReceiver() {
        if(!ListUtil.haveData(mData)){
            return;
        }

        for(CouponEntity couponEntity:mData){
            couponEntity.setNew(false);
        }
        notifyReclyDataChange();
    }

    public boolean isShouldReceiver() {
        return shouldReceiver;
    }

    public void setShouldReceiver(boolean shouldReceiver) {
        this.shouldReceiver = shouldReceiver;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dialog_coupon;
    }

}
