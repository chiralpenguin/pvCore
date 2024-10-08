package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RulesCommand implements CommandExecutor {
    private final pvCore plugin;

    public RulesCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(plugin.config().getMessage("rules"));
        return true;
    }
}
