package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationTeleportCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationTeleportCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("location-teleport-usage"))) return true;

        Player player = (Player) sender;
        String locationLabel = args[0].toLowerCase();
        SavedLocation location = plugin.getLocationData().getLocation(player.getUniqueId(), locationLabel);
        if (location == null) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }

        player.teleportAsync(location.getLocation(plugin));
        player.sendMessage(plugin.config().getMessage("location-teleported",
                CustomTagResolvers.labelResolver(locationLabel)));
        return true;
    }
}
