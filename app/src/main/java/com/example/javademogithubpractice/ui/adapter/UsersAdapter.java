

package com.example.javademogithubpractice.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseAdapter;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseViewHolder;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;
import com.example.javademogithubpractice.util.PrefUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;


public class UsersAdapter extends BaseAdapter<UsersAdapter.ViewHolder, User> {

    private boolean cardEnable = true;

    @Inject
    public UsersAdapter(Context context, BaseFragment fragment){
        super(context, fragment);
    }

    public void setCardEnable(boolean cardEnable) {
        this.cardEnable = cardEnable;
    }

    private List<Integer> colors = new ArrayList<>(Arrays.asList(
            R.drawable.home_gradient_maths,
            R.drawable.home_gradients,
            R.drawable.home_gradientss,
            R.drawable.home_gradientsss,
            R.drawable.home_gradientssss));

    @Override
    protected int getLayoutId(int viewType) {
        return cardEnable ? R.layout.layout_item_user : R.layout.layout_item_user_no_card;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        GlideApp.with(fragment)
                .load(data.get(position).getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(holder.avatar);
        holder.name.setText(data.get(position).getLogin());
        if(holder.linearLayout != null){
            holder.linearLayout.setBackgroundResource(colors.get(position % 5));
        }


    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.name) TextView name;

        @Nullable
        @BindView(R.id.linear_user_layout) LinearLayout linearLayout;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
