package com.bluesgao.databus.core.plugin;

import com.bluesgao.databus.plugin.processor.DataProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DataProcessorManager {
    private static ConcurrentHashMap<String, DataProcessor> registeredProcessors = new ConcurrentHashMap<>(16);

    static {
        load();
        System.out.println("DataProcessorManager load initialized");
    }

    private static void load() {
        ServiceLoader<DataProcessor> loadedDrivers = ServiceLoader.load(DataProcessor.class);
        Iterator<DataProcessor> itr = loadedDrivers.iterator();
        try {
            while (itr.hasNext()) {
                DataProcessor item = itr.next();
                System.out.println("processor name:" + item.getName());
                register(item);
            }
        } catch (Throwable t) {
            // Do nothing
            log.error("load error:{}", t);
        }
    }

    private static void register(DataProcessor processor) {
        if (processor != null) {
            if (registeredProcessors.containsKey(processor.getName())) {
                throw new RuntimeException("processor名称重复:" + processor.getName());
            }
            registeredProcessors.putIfAbsent(processor.getName(), processor);
        } else {
            throw new NullPointerException();
        }
        log.info("register:{}", processor.toString());
    }

    public static DataProcessor get(String name) {
        if (registeredProcessors.containsKey(name)) {
            return registeredProcessors.get(name);
        }
        return null;
    }

}
