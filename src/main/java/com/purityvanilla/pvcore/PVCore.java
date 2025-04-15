package com.purityvanilla.pvcore;

import com.purityvanilla.pvcore.api.PVCoreAPI;
import com.purityvanilla.pvcore.api.impl.PVCoreAPIProvider;
import com.purityvanilla.pvcore.commands.*;
import com.purityvanilla.pvcore.database.*;
import com.purityvanilla.pvcore.database.migration.SchemaMigrator;
import com.purityvanilla.pvcore.listeners.PlayerJoinListener;
import com.purityvanilla.pvcore.listeners.PlayerQuitListener;
import com.purityvanilla.pvcore.tabcompleters.GamemodeCompleter;
import com.purityvanilla.pvcore.tabcompleters.LocationCompleter;
import com.purityvanilla.pvcore.tabcompleters.TeleportCompleter;
import com.purityvanilla.pvcore.tasks.CacheCleanTask;
import com.purityvanilla.pvcore.tasks.SaveDataTask;
import com.purityvanilla.pvcore.util.CacheHelper;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PVCore extends JavaPlugin {
    private static PVCoreAPI apiInstance;

    private Config config;
    private DatabaseConnector database;
    private HashMap<String, DataService> dataServices;
    private CacheHelper cacheHelper;

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        config = new Config(); // Create/load config, copy default config if file doesn't exist

        // Connect to database
        getLogger().info("Connecting to database server...");
        database = new DatabaseConnector(this);
        getLogger().info("Successfully connected to database");

        // Initialise schema DataService and handle pending migrations
        dataServices = new HashMap<>();
        dataServices.put("schema", new SchemaDataService(this, database));
        SchemaMigrator schemaMigrator = new SchemaMigrator(getSchemaData(), getLogger(), config, database);
        schemaMigrator.handleMigrations();

        // Initialise remaining DataServices
        dataServices.put("player", new PlayerDataService(this, database));
        dataServices.put("locations", new LocationDataService(this, database));

        // Initialise external API objects
        luckPerms = LuckPermsProvider.get();
        cacheHelper = new CacheHelper(this);

        // Register api provider as Bukkit service
        PVCoreAPIProvider apiProvider = new PVCoreAPIProvider(this);
        apiInstance = apiProvider;
        getServer().getServicesManager().register(PVCoreAPI.class, apiProvider, this, ServicePriority.Normal);

        // Other plugin startup tasks
        registerCommands();
        registerListeners();
        scheduleTasks();

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

    public static PVCoreAPI getAPI() {
        return apiInstance;
    }

    public Config config() {
        return config;
    }

    private SchemaDataService getSchemaData() {
        return (SchemaDataService) dataServices.get("schema");
    }

    public PlayerDataService getPlayerData() {
        return (PlayerDataService) dataServices.get("player");
    }

    public LocationDataService getLocationData() {
        return (LocationDataService) dataServices.get("locations");
    }

    public CacheHelper getCacheHelper() {
        return cacheHelper;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public void reload() {
        config = new Config(); // Reload config (including messages.json)
        scheduleTasks(); // Cancel and then re-schedule all tasks
    }

    private void registerCommands() {
        CommandExecutor textCommandExecutor = new TextCommand(this);

        getCommand("blog").setExecutor(textCommandExecutor);
        getCommand("discord").setExecutor(textCommandExecutor);
        getCommand("donate").setExecutor(textCommandExecutor);
        getCommand("faq").setExecutor(textCommandExecutor);
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommand("gamemode").setTabCompleter(new GamemodeCompleter(this));
        getCommand("realname").setExecutor(new RealnameCommand(this));
        getCommand("reddit").setExecutor(textCommandExecutor);
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("rewards").setExecutor(textCommandExecutor);
        getCommand("rules").setExecutor(textCommandExecutor);
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("nick").setExecutor(new NicknameCommand(this));
        getCommand("nickremove").setExecutor(new NickRemoveCommand(this));
        getCommand("dloc").setExecutor(new LocationDeleteCommand(this));
        getCommand("dloc").setTabCompleter(new LocationCompleter(this));
        getCommand("rloc").setExecutor(new LocationRenameCommand(this));
        getCommand("rloc").setTabCompleter(new LocationCompleter(this));
        getCommand("sloc").setExecutor(new LocationSaveCommand(this));
        getCommand("sloc").setTabCompleter(new LocationCompleter(this));
        getCommand("tloc").setExecutor(new LocationTeleportCommand(this));
        getCommand("tloc").setTabCompleter(new LocationCompleter(this));
        getCommand("suicide").setExecutor(new SuicideCommand(this));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("teleport").setTabCompleter(new TeleportCompleter(this));
        getCommand("tphere").setExecutor(new TeleportHereCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    private void scheduleTasks() {
        getServer().getGlobalRegionScheduler().cancelTasks(this);

        // Run saveData every 5 minutes after 1 minute
        SaveDataTask saveDataTask = new SaveDataTask(dataServices);
        getServer().getGlobalRegionScheduler().runAtFixedRate(
                this, task -> saveDataTask.run(),1200L, 6000L);

        // Run cacheClean every 10 minutes after 2 minute
        CacheCleanTask cacheCleanTask = new CacheCleanTask(dataServices);
        getServer().getGlobalRegionScheduler().runAtFixedRate(
                this, task -> cacheCleanTask.run(),2400L, 12000L);
    }

    private void closeDatabase() {
        database.getDataSource().close();
    }
}
