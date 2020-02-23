package com.yunbao.live.dialog;

import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.internal.LinkedTreeMap;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.server.observer.DefaultObserver;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.R;
import com.yunbao.live.R2;
import com.yunbao.live.api.API;
import com.yunbao.live.socket.SocketClient;
import com.yunbao.live.socket.SocketSendBean;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Function;

public class LiveSelectOpenCouponFragment extends AbsDialogFragment {
    @BindView(R2.id.btn_cancle)
    Button btnCancle;
    @BindView(R2.id.btn_open)
    Button btnOpen;

    Unbinder unbinder;
    private SocketClient socketClient;
    private boolean isOpen;

    private String streamId;


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_open_select_coupon;
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(200);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder= ButterKnife.bind(this, mRootView);
        if(!isOpen){
            btnOpen.setEnabled(true);
            btnCancle.setEnabled(false);
        }else{
            btnOpen.setEnabled(false);
            btnCancle.setEnabled(true);
        }
    }

    @OnClick(R2.id.btn_cancle)
    public void cancle(View view) {
        if(ClickUtil.canClick())
         request(0);
    }
    @OnClick(R2.id.btn_open)
    public void open(View view) {
        if(ClickUtil.canClick())
        request(1);
    }


    private void request(final int isOpen) {
        API.isOpenCoupon(streamId,isOpen).map(new Function<List<LinkedTreeMap>, Boolean>() {
            @Override
            public Boolean apply(List<LinkedTreeMap> maps) throws Exception {
                double rs = (double) maps.get(0).get("rs");
                return rs==1;
            }
        }).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    sendGoodsLink(isOpen);
                }
                dismiss();
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismiss();
            }
        });
    }

    private void sendGoodsLink(int isOpen) {
        socketClient.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SHOP)
                .param("isopen", isOpen)
                .param("iscoupon", 1)
                .param("link", "")
                .param("city", CommonAppConfig.getInstance().getCity())
                .param("imagelink", ""));
    }


    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}


