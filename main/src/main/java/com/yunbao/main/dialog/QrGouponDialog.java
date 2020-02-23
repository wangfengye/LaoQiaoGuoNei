package com.yunbao.main.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.yunbao.common.bean.CouponEntity;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.main.R;
import com.yunbao.main.R2;
import com.yunbao.main.databinding.DialogEcodeCouponBinding;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class QrGouponDialog extends AbsDialogFragment {
    private CouponEntity couponEntity;
    private Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ecode_coupon;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }
    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(300);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this,mRootView);
        DialogEcodeCouponBinding binding= DataBindingUtil.bind(mRootView);
        binding.setCoupon(couponEntity);
        binding.executePendingBindings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R2.id.btn_dismiss)
    public void disMissDialog(){
        dismiss();
    }


    public void setCouponEntity(CouponEntity couponEntity) {
        this.couponEntity = couponEntity;
    }
}
