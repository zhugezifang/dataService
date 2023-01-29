package com.vince.xq.common.utils;

import java.util.LinkedHashMap;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {
    /**
     * 缓存容量
     */
    private int CAPACITY;

    public LRUMap(int capacity) {
        super(capacity, 1F, false);
        this.CAPACITY = capacity;
    }

    protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
        /** 当size 超过容量时，linkedHashMap会删除最不常用元素 */
        return size() > CAPACITY;
    }
}