package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;

import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Client...");
        ArrayList<String> cacheNodes = new ArrayList<String>();

        //storing all nodes in an arrayList
        cacheNodes.add("http://localhost:3000");
        cacheNodes.add("http://localhost:3001");
        cacheNodes.add("http://localhost:3002");

        //passing hashing algo, number of replicas and arrayList of nodes to our consistenthash
        /*ConsistentHash<String> ch = new ConsistentHash<String>(Hashing.md5(),
                1, cacheNodes);

        for (int count = 1, letterCount = 97; count <= 10 && letterCount <= 106; count++, letterCount++) {
            String cacheURL = ch.get(count);
            CacheServiceInterface cache = new DistributedCacheService(cacheURL);
            cache.put(count, String.valueOf((char) letterCount));

            System.out.println("PUT ==> node "+cacheURL);
            System.out.println("put ==>("+count+","+String.valueOf((char) letterCount)+")");

            System.out.println("GET ==> node "+cacheURL);
            System.out.println("get(" + count + ") => " + cache.get(count));
        }
        System.out.println("Existing Cache Client...");*/

        //Rendezvous Hashing

        RendezvousHash<String> ch = new RendezvousHash<String>(Hashing.md5(),
                1, cacheNodes);

        for (int count = 1, letterCount = 97; count <= 10 && letterCount <= 106; count++, letterCount++) {
            String cacheURL = ch.get(count);
            CacheServiceInterface cache = new DistributedCacheService(cacheURL);
            cache.put(count, String.valueOf((char) letterCount));

            System.out.println("PUT ==> node "+cacheURL);
            System.out.println("put ==>("+count+","+String.valueOf((char) letterCount)+")");

            System.out.println("GET ==> node "+cacheURL);
            System.out.println("get(" + count + ") => " + cache.get(count));
        }
        System.out.println("Existing Cache Client...");


    }

}
