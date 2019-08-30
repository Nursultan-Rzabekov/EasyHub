

package com.example.javademogithubpractice.mvp.contract;
import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.SearchModel;

import java.util.ArrayList;


public interface ISearchContract {

    interface View extends IBaseContract.View{
        void showSearches(ArrayList<SearchModel> searchModels);
    }

    interface Presenter extends IBaseContract.Presenter<ISearchContract.View>{
        ArrayList<SearchModel> getQueryModels(@NonNull String query);
        @NonNull ArrayList<String> getSearchRecordList();
        void addSearchRecord(@NonNull String record);
    }

}
