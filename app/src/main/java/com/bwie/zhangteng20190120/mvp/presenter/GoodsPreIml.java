package com.bwie.zhangteng20190120.mvp.presenter;

import com.bwie.zhangteng20190120.mvp.callback.MyCallBack;
import com.bwie.zhangteng20190120.mvp.model.GoodsModel;
import com.bwie.zhangteng20190120.mvp.view.MyView;

/**
 * author：张腾
 * date：2019/1/20
 */
public class GoodsPreIml {
    private MyView myView;
    private GoodsModel goodsModel;
    public GoodsPreIml(MyView myView){
        this.myView = myView;
        goodsModel = new GoodsModel();
    }

    public void deach(){
        this.myView = null;
        this.goodsModel = null;
    }

    public void show(String url,Class clazz){
        goodsModel.show(url, clazz, new MyCallBack() {
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
