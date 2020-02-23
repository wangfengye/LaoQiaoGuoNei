package com.yunbao.common.server;

import com.lzy.okgo.model.HttpParams;
import com.yunbao.common.http.CommonHttpUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;

public class OkGoRequestMannger implements IRequestManager {
    private static OkGoRequestMannger sOkGoRequestMannger;
    private OkGoRequestMannger() {

    }
    @Override
    public <T> Observable<List<T>> get(String url, Map<String, Object> map, Class<T> cs) {
        return CommonHttpUtil.get(url,parse(map),cs);
    }

    @Override
    public <T> Observable<List<T>> post(String url, Map<String, Object> map, Class<T> cs) {
        return CommonHttpUtil.post(url,parse(map),cs);
    }


    public Observable<Boolean> noReturnPost(String url, boolean showToast,Map<String, Object> map) {
        return CommonHttpUtil.postNoReturn(url,showToast,parse(map));
    }

    public Observable<Boolean> noReturnPost(String url, Map<String, Object> map) {
        return CommonHttpUtil.postNoReturn(url,true,parse(map));
    }

    @Override
    public void cancle(String tag) {
        CommonHttpUtil.cancel(tag);
    }

    public static OkGoRequestMannger getInstance(){
        if(sOkGoRequestMannger==null){
            synchronized (OkGoRequestMannger.class){
                sOkGoRequestMannger=new OkGoRequestMannger();
            }
        }
        return sOkGoRequestMannger;
    }

    public static HttpParams parse(Map<String,Object>map){
        if(map==null){
            return null;
        }
        HttpParams httpParams=new HttpParams();
        Set<String>set= map.keySet();
       Iterator<String>iterator= set.iterator();
       while (iterator.hasNext()){
        String key=iterator.next();
        Object value=map.get(key);

        if(value==null){
            continue;
        }

        if(value instanceof Boolean){
            httpParams.put(key,(Boolean)value);
        }else if(value instanceof Integer){
            httpParams.put(key,(Integer)value);
        }else if(value instanceof String){
            httpParams.put(key,(String)value);
        }else if(value instanceof Double){
            httpParams.put(key,(Double)value);
        }else if(value instanceof Float){
            httpParams.put(key,(Float)value);
        }else  if(value instanceof File){
            httpParams.put(key,(File)value);
        }else if(value instanceof Long){
            httpParams.put(key,(Long)value);
        }
        else if(value instanceof Character){
            httpParams.put(key,(Character)value);
        }
       }
        return httpParams;
    }

}
