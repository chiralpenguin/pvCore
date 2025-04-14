package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SuicideCommand implements CommandExecutor {
    private final PVCore plugin;

    public SuicideCommand(PVCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;

        Player player = (Player) sender;
        player.sendMessage(plugin.config().getMessage("suicide"));
        player.setHealth(0);
        return true;
    }
}
