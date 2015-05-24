package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by davchen on 5/17/15.
 */
public class ChronicleMapCache implements CacheInterface{

    ChronicleMapBuilder<Long, Entry> builder;
    ChronicleMap<Long, Entry> map;

    public ChronicleMapCache(String serverUrl) {
        try {
            String serverName = extractServerName(serverUrl);
            String fileName = getFileName(serverName);
            File file = new File(fileName);
            builder = ChronicleMapBuilder.of(Long.class, Entry.class);
            map = builder.createPersistedTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String serverName) {
        if(serverName.equals("server_A")) {
            System.out.println("1");
            return "File_A.dat";
        }
        else if(serverName.equals("server_B")) {
            System.out.println("2");
            return "File_B.dat";
        }
        else if(serverName.equals("server_C")) {
            System.out.println("3");
            return "File_C.dat";
        }
        throw new IllegalArgumentException("Incorrect serverName :" + serverName);
    }

    private String extractServerName(String serverName) {
        System.out.println("Server Name: " + serverName);
        String[] split = serverName.split("/");
        System.out.println("split size: " + split.length);
        String serverSplit = split[1];
        String[] split2 = serverSplit.split("_");
        String finalServerName = split2[0] + "_" + split2[1];
        System.out.println("finalServerName: "  + finalServerName);

        return finalServerName;
    }

    @Override
    public Entry save(Entry thisEntry) {
        map.putIfAbsent(thisEntry.getKey(), thisEntry);
        return thisEntry;
    }

    @Override
    public Entry get(Long key) {

        return map.get(key);
    }

    @Override
    public List<Entry> getAll() {

        return new ArrayList<Entry>(map.values());
    }
}
