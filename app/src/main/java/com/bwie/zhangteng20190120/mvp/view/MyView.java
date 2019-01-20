package com.bwie.zhangteng20190120.mvp.view;

/**
 * author：张腾
 * date：2019/1/20
 */
public interface MyView<T> {
    void onSuccess(T data);
    void onFailer(String msg);
}
