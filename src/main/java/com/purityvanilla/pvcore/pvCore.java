package com.purityvanilla.pvcore;

import com.purityvanilla.pvcore.commands.*;
import com.purityvanilla.pvcore.database.DataService;
import com.purityvanilla.pvcore.database.DatabaseHandler;
import com.purityvanilla.pvcore.database.PlayerDataService;
import com.purityvanilla.pvcore.database.SchemaDataService;
import com.purityvanilla.pvcore.listeners.PlayerJoinListener;
import com.purityvanilla.pvcore.listeners.PlayerQuitListener;
import com.purityvanilla.pvcore.tabcompleters.GamemodeCompleter;
import com.purityvanilla.pvcore.tabcompleters.TeleportCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class pvCore extends JavaPlugin {
    private Config config;
    private DatabaseHandler database;
    private HashMap<String, DataService> dataServices;

    @Override
    public void onEnable() {
        config = new Config(); // Create/load config, copy default config if file doesn't exist

        // Connect to database
        getLogger().info("Connecting to database server...");
        database = new DatabaseHandler(this);
        getLogger().info("Successfully connected to database!");

        // Initialise DataServices and update schema
        dataServices = new HashMap<>();
        dataServices.put("schema", new SchemaDataService(this, database));
        dataServices.put("player", new PlayerDataService(this, database));

        // Register commands
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommand("gamemode").setTabCompleter(new GamemodeCompleter(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("teleport").setTabCompleter(new TeleportCompleter(this));
        getCommand("tphere").setExecutor(new TeleportHereCommand(this));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        getLogger().info("Plugin loaded");
    }

    @Override
    public void onDisable() {
        for (DataService service : dataServices.values()) {
            service.saveAll();
        }

        closeDatabase();
        getLogger().info("Plugin disabled");
    }

    public Config config() {
        return config;
    }

    public DatabaseHandler getDatabase() {
        return database;
    }

    public SchemaDataService getSchemaData() {
        return (SchemaDataService) dataServices.get("schema");
    }

    public PlayerDataService getPlayerData() {
        return (PlayerDataService) dataServices.get("player");
    }

    public void closeDatabase() {
        database.getDataSource().close();
    }

    public void reload() {
        config = new Config(); // Reload config (including messages.json)
    }
}
