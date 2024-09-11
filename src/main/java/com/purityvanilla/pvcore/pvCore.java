package com.purityvanilla.pvcore;

import org.bukkit.plugin.java.JavaPlugin;

public class pvCore extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        config = new Config("plugins/pvCore/config.yml");
        getLogger().info("Plugin enabled");
    }
}
