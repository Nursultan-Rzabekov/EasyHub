package com.example.javademogithubpractice.mvp.contract;


public interface IBaseListContract {

    interface View {
        void showLoadError(String errorMsg);
        void setCanLoadMore(boolean canLoadMore);
    }

}
