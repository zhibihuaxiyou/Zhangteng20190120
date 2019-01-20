package com.bwie.zhangteng20190120;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bwie.zhangteng20190120.adapter.GoodsAdapter;
import com.bwie.zhangteng20190120.bean.GoodsBean;
import com.bwie.zhangteng20190120.mvp.presenter.GoodsPreIml;
import com.bwie.zhangteng20190120.mvp.view.MyView;
import com.bwie.zhangteng20190120.network.Apis;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MyView {

    @BindView(R.id.btn_location)
    Button mBtnLocation;
    @BindView(R.id.recy_goods)
    RecyclerView mRecyGoods;
    private GoodsPreIml goodsPreIml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        goodsPreIml = new GoodsPreIml(this);
        goodsPreIml.show(Apis.GOODS_URL, GoodsBean.class);
    }
    //点击事件
    @OnClick(R.id.btn_location)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_location:
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
                break;
        }
    }

    //成功回调
    @Override
    public void onSuccess(Object data) {
        GoodsBean goodsBean = (GoodsBean) data;
        final List<GoodsBean.Data> dataList = goodsBean.getData();
        GoodsAdapter goodsAdapter = new GoodsAdapter(dataList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyGoods.setLayoutManager(manager);
        mRecyGoods.setAdapter(goodsAdapter);
        goodsAdapter.setOnGoodsClick(new GoodsAdapter.OnGoodsClick() {
            @Override
            public void onGoodsItem(int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("pid",position+"");
                startActivity(intent);
            }
        });
    }

    //失败回调
    @Override
    public void onFailer(String msg) {
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
