

package com.example.javademogithubpractice.ui.adapter.baseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;

import java.util.ArrayList;


public abstract class BaseAdapter<VH extends BaseViewHolder, D extends Object> extends RecyclerView.Adapter<VH>
        implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnItemLongClickListener{

    private BaseViewHolder.OnItemClickListener mOnItemClickListener;
    private BaseViewHolder.OnItemLongClickListener mOnItemLongClickListener;

    protected ArrayList<D> data;
    protected Context context;
    protected BaseFragment fragment;

    public BaseAdapter(Context context){
        this.context = context;
    }

    public BaseAdapter(Context context, BaseFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(ArrayList<D> data){
        this.data = data;
    }

    public ArrayList<D> getData() {
        return data;
    }

    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(BaseViewHolder.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        VH viewHolder = getViewHolder(itemView, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {

        if(mOnItemClickListener != null){
            holder.setOnItemClickListener(this);
        }

        if(mOnItemLongClickListener != null){
            holder.setOnItemLongClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract VH getViewHolder(View itemView, int viewType);

    protected void showShortToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        mOnItemClickListener.onItemClick(position, view);
    }

    @Override
    public boolean onItemLongClick(int position, @NonNull View view) {
        return mOnItemLongClickListener.onItemLongClick(position, view);
    }

    @NonNull protected String getString(@StringRes int resId){
        return context.getString(resId);
    }

}
