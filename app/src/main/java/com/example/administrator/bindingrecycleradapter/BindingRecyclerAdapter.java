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
    @LayoutRes
    private int resId;

    protected Context mContext;
    protected List<M> datas;
    protected OnItemClickListener<T,M> onItemClickListener;

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
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent, resId);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
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
        return datas.size();
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
