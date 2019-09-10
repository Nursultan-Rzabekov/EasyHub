package com.example.javademogithubpractice.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.example.javademogithubpractice.BuildConfig;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.util.AppOpener;
import com.example.javademogithubpractice.util.AppUtils;


public class AboutActivity extends MaterialAboutActivity {

    public static void show(@NonNull Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        MaterialAboutCard.Builder appBuilder = new MaterialAboutCard.Builder();
        buildApp(appBuilder, context);
        MaterialAboutCard.Builder authorBuilder = new MaterialAboutCard.Builder();
        buildAuthor(authorBuilder, context);
        MaterialAboutCard.Builder shareBuilder = new MaterialAboutCard.Builder();
        buildShare(shareBuilder, context);
        return new MaterialAboutList(appBuilder.build(), authorBuilder.build(), shareBuilder.build());
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about);
    }


    private void buildApp(MaterialAboutCard.Builder appBuilder, final Context context){

        appBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(getString(R.string.app_github_name))
                .desc(getString(R.string.app_copyright))
                .icon(R.drawable.logoeasyhub)
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.version)
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_menu_manage)
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.source_code)
                .subText(R.string.source_code_wishes)
                .icon(R.drawable.ic_mark_readed)
                .setOnClickAction(() ->
                        RepositoryActivity.show(context, getString(R.string.author_login_id),
                                getString(R.string.app_github_name)))
                .build());
    }

    private void buildAuthor(MaterialAboutCard.Builder appBuilder, final Context context){
        appBuilder.title(R.string.author);
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.author_name)
                .subText(R.string.author_location)
                .icon(R.drawable.ic_menu_person)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        ProfileActivity.show(AboutActivity.this, getString(R.string.author_login_id),
                                getString(R.string.author_avatar_url));
                    }
                })
                .build());

        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.follow_on_github)
                .icon(R.drawable.ic_github)
                .setOnClickAction(() -> ProfileActivity.show(AboutActivity.this, getString(R.string.author_login_id),
                        getString(R.string.author_avatar_url)))
                .build());

        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.email)
                .subText(R.string.auth_email_address)
                .icon(R.drawable.ic_mail)
                .setOnClickAction(() -> {
                    AppOpener.launchEmail(context, getString(R.string.auth_email_address));
                })
                .setOnLongClickAction(() -> {
                    AppUtils.copyToClipboard(context, getString(R.string.auth_email_address));
                })
                .build());
    }

    private void buildShare(MaterialAboutCard.Builder appBuilder, final Context context) {
        appBuilder.title(R.string.feedback_and_share);
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.share_to_friends)
                .icon(R.drawable.ic_menu_share)
                .setOnClickAction(() ->
                        AppOpener.shareText(AboutActivity.this, getString(R.string.cookapk_download_url)))
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.feedback)
                .icon(R.drawable.ic_mark_readed)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
