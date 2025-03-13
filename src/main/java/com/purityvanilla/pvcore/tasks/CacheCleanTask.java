package com.purityvanilla.pvcore.tasks;

import com.purityvanilla.pvcore.database.DataService;

import java.util.HashMap;

public class CacheCleanTask implements Runnable {
    private final HashMap<String, DataService> dataServices;

    public CacheCleanTask(HashMap<String, DataService> dataServices) {
        this.dataServices = dataServices;
    }

    @Override
    public void run() {
        for (DataService dataService : dataServices.values()) {
            dataService.cleanCache();
        }
    }
}
