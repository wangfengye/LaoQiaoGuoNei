package com.yunbao.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
  public  static <T> List<T> listData(T...data){
     List<T>array=new ArrayList<>();
     if(data!=null){
         for(T t:data){
             array.add(t);
         }
     }
     return array;
    }

    public  static boolean haveData(List data){
      return !(data==null||data.size()==0);
    }
}
