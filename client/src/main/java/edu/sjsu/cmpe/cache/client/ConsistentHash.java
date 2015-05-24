package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by David Chen on 5/9/15.
 */
public class ConsistentHash<T> {

    private final HashFunction hashFunction;
    private final int replicas;
    private final SortedMap<Long, T> ring = new TreeMap<Long, T>();

    public ConsistentHash(HashFunction hashFunction, int replicas,
                          Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.replicas = replicas;

        for (T node : nodes) {
            add(node);
        }
    }

    public boolean add(T node) {
        for (int i = 0; i < replicas; i++) {
            Long l = hashFunction.hashString(node.toString() + i).asLong();
            ring.put(l, node);
        }
        return true;
    }

    public boolean remove(T node) {
        for (int i = 0; i < replicas; i++) {
            ring.remove(hashFunction.hashString(node.toString() + i).asLong());
        }
        return true;
    }

    public T get(Object key) {
        long hash = hashFunction.hashString(key.toString()).asLong();
        if (!ring.containsKey(hash)) {
            SortedMap<Long, T> tailMap = ring.tailMap(hash);
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }
        return ring.get(hash);
    }
}
