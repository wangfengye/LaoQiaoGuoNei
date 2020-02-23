package com.yunbao.common.bean;

import java.util.List;
public class ShopResponse {
    /**
     * Status : 1
     * Msg : OK
     * Count : 2
     * Coupons : [{"Couponid":"10000","Image":"http://www.baidu.com/img/baidu_jgylogo3.gif","Price":"100.00","Title":"清明节优惠券","Count":100,"Leftcount":5,"Endtime":"1554017068","Location":"辽宁省沈阳市沈河区星期六洗浴","Mobile":"13022432088","Name":"星期六洗浴"},{"Couponid":"10001","Image":"http://www.baidu.com/img/baidu_jgylogo3.gif","Price":"100.00","Title":"5.1劳动节优惠券","Count":200,"Leftcount":100,"Endtime":"1554017068","Location":"辽宁省沈阳市沈河区星期六洗浴","Mobile":"13022432088","Name":"星期六洗浴"}]
     */

    private int Status;
    private String Msg;
    private int Count;
    private List<CouponEntity> Coupons;


    private int type1;
    private int type2;
    private int type3;


    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public List<CouponEntity> getCoupons() {
        return Coupons;
    }

    public void setCoupons(List<CouponEntity> Coupons) {
        this.Coupons = Coupons;
    }

    public int getType1() {
        return type1;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

    public int getType3() {
        return type3;
    }

    public void setType3(int type3) {
        this.type3 = type3;
    }
}

