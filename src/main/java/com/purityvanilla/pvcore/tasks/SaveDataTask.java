package com.purityvanilla.pvcore.tasks;

import com.purityvanilla.pvcore.database.DataService;

import java.util.HashMap;

public class SaveDataTask implements Runnable {
    private final HashMap<String, DataService> dataServices;

    public SaveDataTask(HashMap<String, DataService> dataServices) {
        this.dataServices = dataServices;
    }

    @Override
    public void run() {
        for (DataService dataService : dataServices.values()) {
            dataService.saveAll();
        }
    }
}
