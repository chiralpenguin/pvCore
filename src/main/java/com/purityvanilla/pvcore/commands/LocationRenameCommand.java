package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.database.LocationDataService;
import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationRenameCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationRenameCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.config().getMessage("location-rename-usage"));
            return true;
        }

        LocationDataService locationData = plugin.getLocationData();
        UUID playerID = player.getUniqueId();
        String oldLabel = args[0].toLowerCase();
        String locationLabel = args[1].toLowerCase();

        if (!locationData.locationExists(playerID, oldLabel)) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }
        
        SavedLocation location = locationData.getLocation(playerID, oldLabel);
        location.setLabel(locationLabel);
        if (locationData.locationExists(playerID, locationLabel)) {
            location.playerSave(plugin, locationData.getLocation(playerID, oldLabel).getLocation(plugin));
            return true;
        }

        locationData.addLocation(playerID, location);
        locationData.removeLocation(playerID, oldLabel);

        TagResolver resolver = TagResolver.resolver(Placeholder.component("oldlabel", Component.text(oldLabel)),
                Placeholder.component("label", Component.text(locationLabel)));
        player.sendMessage(plugin.config().getMessage("location-renamed", resolver));
        return true;
    }
}
