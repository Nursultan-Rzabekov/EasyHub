

package com.example.javademogithubpractice.common;

import org.greenrobot.eventbus.EventBus;

//pull
public enum  AppEventBus {
    INSTANCE;

    AppEventBus(){
        init();
    }

    private EventBus eventBus ;

    private void init(){
        eventBus = EventBus.builder()
                .installDefaultEventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
