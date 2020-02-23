package com.yunbao.common.http;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.yunbao.common.bean.ShopResponse;

public abstract class ShopCallBack extends AbsCallback<ShopResponse> {

    @Override
    public void onSuccess(Response<ShopResponse> response) {
        ShopResponse shopResponse=response.body();
        success(shopResponse);
    }
    public abstract void success(ShopResponse shopResponse);

    @Override
    public ShopResponse convertResponse(okhttp3.Response response) throws Throwable {
        return JSON.parseObject(response.body().string(), ShopResponse.class);
    }
}
