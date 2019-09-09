

package com.example.javademogithubpractice.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.mvp.model.Notification;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseAdapter;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseViewHolder;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;
import com.example.javademogithubpractice.util.StringUtils;
import com.example.javademogithubpractice.util.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;



public class NotificationsAdapter extends BaseAdapter<BaseViewHolder, Notification> {

    @Inject
    public NotificationsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    List<Integer> colors = new ArrayList<>(Arrays.asList(
            R.drawable.home_gradient_maths,
            R.drawable.home_gradients,
            R.drawable.home_gradientss,
            R.drawable.home_gradientsss,
            R.drawable.home_gradientssss));

    List<Integer> images = new ArrayList<>(Arrays.asList(
            R.drawable.home_ic_biology,
            R.drawable.home_ic_chemistry,
            R.drawable.home_ic_geography,
            R.drawable.home_ic_maths,
            R.drawable.home_ic_physics));


    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_notification;
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        return new NotificationViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
        Notification model = data.get(position);

        holder.title.setText(model.getSubject().getTitle());
        holder.status.setBackgroundResource(images.get(position % 5));

//        if(model.isUnread()){
//            holder.status.setBackgroundResource(R.drawable.ic_mark_unread);
//        } else {
//            //holder.status.setVisibility(View.INVISIBLE);
//            holder.status.setBackgroundResource(R.drawable.ic_mark_readed);
//        }

        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getUpdateAt()));
        holder.linearLayout.setBackgroundResource(colors.get(position % 5));
    }

    class NotificationViewHolder extends BaseViewHolder {

        @BindView(R.id.type_icon) AppCompatImageView typeIcon;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.status) ImageView status;
        @BindView(R.id.linear_notifications) LinearLayout linearLayout;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
