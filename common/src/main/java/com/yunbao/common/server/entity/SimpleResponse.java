package com.yunbao.common.server.entity;

import java.util.List;

public class SimpleResponse {
    private int ret;
    private String msg;
    private Data data;

    public BaseResponse toBaseResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setRet(ret);
        baseResponse.setMsg(msg);
        baseResponse.setData(data);
        return baseResponse;
    }

    public BaseResponse toBaseResponse(List list) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setRet(ret);
        baseResponse.setMsg(msg);
        baseResponse.setData(data);
        data.setInfo(list);
        return baseResponse;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
