

package com.example.javademogithubpractice.ui.adapter.baseAdapter;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;


public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
            mOnItemClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mOnItemLongClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
            return mOnItemLongClickListener.onItemLongClick(getAdapterPosition(), v);
        }
        return false;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, @NonNull View view);
    }

    public interface OnItemLongClickListener{
        boolean onItemLongClick(int position, @NonNull View view);
    }

}
