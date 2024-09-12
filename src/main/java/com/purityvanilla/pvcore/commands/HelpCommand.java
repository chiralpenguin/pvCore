package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    private final pvCore plugin;

    public HelpCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            try {
                int page = Integer.parseInt(args[0]);
                String messageKey = "help" + page;
                sender.sendMessage(plugin.config().getMessage(messageKey));
                return true;
            } catch (NumberFormatException | NullPointerException e) {
                sender.sendMessage(plugin.config().getMessage("help-invalid-page"));
                return true;
            }
        }

        sender.sendMessage(plugin.config().getMessage("help1"));
        return true;
    }
}
