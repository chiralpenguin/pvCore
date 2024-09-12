package com.purityvanilla.pvcore;

import com.purityvanilla.pvcore.commands.HelpCommand;
import com.purityvanilla.pvcore.commands.RulesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class pvCore extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        config = new Config(); // Create/load config, copy default config if file doesn't exist

       getCommand("rules").setExecutor(new RulesCommand(this));
       getCommand("help").setExecutor(new HelpCommand(this));

        getLogger().info("Plugin loaded");
    }

    public Config config() {
        return config;
    }
}

