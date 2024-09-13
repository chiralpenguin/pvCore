package com.purityvanilla.pvcore;

import com.purityvanilla.pvcore.commands.HelpCommand;
import com.purityvanilla.pvcore.commands.RulesCommand;
import com.purityvanilla.pvcore.commands.TeleportCommand;
import com.purityvanilla.pvcore.tabcompleters.TeleportCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class pvCore extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        config = new Config(); // Create/load config, copy default config if file doesn't exist

        // Register commands
       getCommand("rules").setExecutor(new RulesCommand(this));
       getCommand("help").setExecutor(new HelpCommand(this));
       getCommand("teleport").setExecutor(new TeleportCommand(this));
       getCommand("teleport").setTabCompleter(new TeleportCompleter(this));

        getLogger().info("Plugin loaded");
    }

    public Config config() {
        return config;
    }
}
