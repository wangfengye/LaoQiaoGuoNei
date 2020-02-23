package com.yunbao.common.server.observer;

import android.arch.lifecycle.MutableLiveData;

public class LiveDataObserver<T> extends DefaultObserver<T> {
   private MutableLiveData<T> liveData;

    public LiveDataObserver(MutableLiveData<T> liveData) {
        this.liveData = liveData;
    }
    public MutableLiveData<T> getLiveData() {
        return liveData;
    }
    @Override
    public void onNext(T t) {
        liveData.setValue(t);
    }
}
