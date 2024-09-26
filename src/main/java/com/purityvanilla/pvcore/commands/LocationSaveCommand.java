package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.database.LocationDataService;
import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
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

        String locationLabel = args[0].toLowerCase();
        saveLocation(plugin, player, locationLabel);
        return true;
    }

    public void saveLocation(pvCore plugin, Player player, String locationLabel) {
        UUID playerID = player.getUniqueId();
        LocationDataService locationData = plugin.getLocationData();

        SavedLocation currentLocation = new SavedLocation(playerID, locationLabel, player.getLocation());
        SavedLocation location = locationData.getLocation(playerID, locationLabel);
        if (location == null) {
            locationData.addLocation(playerID, currentLocation);

            TagResolver resolver = TagResolver.resolver(Placeholder.component("label", Component.text(locationLabel)));
            player.sendMessage(plugin.config().getMessage("location-save", resolver));
            return;
        }

        location.setLocation(player.getLocation());
        locationData.addLocation(playerID, location);

        TagResolver resolver = TagResolver.resolver(Placeholder.component("label", Component.text(locationLabel)));
        player.sendMessage(plugin.config().getMessage("location-save-overwrite", resolver));
    }
}
