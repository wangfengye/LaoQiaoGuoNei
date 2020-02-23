package com.yunbao.common.server.entity;

public class BaseRequest{
    public static final int APPEND=1;
    public static final int OVERLAP=2;

    public int type=OVERLAP;

    public  Object[] appendData;
    private BaseRequest(Object[] appendData) {
        this.appendData=appendData;
    }

    private BaseRequest(int type,Object[] appendData) {
        this.appendData=appendData;
        this.type=type;
    }

 public static BaseRequest creatRequest(Object...appendData){
        return new BaseRequest(appendData);
    }
 }
