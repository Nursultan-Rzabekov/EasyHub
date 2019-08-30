

package com.example.javademogithubpractice.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.ui.activity.ProfileActivity;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseAdapter;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseViewHolder;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;



public class ActivitiesAdapter extends BaseAdapter<ActivitiesAdapter.ViewHolder, Event> {

    @Inject
    public ActivitiesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_activity;
    }

    List<Integer> colors = new ArrayList<>(Arrays.asList(
            R.drawable.home_gradient_maths,
            R.drawable.home_gradients,
            R.drawable.home_gradientss,
            R.drawable.home_gradientsss,
            R.drawable.home_gradientssss));

    List<String> info = new ArrayList<>(Arrays.asList(
            "Created repository StudyPractice",
            "Created repository SHOP-MicroServices",
            "Created repository CV",
            "Created repository CRMSBeta",
            "Created repository vue-method",
            "Forked repository mlCourse.ai"));

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Event model = data.get(position);
        GlideApp.with(fragment)
                .load(model.getActor().getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(holder.userAvatar);
        holder.userName.setText(model.getActor().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));
        holder.linearLayout.setBackgroundResource(colors.get(position % 5));
        holder.action.setText(info.get(position % 6));
        holder.desc.setText(model.getType().toString());
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) ImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.action) TextView action;
        @BindView(R.id.desc) TextView desc;
        @BindView(R.id.linear_news_layout) LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        void onUserClick() {
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                String loginId = data.get(getAdapterPosition()).getActor().getLogin();
                String userAvatar = data.get(getAdapterPosition()).getActor().getAvatarUrl();
                ProfileActivity.show((Activity) context, ViewHolder.this.userAvatar, loginId, userAvatar);
            }
        }
    }

}
