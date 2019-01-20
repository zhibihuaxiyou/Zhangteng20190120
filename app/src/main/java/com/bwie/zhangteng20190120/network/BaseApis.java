package com.bwie.zhangteng20190120.network;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * author：张腾
 * date：2019/1/20
 */
public interface BaseApis {

    @GET
    abstract Observable<ResponseBody> get(@Url String url);
}
