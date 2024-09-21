package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LocationSaveCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationSaveCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return true;
    }
}
