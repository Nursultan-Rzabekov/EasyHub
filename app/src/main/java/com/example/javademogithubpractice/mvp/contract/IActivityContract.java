

package com.example.javademogithubpractice.mvp.contract;
import com.example.javademogithubpractice.mvp.model.Event;
import java.util.ArrayList;

public interface IActivityContract {
    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showEvents(ArrayList<Event> events);
    }

    interface Presenter extends IBasePagerContract.Presenter<IActivityContract.View>{
        void loadEvents(boolean isReload, int page);
    }
}
