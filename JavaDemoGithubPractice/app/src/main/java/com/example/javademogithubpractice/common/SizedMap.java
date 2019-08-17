

package com.example.javademogithubpractice.common;

import java.util.LinkedHashMap;

/**
 * Created by ThirtyDegreesRay on 2017/8/22 18:46:06
 */

public class SizedMap<K,V> extends LinkedHashMap<K,V> {

    private int maxSize ;
    private final int DEFAULT_SIZE = 64;

    public SizedMap() {
        super();
        this.maxSize = DEFAULT_SIZE;
    }

    public SizedMap(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > maxSize;
    }

}
