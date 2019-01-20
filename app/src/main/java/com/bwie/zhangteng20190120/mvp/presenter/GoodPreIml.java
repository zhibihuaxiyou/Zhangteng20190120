package com.bwie.zhangteng20190120.mvp.presenter;

import com.bwie.zhangteng20190120.mvp.callback.MyCallBack;
import com.bwie.zhangteng20190120.mvp.model.GoodModel;
import com.bwie.zhangteng20190120.mvp.model.GoodsModel;
import com.bwie.zhangteng20190120.mvp.view.MyView;

/**
 * author：张腾
 * date：2019/1/20
 */
public class GoodPreIml {
    private MyView myView;
    private GoodModel goodModel;
    public GoodPreIml(MyView myView){
        this.myView = myView;
        goodModel = new GoodModel();
    }

    public void deach(){
        this.myView = null;
        this.goodModel = null;
    }

    public void show(int pid,Class clazz){
        goodModel.show(pid, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                myView.onSuccess(data);
            }

            @Override
            public void onFailer(String msg) {
                myView.onFailer(msg);
            }
        });
    }
}
