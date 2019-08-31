

package com.example.javademogithubpractice.mvp.contract;



import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.Notification;
import com.example.javademogithubpractice.mvp.model.Repository;

import java.util.ArrayList;

public interface INotificationsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showNotifications(ArrayList<Notification> notifications);
    }

    interface Presenter extends IBasePagerContract.Presenter<INotificationsContract.View> {
        void loadNotifications(int page, boolean isReload);
//        void markNotificationAsRead(String threadId);
//        void markAllNotificationsAsRead();
//        boolean isNotificationsAllRead();
//        void markRepoNotificationsAsRead(@NonNull Repository repository);
    }

}
