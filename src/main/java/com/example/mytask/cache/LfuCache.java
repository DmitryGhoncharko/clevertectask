package com.example.mytask.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LfuCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, Node> storage;
    private final int capacity;

    public LfuCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity should be more than 0");
        }
        this.capacity = capacity;
        this.storage = new LinkedHashMap<>(capacity, 1);
    }

    @Override
    public final V get(K key) {
        Node node = storage.get(key);
        if (node == null) {
            return null;
        }
        synchronized (this) {
            return node.incrementFrequency().getValue();
        }
    }

    @Override
    public final V remove(K key) {
        if (key == null) {
            return null;
        }
        synchronized (this) {
            return (V) storage.remove(key);
        }
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public final V put(K key, V value) {
        doEvictionIfNeeded(key);
        Node oldNode = storage.put(key, new Node(value));
        if (oldNode == null) {
            return null;
        }
        synchronized (this) {
            return oldNode.getValue();
        }
    }


    private void doEvictionIfNeeded(K putKey) {
        if (storage.size() < capacity) {
            return;
        }
        long minFrequency = Long.MAX_VALUE;
        K keyToRemove = null;
        for (Map.Entry<K, Node> entry : storage.entrySet()) {
            if (Objects.equals(entry.getKey(), putKey)) {
                //no eviction required cause element already exists, we just need to replace it
                return;
            }
            if (minFrequency >= entry.getValue().getFrequency()) {
                minFrequency = entry.getValue().getFrequency();
                keyToRemove = entry.getKey();
            }
        }
        storage.remove(keyToRemove);
    }

    private class Node {
        private final V value;
        private long frequency;

        public Node(V value) {
            this.value = value;
            this.frequency = 1;
        }

        public V getValue() {
            return value;
        }

        public long getFrequency() {
            return frequency;
        }

        public Node incrementFrequency() {
            ++frequency;
            return this;
        }

        @Override
        public String toString() {
            return "Node [value=" + value + ", frequency=" + frequency + "]";
        }

    }

    @Override
    public String toString() {
        return "storage = " + storage + ", capacity=" + capacity;
    }
}
