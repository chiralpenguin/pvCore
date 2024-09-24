package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public abstract class DataService {
    protected final pvCore plugin;
    protected final DatabaseHandler database;

    public DataService(pvCore plugin, DatabaseHandler database) {
        this.plugin = plugin;
        this.database = database;

        createTables();
    }

    protected void createTables(){

    }

    public void saveAll() {

    }

    // TODO add onPlayerJoin() and onPlayerDisconnect() methods to load/unload all relevant data into cache on events
}
