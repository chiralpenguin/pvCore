package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.database.LocationDataService;
import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationDeleteCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationDeleteCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.config().getMessage("location-delete-usage"));
            return true;
        }

        String locationLabel = args[0].toLowerCase();
        SavedLocation location = plugin.getLocationData().getLocation(player.getUniqueId(), locationLabel);
        if (location == null) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }

        locationLabel = location.getLabel();
        plugin.getLocationData().removeLocation(player.getUniqueId(), locationLabel);
        player.sendMessage(plugin.config().getMessage("location-deleted",
                CustomTagResolvers.labelResolver(locationLabel)));
        return true;
    }
}
