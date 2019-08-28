

package com.example.javademogithubpractice.common;


import com.example.javademogithubpractice.mvp.model.SearchModel;

public class Event {
    public static class NetChangedEvent {
        public int preNetStatus;
        public int curNetStatus;

        public NetChangedEvent(int preNetStatus, int curNetStatus) {
            this.preNetStatus = preNetStatus;
            this.curNetStatus = curNetStatus;
        }
    }

    public static class SearchEvent{
        public SearchModel searchModel;

        public SearchEvent(SearchModel searchModel) {
            this.searchModel = searchModel;
        }
    }
}
