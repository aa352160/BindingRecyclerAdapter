package com.example.administrator.bindingrecycleradapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public abstract class BindingRecyclerAdapter<T extends ViewDataBinding, M> extends RecyclerView.Adapter{
    private final int TYPE_HEADER = 1;
    private final int TYPE_FOOTER = 2;

    protected Context mContext;
    protected List<M> datas;
    protected OnItemClickListener<T,M> onItemClickListener;

    @LayoutRes private int resId;
    private View footerView;
    private View headerView;

    /**
     * 空数据构造方法
     */
    public BindingRecyclerAdapter(Context mContext) {
        this(mContext,new ArrayList<M>());
    }

    public BindingRecyclerAdapter(Context mContext,List<M> datas) {
        this.mContext=mContext;
        this.datas = datas;
        this.resId = setLayoutResId();
    }

    protected abstract @LayoutRes int setLayoutResId();

    @Override
    public int getItemViewType(int position) {
        if (headerView!=null&&(position==0)){
            return TYPE_HEADER;
        }
        if (footerView!=null && (position == getItemCount()-1)){
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER){
            return new HeaderAndFooterViewHolder(footerView);
        }
        if (viewType == TYPE_HEADER){
            return new HeaderAndFooterViewHolder(headerView);
        }
        return new BaseRecyclerViewHolder(parent, resId);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //头布局与尾布局不走接下来的方法
        if (footerView!=null && (position == getItemCount()-1)){
            return;
        }
        if (headerView!=null){
            if (position==0) {
                return;
            }else{
                //若存在头布局，position应该除去头布局，应为实际position-1
                position--;
            }
        }

        final BaseRecyclerViewHolder baseHolder = (BaseRecyclerViewHolder) holder;
        onBindViewHolder(baseHolder.binding,datas.get(position), holder, position);

        if (onItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(baseHolder.binding,datas.get(
                            holder.getAdapterPosition()),holder.getAdapterPosition());
                }
            });
        }
    }

    public abstract void onBindViewHolder(T binding,M itemData, RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        int itemCount = datas.size();
        if (footerView!=null) itemCount++;
        if (headerView!=null) itemCount++;

        return itemCount;
    }

    /**
     * adapter中的ViewHolder类，主要作用为构建item与其binding对象
     */
    class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private T binding;

        BaseRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
            super(DataBindingUtil.inflate(LayoutInflater.from(
                    viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
            binding = DataBindingUtil.getBinding(this.itemView);
        }
    }

    /**
     * adapter中header与footer的viewholder
     */
    class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder {
        HeaderAndFooterViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 添加尾布局
     */
    public void addFooter(View view){
        footerView=view;
    }

    /**
     * 添加头布局
     */
    public void addHeader(View view){
        headerView=view;
    }

    public void setOnItemClickListener(OnItemClickListener<T,M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T,M>{
        void onItemClick(T binding,M item,int position);
    }

    public void addAll(List<M> data) {
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public void add(M object) {
        datas.add(object);
        notifyDataSetChanged();
    }

    public void add(int position,M object) {
        datas.add(position,object);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }
}
