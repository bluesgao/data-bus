package com.wwyt.databus.core.plugin;

import com.wwyt.databus.plugin.fetcher.DataFetcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DataFetcherManager {
    private static ConcurrentHashMap<String, DataFetcher> registeredFetchers = new ConcurrentHashMap<>(16);

    static {
        load();
        System.out.println("DataFetcherManager load initialized");
    }

    private static void load() {
        ServiceLoader<DataFetcher> loadedDrivers = ServiceLoader.load(DataFetcher.class);
        Iterator<DataFetcher> itr = loadedDrivers.iterator();
        try {
            while (itr.hasNext()) {
                DataFetcher item = itr.next();
                System.out.println("fetcher name:" + item.getName());
                register(item);
            }
        } catch (Throwable t) {
            // Do nothing
            log.error("load error:{}", t);
        }
    }

    private static void register(DataFetcher fetcher) {
        if (fetcher != null) {
            if (registeredFetchers.containsKey(fetcher.getName())) {
                throw new RuntimeException("fetcher名称重复:" + fetcher.getName());
            }
            registeredFetchers.putIfAbsent(fetcher.getName(), fetcher);
        } else {
            throw new NullPointerException();
        }
        log.info("register:{}", fetcher.toString());
    }

    public static DataFetcher get(String name) {
        if (registeredFetchers.containsKey(name)) {
            return registeredFetchers.get(name);
        }
        return null;
    }

}
