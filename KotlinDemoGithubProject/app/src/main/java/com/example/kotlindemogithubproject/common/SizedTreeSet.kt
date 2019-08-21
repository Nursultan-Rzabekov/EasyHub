package com.example.kotlindemogithubproject.common

import java.util.TreeSet


class SizedTreeSet<E>(private val maxSize: Int) : TreeSet<E>() {

    override fun add(e: E): Boolean {
        if (size >= maxSize && !contains(e)) {
            var last: E? = null
            val iterator = iterator()
            while (iterator.hasNext()) {
                last = iterator.next()
            }
            remove(last)
        }
        return super.add(e)
    }

}
