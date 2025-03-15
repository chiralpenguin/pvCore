package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.PVCore;

public abstract class DataService {
    protected final PVCore plugin;

    public DataService(PVCore plugin) {
        this.plugin = plugin;
    }

    public void saveAll() {

    }

    public void cleanCache() {

    }

    // TODO add onPlayerJoin() and onPlayerDisconnect() methods to load/unload all relevant data into cache on events
}
