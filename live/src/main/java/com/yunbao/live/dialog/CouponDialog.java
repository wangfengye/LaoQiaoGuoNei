package com.yunbao.live.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.yunbao.common.adapter.CouponAdapter;
import com.yunbao.common.bean.CouponEntity;
import com.yunbao.common.bean.ShopResponse;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.expand.RefreshView;
import com.yunbao.common.http.ShopCallBack;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.R2;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CouponDialog extends AbsDialogFragment {

    @BindView(R2.id.imageView)
    ImageView imageView;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.btn_one_key)
    ImageView btnOneKey;
    Unbinder unbinder;

    private CouponAdapter myCouponAdapter;
    private List<CouponEntity>data;

    private boolean shouldReceiver=true;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_coupon;
    }
    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }
    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(350);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this, mRootView);
        myCouponAdapter=new CouponAdapter(data);
        recyclerView.setAdapter(myCouponAdapter);
        RefreshView.ReclyViewSetting reclyViewSetting=RefreshView.ReclyViewSetting.createLinearSetting(getContext());
        reclyViewSetting.settingRecyclerView(recyclerView);
        myCouponAdapter.setShouldReceiver(shouldReceiver);
    }

    public List<CouponEntity> getData() {
        return data;
    }
    public void setData(List<CouponEntity> data) {
        this.data = data;
    }


    @OnClick(R2.id.btn_dismiss)
    public void disMissDialog(){
        dismiss();
    }

    @OnClick(R2.id.btn_one_key)
    public void receiverAll(){
        if(!shouldReceiver){
            ToastUtil.show("主播不能领取");
            return;
        }


        myCouponAdapter.receiverAll(new ShopCallBack() {
            @Override
            public void success(ShopResponse shopResponse) {
                ToastUtil.show(shopResponse.getMsg());
                if(shopResponse.getStatus()==1){
                    dismiss();
                }
            }
        });
    }

    public boolean isShouldReceiver() {
        return shouldReceiver;
    }

    public void setShouldReceiver(boolean shouldReceiver) {
        this.shouldReceiver = shouldReceiver;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
