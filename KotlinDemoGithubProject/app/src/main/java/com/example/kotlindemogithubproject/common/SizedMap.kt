package com.example.kotlindemogithubproject.common

import java.util.LinkedHashMap
import java.util.Map


class SizedMap<K, V> : LinkedHashMap<K, V> {

    private var maxSize: Int = 0
    private val DEFAULT_SIZE = 64

    constructor() : super() {
        this.maxSize = DEFAULT_SIZE
    }

    constructor(maxSize: Int) : super() {
        this.maxSize = maxSize
    }

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > maxSize
    }

}
