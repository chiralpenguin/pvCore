package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public abstract class DataService {
    protected final pvCore plugin;

    public DataService(pvCore plugin) {
        this.plugin = plugin;
    }

    public void saveAll() {

    }

    // TODO add onPlayerJoin() and onPlayerDisconnect() methods to load/unload all relevant data into cache on events
}
