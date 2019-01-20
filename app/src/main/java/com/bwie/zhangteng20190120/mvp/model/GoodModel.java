package com.bwie.zhangteng20190120.mvp.model;

import android.util.Log;

import com.bwie.zhangteng20190120.mvp.callback.MyCallBack;
import com.bwie.zhangteng20190120.network.Apis;
import com.bwie.zhangteng20190120.network.RetrofitManagerUtil;
import com.google.gson.Gson;

/**
 * author：张腾
 * date：2019/1/20
 */
public class GoodModel {
    public void show(int pid, final Class clazz, final MyCallBack myCallBack){
        RetrofitManagerUtil.getInstance().get(String.format(Apis.DETAILS_URL,pid), new RetrofitManagerUtil.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o = new Gson().fromJson(data, clazz);
                    if (myCallBack!=null) {
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e){
                    Log.d("sssss", "onSuccess: "+e);
                }
            }

            @Override
            public void onFailed(String error) {
                if (myCallBack!=null) {
                    myCallBack.onFailer(error);
                }
            }
        });
    }
}
