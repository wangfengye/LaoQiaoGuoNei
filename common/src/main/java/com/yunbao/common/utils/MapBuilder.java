package com.yunbao.common.utils;

import android.util.ArrayMap;

import java.util.Map;

public class MapBuilder {
private Map<String,Object>map;
public MapBuilder(){
    map=new ArrayMap<>();
}

 public MapBuilder put(String key,Object value){
    map.put(key,value);
    return this;
 }

 public Map<String,Object> build(){
    return map;
 }

 public static MapBuilder factory(){
    return new MapBuilder();
 }

}
