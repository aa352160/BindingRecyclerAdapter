# BindingRecyclerAdapter

对于DataBinding这个库相信大家都很熟悉了，今天我就为大家带来一个针对DataBinding封装的供RecyclerView使用的Adapter基类，当然也还有很多不足的地方，比如不支持多种类型的Model与多种类型布局等，欢迎大家多提意见与建议。

本次的封装有以下几个优点：

1. 代码量少，封装逻辑简单，易于修改
2. 将模板代码的量尽量减少，能专注于业务逻辑
3. 结合DataBinding，去除繁琐的控件绑定操作

首先，以一个简单的例子来看看如果不使用封装的话，adapter中的代码会是什么样的：

```java
public class OldAdapter extends RecyclerView.Adapter<OldAdapter.ViewHolder>{
    private Context context;
    private List<TestModel> dataList;

    public OldAdapter(Context context, List<TestModel> dataList){
        this.context=context;
        this.dataList=dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textView);
        }
    }
}
```

那么之后来看看对于相同的效果，使用BindingRecyclerAdapter后的代码：

```java
public class NewAdapter extends BindingRecyclerAdapter<ItemListBinding,TestModel>{
    public NewAdapter(Context mContext, List<TestModel> datas) {
        super(mContext, datas);
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.item_list;
    }

    @Override
    public void onBindViewHolder(TestModel itemData, RecyclerView.ViewHolder holder, int position) {
        binding.textView.setText(itemData.getText());
    }
}
```

可以看到，在使用了BindingRecyclerAdapter后，省去了ViewHolder的构造与大量的模板代码。
