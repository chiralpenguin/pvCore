package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationDeleteCommand implements CommandExecutor {
    private final PVCore plugin;

    public LocationDeleteCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("location-delete-usage"))) return true;

        Player player = (Player) sender;
        String locationLabel = args[0].toLowerCase();
        SavedLocation location = plugin.getLocationData().getLocation(player.getUniqueId(), locationLabel);
        if (location == null) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }

        locationLabel = location.getLabel();
        plugin.getLocationData().removeLocation(player.getUniqueId(), location);
        player.sendMessage(plugin.config().getMessage("location-deleted",
                CustomTagResolvers.locationLabelResolver(locationLabel)));
        return true;
    }
}
