package com.yunbao.live.dialog;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.server.observer.DefaultObserver;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ProcessImageUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.R2;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.activity.LiveAnchorActivity;
import com.yunbao.live.api.API;
import com.yunbao.live.bean.LiveLinkEntity;
import com.yunbao.live.socket.SocketClient;
import com.yunbao.live.socket.SocketSendBean;
import org.json.JSONObject;
import java.io.File;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Function;

public class LivePublishGoodsDIalogFragment extends AbsDialogFragment {
    @BindView(R2.id.img_add)
    ImageView imgAdd;

    @BindView(R2.id.et_input_link)
    TextView et_input_link;

    Unbinder unbinder;

    private ProcessImageUtil mImageUtil;
    private UploadManager mUploadManager;

    private LiveLinkEntity linkEntity;

    private SocketClient socketClient;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_publish_goods;
    }

    public LivePublishGoodsDIalogFragment(){
        super();
        linkEntity=new LiveLinkEntity();
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
        params.width = DpUtil.dp2px(300);
        params.height = DpUtil.dp2px(320);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this, mRootView);
        mImageUtil = new ProcessImageUtil(getActivity());
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {
            }
            @Override
            public void onSuccess(File file) {
                linkEntity.setFile(file);
                linkEntity.setFilelink(null);
                ImgLoader.display(file,imgAdd);
            }
            @Override
            public void onFailure() {
            }
        });

        if(linkEntity.getFilelink()!=null){
            setData();
        }
    }

    private void setData() {
        ImgLoader.display(linkEntity.getFilelink(),imgAdd);
        et_input_link.setText(linkEntity.getLink());
    }

    @OnClick(R2.id.img_add)
    public void selectImgage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }


    @OnClick(R2.id.btn_delete)
    public void disMissDIalog() {
        dismiss();
    }




    UpCompletionHandler handler=  new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            String host= CommonAppConfig.getInstance().getConfig().getVideoQiNiuHost();
            linkEntity.setHost(host);
            uploadLink();

        }
    };

    @OnClick(R2.id.btn_commit)
    public void commit(final View view){
        if(!ClickUtil.canClick()){
            return;
        }

        String url=et_input_link.getText().toString();


        linkEntity.setLink(et_input_link.getText().toString());
        if(!url.startsWith("http")){
            ToastUtil.show("请填写带有http格式的商品链接");
            return;
        }

        String fileLink=linkEntity.getFilelink();
        if(!TextUtils.isEmpty(fileLink)){
            uploadLink();
            return;
        }



        final File file=linkEntity.getFile();
        if(file==null){
            ToastUtil.show("请添加商品图标");
            return;
        }



        API.getToken().map(new Function<List<LinkedTreeMap>, String>() {
                @Override
                public String apply(List<LinkedTreeMap> linkedTreeMaps) throws Exception {
                    return (String) linkedTreeMaps.get(0).get("token");
                }
          }).subscribe(new DefaultObserver<String>() {
                @Override
               public void onNext(String token) {
                    if (mUploadManager == null) {
                        mUploadManager = new UploadManager();}
                        mUploadManager.put(file, file.getName(), token, handler, null);
                }
             }
            );
        }

    private void uploadLink() {
        API.openGoodsWeb(linkEntity.getLiveuid(),linkEntity.getLink(),linkEntity.getStream(),linkEntity.getFilelink()).subscribe(new DefaultObserver<List<LinkedTreeMap>>() {
            @Override
            public void onNext(List<LinkedTreeMap> map) {
               String shopId=  map.get(0).get("rs").toString();
                ((LiveAnchorActivity)mContext).setLiveLinkEntity(linkEntity);
                    sendGoodsLink(shopId);
                    dismiss();
            }
        });
    }


    private void sendGoodsLink(String shopId) {
       socketClient.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SHOP)
                .param("isopen", 1)
                .param("iscoupon",0)
                .param("link",linkEntity.getLink())
                .param("shopid",shopId)
               .param("imagelink",linkEntity.getFilelink()));
    }


    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }



    public void setMessage(String liveuid, String stream ){
    linkEntity.setLiveuid(liveuid);
    linkEntity.setStream(stream);
}

    public void setLinkEntity(LiveLinkEntity linkEntity) {
        if(linkEntity!=null)
        this.linkEntity = linkEntity;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(linkEntity!=null){
            linkEntity.setFile(null);
        }
    }
}
