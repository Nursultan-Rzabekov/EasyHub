

package com.example.javademogithubpractice.mvp.contract;
import com.example.javademogithubpractice.mvp.model.Notification;
import java.util.ArrayList;

public interface INotificationsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showNotifications(ArrayList<Notification> notifications);
    }

    interface Presenter extends IBasePagerContract.Presenter<INotificationsContract.View> {
        void loadNotifications(int page, boolean isReload);
    }

}
