

package com.example.javademogithubpractice.mvp.presenter;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.mvp.contract.ISearchContract;
import com.example.javademogithubpractice.mvp.model.SearchModel;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<ISearchContract.View> implements ISearchContract.Presenter {

    @AutoAccess ArrayList<SearchModel> searchModels;

    @Inject
    public SearchPresenter(AuthSessionRepository daoSession) {
        super(daoSession);
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (searchModels == null) {
            createSearchModels();
        } else {
            mView.showSearches(searchModels);
        }
    }

    private void createSearchModels() {
        searchModels = new ArrayList<>();
        searchModels.add(new SearchModel(SearchModel.SearchType.Repository));
        searchModels.add(new SearchModel(SearchModel.SearchType.User));
    }

    public ArrayList<SearchModel> getSearchModels() {
        return searchModels;
    }

    @Override
    public ArrayList<SearchModel> getQueryModels(@NonNull String query) {
        for (SearchModel searchModel : searchModels) {
            searchModel.setQuery(query);
        }
        return searchModels;
    }

    @NonNull
    @Override
    public ArrayList<String> getSearchRecordList() {
        String records = PrefUtils.getSearchRecords();
        ArrayList<String> recordList = new ArrayList<>();
        if (!StringUtils.isBlank(records)) {
            String[] recordArray = records.split("\\$\\$");
            Collections.addAll(recordList, recordArray);
        }
        return recordList;
    }

    @Override
    public void addSearchRecord(@NonNull String record) {
        if(record.contains("$")){
            return;
        }
        int MAX_SEARCH_RECORD_SIZE = 30;
        ArrayList<String> recordList = getSearchRecordList();
        if(recordList.contains(record)){
            recordList.remove(record);
        }
        if(recordList.size() >= MAX_SEARCH_RECORD_SIZE){
            recordList.remove(recordList.size() - 1);
        }
        recordList.add(0, record);
        StringBuilder recordStr = new StringBuilder("");
        String lastRecord = recordList.get(recordList.size() - 1);
        for(String str : recordList){
            recordStr.append(str);
            if(!str.equals(lastRecord)){
                recordStr.append("$$");
            }
        }
        PrefUtils.set(PrefUtils.SEARCH_RECORDS, recordStr.toString());
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void logout() {
        addDisposable(daoSession.deleteAuthUser(AppData.INSTANCE.getAuthUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successDeleteUser,this::errorDeleteUser));

        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

    private void errorDeleteUser(Throwable throwable) {
        Toast.makeText(getContext(),"Error delete user" + throwable,Toast.LENGTH_SHORT).show();
    }

    private void successDeleteUser() {
        Toast.makeText(getContext(),"Success delete user",Toast.LENGTH_SHORT).show();
    }

}
