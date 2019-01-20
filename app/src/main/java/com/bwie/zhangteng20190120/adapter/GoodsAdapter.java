package com.bwie.zhangteng20190120.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwie.zhangteng20190120.R;
import com.bwie.zhangteng20190120.bean.GoodsBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * author：张腾
 * date：2019/1/20
 * 展示商品适配器
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private List<GoodsBean.Data> list = new ArrayList<>();
    private Context context;

    public GoodsAdapter(List<GoodsBean.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_goods_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodsAdapter.ViewHolder viewHolder, final int i) {
        String images = list.get(i).getImages();
        String[] split = images.split("\\|");
        viewHolder.mIconGoods.setImageURI(split[0]);
        viewHolder.mTvGoodsTitle.setText(list.get(i).getTitle());
        viewHolder.mTvGoodsPrice.setText("￥"+list.get(i).getPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnGoodsClick.onGoodsItem(list.get(i).getPid());
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeData(i);
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.itemView, "alpha", 1f, 0f);
                animator.setDuration(3000);
                animator.start();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 删除数据
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvGoodsTitle,mTvGoodsPrice;
        SimpleDraweeView mIconGoods;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIconGoods = itemView.findViewById(R.id.icon_goods);
            mTvGoodsTitle = itemView.findViewById(R.id.tv_goods_title);
            mTvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);
        }
    }

    //自定义接口回调
    OnGoodsClick mOnGoodsClick;

    public void setOnGoodsClick(OnGoodsClick mOnGoodsClick){
        this.mOnGoodsClick = mOnGoodsClick;
    }
    public interface OnGoodsClick{
        void onGoodsItem(int position);
    }

    //自定义接口回调
    OnGoodsLongClick mOnGoodsLongClick;

    public void setOnGoodsClick(OnGoodsLongClick mOnGoodsLongClick){
        this.mOnGoodsLongClick = mOnGoodsLongClick;
    }
    public interface OnGoodsLongClick{
        void onGoodsLongItem(int position);
    }
}
