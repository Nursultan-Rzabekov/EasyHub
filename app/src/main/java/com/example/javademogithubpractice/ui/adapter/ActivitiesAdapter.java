

package com.example.javademogithubpractice.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.mvp.model.EventPayload;
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
            "StudyPractice",
            "SHOP-MicroServices",
            "CV",
            "CRMSBeta",
            "vue-method",
            "mlCourse.ai"));

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
        //holder.action.setText(info.get(position % 6));
        holder.desc.setText(model.getType().toString());

        holder.setActionAndDesc(model,position);
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
                ProfileActivity.show((Activity) context, null, loginId, userAvatar);
            }
        }

        //TODO to be better event action and desc
        void setActionAndDesc(Event model,int position) {
            String actionStr = null;
            SpannableStringBuilder descSpan = null;
            String fullName = info.get(position % 6);
            EventPayload.RefType refType = model.getPayload().getRefType();
            String action = model.getPayload() != null ? model.getPayload().getAction() : info.get(position % 6);

            switch (model.getType()) {
                case CreateEvent:
                    if (EventPayload.RefType.repository.equals(refType)) {
                        actionStr = String.format(getString(R.string.created_repo), fullName);
                    } else if (EventPayload.RefType.branch.equals(refType)) {
                        actionStr = String.format(getString(R.string.created_branch_at),
                                model.getPayload().getRef(), fullName);
                    } else if (EventPayload.RefType.tag.equals(refType))  {
                        actionStr = String.format(getString(R.string.created_tag_at),
                                model.getPayload().getRef(), fullName);
                    }
                    break;
                case DeleteEvent:
                    if (EventPayload.RefType.branch.equals(refType)) {
                        actionStr = String.format(getString(R.string.delete_branch_at),
                                model.getPayload().getRef(), fullName);
                    } else if (EventPayload.RefType.tag.equals(refType))  {
                        actionStr = String.format(getString(R.string.delete_tag_at),
                                model.getPayload().getRef(), fullName);
                    }
                    break;
                case ForkEvent:
                    String oriRepo = model.getRepo().getFullName();
                    String newRepo = model.getActor().getLogin() + "/" + model.getRepo().getName();
                    actionStr = String.format(getString(R.string.forked_to), oriRepo, newRepo);
                    break;
                case GollumEvent:
                    actionStr = action + " a wiki page ";
                    break;

                case InstallationEvent:
                    actionStr = action + " an GitHub App ";
                    break;
                case InstallationRepositoriesEvent:
                    actionStr = action + " repository from an installation ";
                    break;
                case MarketplacePurchaseEvent:
                    actionStr = action + " marketplace plan ";
                    break;
                case MemberEvent:
                    String memberEventStr = getMemberEventStr(action);
                    actionStr = String.format(memberEventStr,
                            model.getPayload().getMember().getLogin(), fullName);
                    break;
                case OrgBlockEvent:
                    String orgBlockEventStr ;
                    if(EventPayload.OrgBlockEventActionType.blocked.name().equals(action)){
                        orgBlockEventStr = getString(R.string.org_blocked_user);
                    }else{
                        orgBlockEventStr = getString(R.string.org_unblocked_user);
                    }
                    actionStr = String.format(orgBlockEventStr,
                            model.getPayload().getOrganization().getLogin(),
                            model.getPayload().getBlockedUser().getLogin());
                    break;
                case ProjectCardEvent:
                    actionStr = action + " a project ";
                    break;
                case ProjectColumnEvent:
                    actionStr = action + " a project ";
                    break;

                case ProjectEvent:
                    actionStr = action + " a project ";
                    break;
                case PublicEvent:
                    actionStr = String.format(getString(R.string.made_repo_public), fullName);
                    break;
                case PullRequestEvent:
                    actionStr = action + " pull request " + model.getRepo().getFullName();
                    break;
                case PullRequestReviewEvent:
                    String pullRequestReviewStr = getPullRequestReviewEventStr(action);
                    actionStr = String.format(pullRequestReviewStr, fullName);
                    break;
                case WatchEvent:
                    actionStr = String.format(getString(R.string.starred_repo), fullName);
                    break;
            }

            this.action.setVisibility(View.VISIBLE);
            if(descSpan != null){
                desc.setVisibility(View.VISIBLE);
                desc.setText(descSpan);
            }else{
                desc.setVisibility(View.GONE);
            }

            actionStr = StringUtils.upCaseFirstChar(actionStr);
            actionStr = actionStr == null ? "" : actionStr;
            this.action.setText(actionStr);
        }

        private String getPullRequestReviewEventStr(String action){
            EventPayload.PullRequestReviewEventActionType actionType =
                    EventPayload.PullRequestReviewEventActionType.valueOf(action);
            switch (actionType){
                case submitted:
                    return getString(R.string.submitted_pull_request_review_at);
                case edited:
                    return getString(R.string.edited_pull_request_review_at);
                case dismissed:
                    return getString(R.string.dismissed_pull_request_review_at);
                default:
                    return getString(R.string.submitted_pull_request_review_at);
            }
        }

        private String getMemberEventStr(String action){
            EventPayload.MemberEventActionType actionType = EventPayload.MemberEventActionType.valueOf(action);
            switch (actionType){
                case added:
                    return getString(R.string.added_member_to);
                case deleted:
                    return getString(R.string.deleted_member_at);
                case edited:
                    return getString(R.string.edited_member_at);
                default:
                    return getString(R.string.added_member_to);
            }
        }

    }

}
