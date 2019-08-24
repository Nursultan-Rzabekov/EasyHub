

package com.example.javademogithubpractice.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseAdapter;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.BaseViewHolder;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;
import javax.inject.Inject;
import butterknife.BindView;


public class RepositoriesAdapter extends BaseAdapter<RepositoriesAdapter.ViewHolder, Repository> {

    @Inject
    public RepositoriesAdapter(Context context, BaseFragment fragment){
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_repo;
    }

    @NonNull
    @Override
    protected ViewHolder getViewHolder(@NonNull View itemView, int viewType) {
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_user_avatar) ImageView ivUserAvatar;
        //@BindView(R.id.language_color) ImageView languageColor;
        @BindView(R.id.tv_repo_name) TextView tvRepoName;
        @BindView(R.id.tv_language) TextView tvLanguage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Repository repository = data.get(position);
        boolean hasOwnerAvatar = !StringUtils.isBlank(repository.getOwner().getAvatarUrl());
        holder.tvRepoName.setText(hasOwnerAvatar ? repository.getName(): repository.getFullName());

        if(StringUtils.isBlank(repository.getLanguage())){
            holder.tvLanguage.setText("");
            //holder.languageColor.setVisibility(View.INVISIBLE);
        } else {
            //holder.languageColor.setVisibility(View.VISIBLE);
            holder.tvLanguage.setText(repository.getLanguage());
//            int languageColor = LanguageColorsHelper.INSTANCE.getColor(context, repository.getLanguage());
//            holder.languageColor.setImageTintList(ColorStateList.valueOf(languageColor));
        }


        if(hasOwnerAvatar){
            holder.ivUserAvatar.setVisibility(View.VISIBLE);
            GlideApp.with(fragment)
                    .load(repository.getOwner().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(holder.ivUserAvatar);
        } else {
            holder.ivUserAvatar.setVisibility(View.GONE);
        }
    }
}
