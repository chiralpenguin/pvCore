package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationSaveCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationSaveCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.config().getMessage("location-save-usage"));
            return true;
        }

        UUID uuid = player.getUniqueId();
        String locationLabel = args[0].toLowerCase();
        SavedLocation location = new SavedLocation(uuid, locationLabel, player.getLocation());
        location.playerSave(plugin, player.getLocation());
        return true;
    }


}
