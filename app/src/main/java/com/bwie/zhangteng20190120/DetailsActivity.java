package com.bwie.zhangteng20190120;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangteng20190120.adapter.MyPagerAdapter;
import com.bwie.zhangteng20190120.bean.GoodBean;
import com.bwie.zhangteng20190120.mvp.presenter.GoodPreIml;
import com.bwie.zhangteng20190120.mvp.view.MyView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity implements MyView {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.btn_add_shop)
    Button mBtnAddShop;
    @BindView(R.id.icon_share)
    ImageView mIconShare;
    @BindView(R.id.icon_touxiang)
    ImageView mIconTou;
    @BindView(R.id.relative)
    RelativeLayout mRelative;
    private GoodPreIml goodPreIml;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                int item = mViewPager.getCurrentItem();
                item++;
                mViewPager.setCurrentItem(item);
                handler.sendEmptyMessageDelayed(0, 1500);
            }
        }
    };
    private GoodBean.Data beanData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        //接收传值
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("pid");
        int pid = Integer.parseInt(stringExtra);
        goodPreIml = new GoodPreIml(this);
        goodPreIml.show(pid, GoodBean.class);

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
    }



    @Override
    public void onSuccess(Object data) {
        GoodBean goodBean = (GoodBean) data;
        beanData = goodBean.getData();
        String images = beanData.getImages();
        String[] split = images.split("\\|");
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            imageList.add(split[i]);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(imageList, this);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setCurrentItem(imageList.size() * 1000);
        mTvTitle.setText(beanData.getTitle());
        mTvPrice.setText("￥ " + beanData.getPrice());
    }

    @Override
    public void onFailer(String msg) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.btn_add_shop, R.id.icon_share})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_add_shop:
                Toast.makeText(DetailsActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
                mRelative.setVisibility(View.VISIBLE);
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        String iconurl = map.get("iconurl");
                        mIconTou.setImageURI(Uri.parse(iconurl));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
                break;
            case R.id.icon_share:
                UMWeb  web = new UMWeb(beanData.getDetailUrl());
                web.setTitle("这是一个商品链接");//标题
                web.setDescription("my description");//描述

                new ShareAction(this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .setCallback(umShareListener)//回调监听器
                        .open();
                break;
        }
    }

    private UMShareListener  umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(DetailsActivity.this,"分享成功了",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(DetailsActivity.this,"失败"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(DetailsActivity.this,"取消分享",Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }
}
