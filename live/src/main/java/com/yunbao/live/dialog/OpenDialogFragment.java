package com.yunbao.live.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.server.observer.DefaultObserver;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.R;
import com.yunbao.live.R2;
import com.yunbao.live.activity.LiveAnchorActivity;
import com.yunbao.live.api.API;
import com.yunbao.live.socket.SocketClient;
import com.yunbao.live.socket.SocketSendBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OpenDialogFragment extends AbsDialogFragment {
    @BindView(R2.id.btn_open)
    public Button btnOpen;
    @BindView(R2.id.btn_close)
    public Button btnClose;
    Unbinder unbinder;
    private String stream;
    private SocketClient socketClient;

    private boolean isOpen;
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_open_goods;
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
            btnClose.setEnabled(false);
        }else{
            btnOpen.setEnabled(false);
            btnClose.setEnabled(true);
        }
    }

    @OnClick(R2.id.btn_open)
    public void open() {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).openShopLinkDialog();}
            dismiss();
    }

    @OnClick(R2.id.btn_close)
    public void close() {
        API.setShopstatus("0", stream).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    sendGoodsLink();
                    dismiss();
                }
            }
        });
    }

    private void sendGoodsLink() {
        socketClient.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SHOP)
                .param("isopen", 0)
                .param("iscoupon", 0)
        );
    }


    public void setStream(String stream) {
        this.stream = stream;
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
}
