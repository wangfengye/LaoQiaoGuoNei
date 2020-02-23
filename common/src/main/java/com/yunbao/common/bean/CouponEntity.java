package com.yunbao.common.bean;

import com.yunbao.common.utils.DateFormatUtil;

import java.util.Date;

public class CouponEntity {

    private String Couponid;
    private String Image;
    private String Price;
    private String Title;
    private String Count;
    private String Leftcount;
    private long Endtime;
    private String Mobile;
    private String Name;
    private String  Location;
    private String  ControlPrice;

    private String Qr;

    private boolean isNew=true;


    public String getCouponid() {
        return Couponid;
    }

    public void setCouponid(String couponid) {
        Couponid = couponid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getLeftcount() {
        return Leftcount;
    }

    public void setLeftcount(String leftcount) {
        Leftcount = leftcount;
    }

    public long getEndtime() {
        return Endtime;
    }

    public void setEndtime(long endtime) {
        Endtime = endtime;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEndDate(){
        Date date=new Date(Endtime*1000);
        return DateFormatUtil.format(date);
    }

    public String getControlPrice() {
        return ControlPrice;
    }

    public void setControlPrice(String controlPrice) {
        ControlPrice = controlPrice;
    }

    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }


    public String getQr() {
        return Qr;
    }

    public void setQr(String qr) {
        Qr = qr;
    }
}
