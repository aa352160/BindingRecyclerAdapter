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

下面来看看BindingRecyclerAdapter的代码是什么样的：

```java
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
}
```

其中的代码量很少，逻辑也非常的清晰，主要的部分就在于使用DataBinding实现ViewHolder的构造，从而节省需要编写的代码量。
