package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by David Chen on 5/9/15.
 */
public class RendezvousHash<T> {
    /**
     * A hashing function from guava, ie Hashing.murmur3_128()
     */
    private final HashFunction hasher;

    private final int replicas;
    private final ConcurrentSkipListSet<T> ordered;

    public RendezvousHash(HashFunction hasher, int replicas, Collection<T> init) {
        if (hasher == null) throw new NullPointerException("hasher");
        if (init == null) throw new NullPointerException("init");
        this.hasher = hasher;
        this.replicas=replicas;
        this.ordered = new ConcurrentSkipListSet<T>(init);
    }

    public void remove(T node) {
        ordered.remove(node);
    }

    public void add(T node) {
        ordered.add(node);
    }

    public T get(Object key) {
        long maxValue = Long.MIN_VALUE;
        T max = null;
        for (T node : ordered) {
            long nodesHash=hasher.newHasher().putString(key.toString())
                        .putString(node.toString()).hash().asLong();

            if (nodesHash > maxValue) {
                max = node;
                maxValue = nodesHash;
            }
        }
        return max;
    }
}
