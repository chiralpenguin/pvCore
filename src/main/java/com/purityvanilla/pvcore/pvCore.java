package com.purityvanilla.pvcore;

import com.purityvanilla.pvcore.commands.*;
import com.purityvanilla.pvcore.database.DatabaseConnection;
import com.purityvanilla.pvcore.tabcompleters.GamemodeCompleter;
import com.purityvanilla.pvcore.tabcompleters.TeleportCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class pvCore extends JavaPlugin {
    private Config config;
    private DatabaseConnection database;

    @Override
    public void onEnable() {
        config = new Config(); // Create/load config, copy default config if file doesn't exist

        // Connect to database
        getLogger().info("Connecting to database server...");
        database = new DatabaseConnection(this);
        getLogger().info("Successfully connected to database!");

        // Register commands
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommand("gamemode").setTabCompleter(new GamemodeCompleter(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("teleport").setTabCompleter(new TeleportCompleter(this));
        getCommand("tphere").setExecutor(new TeleportHereCommand(this));

        getLogger().info("Plugin loaded");
    }

    public Config config() {
        return config;
    }

    public void reload() {
        config = new Config(); // Reload config (including messages.json)
    }
}
