package com.example.administrator.bindingrecycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.bindingrecycleradapter.databinding.ItemListBinding;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class NewAdapter extends BindingRecyclerAdapter<ItemListBinding,TestModel>{
    public NewAdapter(Context mContext, List<TestModel> datas) {
        super(mContext, datas);
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.item_list;
    }

    @Override
    public void onBindViewHolder(ItemListBinding binding, TestModel itemData, RecyclerView.ViewHolder holder, int position) {
        binding.textView.setText(itemData.getText());
    }
}
