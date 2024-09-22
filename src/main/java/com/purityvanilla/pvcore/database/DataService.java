package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public class DataService {
    protected final pvCore plugin;
    protected final DatabaseHandler database;

    public DataService(pvCore plugin, DatabaseHandler database) {
        this.plugin = plugin;
        this.database = database;

        createTable();
    }

    protected void createTable(){

    }
}
