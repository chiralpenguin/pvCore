package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.PVCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TextCommand implements CommandExecutor {
    private final PVCore plugin;

    public TextCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(plugin.config().getMessage(command.getName().toLowerCase()));
        return true;
    }
}
