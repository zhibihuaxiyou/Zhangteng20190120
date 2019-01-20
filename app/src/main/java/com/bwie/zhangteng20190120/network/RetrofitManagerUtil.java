package com.bwie.zhangteng20190120.network;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.parse;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * author：张腾
 * date：2019/1/20
 * 工具类
 */
public class RetrofitManagerUtil<T> {


    private final String BASE_URL = "http://www.zhaoapi.cn/product/";

    private static RetrofitManagerUtil manager;

    //单例模式
    public static synchronized RetrofitManagerUtil getInstance() {
        if (manager == null) {
            manager = new RetrofitManagerUtil();
        }
        return manager;
    }

    //声明BaseApis
    private BaseApis mBaseApis;

    public RetrofitManagerUtil() {
        //日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BODY);
        //获取 builder对象
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(15, SECONDS);//写入超时
        builder.readTimeout(15, SECONDS);//读取超时
        builder.connectTimeout(15, SECONDS);//连接超时
        builder.addNetworkInterceptor(interceptor);//添加自定义拦截器
        builder.retryOnConnectionFailure(true);
        //获取 client 对象
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
        mBaseApis = retrofit.create(BaseApis.class);
    }


    /**
     * get请求
     *
     * @param url
     * @return
     */
    public RetrofitManagerUtil get(String url, HttpListener mHttpListener) {
        mBaseApis.get(url)
                .subscribeOn(io())//后台执行在哪个线程
                .observeOn(mainThread())//最终完成后执行在哪个线程
                .subscribe(getObserver(mHttpListener));//设置我们的    RxJava
        return manager;
    }

    /**
     * 创建观察者模式
     */

    private Observer getObserver(final HttpListener mHttpListener) {
        Observer mObserver = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (mHttpListener != null) {
                    mHttpListener.onFailed(e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();// TODO: 2018/12/27 注意： 这里是string(),不是toString()!!!
                    if (mHttpListener != null) {
                        mHttpListener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (mHttpListener != null) {
                        mHttpListener.onFailed(e.getMessage());
                    }
                }
            }

        };
        return mObserver;
    }

    //声明接口
    private HttpListener mHttpListener;

    //  set方法
    public void setHttpListener(HttpListener httpListener) {
        mHttpListener = httpListener;
    }

    //自定义接口
    public interface HttpListener {
        void onSuccess(String data);//成功

        void onFailed(String error);//失败
    }
}
