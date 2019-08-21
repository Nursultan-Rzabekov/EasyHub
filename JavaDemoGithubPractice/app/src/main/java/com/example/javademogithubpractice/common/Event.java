

package com.example.javademogithubpractice.common;


public class Event {
    public static class NetChangedEvent {
        public int preNetStatus;
        public int curNetStatus;

        public NetChangedEvent(int preNetStatus, int curNetStatus) {
            this.preNetStatus = preNetStatus;
            this.curNetStatus = curNetStatus;
        }
    }
}
