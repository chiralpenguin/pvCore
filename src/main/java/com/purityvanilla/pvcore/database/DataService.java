package com.purityvanilla.pvcore.database;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class DataService {
    protected final JavaPlugin plugin;

    public DataService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void saveAll() {

    }

    public void cleanCache() {

    }

    // TODO add onPlayerJoin() and onPlayerDisconnect() methods to load/unload all relevant data into cache on events
}
