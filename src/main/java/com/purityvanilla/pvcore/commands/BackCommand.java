package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {
    private final PVCore plugin;

    public BackCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;

        Player player = (Player) sender;
        CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(player.getUniqueId());

        if (cPlayer.hasPreviousLocation()) {
            player.teleportAsync(cPlayer.removeLastLocation());
            player.sendMessage(plugin.config().getMessage("teleported-back"));
            return true;
        }

        player.sendMessage(plugin.config().getMessage("back-no-previous-location"));
        return true;
    }
}
