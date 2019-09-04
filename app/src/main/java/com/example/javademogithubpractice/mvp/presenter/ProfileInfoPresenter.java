

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.mvp.contract.IProfileInfoContract;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import java.util.ArrayList;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProfileInfoPresenter extends BasePagerPresenter<IProfileInfoContract.View>
        implements IProfileInfoContract.Presenter{

    @AutoAccess User user;
    private ArrayList<User> orgs;

    @Inject
    public ProfileInfoPresenter(AuthSessionRepository daoSession) {
        super(daoSession);
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        mView.showProfileInfo(user);
        if(user.isUser()) loadOrgs();
    }

    public User getUser() {
        return user;
    }

    private void loadOrgs(){
        if(orgs != null && orgs.size() != 0){
            mView.showUserOrgs(orgs);
            return;
        }

        addDisposable(getUserRepositoryImpl().getUserOrgs(true, user.getLogin())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successInfoUserOrg,this::errorInfoUserOrg));
    }

    private void errorInfoUserOrg(Throwable throwable) {
        mView.showErrorToast(getErrorTip(throwable));
    }

    private void successInfoUserOrg(Response<ArrayList<User>> response) {
        if(response.body().size() != 0){
            orgs = response.body();
            mView.showUserOrgs(orgs);
        }
    }
    public void setUser(User user) {
        this.user = user;
    }
}
