package com.yunbao.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.R;
import com.yunbao.common.activity.ErrorActivity;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.ShopResponse;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.server.entity.BaseResponse;
import com.yunbao.common.server.generic.ParameterizedTypeImpl;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.RxUtils;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by cxf on 2018/9/17.
 */

public class CommonHttpUtil {


    /**
     * 初始化
     */
    public static void init() {
        HttpClient.getInstance().init();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

    /**
     * 使用腾讯定位sdk获取 位置信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @param poi 是否要查询POI
     */
    public static void getAddressInfoByTxLocaitonSdk(final double lng, final double lat, final int poi, int pageIndex, String tag, final HttpCallback commonCallback) {
        String txMapAppKey = CommonAppConfig.getInstance().getTxMapAppKey();
        String s = "/ws/geocoder/v1/?get_poi=" + poi + "&key=" + txMapAppKey + "&location=" + lat + "," + lng
                + "&poi_options=address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5" + CommonAppConfig.getInstance().getTxMapAppSecret();
        String sign = MD5Util.getMD5(s);
        OkGo.<String>get("https://apis.map.qq.com/ws/geocoder/v1/")
                .params("location", lat + "," + lng)
                .params("get_poi", poi)
                .params("poi_options", "address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5")
                .params("key", txMapAppKey)
                .params("sig", sign)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj != null && commonCallback != null) {
                            commonCallback.onSuccess(obj.getIntValue("status"), "", new String[]{obj.getString("result")});
                        }
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (commonCallback != null) {
                            commonCallback.onError();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (commonCallback != null) {
                            commonCallback.onFinish();
                        }
                    }
                });
    }

    /**
     * 使用腾讯地图API进行搜索
     *
     * @param lng 经度
     * @param lat 纬度
     */
    public static void searchAddressInfoByTxLocaitonSdk(final double lng, final double lat, String keyword, int pageIndex, final HttpCallback commonCallback) {

        String txMapAppKey = CommonAppConfig.getInstance().getTxMapAppKey();
        String s = "/ws/place/v1/search?boundary=nearby(" + lat + "," + lng + ",1000)&key=" + txMapAppKey + "&keyword=" + keyword + "&orderby=_distance&page_index=" + pageIndex +
                "&page_size=20" + CommonAppConfig.getInstance().getTxMapAppSecret();
        String sign = MD5Util.getMD5(s);
        OkGo.<String>get("https://apis.map.qq.com/ws/place/v1/search")
                .params("keyword", keyword)
                .params("boundary", "nearby(" + lat + "," + lng + ",1000)&orderby=_distance&page_size=20&page_index=" + pageIndex)
                .params("key", txMapAppKey)
                .params("sig", sign)
                .tag(CommonHttpConsts.GET_MAP_SEARCH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj != null && commonCallback != null) {
                            commonCallback.onSuccess(obj.getIntValue("status"), "", new String[]{obj.getString("data")});
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (commonCallback != null) {
                            commonCallback.onError();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (commonCallback != null) {
                            commonCallback.onFinish();
                        }
                    }
                });
    }


    /**
     * 获取config
     */
    public static void getConfig(final CommonCallback<ConfigBean> commonCallback) {
        HttpClient.getInstance().get("Home.getConfig", CommonHttpConsts.GET_CONFIG)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            try {
                                JSONObject obj = JSON.parseObject(info[0]);
                                ConfigBean bean = JSON.toJavaObject(obj, ConfigBean.class);
                                CommonAppConfig.getInstance().setConfig(bean);
                                CommonAppConfig.getInstance().setLevel(obj.getString("level"));
                                CommonAppConfig.getInstance().setAnchorLevel(obj.getString("levelanchor"));
                                SpUtil.getInstance().setStringValue(SpUtil.CONFIG, info[0]);
                                if (commonCallback != null) {
                                    commonCallback.callback(bean);
                                }
                            } catch (Exception e) {
                                String error = "info[0]:" + info[0] + "\n\n\n" + "Exception:" + e.getClass() + "---message--->" + e.getMessage();
                                ErrorActivity.forward("GetConfig接口返回数据异常", error);
                            }
                        }
                    }


                    @Override
                    public void onError() {
                        if (commonCallback != null) {
                            commonCallback.callback(null);
                        }
                    }
                });
    }


    /**
     * QQ登录的时候 获取unionID 与PC端互通的时候用
     */
    public static void getQQLoginUnionID(String accessToken, final CommonCallback<String> commonCallback) {
        OkGo.<String>get("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken + "&unionid=1")
                .tag(CommonHttpConsts.GET_QQ_LOGIN_UNION_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (commonCallback != null) {
                            String data = response.body();
                            data = data.substring(data.indexOf("{"), data.lastIndexOf("}") + 1);
                            L.e("getQQLoginUnionID------>" + data);
                            JSONObject obj = JSON.parseObject(data);
                            commonCallback.callback(obj.getString("unionid"));
                        }
                    }
                });
    }


    /**
     * 关注别人 或 取消对别人的关注的接口
     */
    public static void setAttention(String touid, CommonCallback<Integer> callback) {
        setAttention(CommonHttpConsts.SET_ATTENTION, touid, callback);
    }

    /**
     * 关注别人 或 取消对别人的关注的接口
     */
    public static void setAttention(String tag, final String touid, final CommonCallback<Integer> callback) {
        if (touid.equals(CommonAppConfig.getInstance().getUid())) {
            ToastUtil.show(WordUtil.getString(R.string.cannot_follow_self));
            return;
        }
        HttpClient.getInstance().get("User.setAttent", tag)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            int isAttention = JSON.parseObject(info[0]).getIntValue("isattent");//1是 关注  0是未关注
                            EventBus.getDefault().post(new FollowEvent(touid, isAttention));
                            if (callback != null) {
                                callback.callback(isAttention);
                            }
                        }
                    }
                });
    }


    /**
     * 用支付宝充值 的时候在服务端生成订单号
     *
     * @param money    RMB价格
     * @param changeid 要购买的钻石的id
     * @param coin     要购买的钻石的数量
     * @param callback
     */
    public static void getAliOrder(String money, String changeid, String coin, HttpCallback callback) {
        HttpClient.getInstance().get("Charge.getAliOrder", CommonHttpConsts.GET_ALI_ORDER)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("money", money)
                .params("changeid", changeid)
                .params("coin", coin)
                .execute(callback);
    }

    /**
     * 用微信支付充值 的时候在服务端生成订单号
     *
     * @param money    RMB价格
     * @param changeid 要购买的钻石的id
     * @param coin     要购买的钻石的数量
     * @param callback
     */
    public static void getWxOrder(String money, String changeid, String coin, HttpCallback callback) {
        HttpClient.getInstance().get("Charge.getWxOrder", CommonHttpConsts.GET_WX_ORDER)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("money", money)
                .params("changeid", changeid)
                .params("coin", coin)
                .execute(callback);
    }

    //不做任何操作的HttpCallback
    public static final HttpCallback NO_CALLBACK = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {

        }
    };


    public  static <T> io.reactivex.Observable<List<T>> request(HttpMethod httpMethod, String url, HttpParams params, final Class<T>cs){
        Type type = new ParameterizedTypeImpl(BaseResponse.class, new Class[]{cs});
        // 根据List<T>生成完整的Result<List<T>>
        // Type type = new ParameterizedTypeImpl(BaseResponse.class, new Type[]{listType});
        Observable<BaseResponse<T>> observable=RxUtils.request(httpMethod, url, type,params);
        return observable.map(new Function<BaseResponse<T>, List<T>>() {
            @Override
            public List<T> apply(BaseResponse<T> response) throws Exception {
                try {
                    ToastUtil.show(response.getData().getMsg());

                }catch (Exception e){
                    e.printStackTrace();
                }
                return response.getData().getInfo();
            }
        });
    }

    public  static <T> io.reactivex.Observable<List<T>> get(String url, HttpParams params,Class<T>cs){
        return request(HttpMethod.GET,url,params,cs);
    }

    public  static <T> io.reactivex.Observable<List<T>> post(String url, HttpParams params,Class<T>cs){
        return request(HttpMethod.POST,url,params,cs);
    }

    public  static <T> io.reactivex.Observable<Boolean> postNoReturn(String url,final boolean showToast,HttpParams params){
        Type type = new ParameterizedTypeImpl(BaseResponse.class, new Class[]{Object.class});
        Observable<BaseResponse<T>>observable= RxUtils.request(HttpMethod.POST, url, type,params);
        return   observable.map(new Function<BaseResponse<T>, Boolean>() {
            @Override
            public Boolean apply(BaseResponse<T> baseResponse) throws Exception {
                if(showToast){
                    ToastUtil.show(baseResponse.getData().getMsg());
                }

                return baseResponse.getData().getCode()==0;
            }
        });
    }

    public static void getNeedGetCoupon(String Page,String Location,String Viewid,ShopCallBack shopCallBack){
        GetRequest<ShopResponse> request=  OkGo.<ShopResponse>get("http://cp.dslyf.com/api/index/index")
                .headers("Connection","keep-alive")
                .tag("getNeedGetCoupon")
                .params("Page",Page);
                if(Location!=null){
                    request.params("Location",Location);
                }
        if(Location!=null){
            request.params("Viewid",Viewid);
        }
        request.execute(shopCallBack);
    }

    public static void receiveGetCoupon(ShopCallBack shopCallBack,String...couponid){
        StringBuilder builder=new StringBuilder();
        int size=couponid.length;
        for(int i=0;i<size;i++){
            builder.append(couponid[i]);
            if(i!=size-1){
              builder.append(",");
            }
        }

        PostRequest<ShopResponse> request= OkGo.<ShopResponse>post("http://cp.dslyf.com/api/index/token")
                .headers("Connection","keep-alive")
                .tag("receiveGetCoupon")
                .params("Tokenid",System.currentTimeMillis()+"")
                .params("Openid",CommonAppConfig.getInstance().getOpenId())
                .params("Couponid",builder.toString())
                .params("AppKey","134902658994")
                ;

        builder=new StringBuilder();
        builder .append("AppKey=").append(request.getUrlParam("AppKey"))
        .append("&Couponid=").append(request.getUrlParam("Couponid"))
                .append("&Openid=")
                .append(request.getUrlParam("Openid"))
        .append("&Tokenid=").append(request.getUrlParam("Tokenid"));


        String data=  builder.toString();
        data+="&key=YnnbbywhLpOeG6QeX518VV1FbZs4D5LT";
        data= MD5Util.getMD5(data);
        data=data.toUpperCase();
        request.params("Sign",data);
        request.execute(shopCallBack);
    }


    public static void getUserCoupon(int type,ShopCallBack shopCallBack){
        GetRequest<ShopResponse> request=  OkGo.<ShopResponse>get("http://cp.dslyf.com/api/index/user")
                .headers("Connection","keep-alive")
                .tag("getUserCoupon")
                .params("Openid",CommonAppConfig.getInstance().getOpenId())
                .params("Type",type);

        request.execute(shopCallBack);
    }



}




