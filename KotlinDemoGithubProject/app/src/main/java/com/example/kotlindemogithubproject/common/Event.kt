package com.example.kotlindemogithubproject.common


class Event {
    class NetChangedEvent(var preNetStatus: Int, var curNetStatus: Int)

    class ServerStatusChangedEvent(var serverStatus: Int) {
        companion object {
            val SERVER_START = 0

            val SERVER_STOP = 1

            val SERVER_STARTED = 2
        }
    }
}
